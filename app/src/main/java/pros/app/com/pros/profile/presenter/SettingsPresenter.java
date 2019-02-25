package pros.app.com.pros.profile.presenter;

import android.net.Uri;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import pros.app.com.pros.ProsApplication;
import pros.app.com.pros.R;
import pros.app.com.pros.base.ApiEndPoints;
import pros.app.com.pros.base.HttpServiceUtil;
import pros.app.com.pros.base.HttpServiceView;
import pros.app.com.pros.base.JsonUtils;
import pros.app.com.pros.base.ProsConstants;
import pros.app.com.pros.profile.model.UploadUrlModel;
import pros.app.com.pros.profile.views.SettingsView;

public class SettingsPresenter implements HttpServiceView {

    private final SettingsView settingsView;
    private UploadUrlModel uploadUrlModel;
    private byte[] byteArray;

    public SettingsPresenter(SettingsView settingsView) {
        this.settingsView = settingsView;
    }

    @Override
    public void response(String response, int tag) {

        if (tag == ApiEndPoints.sign_out.getTag() || tag == ApiEndPoints.deactivate.getTag()) {
            settingsView.onSucessLogout();
        } else if (tag == ApiEndPoints.upload_avatar.getTag()) {
            try {
                uploadUrlModel = JsonUtils.from(response, UploadUrlModel.class);

                if (!TextUtils.isEmpty(uploadUrlModel.getUploadUrl())) {
                    uploadUrlToDb();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (tag == ApiEndPoints.upload_url_to_db.getTag()) {
            updateProfilePic();
        }
    }

    private void updateProfilePic() {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("image_guid", uploadUrlModel.getGuid());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new HttpServiceUtil(
                this,
                ApiEndPoints.update_me.getApi(),
                ProsConstants.PATCH_METHOD,
                jsonRequest.toString(),
                ApiEndPoints.update_me.getTag()
        ).execute();
    }

    private void uploadUrlToDb() {
        new HttpServiceUtil(
                this,
                uploadUrlModel.getUploadUrl(),
                ProsConstants.PUT_METHOD,
                "",
                byteArray,
                ApiEndPoints.upload_url_to_db.getTag()
        ).execute();
    }

    public void onLogout() {
        new HttpServiceUtil(
                this,
                ApiEndPoints.sign_out.getApi(),
                ProsConstants.DELETE_METHOD,
                null,
                ApiEndPoints.sign_out.getTag()
        ).execute();
    }

    public void onDeactivate() {
        new HttpServiceUtil(
                this,
                ApiEndPoints.deactivate.getApi(),
                ProsConstants.PATCH_METHOD,
                "",
                ApiEndPoints.deactivate.getTag()
        ).execute();
    }

    public void getUploadUrl(byte[] byteArray) {
        this.byteArray = byteArray;
        new HttpServiceUtil(
                this,
                ApiEndPoints.upload_avatar.getApi(),
                ProsConstants.GET_METHOD,
                null,
                ApiEndPoints.upload_avatar.getTag()
        ).execute();
    }

    @Override
    public void onError(int tag) {
        if (tag == ApiEndPoints.sign_out.getTag() || tag == ApiEndPoints.deactivate.getTag()) {
            settingsView.onFailure(ProsApplication.getInstance().getApplicationContext().getString(R.string.internal_error));
        }
    }

}
