package pros.app.com.pros.search.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pros.app.com.pros.R;
import pros.app.com.pros.base.KeyboardAction;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.home.activity.HomeActivity;
import pros.app.com.pros.home.model.AthleteModel;
import pros.app.com.pros.home.model.PostModel;
import pros.app.com.pros.profile.activity.AthleteActivity;
import pros.app.com.pros.profile.activity.ProfileActivity;
import pros.app.com.pros.search.adapter.AllAthleteAdapter;
import pros.app.com.pros.search.adapter.TopPostsAdapter;
import pros.app.com.pros.search.adapter.TopProsAdapter;
import pros.app.com.pros.search.presenter.SearchPresenter;
import pros.app.com.pros.search.views.SearchView;


public class SearchActivity extends AppCompatActivity implements SearchView {

    @BindView(R.id.topPros)
    RecyclerView topProsRecyclerview;

    @BindView(R.id.topPosts)
    AAH_CustomRecyclerView topPostsRecyclerview;

    @BindView(R.id.all_athletes_list)
    RecyclerView allAthletesListRecyclerview;

    @BindView(R.id.viewSearch)
    View viewSearch;

    @BindView(R.id.edtSearch)
    EditText edtSearch;

    @BindView(R.id.tvCancel)
    TextView tvCancel;

    @BindView(R.id.ivClose)
    ImageView ivClose;

    @BindView(R.id.ivProfile)
    CircleImageView ivProfile;


    private SearchPresenter searchPresenter;
    private TopProsAdapter topProsAdapter;
    private TopPostsAdapter topPostsAdapter;
    private AllAthleteAdapter allAthleteAdapter;

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            ivClose.setVisibility(View.VISIBLE);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (allAthleteAdapter != null) {
                allAthleteAdapter.getFilter().filter(editable.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        setImage();

        searchPresenter = new SearchPresenter(this);
        searchPresenter.getSearchData();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void setImage() {
        if (!TextUtils.isEmpty(PrefUtils.getString("Image"))) {
            Picasso.get().load(PrefUtils.getString("Image")).into(ivProfile);
        } else if (!TextUtils.isEmpty(PrefUtils.getUser().getThumbUrl())) {
            Picasso.get().load(PrefUtils.getUser().getThumbUrl()).placeholder(R.drawable.profile).into(ivProfile);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setImage();
    }

    @OnClick(R.id.ivProfile)
    public void onClickProfile() {
        if (PrefUtils.isAthlete()) {
            startActivity(new Intent(this, AthleteActivity.class));
        } else {
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }


    @Override
    public void updateTopPros(ArrayList<AthleteModel> topProsList) {
        topProsAdapter = new TopProsAdapter(getApplicationContext(), topProsList);

        topProsRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        topProsRecyclerview.setAdapter(topProsAdapter);

    }

    @Override
    public void updateTopPosts(ArrayList<PostModel> topPostsList) {

        topPostsRecyclerview.setActivity(this);
        topPostsRecyclerview.setDownloadPath(Environment.getExternalStorageDirectory() + "/MyVideo"); //optional
        topPostsRecyclerview.setDownloadVideos(true);

        List<String> urls = new ArrayList<>();
        for (PostModel object : topPostsList) {
            if (null != object.getUrls() && object.getUrls().getIntroUrl() != null && object.getUrls().getIntroUrl().endsWith(".mp4"))
                urls.add(object.getUrls().getIntroUrl());
        }
        topPostsRecyclerview.preDownload(urls);
        topPostsRecyclerview.setItemAnimator(new DefaultItemAnimator());
        topPostsRecyclerview.setVisiblePercent(50);

        topPostsAdapter = new TopPostsAdapter(topPostsList);
        topPostsRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        topPostsRecyclerview.setAdapter(topPostsAdapter);
    }

    @Override
    public void updateAllAthletes(List<AthleteModel> allAthleteList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        allAthletesListRecyclerview.setLayoutManager(layoutManager);
        allAthleteAdapter = new AllAthleteAdapter(this, allAthleteList);
        allAthletesListRecyclerview.setAdapter(allAthleteAdapter);
    }

   /* @OnClick(R.id.ivBack)
    public void onClickBack() {
        finish();
    }*/

    @OnClick(R.id.ivClose)
    public void onClickClose() {
        ivClose.setVisibility(View.GONE);
        edtSearch.setText("");
        edtSearch.setHint(getString(R.string.label_search));
        KeyboardAction.hideSoftKeyboard(this, edtSearch);
    }

    @OnClick(R.id.tvCancel)
    public void onClickCancel() {
        allAthletesListRecyclerview.setVisibility(View.GONE);
        tvCancel.setVisibility(View.GONE);
        ivClose.setVisibility(View.GONE);
        KeyboardAction.hideSoftKeyboard(this, edtSearch);
    }

    @OnClick(R.id.tvPros)
    public void onClickPros() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick({R.id.viewSearch,R.id.edtSearch})
    public void onClickSearch() {
        edtSearch.addTextChangedListener(watcher);
        edtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                allAthletesListRecyclerview.setVisibility(View.VISIBLE);
                tvCancel.setVisibility(View.VISIBLE);
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(edtSearch, 0);
                edtSearch.requestFocus();
                return false;
            }
        });
    }
}
