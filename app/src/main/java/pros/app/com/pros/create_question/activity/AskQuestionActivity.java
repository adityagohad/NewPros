package pros.app.com.pros.create_question.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pros.app.com.pros.R;
import pros.app.com.pros.base.BaseActivity;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.create_question.presenter.CreateQuestionPresenter;
import pros.app.com.pros.create_question.view.CreateQuestionView;
import pros.app.com.pros.home.model.AthleteModel;

public class AskQuestionActivity extends BaseActivity implements CreateQuestionView {

    @BindView(R.id.ivIcon)
    CircleImageView athleteIcon;

    @BindView(R.id.question_editText)
    EditText questionEditText;

    @BindView(R.id.athletes_count)
    TextView athletesCount;

    @BindView(R.id.tags_button)
    TextView tagsTextview;

    private ArrayList<AthleteModel> athleteModelArrayList = new ArrayList<>();
    private ArrayList<AthleteModel> userSelectedList = new ArrayList<>();

    private CreateQuestionPresenter createQuestionPresenter;

    private static final int TAGS_ACTIVITY = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);
        ButterKnife.bind(this);
        if (PrefUtils.getUser().getThumbUrl() != null) {
            Picasso.get().load(PrefUtils.getUser().getThumbUrl()).placeholder(R.drawable.profile).into(athleteIcon);
        }
        questionEditText.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        createQuestionPresenter = new CreateQuestionPresenter(this);
        createQuestionPresenter.getAthletesData();

    }

    @OnClick(R.id.close_button)
    void closeApp() {
        this.finish();
    }

    @Override
    public void updateAthletesData(List<AthleteModel> athletes) {
        athletesCount.setText("" + athletes.size());
        athleteModelArrayList = new ArrayList<>(athletes);
    }

    @Override
    public void closeActivity() {
        this.finish();
    }

    @Override
    public void showLoader() {

    }

    @Override
    public void showPostErrorMessage() {
        openDialog("Info", getResources().getString(R.string.create_question_error), "Close");
    }


    @OnClick({R.id.tags_iv, R.id.tags_button})
    void openTagsActivity() {
        if (athleteModelArrayList.size() > 0) {
            Intent intent = new Intent(this, TagsActivity.class);
            intent.putExtra("athletesList", athleteModelArrayList);
            intent.putExtra("userSelectedList", userSelectedList);
            startActivityForResult(intent, TAGS_ACTIVITY);
        } else {
            Toast.makeText(this, "Fetching List Please Wait...", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.post_button)
    void postQuestion() {
        String question = questionEditText.getText().toString();
        ArrayList<String> tagsIds = new ArrayList<>();

        if (!userSelectedList.isEmpty()) {
            for (AthleteModel athleteModel : userSelectedList) {
                tagsIds.add("" + athleteModel.getId());
            }
            String[] tagsArray = new String[tagsIds.size()];
            tagsArray = tagsIds.toArray(tagsArray);
            createQuestionPresenter.postQuestion(question, tagsArray);
        } else {
            String[] tagsArray = new String[0];
            createQuestionPresenter.postQuestion(question, tagsArray);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAGS_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                userSelectedList = data.getParcelableArrayListExtra("userSelectedList");
                updateTags(userSelectedList);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void updateTags(ArrayList<AthleteModel> userSelectedList) {
        if (userSelectedList.size() > 0) {
            tagsTextview.setText("" + userSelectedList.size() + " Tags");
        } else if (userSelectedList.isEmpty()) {
            tagsTextview.setText("Tags");
        }

    }
}
