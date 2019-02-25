package pros.app.com.pros.create_post.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pros.app.com.pros.R;
import pros.app.com.pros.base.LogUtils;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class CreatePost extends AppCompatActivity {

    @BindView(R.id.camera)
    CameraView camera;

    private int cameraMethod = CameraKit.Constants.METHOD_STANDARD;
    private boolean cropOutput = false;

    private static final int IMAGE_PICKER_SELECT = 21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_post);
        ButterKnife.bind(this);

        camera.setMethod(cameraMethod);
        camera.setCropOutput(cropOutput);

        PermissionListener dialogPermissionListener =
                DialogOnDeniedPermissionListener.Builder
                        .withContext(this)
                        .withTitle("Storage permission")
                        .withMessage("Storage Permission is required to record videos")
                        .withButtonText(android.R.string.ok)
                        .withIcon(R.mipmap.ic_launcher)
                        .build();


        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(dialogPermissionListener).check();

    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    protected void onPause() {
        camera.stop();
        super.onPause();
    }

    @OnClick(R.id.close_button)
    void closeActivity() {
        this.finish();
    }

    @OnClick(R.id.gallery_picker_button)
    void openGallery() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Do something for lollipop and above versions
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("*/*");
            photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*", "video/*"});
            startActivityForResult(photoPickerIntent, IMAGE_PICKER_SELECT);
        } else {
            // do something for phones running an SDK before lollipop
            Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
            //comma-separated MIME types
            mediaChooser.setType("video/*, image/*");
            startActivityForResult(mediaChooser, IMAGE_PICKER_SELECT);
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Uri selectedMediaUri = data.getData();
            LogUtils.LOGE("CreatePost", selectedMediaUri.toString());
            Intent intent = new Intent(this, PreviewActivity.class);
            intent.putExtra("fromPicker", true);
            if (selectedMediaUri.toString().contains("image")) {
                //handle image
                intent.putExtra("imageFileUri", selectedMediaUri.toString());

            } else if (selectedMediaUri.toString().contains("video")) {
                //handle video
                intent.putExtra("videoFileUri", selectedMediaUri.toString());
            }
            startActivity(intent);
        }

    }
}
