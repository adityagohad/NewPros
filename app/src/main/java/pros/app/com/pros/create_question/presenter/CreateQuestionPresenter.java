package pros.app.com.pros.create_question.presenter;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.ProsConstants;
import pros.app.com.pros.create_question.view.CreateQuestionView;
import pros.app.com.pros.profile.model.FollowingModel;

public class CreateQuestionPresenter implements HttpServiceView {

    private CreateQuestionView createQuestionView;

    public CreateQuestionPresenter(CreateQuestionView createQuestionView) {
        this.createQuestionView = createQuestionView;
    }

    public void getAthletesData() {
        new HttpServiceUtil(this,
                ApiEndPoints.atheltes.getApi(),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.atheltes.getTag()
        ).execute();
    }

    public void postQuestion(String question, String[] tags) {
        if (countWords(question) > 1) {
            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("text", question);
                jsonRequest.put("tags", tags);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            new HttpServiceUtil(
                    this,
                    ApiEndPoints.post_question.getApi(),
                    ProsConstants.POST_METHOD,
                    jsonRequest.toString(),
                    ApiEndPoints.post_question.getTag()
            ).execute();
        } else {
            createQuestionView.showPostErrorMessage();
        }
    }

    @Override
    public void response(String response, int tag) {

        if (tag == ApiEndPoints.atheltes.getTag()) {
            try {
                FollowingModel followingModel = JsonUtils.from(response, FollowingModel.class);
                createQuestionView.updateAthletesData(followingModel.getAthletes());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (tag == ApiEndPoints.post_question.getTag()) {
            try {
                Log.e("Response", response);
                if (response.isEmpty()) {
                    createQuestionView.closeActivity();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onError(int tag) {

    }

    public static int countWords(String sentence) {
        if (sentence == null || sentence.isEmpty()) {
            return 0;
        }
        String[] words = sentence.split("\\s+");
        return words.length;
    }
}
