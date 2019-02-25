package pros.app.com.pros.launch_screen;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.IOException;

import pros.app.com.pros.account.model.SignInModel;
import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.base.ProsConstants;

public class LaunchPresenter implements HttpServiceView {

    private SignInModel signInModel;

    public LaunchPresenter() {
    }

    public void fbCallback(CallbackManager callbackManager) {
        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                callSignUp(ApiEndPoints.fb_sign_in.getApi() + "?code=" + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
    }

    private void callSignUp(String url) {

        new HttpServiceUtil(
                this,
                url,
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.fb_sign_in.getTag()
        ).execute();
    }

    @Override
    public void response(String response, int tag) {
        try {
            signInModel = JsonUtils.from(response, SignInModel.class);
            PrefUtils.saveUser(signInModel.getFan());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(int tag) {

    }
}
