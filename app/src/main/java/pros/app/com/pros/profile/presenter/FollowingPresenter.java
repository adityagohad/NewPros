package pros.app.com.pros.profile.presenter;

import java.io.IOException;

import pros.app.com.pros.ProsApplication;
import pros.app.com.pros.R;
import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.ProsConstants;
import pros.app.com.pros.profile.model.FollowingModel;
import pros.app.com.pros.profile.views.FollowingView;

public class FollowingPresenter implements HttpServiceView {

    private final FollowingView view;
    private int unfollowId;

    public FollowingPresenter(FollowingView view) {
        this.view = view;
    }


    @Override
    public void response(String response, int tag) {
        if (tag == ApiEndPoints.fan_following.getTag() || tag == ApiEndPoints.followers.getTag() || tag == ApiEndPoints.pro_following.getTag()) {
            try {
                FollowingModel followingModel = JsonUtils.from(response, FollowingModel.class);
                view.bindData(followingModel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (tag == ApiEndPoints.follow_atheltes.getTag()) {
            view.onSuccessFollow();
        } else if (tag == ApiEndPoints.unfollow_atheltes.getTag()) {
            view.onsucessUnfollow();
        }
    }

    @Override
    public void onError(int tag) {
        view.showToast(ProsApplication.getInstance().getApplicationContext().getString(R.string.internal_error));
    }

    public void getFollowingList(int profileId) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.fan_following.getApi(), profileId),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.fan_following.getTag()
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

    public void unFollowAthlete(int id, String name) {
        unfollowId = id;
        view.confirmToUnfollow(name);
    }

    public void confirmedUnfollow() {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.unfollow_atheltes.getApi(), unfollowId),
                ProsConstants.POST_METHOD,
                null,
                ApiEndPoints.unfollow_atheltes.getTag()
        ).execute();
    }

    public void getFollowersList(int profileId) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.followers.getApi(), profileId),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.followers.getTag()
        ).execute();
    }

    public void getProsFollowingList(int profileId) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.pro_following.getApi(), profileId),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.pro_following.getTag()
        ).execute();
    }
}
