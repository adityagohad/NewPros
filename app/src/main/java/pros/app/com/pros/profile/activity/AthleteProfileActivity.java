package pros.app.com.pros.profile.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.BaseActivity;
import pros.app.com.pros.base.CustomDialogFragment;
import pros.app.com.pros.base.CustomDialogListener;
import pros.app.com.pros.base.LogUtils;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.home.model.PostModel;
import pros.app.com.pros.profile.adapter.CustomAdapter;
import pros.app.com.pros.profile.fragment.PostFragment;
import pros.app.com.pros.profile.fragment.QuestionFragment;
import pros.app.com.pros.profile.model.MetaDataModel;
import pros.app.com.pros.profile.model.ProfileMainModel;
import pros.app.com.pros.profile.presenter.ProfilePresenter;
import pros.app.com.pros.profile.views.ProfileView;

import static pros.app.com.pros.base.ProsConstants.FOLLOWING_LIST;
import static pros.app.com.pros.base.ProsConstants.IMAGE_URL;
import static pros.app.com.pros.base.ProsConstants.NAME;
import static pros.app.com.pros.base.ProsConstants.PROFILE_ID;

public class AthleteProfileActivity extends BaseActivity implements ProfileView, CustomDialogListener,
        PostFragment.OnFragmentInteractionListener, QuestionFragment.OnFragmentInteractionListener, AdapterView.OnItemSelectedListener {

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvFollow)
    TextView tvFollow;

    @BindView(R.id.tvFollowing)
    TextView tvFollowing;

    @BindView(R.id.tvNumFollowing)
    TextView tvNumFollowing;

    @BindView(R.id.tvNumFollowers)
    TextView tvNumFollowers;

    @BindView(R.id.tvLikedVideos)
    TextView tvLikedVideos;

    @BindView(R.id.tvLikedQuestions)
    TextView tvLikedQuestions;


    @BindView(R.id.bsConfirm)
    View bsConfirm;

    @BindView(R.id.bsButtons)
    View bsButtons;

    @BindView(R.id.tvHeading)
    TextView tvHeading;

    @BindView(R.id.tvAction1)
    TextView tvAction1;

    @BindView(R.id.tvAction2)
    TextView tvAction2;

    @BindView(R.id.btn1)
    Button btn1;

    @BindView(R.id.btn2)
    Button btn2;

    @BindView(R.id.posts_underline)
    View postsUnderline;

    @BindView(R.id.questions_underline)
    View questionsUnderline;

    @BindView(R.id.dropdown)
    Spinner dropDown;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.toolbar_background)
    ConstraintLayout toolbarBackground;

    private static final String POSTS = "posts";
    private static final String QUESTIONS = "questions";
    private static final String REACTIONS = "reactions";
    private static final String ANSWERS = "answers";

    private BottomSheetBehavior behavior;
    private ProfilePresenter profilePresenter;
    private MetaDataModel metaData;

    private String name;
    private String imageUrl;
    private int profileId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_athlete_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);
        profilePresenter = new ProfilePresenter(this);

        tvNumFollowers.setText("-");
        tvNumFollowing.setText("-");
        tvLikedVideos.setText(String.format(getString(R.string.label_posts), 0));
        tvLikedQuestions.setText(String.format(getString(R.string.label_questions), 0));

        profileId = getIntent().getIntExtra(PROFILE_ID, 0);
        imageUrl = getIntent().getStringExtra(IMAGE_URL);
        name = getIntent().getStringExtra(NAME);

        profilePresenter.getAthleteProfile(profileId);
        PostFragment postFragment = PostFragment.newInstance("postData", profileId);
        this.replaceFragment(postFragment);


        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), getResources().getStringArray(R.array.content_type));
        // Specify the layout to use when the list of choices appears
        // Apply the adapter to the spinner
        dropDown.setAdapter(customAdapter);
        dropDown.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        dropDown.setSelection(0, false);
        dropDown.setOnItemSelectedListener(this);


        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (PrefUtils.isAthlete()) {
                    if (isViewVisible()) {
                        // Any portion of the imageView, even a single pixel, is within the visible window
                        hideDropDown();

                    } else {
                        // NONE of the imageView is within the visible window
                        showDropDown();
                    }
                }
            }
        });


    }

    private void showDropDown() {
        dropDown.setVisibility(View.VISIBLE);
        toolbarBackground.setBackgroundColor(getResources().getColor(R.color.bg_dark_gray));
    }

    private void hideDropDown() {
        dropDown.setVisibility(View.GONE);
        toolbarBackground.setBackgroundColor(Color.TRANSPARENT);
    }

    boolean isViewVisible() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        if (tvLikedVideos == null) {
            return false;
        }
        if (!tvLikedVideos.isShown()) {
            return false;
        }
        final Rect actualPosition = new Rect();
        tvLikedVideos.getGlobalVisibleRect(actualPosition);
        final Rect screen = new Rect(0, 0, width, height);
        return actualPosition.intersect(screen);
    }

    @OnClick(R.id.ivBlock)
    public void onClickBlock() {
        behavior = BottomSheetBehavior.from(bsButtons);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {

                    case BottomSheetBehavior.STATE_EXPANDED:
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

    @OnClick(R.id.tvFollowing)
    public void onClickFollowing() {
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

    @OnClick(R.id.tvLikedQuestions)
    void showQuestions() {
        updateUI(QUESTIONS);
        QuestionFragment questionFragment = QuestionFragment.newInstance("athlete_questions", profileId);
        this.replaceFragment(questionFragment);
    }

    @OnClick(R.id.tvLikedVideos)
    void showPosts() {
        updateUI(POSTS);
        PostFragment postFragment = PostFragment.newInstance("postData", profileId);
        this.replaceFragment(postFragment);
    }

    @OnClick(R.id.tvAction1)
    public void onClickAction1() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        profilePresenter.unFollowAthlete(profileId);
    }

    @OnClick(R.id.tvAction2)
    public void onClickAction2() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.btn1)
    public void onClickbtn1() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        confirmationDialog();
    }

    @OnClick(R.id.btn2)
    public void onClickbtn2() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @OnClick(R.id.tvFollow)
    public void onClickFollow() {
        profilePresenter.followAthlete(profileId);
    }

    @OnClick(R.id.ivGoBack)
    public void onClickBack() {
        finish();
    }


    @OnClick({R.id.tvNumFollowing, R.id.labelFollowing})
    public void onCLickFollowing() {
        Intent intent = new Intent(this, FollowingActivity.class);
        intent.putExtra(PROFILE_ID, profileId);
        intent.putExtra(FOLLOWING_LIST, true);
        startActivity(intent);
    }

    @OnClick({R.id.tvNumFollowers, R.id.labelFollowers})
    public void onCLickFollowers() {
        Intent intent = new Intent(this, FollowingActivity.class);
        intent.putExtra(PROFILE_ID, profileId);
        startActivity(intent);
    }


    void updateUI(String dataType) {
        if (dataType.equalsIgnoreCase(POSTS)) {
            postsUnderline.setVisibility(View.VISIBLE);
            questionsUnderline.setVisibility(View.GONE);

        } else if (dataType.equalsIgnoreCase(QUESTIONS)) {
            postsUnderline.setVisibility(View.GONE);
            questionsUnderline.setVisibility(View.VISIBLE);
        }
    }

    // Replace current Fragment with the destination Fragment.
    public void replaceFragment(Fragment destFragment) {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.fragment_container, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }


    @Override
    public void onSuccessGetProfile(ProfileMainModel profileMainModel) {

        metaData = profileMainModel.getMetadata();

        Picasso.get().load(imageUrl).into(ivAvatar);
        tvName.setText(name);
        tvNumFollowing.setText(String.valueOf(metaData.getFollowCount()));
        tvNumFollowers.setText(String.valueOf(metaData.getFollowersCount()));
        tvLikedVideos.setText(String.format(getString(R.string.label_posts), metaData.getPostsCount()));
        tvLikedQuestions.setText(String.format(getString(R.string.label_questions), metaData.getQuestionsAskedCount()));
    }

    @Override
    public void updateLikedQuestions(ArrayList<PostModel> postsList) {

    }

    @Override
    public void updateLikedPosts(ArrayList<PostModel> postList) {

    }

    @Override
    public void onsucessUnfollow() {
        tvFollowing.setVisibility(View.GONE);
        tvFollow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccessFollow() {
        tvFollowing.setVisibility(View.VISIBLE);
        tvFollow.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessBlock() {
        openDialog("Success", getString(R.string.blocked_user), "Close");
    }

    private void confirmationDialog() {
        CustomDialogFragment customDialogFragment = new CustomDialogFragment();
        customDialogFragment.registerCallbackListener(this);
        Bundle bundle = new Bundle();
        bundle.putString("Title", getString(R.string.block_title));
        bundle.putString("Content", getString(R.string.confirm_block));
        bundle.putString("Action1", "Block");
        bundle.putString("Action2", "Cancel");
        customDialogFragment.setArguments(bundle);
        customDialogFragment.show(this.getSupportFragmentManager(), CustomDialogFragment.TAG);
    }

    @Override
    public void handleYes() {
        profilePresenter.blockAthlete(profileId);
    }

    @Override
    public void handleNo() {
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedContentType = (String) parent.getItemAtPosition(position);
        if (POSTS.equalsIgnoreCase(selectedContentType)) {
            updateUI(POSTS);
            PostFragment postFragment = PostFragment.newInstance("postData", profileId);
            this.replaceFragment(postFragment);

        } else if (REACTIONS.equalsIgnoreCase(selectedContentType)) {
            PostFragment postFragment = PostFragment.newInstance("reactionsData", profileId);
            this.replaceFragment(postFragment);
        } else if (ANSWERS.equalsIgnoreCase(selectedContentType)) {
            PostFragment answersFragment = PostFragment.newInstance("athleteAnswers", PrefUtils.getUser().getId());
            this.replaceFragment(answersFragment);
        } else if (QUESTIONS.equalsIgnoreCase(selectedContentType)) {
            updateUI(QUESTIONS);
            QuestionFragment questionFragment = QuestionFragment.newInstance("athlete_questions", profileId);
            this.replaceFragment(questionFragment);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
