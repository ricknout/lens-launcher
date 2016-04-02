package nickrout.lenslauncher.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by nicholasrout on 2016/04/02.
 */
public class Settings {

    private static final float DEFAULT_LENS_DIAMETER = 300.0f;
    private static final float DEFAULT_MIN_ICON_SIZE = 12.0f;
    private static final float DEFAULT_DISTORTION_FACTOR = 2.0f;
    private static final float DEFAULT_SCALE_FACTOR = 2.0f;
    private static final boolean DEFAULT_VIBRATE_APP_HOVER = true;
    private static final boolean DEFAULT_VIBRATE_APP_LAUNCH = true;
    private static final boolean DEFAULT_SHOW_TOUCH_SELECTION = false;

    public static final int MAX_LENS_DIAMETER = 1000;
    public static final int MAX_MIN_ICON_SIZE = 30;
    public static final int MAX_DISTORTION_FACTOR = 5;
    public static final int MAX_SCALE_FACTOR = 5;

    public static final float DEFAULT_FLOAT = Float.MIN_VALUE;
    public static final boolean DEFAULT_BOOLEAN = false;

    public static final String KEY_LENS_DIAMETER = "lens_diameter";
    public static final String KEY_MIN_ICON_SIZE = "min_icon_size";
    public static final String KEY_DISTORTION_FACTOR = "distortion_factor";
    public static final String KEY_SCALE_FACTOR = "scale_factor";
    public static final String KEY_VIBRATE_APP_HOVER = "vibrate_app_hover";
    public static final String KEY_VIBRATE_APP_LAUNCH = "vibrate_app_launch";
    public static final String KEY_SHOW_TOUCH_SELECTION = "show_touch_selection";

    private Context mContext;
    private SharedPreferences mPrefs;

    public Settings(Context context) {
        mContext = context;
    }

    private SharedPreferences sharedPreferences() {
        if (mPrefs == null) {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        }
        return mPrefs;
    }

    public void save(String name, float value) {
        sharedPreferences().edit().putFloat(name, value).commit();
    }

    public float getFloat(String name) {
        if (name.equals(KEY_LENS_DIAMETER)) {
            return sharedPreferences().getFloat(name, DEFAULT_LENS_DIAMETER);
        } else if (name.equals(KEY_MIN_ICON_SIZE)){
            return sharedPreferences().getFloat(name, DEFAULT_MIN_ICON_SIZE);
        } else if (name.equals(KEY_DISTORTION_FACTOR)) {
            return sharedPreferences().getFloat(name, DEFAULT_DISTORTION_FACTOR);
        } else if (name.equals(KEY_SCALE_FACTOR)) {
            return sharedPreferences().getFloat(name, DEFAULT_SCALE_FACTOR);
        } else {
            return sharedPreferences().getFloat(name, DEFAULT_FLOAT);
        }
    }

    public void save(String name, boolean value) {
        sharedPreferences().edit().putBoolean(name, value).commit();
    }

    public boolean getBoolean(String name) {
        if (name.equals(KEY_VIBRATE_APP_HOVER)) {
            return sharedPreferences().getBoolean(name, DEFAULT_VIBRATE_APP_HOVER);
        } else if (name.equals(KEY_VIBRATE_APP_LAUNCH)) {
            return sharedPreferences().getBoolean(name, DEFAULT_VIBRATE_APP_LAUNCH);
        } else if (name.equals(KEY_SHOW_TOUCH_SELECTION)) {
            return sharedPreferences().getBoolean(name, DEFAULT_SHOW_TOUCH_SELECTION);
        } else {
            return sharedPreferences().getBoolean(name, DEFAULT_BOOLEAN);
        }
    }

}
