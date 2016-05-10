package nickrout.lenslauncher.util;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nickrout.lenslauncher.R;
import nickrout.lenslauncher.model.App;

/**
 * Created by nickrout on 2016/04/02.
 */
public class AppUtil {

    // Get all available apps for launcher
    public static ArrayList<App> getApps(PackageManager packageManager) {
        ArrayList<App> apps = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = packageManager.queryIntentActivities(intent, 0);
        for(ResolveInfo resolveInfo : availableActivities){
            App app = new App();
            app.setLabel(resolveInfo.loadLabel(packageManager));
            app.setPackageName(resolveInfo.activityInfo.packageName);
            app.setName(resolveInfo.activityInfo.name);
            app.setIconResId(resolveInfo.activityInfo.getIconResource());
            app.setIcon(BitmapUtil.packageNameToBitmap(packageManager, resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.getIconResource()));
            apps.add(app);
        }
        Collections.sort(apps, new Comparator<App>() {
            @Override
            public int compare(App appOne, App appTwo) {
                return appOne.getLabel().toString().compareToIgnoreCase(appTwo.getLabel().toString());
            }
        });
        return apps;
    }

    // Launch apps, for launcher :-P
    public static void launchComponent(String packageName, String name, Context context){
        if (packageName != null && name != null) {
            Intent componentIntent = new Intent();
            componentIntent.setComponent(new ComponentName(packageName, name));
            componentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(componentIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, R.string.error_app_not_found, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, R.string.error_app_not_found, Toast.LENGTH_SHORT).show();
        }
    }
}
