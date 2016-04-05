package nickrout.lenslauncher.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

/**
 * Created by nickrout on 2016/04/02.
 */
public class AppUtil {

    public static void launchApp(Context context, PackageManager packageManager, String appName) {
        Intent intent = packageManager.getLaunchIntentForPackage(appName);
        context.startActivity(intent);
    }
}
