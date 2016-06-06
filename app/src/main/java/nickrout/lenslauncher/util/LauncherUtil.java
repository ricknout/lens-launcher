package nickrout.lenslauncher.util;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

/**
 * Created by rish on 5/6/16.
 */
public class LauncherUtil {

    public static boolean isMyAppLauncherDefault(Application application) {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = application.getPackageManager().resolveActivity(intent, 0);
        if (res.activityInfo == null) {
            return false;
        } else if ("android".equals(res.activityInfo.packageName)) {
            return false;
        } else {
            if (res.activityInfo.packageName.equals(application.getPackageName())) return true;
            else
                return false;
        }
    }

    public static void resetPreferredLauncherAndOpenChooser(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, nickrout.lenslauncher.ui.FakeLauncherActivity.class);

        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent selector = new Intent(Intent.ACTION_MAIN);
        selector.addCategory(Intent.CATEGORY_HOME);
        selector.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(selector);

        packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
    }
}