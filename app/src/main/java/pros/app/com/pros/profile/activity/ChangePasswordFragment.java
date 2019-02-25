package pros.app.com.pros.profile.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.BaseDialogFragment;
import pros.app.com.pros.base.BaseView;
import pros.app.com.pros.profile.presenter.ChangePasswordPresenter;

public class ChangePasswordFragment extends BaseDialogFragment implements BaseView {

    @BindView(R.id.edtNewPassword)
    EditText edtNewPassword;

    @BindView(R.id.edtRePassword)
    EditText edtRePassword;

    private ChangePasswordPresenter changePasswordPresenter;

    public static final String TAG = "ChangePasswordFragment";

    @Override
    protected int getResourceId() {
        return R.layout.layout_change_pswd;
    }

    @Override
    protected String getClassTag() {
        return TAG;
    }

    public static ChangePasswordFragment newInstance() {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        changePasswordPresenter = new ChangePasswordPresenter(this);

        setStyle(STYLE_NO_TITLE, R.style.DialogTheme);
        Window window = getDialog().getWindow();
        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    @OnClick(R.id.tvSave)
    public void onClickSave() {
        changePasswordPresenter.validateData(edtNewPassword.getText().toString(), edtRePassword.getText().toString());
    }

    @Override
    public void onSuccess() {
        dismiss();
    }

    @Override
    public void onFailure(int message) {
        Toast.makeText(getContext(), getString(message), Toast.LENGTH_SHORT).show();
    }
}
