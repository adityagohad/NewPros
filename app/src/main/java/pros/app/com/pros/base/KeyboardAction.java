package pros.app.com.pros.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardAction {

    public static void showSoftKeyboard(Context activity) {
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (null != imm)
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = activity.getCurrentFocus();
            if (view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public static void hideSoftKeyboard(Context activity, View currentFocus) {
        if (activity != null) {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                if (currentFocus != null) {
                    if (currentFocus.getWindowToken() != null) {
                        inputManager.hideSoftInputFromWindow(
                                currentFocus.getWindowToken(), 0);
                    }
                }
            }
        }
    }

}
