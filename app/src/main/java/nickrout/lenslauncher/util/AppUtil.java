package nickrout.lenslauncher.util;

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
            app.setName(resolveInfo.activityInfo.packageName);
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
    public static void launchApp(Context context, PackageManager packageManager, String appName) {
        Intent appIntent = packageManager.getLaunchIntentForPackage(appName);
        if (appIntent != null) {
            context.startActivity(appIntent);
        } else {
            Toast.makeText(context, R.string.error_app_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    // Determine difference between two sets of apps
    public static App determineChangedApp(ArrayList<App> smallerSet, ArrayList<App> largerSet) {
        if (smallerSet == null || largerSet == null) {
            Log.d("AppUtil", "determineChangedApp - sets cannot be null");
            return null;
        }
        if (smallerSet.size() == largerSet.size()) {
            Log.d("AppUtil", "determineChangedApp - sets must be of different sizes");
            return null;
        }
        if (smallerSet.size() > largerSet.size()) {
            Log.d("AppUtil", "determineChangedApp - first set must be smaller than second set");
            return null;
        }
        for (int i = 0; i < largerSet.size(); i++) {
            App largerSetApp = largerSet.get(i);
            boolean contains = false;
            for (int j = 0; j < smallerSet.size(); j++) {
                App smallerSetApp = smallerSet.get(j);
                if (largerSetApp.getName().equals(smallerSetApp.getName())) {
                    contains = true;
                }
            }
            if (!contains) {
                return largerSetApp;
            }
        }
        return null;
    }
}
