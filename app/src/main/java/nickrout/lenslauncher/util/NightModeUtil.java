package nickrout.lenslauncher.util;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * Created by nicholasrout on 2017/01/15.
 */

public class NightModeUtil {

    public static String getNightModeDisplayName(@AppCompatDelegate.NightMode int nightMode) {
        switch (nightMode) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                return "Light";
            case AppCompatDelegate.MODE_NIGHT_YES:
                return "Dark";
            case AppCompatDelegate.MODE_NIGHT_AUTO:
                return "Auto";
            default:
                return "Follow System";
        }
    }

    public static @AppCompatDelegate.NightMode int getNightModeFromDisplayName(String displayName) {
        switch (displayName) {
            case "Light":
                return AppCompatDelegate.MODE_NIGHT_NO;
            case "Dark":
                return AppCompatDelegate.MODE_NIGHT_YES;
            case "Auto":
                return AppCompatDelegate.MODE_NIGHT_AUTO;
            default:
                return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
    }
}
