package pros.app.com.pros.profile.views;

import java.util.ArrayList;

import pros.app.com.pros.home.model.PostModel;
import pros.app.com.pros.profile.model.ProfileMainModel;

public interface ProfileView {

    void onSuccessGetProfile(ProfileMainModel profileMainModel);

    void updateLikedQuestions(ArrayList<PostModel> postsList);

    void updateLikedPosts(ArrayList<PostModel> postList);

    void onsucessUnfollow();

    void onSuccessFollow();

    void onSuccessBlock();
}
