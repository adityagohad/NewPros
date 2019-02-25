package pros.app.com.pros.account.presenter;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import pros.app.com.pros.ProsApplication;
import pros.app.com.pros.R;
import pros.app.com.pros.account.model.SignInModel;
import pros.app.com.pros.account.views.SignInView;
import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.ProsConstants;

public class SignInPresenter implements HttpServiceView {


    private SignInView signInView;
    private SignInModel signInModel;

    public SignInPresenter(SignInView signInView) {
        this.signInView = signInView;
    }

    public void validateData(String email, String password) {

        if (TextUtils.isEmpty(email))
            signInView.onValidationError(R.string.enter_email);


        if (TextUtils.isEmpty(password))
            signInView.onValidationError(R.string.enter_password);


        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("email", email);
                jsonRequest.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            callSignIn(jsonRequest);
        }
    }

    private void callSignIn(JSONObject jsonRequest) {

        new HttpServiceUtil(
                this,
                ApiEndPoints.sign_in.getApi(),
                ProsConstants.POST_METHOD,
                jsonRequest.toString(),
                ApiEndPoints.sign_in.getTag()
        ).execute();
    }

    @Override
    public void response(String response, int tag) {
        if (tag == ApiEndPoints.sign_in.getTag()) {
            try {
                signInModel = JsonUtils.from(response, SignInModel.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            signInView.onSucess(signInModel);
        } else if (tag == ApiEndPoints.forgot_password.getTag()) {
            signInView.onSucessforgotPswd();
        }
    }

    @Override
    public void onError(int tag) {
        if (tag == ApiEndPoints.sign_in.getTag()) {
            signInView.onFailure(ProsApplication.getInstance().getApplicationContext().getString(R.string.sign_in_error));
        } else if (tag == ApiEndPoints.forgot_password.getTag()) {
            signInView.onFailure(ProsApplication.getInstance().getApplicationContext().getString(R.string.internal_error));
        }
    }

    public void forgotPassword(String email) {
        if (!TextUtils.isEmpty(email)) {

            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("email", email);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new HttpServiceUtil(
                    this,
                    ApiEndPoints.forgot_password.getApi(),
                    ProsConstants.POST_METHOD,
                    jsonRequest.toString(),
                    ApiEndPoints.forgot_password.getTag()
            ).execute();
        }

    }
}
