package pros.app.com.pros.base;

import android.util.Log;

import pros.app.com.pros.BuildConfig;

public class LogUtils {

    public static String getLogTag(Class cls) {
        return cls.getSimpleName();
    }

    private static final String PREFIX = "PROS: ";

    public static void LOGI(String tag, String log) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, PREFIX + log);
        }
    }

    public static void LOGE(String tag, String log) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, PREFIX + log);
        }
    }

    public static void LOGD(String tag, String log) {
        if (BuildConfig.DEBUG) {
            if (log.length() > 4000) {
                Log.d(tag, PREFIX + log.substring(0, 4000));
                LOGD(tag, log.substring(4000));
            } else
                Log.d(tag, PREFIX + log);
        }
    }

    public static void LOGV(String tag, String log) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, PREFIX + log);
        }
    }

}
