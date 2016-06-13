package nickrout.lenslauncher.util;

import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import nickrout.lenslauncher.R;
import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.ui.BaseActivity;

/**
 * Created by nickrout on 2016/04/02.
 */
public class AppUtil {

    private static final String TAG = AppUtil.class.getSimpleName();

    // Get all available apps for launcher
    public static ArrayList<App> getApps(
            PackageManager packageManager, Context context, Application application,
            String iconPackLabelName, AppSorter.SortType sortType) {
        ArrayList<App> apps = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = null;
        try {
            availableActivities = packageManager.queryIntentActivities(intent, 0);
        } catch (RuntimeException e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.error_too_many_apps, Toast.LENGTH_SHORT).show();
        }
        if (availableActivities != null) {
            IconPackManager.IconPack selectedIconPack = null;
            ArrayList<IconPackManager.IconPack> iconPacks = new IconPackManager().getAvailableIconPacksWithIcons(true, application);

            for (IconPackManager.IconPack iconPack : iconPacks) {
                if (iconPack.mName.equals(iconPackLabelName))
                    selectedIconPack = iconPack;
            }

            for (int i = 0; i < availableActivities.size(); i++) {
                ResolveInfo resolveInfo = availableActivities.get(i);
                App app = new App();
                app.setID(i);
                try {
                    app.setInstallDate(packageManager.getPackageInfo(resolveInfo.activityInfo.packageName, 0).firstInstallTime);
                } catch (PackageManager.NameNotFoundException e) {
                    app.setInstallDate(0);
                }
                app.setLabel(resolveInfo.loadLabel(packageManager));
                app.setPackageName(resolveInfo.activityInfo.packageName);
                app.setName(resolveInfo.activityInfo.name);
                app.setIconResId(resolveInfo.activityInfo.getIconResource());
                Bitmap defaultBitmap = BitmapUtil.packageNameToBitmap(packageManager, resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.getIconResource());
                if (selectedIconPack != null)
                    app.setIcon(selectedIconPack.getIconForPackage(app.getPackageName().toString(), defaultBitmap));
                else
                    app.setIcon(defaultBitmap);
				app.setPaletteColor(ColorUtil.getPaletteColorFromApp(app));
                apps.add(app);
            }
        }
        AppSorter.sort(apps, sortType);
        return apps;
    }

    // Launch apps, for launcher :-P
    public static void launchComponent(String packageName, String name, Context context) {
        if (packageName != null && name != null) {
            Intent componentIntent = new Intent();
            componentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            componentIntent.setComponent(new ComponentName(packageName, name));
            if (!packageName.equals("nickrout.lenslauncher")) {
                componentIntent.setAction(Intent.ACTION_MAIN);
            }
            componentIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            try {
                context.startActivity(componentIntent);
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).overridePendingTransition(
                        R.anim.fade_in, R.anim.fade_out
                    );
                }
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, R.string.error_app_not_found, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, R.string.error_app_not_found, Toast.LENGTH_SHORT).show();
        }
    }
}
