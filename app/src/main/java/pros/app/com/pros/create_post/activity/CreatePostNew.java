package pros.app.com.pros.create_post.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import pros.app.com.pros.R;
import pros.app.com.pros.base.BaseActivity;
import pros.app.com.pros.base.LogUtils;


public class CreatePostNew extends BaseActivity {

    private static final int PERMISSION_CAMERA = 0;
    private static final int VIDEO_CAPTURE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

    private void openCamera() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Uri selectedMediaUri = data.getData();
            LogUtils.LOGE("CreatePost", selectedMediaUri.toString());
            Intent intent = new Intent(this, PreviewActivity.class);
            intent.putExtra("fromPicker", true);
            //handle video
            intent.putExtra("videoFileUri", selectedMediaUri.toString());
            startActivity(intent);
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
}
