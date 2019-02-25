package pros.app.com.pros.profile.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.custom.HeightWrappingViewPager;
import pros.app.com.pros.home.model.PostModel;
import pros.app.com.pros.profile.adapter.LikedQuestionsAdapter;
import pros.app.com.pros.profile.adapter.ViewPagerAdapter;
import pros.app.com.pros.profile.fragment.PostFragment;
import pros.app.com.pros.profile.fragment.QuestionFragment;
import pros.app.com.pros.profile.model.MetaDataModel;
import pros.app.com.pros.profile.model.ProfileMainModel;
import pros.app.com.pros.profile.presenter.ProfilePresenter;
import pros.app.com.pros.profile.views.ProfileView;

import static pros.app.com.pros.base.ProsConstants.FOLLOWING_LIST;
import static pros.app.com.pros.base.ProsConstants.IS_FAN;
import static pros.app.com.pros.base.ProsConstants.PROFILE_ID;


public class ProfileActivity extends AppCompatActivity implements ProfileView,
        PostFragment.OnFragmentInteractionListener, QuestionFragment.OnFragmentInteractionListener {

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;

    @BindView(R.id.ivAvatarDefault)
    ImageView ivAvatarDefault;

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvNumFollowing)
    TextView tvNumFollowing;

    @BindView(R.id.viewpager)
    HeightWrappingViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.tvLikedVideos)
    TextView tvLikedVideos;

    @BindView(R.id.tvLikedQuestions)
    TextView tvLikedQuestions;

    private ProfilePresenter profilePresenter;
    private LikedQuestionsAdapter likedQuestionsAdapter;
    private MetaDataModel metaData;
    private ViewPagerAdapter adapter;

    private int followCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);
        profilePresenter = new ProfilePresenter(this);

        tvName.setText(String.format("%s %s", PrefUtils.getUser().getFirstName(), PrefUtils.getUser().getLastName()));
        tvNumFollowing.setText("-");
        tvLikedVideos.setText("" + 0);
        tvLikedQuestions.setText("" + 0);

        profilePresenter.getProfileData();

        setImage();

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        PostFragment postFragment = PostFragment.newInstance("likedPosts", PrefUtils.getUser().getId());
        QuestionFragment questionFragment = QuestionFragment.newInstance("liked_questions", PrefUtils.getUser().getId());

        adapter.addFragment(postFragment, "Liked Posts");
        adapter.addFragment(questionFragment, "Liked Question");
        viewPager.setAdapter(adapter);
        viewPager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setImage() {
        if (!TextUtils.isEmpty(PrefUtils.getString("Image"))) {
            Picasso.get().load(PrefUtils.getString("Image")).into(ivAvatar);
            ivAvatarDefault.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(PrefUtils.getUser().getMediumUrl())) {
            Picasso.get().load(PrefUtils.getUser().getMediumUrl()).into(ivAvatar);
            ivAvatarDefault.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setImage();
    }

    @OnClick(R.id.ivSettings)
    public void onClickSettings() {
        Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
        if (metaData != null)
            intent.putExtra("Follow_Count", metaData.getFollowCount());
        startActivity(intent);
    }

    @OnClick(R.id.ivGoBack)
    public void onClickBack() {
        finish();
    }


    @OnClick({R.id.tvNumFollowing, R.id.labelFollowing})
    public void onCLickFollow() {
        Intent intent = new Intent(this, FollowingActivity.class);
        intent.putExtra(PROFILE_ID, PrefUtils.getUser().getId());
        intent.putExtra(FOLLOWING_LIST, true);
        intent.putExtra(IS_FAN, true);
        startActivity(intent);
    }

    @Override
    public void onSuccessGetProfile(ProfileMainModel profileMainModel) {

        metaData = profileMainModel.getMetadata();

        //Picasso.get().load()
        tvName.setText(String.format("%s %s", PrefUtils.getUser().getFirstName(), PrefUtils.getUser().getLastName()));
        if (profileMainModel.getMetadata().getFollowCount() != 0)
            tvNumFollowing.setText(String.valueOf(metaData.getFollowCount()));

        adapter.notifyDataSetChanged();

        tvLikedVideos.setText("" + metaData.getLikedPostsCount());
        tvLikedQuestions.setText("" + metaData.getLikedQuestionsCount());
    }

    @Override
    public void updateLikedQuestions(ArrayList<PostModel> postsList) {

    }

    @Override
    public void updateLikedPosts(ArrayList<PostModel> postList) {

    }

    @Override
    public void onsucessUnfollow() {
        //Nothing to do here.
    }

    @Override
    public void onSuccessFollow() {
        //Nothing to do here.
    }

    @Override
    public void onSuccessBlock() {
        //Nothing to do here.
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
