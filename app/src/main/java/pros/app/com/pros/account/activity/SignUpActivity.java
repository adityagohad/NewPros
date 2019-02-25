package pros.app.com.pros.account.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.account.model.SignInModel;
import pros.app.com.pros.account.presenter.SignUpPresenter;
import pros.app.com.pros.account.views.SignInView;
import pros.app.com.pros.base.BaseActivity;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.home.activity.HomeActivity;

public class SignUpActivity extends BaseActivity implements SignInView {
    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.edtEmail)
    EditText edtEmail;

    @BindView(R.id.edtPassword)
    EditText edtPassword;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @BindView(R.id.videoView)
    VideoView videoView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private SignUpPresenter signUpPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);

        toolbarTitle.setText(getString(R.string.sign_up));

        signUpPresenter = new SignUpPresenter(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        playVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        playVideo();
    }

    private void playVideo() {

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login);
        videoView.setVideoURI(uri);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f, 0f);
                mp.setLooping(true);
            }
        });

        videoView.start();
    }


    @OnClick(R.id.ivBack)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.tvSignIn)
    public void onClickSignIn() {
        startActivity(new Intent(this, SignInActivity.class));
    }

    @OnClick(R.id.tvTnC)
    public void onClickTnc() {
    }

    @OnClick(R.id.tvSignUp)
    public void onClickSignUp() {
        progressBar.setVisibility(View.VISIBLE);
        signUpPresenter.validateData(edtName.getText().toString(), edtEmail.getText().toString(), edtPassword.getText().toString());
    }

    @Override
    public void onSucess(SignInModel signInModel) {
        if(signInModel.getFan() != null) {
            PrefUtils.saveUser(signInModel.getFan());
        } else {
            PrefUtils.saveUser(signInModel.getAthlete());
        }
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onFailure(String message) {
        progressBar.setVisibility(View.GONE);
        openDialog("", message, "Close");
    }

    @Override
    public void onValidationError(int message) {
        Toast.makeText(getBaseContext(), getResources().getString(message), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSucessforgotPswd() {

    }

    private void releasePlayer() {
        videoView.stopPlayback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releasePlayer();
    }
}
