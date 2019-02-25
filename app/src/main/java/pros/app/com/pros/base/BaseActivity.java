package pros.app.com.pros.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected void openDialog(String title, String message, String action) {
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        bundle.putString("Content", message);
        bundle.putString("Action2", action);
        CustomDialogFragment.newInstance(bundle).show(this.getSupportFragmentManager(), CustomDialogFragment.TAG);
    }
}
