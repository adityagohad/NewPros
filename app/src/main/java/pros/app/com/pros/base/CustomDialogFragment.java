package pros.app.com.pros.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

import butterknife.BindView;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.profile.activity.ContactListActivity;

public class CustomDialogFragment extends BaseDialogFragment {

    public static final String TAG = "CustomDialogFragment";

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvContent)
    TextView tvContent;
    @BindView(R.id.tvAction1)
    TextView tvAction1;
    @BindView(R.id.tvAction2)
    TextView tvAction2;
    @BindView(R.id.separator)
    View separator;

    private CustomDialogListener handleInterface;
    private String lastScreen;

    public static CustomDialogFragment newInstance(Bundle bundle) {
        CustomDialogFragment fragment = new CustomDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void registerCallbackListener(CustomDialogListener handleInterface) {
        this.handleInterface = handleInterface;
    }

    @Override
    protected int getResourceId() {
        return R.layout.layout_custom_dialog;
    }

    @Override
    protected String getClassTag() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, R.style.DialogTheme);
        getDialog().setCanceledOnTouchOutside(false);
        Window window = getDialog().getWindow();
        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER_HORIZONTAL);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @OnClick(R.id.tvAction1)
    public void onClickActionOne() {
        if (null != handleInterface)
            handleInterface.handleYes();

        if(ContactListActivity.class.getName().equals(lastScreen)){
            ((ContactListActivity)getActivity()).finish();
        }
        dismiss();
    }

    @OnClick(R.id.tvAction2)
    public void onClickActionTwo()
    {

        if(ContactListActivity.class.getName().equals(lastScreen)){
            ((ContactListActivity)getActivity()).finish();
        }
        dismiss();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getString("Title") != null && bundle.getString("Title").trim().length() > 0)
                tvTitle.setText(bundle.getString("Title"));

            tvContent.setText(bundle.getString("Content"));
            tvAction2.setText(bundle.getString("Action2"));
            lastScreen = bundle.getString("LAST_SCREEN");

            String action1 = bundle.getString("Action1");
            if (TextUtils.isEmpty(action1)) {
                tvAction1.setVisibility(View.GONE);
                separator.setVisibility(View.GONE);
            } else {
                tvAction1.setVisibility(View.VISIBLE);
                separator.setVisibility(View.VISIBLE);
                tvAction1.setText(action1);
            }
            tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
