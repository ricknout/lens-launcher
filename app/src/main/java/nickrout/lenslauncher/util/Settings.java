package nickrout.lenslauncher.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by nicholasrout on 2016/04/02.
 */
public class Settings {

    public static final float DEFAULT_MIN_ICON_SIZE = 18.0f;
    public static final float DEFAULT_DISTORTION_FACTOR = 2.5f;
    public static final float DEFAULT_SCALE_FACTOR = 1.0f;
    public static final long DEFAULT_ANIMATION_TIME = 200;
    public static final boolean DEFAULT_VIBRATE_APP_HOVER = false;
    public static final boolean DEFAULT_VIBRATE_APP_LAUNCH = true;
    public static final boolean DEFAULT_SHOW_NAME_APP_HOVER = true;
    public static final boolean DEFAULT_SHOW_TOUCH_SELECTION = false;
    public static final boolean DEFAULT_SHOW_NEW_APP_TAG = true;
    public static final String DEFAULT_TOUCH_SELECTION_COLOR = "#F50057"; /* Unable to access strings.xml from here */
    public static final String DEFAULT_ICON_PACK_LABEL_NAME = "Default Icon Pack"; /* Unable to access strings.xml from here */
    public static final int DEFAULT_SORT_TYPE = 0;

    // These values are for the progress bars, their real values = (MAX_VALUE / INTERVAL (eg. 2)) + MIN_VALUE
    public static final int MAX_MIN_ICON_SIZE = 20;
    public static final int MAX_DISTORTION_FACTOR = 8;
    public static final int MAX_SCALE_FACTOR = 4;
    public static final int MAX_ANIMATION_TIME = 600;

    public static final int SHOW_NEW_APP_TAG_DURATION = 12 * 60 * 60 * 1000; /* An app has the new tag for twelve hours. If openCount >= 1, the new tag is not drawn. */

    public static final float MIN_MIN_ICON_SIZE = 10.0f;
    public static final float MIN_DISTORTION_FACTOR = 0.5f;
    public static final float MIN_SCALE_FACTOR = 1.0f;
    public static final long MIN_ANIMATION_TIME = 100;

    public static final float DEFAULT_FLOAT = Float.MIN_VALUE;
    public static final long DEFAULT_LONG = Long.MIN_VALUE;
    public static final boolean DEFAULT_BOOLEAN = false;
    public static final String DEFAULT_STRING = "";

    public static final String KEY_MIN_ICON_SIZE = "min_icon_size";
    public static final String KEY_DISTORTION_FACTOR = "distortion_factor";
    public static final String KEY_SCALE_FACTOR = "scale_factor";
    public static final String KEY_ANIMATION_TIME = "animation_time";
    public static final String KEY_VIBRATE_APP_HOVER = "vibrate_app_hover";
    public static final String KEY_VIBRATE_APP_LAUNCH = "vibrate_app_launch";
    public static final String KEY_SHOW_NAME_APP_HOVER = "show_name_app_hover";
    public static final String KEY_SHOW_TOUCH_SELECTION = "show_touch_selection";
    public static final String KEY_SHOW_NEW_APP_TAG = "show_new_tag_app";
    public static final String KEY_TOUCH_SELECTION_COLOR = "show_touch_selection_color";
    public static final String KEY_ICON_PACK_LABEL_NAME = "icon_pack_label_name";
    public static final String KEY_SORT_TYPE = "sort_type";

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
        if (name.equals(KEY_MIN_ICON_SIZE)) {
            if (sharedPreferences().getFloat(name, DEFAULT_MIN_ICON_SIZE) < MIN_MIN_ICON_SIZE) {
                save(name, MIN_MIN_ICON_SIZE);
            } else if (sharedPreferences().getFloat(name, DEFAULT_MIN_ICON_SIZE) > getMaxFloatValue(name)) {
                save(name, getMaxFloatValue(name));
            }
            return sharedPreferences().getFloat(name, DEFAULT_MIN_ICON_SIZE);
        } else if (name.equals(KEY_DISTORTION_FACTOR)) {
            if (sharedPreferences().getFloat(name, DEFAULT_DISTORTION_FACTOR) < MIN_DISTORTION_FACTOR) {
                save(name, MIN_DISTORTION_FACTOR);
            } else if (sharedPreferences().getFloat(name, DEFAULT_DISTORTION_FACTOR) > getMaxFloatValue(name)) {
                save(name, getMaxFloatValue(name));
            }
            return sharedPreferences().getFloat(name, DEFAULT_DISTORTION_FACTOR);
        } else if (name.equals(KEY_SCALE_FACTOR)) {
            if (sharedPreferences().getFloat(name, DEFAULT_SCALE_FACTOR) < MIN_SCALE_FACTOR) {
                save(name, MIN_SCALE_FACTOR);
            } else if (sharedPreferences().getFloat(name, DEFAULT_SCALE_FACTOR) > getMaxFloatValue(name)) {
                save(name, getMaxFloatValue(name));
            }
            return sharedPreferences().getFloat(name, DEFAULT_SCALE_FACTOR);
        } else {
            return sharedPreferences().getFloat(name, DEFAULT_FLOAT);
        }
    }

    public void save(String name, long value) {
        sharedPreferences().edit().putLong(name, value).commit();
    }

    public long getLong(String name) {
        if (name.equals(KEY_ANIMATION_TIME)) {
            if (sharedPreferences().getLong(name, DEFAULT_ANIMATION_TIME) < MIN_ANIMATION_TIME) {
                save(name, MIN_ANIMATION_TIME);
            } else if (sharedPreferences().getLong(name, DEFAULT_ANIMATION_TIME) > getMaxLongValue(name)) {
                save(name, getMaxLongValue(name));
            }
            return sharedPreferences().getLong(name, DEFAULT_ANIMATION_TIME);
        } else {
            return sharedPreferences().getLong(name, DEFAULT_LONG);
        }
    }

    public void save(String name, String value) {
        sharedPreferences().edit().putString(name, value).commit();
    }

    public String getString(String name) {
        if (name.equals(KEY_TOUCH_SELECTION_COLOR)) {
            return sharedPreferences().getString(name, DEFAULT_TOUCH_SELECTION_COLOR);
        } else if (name.equals(KEY_ICON_PACK_LABEL_NAME)) {
            return sharedPreferences().getString(name, DEFAULT_ICON_PACK_LABEL_NAME);
        } else {
            return sharedPreferences().getString(name, DEFAULT_STRING);
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
        } else if (name.equals(KEY_SHOW_NAME_APP_HOVER)) {
            return sharedPreferences().getBoolean(name, DEFAULT_SHOW_NAME_APP_HOVER);
        } else if (name.equals(KEY_SHOW_TOUCH_SELECTION)) {
            return sharedPreferences().getBoolean(name, DEFAULT_SHOW_TOUCH_SELECTION);
        } else if (name.equals(KEY_SHOW_NEW_APP_TAG)) {
            return sharedPreferences().getBoolean(name, DEFAULT_SHOW_NEW_APP_TAG);
        } else {
            return sharedPreferences().getBoolean(name, DEFAULT_BOOLEAN);
        }
    }

    public void save(AppSorter.SortType value) {
        save(KEY_SORT_TYPE, value.ordinal());
    }

    public void save(String name, int value) {
        sharedPreferences().edit().putInt(name, value).commit();
    }

    public AppSorter.SortType getSortType() {
        int ordinal = sharedPreferences().getInt(KEY_SORT_TYPE, DEFAULT_SORT_TYPE);
        return AppSorter.SortType.values()[ordinal];
    }

    public float getMaxFloatValue(String name) {
        if (name.equals(KEY_MIN_ICON_SIZE)) {
            return (float) MAX_MIN_ICON_SIZE + MIN_MIN_ICON_SIZE;
        } else if (name.equals(KEY_DISTORTION_FACTOR)) {
            return (float) MAX_DISTORTION_FACTOR / 2 + MIN_DISTORTION_FACTOR;
        } else if (name.equals(KEY_SCALE_FACTOR)) {
            return (float) MAX_SCALE_FACTOR / 2 + MIN_SCALE_FACTOR;
        } else {
            return DEFAULT_FLOAT;
        }
    }

    public long getMaxLongValue(String name) {
        if (name.equals(KEY_ANIMATION_TIME)) {
            return (long) MAX_ANIMATION_TIME / 2 + MIN_ANIMATION_TIME;
        } else {
            return DEFAULT_LONG;
        }
    }
}