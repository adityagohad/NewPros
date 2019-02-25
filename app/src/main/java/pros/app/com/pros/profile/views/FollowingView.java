package pros.app.com.pros.profile.views;

import pros.app.com.pros.profile.model.FollowingModel;

public interface FollowingView {

    void bindData(FollowingModel followingModel);

    void onsucessUnfollow();

    void onSuccessFollow();

    void showToast(String message);

    void confirmToUnfollow(String name);
}
