package pros.app.com.pros.profile.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.base.ProsConstants;
import pros.app.com.pros.home.model.HomeMainModel;
import pros.app.com.pros.profile.model.ProfileMainModel;
import pros.app.com.pros.profile.views.ProfileView;

public class ProfilePresenter implements HttpServiceView {


    private ProfileView view;
    private ProfileMainModel profileMainModel;

    public ProfilePresenter(ProfileView view) {
        this.view = view;
    }

    @Override
    public void response(String response, int tag) {
        if (tag == ApiEndPoints.fans_profile_metadata.getTag() || tag == ApiEndPoints.pros_profile_metadata.getTag()) {
            try {
                profileMainModel = JsonUtils.from(response, ProfileMainModel.class);
                view.onSuccessGetProfile(profileMainModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (tag == ApiEndPoints.fans_liked_questions.getTag()) {
            try {
                HomeMainModel homeMainModel = JsonUtils.from(response, HomeMainModel.class);
                view.updateLikedQuestions(homeMainModel.getQuestions());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (tag == ApiEndPoints.fans_liked_posts.getTag()) {
            try {
                HomeMainModel homeMainModel = JsonUtils.from(response, HomeMainModel.class);
                view.updateLikedPosts(homeMainModel.getPosts());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (tag == ApiEndPoints.follow_atheltes.getTag()) {
            view.onSuccessFollow();
        } else if (tag == ApiEndPoints.unfollow_atheltes.getTag()) {
            view.onsucessUnfollow();
        } else if (tag == ApiEndPoints.block_user.getTag()) {
            view.onSuccessBlock();
        }
    }

    @Override
    public void onError(int tag) {

    }

    public void getProfileData() {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.fans_profile_metadata.getApi(), PrefUtils.getUser().getId()),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.fans_profile_metadata.getTag()
        ).execute();
    }

    public void getAthleteProfile(int profileId) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.pros_profile_metadata.getApi(), profileId),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.pros_profile_metadata.getTag()
        ).execute();
    }

    public void getLikedQuestionsData() {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.fans_liked_questions.getApi(), PrefUtils.getUser().getId()),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.fans_liked_questions.getTag()
        ).execute();
    }

    public void getLikedPostsData() {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.fans_liked_posts.getApi(), PrefUtils.getUser().getId()),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.fans_liked_posts.getTag()
        ).execute();
    }

    public void followAthlete(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.follow_atheltes.getApi(), id),
                ProsConstants.POST_METHOD,
                null,
                ApiEndPoints.follow_atheltes.getTag()
        ).execute();
    }

    public void unFollowAthlete(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.unfollow_atheltes.getApi(), id),
                ProsConstants.POST_METHOD,
                null,
                ApiEndPoints.unfollow_atheltes.getTag()
        ).execute();
    }

    public void blockAthlete(int id) {

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("user_id", id);
            jsonRequest.put("user_type", "Athlete");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpServiceUtil(
                this,
                ApiEndPoints.block_user.getApi(),
                ProsConstants.POST_METHOD,
                jsonRequest.toString(),
                ApiEndPoints.block_user.getTag()
        ).execute();
    }
}
