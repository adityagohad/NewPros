package pros.app.com.pros.profile.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.BaseActivity;
import pros.app.com.pros.base.CustomDialogFragment;
import pros.app.com.pros.base.CustomDialogListener;
import pros.app.com.pros.base.PrefUtils;
import pros.app.com.pros.create_post.activity.CreatePost;
import pros.app.com.pros.launch_screen.LaunchActivity;
import pros.app.com.pros.profile.presenter.SettingsPresenter;
import pros.app.com.pros.profile.views.SettingsView;

import static pros.app.com.pros.base.ProsConstants.FOLLOWING_LIST;
import static pros.app.com.pros.base.ProsConstants.IS_FAN;
import static pros.app.com.pros.base.ProsConstants.PROFILE_ID;

public class SettingsActivity extends BaseActivity implements SettingsView, CustomDialogListener {

    @BindView(R.id.ivPic)
    ImageView ivPic;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvNumFollowing)
    TextView tvNumFollowing;
    @BindView(R.id.tvContact)
    TextView tvContact;
    @BindView(R.id.tvNumFollower)
    TextView tvNumFollower;
    @BindView(R.id.viewFollower)
    View viewFollower;
    @BindView(R.id.viewFollowing)
    View viewFollowing;
    @BindView(R.id.separator)
    View separator;

    private SettingsPresenter settingsPresenter;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_OPEN_GALLERY = 102;
    private static final int PERMISSION_CAMERA = 0;
    private Bitmap imageBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);

        settingsPresenter = new SettingsPresenter(this);
        initializeViews();
    }

    private void initializeViews() {

        if (PrefUtils.isAthlete()) {
            viewFollower.setVisibility(View.VISIBLE);
            separator.setVisibility(View.VISIBLE);
            tvNumFollower.setText(String.valueOf(getIntent().getIntExtra("Follower_Count", 0)));
            tvContact.setText(getString(R.string.invite_a_pro));
        } else {
            viewFollower.setVisibility(View.GONE);
            separator.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(PrefUtils.getString("Image"))) {
            Picasso.get().load(PrefUtils.getString("Image")).into(ivPic);
        } else if (!TextUtils.isEmpty(PrefUtils.getUser().getMediumUrl())) {
            Picasso.get().load(PrefUtils.getUser().getMediumUrl()).into(ivPic);
        }

        tvName.setText(String.format("%s %s", PrefUtils.getUser().getFirstName(), PrefUtils.getUser().getLastName()));
        tvNumFollowing.setText(String.valueOf(getIntent().getIntExtra("Follow_Count", 0)));
    }

    @OnClick(R.id.tvContact)
    public void onClickContactAdmin() {
        if (PrefUtils.isAthlete()) {
            InviteAProFragment.newInstance().show(this.getSupportFragmentManager(), InviteAProFragment.TAG);
        } else {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emailIntent.setType("message/rfc822");
            emailIntent.setData(Uri.parse("mailto:hello@theprosapp.com"));
            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
            } catch (ActivityNotFoundException e) {
                openDialog(getString(R.string.email_error_title), getString(R.string.email_error_content), "Close");
            }
        }
    }

    @OnClick(R.id.ivBack)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.tvChangePswd)
    public void onClickChangePswd() {
        ChangePasswordFragment.newInstance().show(this.getSupportFragmentManager(), ChangePasswordFragment.TAG);
    }

    @OnClick(R.id.tvLogout)
    public void onClickLogout() {
        File sharedPreferenceFile = new File("/data/data/" + getPackageName() + "/shared_prefs/");
        File[] listFiles = sharedPreferenceFile.listFiles();
        for (File file : listFiles) {
            file.delete();
        }
        System.exit(0);
        //settingsPresenter.onLogout();
    }

    @OnClick(R.id.tvDeactivate)
    public void onClickDeactivate() {
        CustomDialogFragment customDialogFragment = new CustomDialogFragment();
        customDialogFragment.registerCallbackListener(this);
        Bundle bundle = new Bundle();
        bundle.putString("Title", getString(R.string.deactivate_title));
        bundle.putString("Content", getString(R.string.deactivate_account_content));
        bundle.putString("Action1", "Yes");
        bundle.putString("Action2", "Cancel");
        customDialogFragment.setArguments(bundle);
        customDialogFragment.show(this.getSupportFragmentManager(), CustomDialogFragment.TAG);
    }


    @OnClick({R.id.tvNumFollowing, R.id.labelFollowing})
    public void onCLickFollow() {
        Intent intent = new Intent(this, FollowingActivity.class);
        intent.putExtra(PROFILE_ID, PrefUtils.getUser().getId());
        intent.putExtra(FOLLOWING_LIST, true);
        intent.putExtra(IS_FAN, true);
        startActivity(intent);
    }

    @OnClick({R.id.tvNumFollower, R.id.labelFollower})
    public void onCLickFollower() {
        Intent intent = new Intent(this, FollowingActivity.class);
        intent.putExtra(PROFILE_ID, PrefUtils.getUser().getId());
        startActivity(intent);
    }

    @OnClick(R.id.ivAvatar)
    public void onClickAvatar() {

        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, REQUEST_OPEN_GALLERY);
    }

    private void takePhotoFromCamera() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)

        {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CAMERA);
        } else

        {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    openCamera();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    if (ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.CAMERA)) {

                        openDialog("", "Change your Settings to allow Pros to access Camera", "Ok");

                    }
                }
                return;
            }
        }
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");

            } else if (requestCode == REQUEST_OPEN_GALLERY) {
                Uri contentURI = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ivPic.setImageBitmap(imageBitmap);


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), imageBitmap);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));

            byte[] byteArray = baos.toByteArray();

            PrefUtils.putString("Image", Uri.fromFile(finalFile).toString());
            settingsPresenter.getUploadUrl(byteArray);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void onSucessLogout() {
        PrefUtils.clearAllSharedPref();
        Intent intent = new Intent(this, LaunchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onFailure(String message) {
        openDialog("Sorry!", message, "Close");
    }

    @Override
    public void handleYes() {
        settingsPresenter.onDeactivate();
    }

    @Override
    public void handleNo() {

    }
}
