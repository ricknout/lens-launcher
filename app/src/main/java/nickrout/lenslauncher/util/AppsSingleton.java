package nickrout.lenslauncher.util;

import java.util.ArrayList;
import nickrout.lenslauncher.model.App;

/**
 * Created by nicholasrout on 2016/05/30.
 */
public class AppsSingleton {

    private static AppsSingleton mAppsSingleton;

    private ArrayList<App> mApps;

    private boolean mNeedsUpdate = true;

    private AppsSingleton() {}

    public static AppsSingleton getInstance() {
        if (mAppsSingleton == null) {
            mAppsSingleton = new AppsSingleton();
        }
        return mAppsSingleton;
    }

    public ArrayList<App> getApps() {
        return mApps;
    }

    public void setApps(ArrayList<App> apps) {
        mApps = apps;
    }

    public boolean doesNeedUpdate() {
        return mNeedsUpdate;
    }

    public void setNeedsUpdate(boolean needsUpdate) {
        mNeedsUpdate = needsUpdate;
    }
}
