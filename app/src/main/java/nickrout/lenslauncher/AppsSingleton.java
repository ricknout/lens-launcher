package nickrout.lenslauncher;

import android.graphics.Bitmap;

import java.util.ArrayList;
import nickrout.lenslauncher.model.App;

/**
 * Created by nicholasrout on 2016/05/30.
 */
public class AppsSingleton {

    private static AppsSingleton mAppsSingleton;

    private ArrayList<App> mApps;
    private ArrayList<Bitmap> mAppIcons;

    private AppsSingleton() {}

    public static AppsSingleton getInstance() {
        if (mAppsSingleton == null) {
            mAppsSingleton = new AppsSingleton();
        }
        return mAppsSingleton;
    }

    public ArrayList<App> getApps() {
        ArrayList<App> apps = new ArrayList<>();
        if (mApps == null) {
            return apps;
        }
        apps.addAll(mApps);
        return apps;
    }

    public void setApps(ArrayList<App> apps) {
        mApps = apps;
    }

    public ArrayList<Bitmap> getAppIcons() {
        ArrayList<Bitmap> appIcons = new ArrayList<>();
        if (mAppIcons == null) {
            return appIcons;
        }
        appIcons.addAll(mAppIcons);
        return appIcons;
    }

    public void setAppIcons(ArrayList<Bitmap> appIcons) {
        mAppIcons = appIcons;
    }
}
