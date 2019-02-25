package pros.app.com.pros.profile.presenter;

import java.io.IOException;

import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.ProsConstants;
import pros.app.com.pros.home.model.HomeMainModel;
import pros.app.com.pros.profile.model.ProfileMainModel;
import pros.app.com.pros.profile.views.ProfileView;

public class AthleteProfilePresenter implements HttpServiceView {

    private ProfileView view;
    private ProfileMainModel profileMainModel;

    public AthleteProfilePresenter(ProfileView view) {
        this.view = view;
    }

    @Override
    public void response(String response, int tag) {
        if (tag == ApiEndPoints.pros_posts.getTag()
                || tag == ApiEndPoints.pros_reactions.getTag()) {
            try {
                HomeMainModel homeMainModel = JsonUtils.from(response, HomeMainModel.class);
                view.updateLikedPosts(homeMainModel.getPosts());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (tag == ApiEndPoints.pros_answers.getTag()) {
            try {
                HomeMainModel homeMainModel = JsonUtils.from(response, HomeMainModel.class);
                view.updateLikedPosts(homeMainModel.getAnswers());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (tag == ApiEndPoints.pros_questions.getTag()) {
            try {
                HomeMainModel homeMainModel = JsonUtils.from(response, HomeMainModel.class);
                view.updateLikedQuestions(homeMainModel.getQuestions());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(int tag) {

    }

    public void getPostData(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.pros_posts.getApi(), id),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.pros_posts.getTag()
        ).execute();
    }

    public void getReactionsData(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.pros_reactions.getApi(), id),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.pros_reactions.getTag()
        ).execute();
    }

    public void getQuestionsData(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.pros_questions.getApi(), id),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.pros_questions.getTag()
        ).execute();
    }

    public void getAnswersData(int id) {
        new HttpServiceUtil(
                this,
                String.format(ApiEndPoints.pros_answers.getApi(), id),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.pros_answers.getTag()
        ).execute();
    }
}
