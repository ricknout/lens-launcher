package nickrout.lenslauncher.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import nickrout.lenslauncher.R;
import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.model.AppChange;
import nickrout.lenslauncher.util.AppUtil;
import nickrout.lenslauncher.util.ObservableObject;

/**
 * Created by nickrout on 2016/04/02.
 */
public class HomeActivity extends BaseActivity implements Observer {

    private LensView mLensView;
    private PackageManager mPackageManager;
    private ArrayList<App> mApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ObservableObject.getInstance().addObserver(this);
        mLensView = (LensView) findViewById(R.id.lens_view_apps);
        loadApps();
    }

    private void loadApps() {
        mPackageManager = getPackageManager();
        mApps = AppUtil.getApps(mPackageManager);
        mLensView.setPackageManager(mPackageManager);
        mLensView.setApps(mApps);
    }

    private void addApp(App app) {
        if (mApps != null) {
            mApps.add(app);
            mLensView.setPackageManager(mPackageManager);
            mLensView.addApp(app);
        }
    }

    private void removeApp(App app) {
        if (mApps != null) {
            for (int i = 0; i < mApps.size(); i++) {
                if (mApps.get(i).getName().equals(app.getName())) {
                    mApps.remove(i);
                    break;
                }
            }
            mLensView.setPackageManager(mPackageManager);
            mLensView.removeApp(app);
        }
    }

    @Override
    public void onBackPressed() {
        // Do Nothing
    }

    public static class AppAddedBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String packageName = intent.getData().getSchemeSpecificPart();
            AppChange appChange = new AppChange(AppChange.ChangeType.ADD, packageName);
            ObservableObject.getInstance().updateValue(appChange);
        }
    }

    public static class AppRemovedBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String packageName = intent.getData().getSchemeSpecificPart();
            AppChange appChange = new AppChange(AppChange.ChangeType.REMOVE, packageName);
            ObservableObject.getInstance().updateValue(appChange);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof AppChange) {
            AppChange appChange = (AppChange) data;
            if (appChange.getChangeType() == AppChange.ChangeType.ADD) {
                App app = AppUtil.getApp(mPackageManager, appChange.getPackageName());
                //addApp(app);
                loadApps();

            } else if (appChange.getChangeType() == AppChange.ChangeType.REMOVE) {
                App app = AppUtil.getApp(mPackageManager, appChange.getPackageName());
                //removeApp(app);
                loadApps();
            }
        }
    }
}
