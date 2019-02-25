package pros.app.com.pros.account.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;

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

public class SignUpPresenter implements HttpServiceView {

    private SignInModel signInModel;
    private SignInView signInView;
    private Gson gson = new Gson();

    public SignUpPresenter(SignInView signInView) {
        this.signInView = signInView;
    }

    public void validateData(String name, String email, String password) {

        if (TextUtils.isEmpty(name))
            signInView.onValidationError(R.string.enter_name);

        if (TextUtils.isEmpty(email))
            signInView.onValidationError(R.string.enter_email);


        if (TextUtils.isEmpty(password))
            signInView.onValidationError(R.string.enter_password);


        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name)) {

            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("email", email);
                jsonRequest.put("password", password);
                jsonRequest.put("password_confirmation", password);
                jsonRequest.put("first_name", name);
                jsonRequest.put("last_name", name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            callSignUp(jsonRequest);
        }
    }

    private void callSignUp(JSONObject jsonRequest) {

        new HttpServiceUtil(
                this,
                ApiEndPoints.sign_up.getApi(),
                ProsConstants.POST_METHOD,
                jsonRequest.toString(),
                ApiEndPoints.sign_in.getTag()
        ).execute();
    }

    @Override
    public void response(String response, int tag) {
        try {
            signInModel = JsonUtils.from(response, SignInModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        signInView.onSucess(signInModel);
    }

    @Override
    public void onError(int tag) {
        signInView.onFailure(ProsApplication.getInstance().getApplicationContext().getString(R.string.sign_up_error));
    }
}
