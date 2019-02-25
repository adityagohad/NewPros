package pros.app.com.pros.profile.presenter;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import pros.app.com.pros.R;
import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.BaseView;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.ProsConstants;

public class ChangePasswordPresenter implements HttpServiceView {

    private final BaseView baseView;

    public ChangePasswordPresenter(BaseView baseView) {
        this.baseView = baseView;
    }

    public void changePswd(String request) {
        new HttpServiceUtil(
                this,
                ApiEndPoints.update_me.getApi(),
                ProsConstants.PATCH_METHOD,
                request,
                ApiEndPoints.update_me.getTag()
        ).execute();
    }

    @Override
    public void response(String response, int tag) {
        baseView.onSuccess();
    }

    @Override
    public void onError(int tag) {
        baseView.onFailure(R.string.internal_error);
    }

    public void validateData(String pswd, String rePswd) {

        if (!TextUtils.isEmpty(pswd) && !TextUtils.isEmpty(rePswd) && pswd.equals(rePswd)) {
            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("newPassword", pswd);
                jsonRequest.put("reEntry", rePswd);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            changePswd(jsonRequest.toString());
        }

    }

    public void sendInvite(String request) {
        new HttpServiceUtil(
                this,
                ApiEndPoints.post_invite.getApi(),
                ProsConstants.POST_METHOD,
                request,
                ApiEndPoints.post_invite.getTag()
        ).execute();
    }
}
