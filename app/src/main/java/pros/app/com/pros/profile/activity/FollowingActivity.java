package pros.app.com.pros.profile.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.KeyboardAction;
import pros.app.com.pros.profile.adapter.FollowingAdapter;
import pros.app.com.pros.profile.model.FollowingModel;
import pros.app.com.pros.profile.presenter.FollowingPresenter;
import pros.app.com.pros.profile.views.FollowingView;

import static pros.app.com.pros.base.ProsConstants.FOLLOWING_LIST;
import static pros.app.com.pros.base.ProsConstants.IS_FAN;
import static pros.app.com.pros.base.ProsConstants.PROFILE_ID;

public class FollowingActivity extends AppCompatActivity implements FollowingView {

    @BindView(R.id.rvFollowList)
    RecyclerView rvFollowList;

    @BindView(R.id.viewSearch)
    View viewSearch;

    @BindView(R.id.edtSearch)
    EditText edtSearch;

    @BindView(R.id.tvCancel)
    TextView tvCancel;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.ivClose)
    ImageView ivClose;

    @BindView(R.id.bsConfirm)
    View bsConfirm;

    @BindView(R.id.tvHeading)
    TextView tvHeading;

    @BindView(R.id.tvAction1)
    TextView tvAction1;

    @BindView(R.id.tvAction2)
    TextView tvAction2;

    private BottomSheetBehavior behavior;
    private FollowingPresenter followingPresenter;
    private FollowingAdapter adapter;

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
            if (adapter != null) {
                adapter.getFilter().filter(editable.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);

        followingPresenter = new FollowingPresenter(this);

        int profileId = getIntent().getIntExtra(PROFILE_ID, 0);
        boolean followingList = getIntent().getBooleanExtra(FOLLOWING_LIST, false);
        boolean isFan = getIntent().getBooleanExtra(IS_FAN, false);

        if (followingList) {
            title.setText(getString(R.string.label_following));
            if (isFan) {
                followingPresenter.getFollowingList(profileId);
            } else {
                followingPresenter.getProsFollowingList(profileId);
            }
        } else {
            title.setText(getString(R.string.label_followers));
            followingPresenter.getFollowersList(profileId);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        edtSearch.addTextChangedListener(watcher);
        edtSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                tvCancel.setVisibility(View.VISIBLE);
                InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(edtSearch, 0);
                edtSearch.requestFocus();
                return false;
            }
        });
    }

    @Override
    public void bindData(FollowingModel followingModel) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvFollowList.setLayoutManager(layoutManager);
        adapter = new FollowingAdapter(this, followingModel.getAthletes(), followingPresenter);
        rvFollowList.setAdapter(adapter);
    }

    @Override
    public void onsucessUnfollow() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccessFollow() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void confirmToUnfollow(final String name) {

        behavior = BottomSheetBehavior.from(bsConfirm);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {

                    case BottomSheetBehavior.STATE_EXPANDED:
                        tvHeading.setText(getString(R.string.confirm_unfollow, name));
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    @OnClick(R.id.ivBack)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.tvAction1)
    public void onClickAction1() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        followingPresenter.confirmedUnfollow();
    }

    @OnClick(R.id.tvAction2)
    public void onClickAction2() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.ivClose)
    public void onClickClose() {
        ivClose.setVisibility(View.GONE);
        edtSearch.setText("");
        edtSearch.setHint(getString(R.string.label_search));
        KeyboardAction.hideSoftKeyboard(this, edtSearch);
    }

    @OnClick(R.id.tvCancel)
    public void onClickCancel() {
        tvCancel.setVisibility(View.GONE);
        ivClose.setVisibility(View.GONE);
        KeyboardAction.hideSoftKeyboard(this, edtSearch);
    }
}
