package pros.app.com.pros.base;

import android.os.AsyncTask;

public class MediaUpload extends AsyncTask {

    private HttpServiceView mListener;

    MediaUpload(HttpServiceView mListener) {
        this.mListener = mListener;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }
}
