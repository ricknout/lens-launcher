package nickrout.lenslauncher.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import nickrout.lenslauncher.R;
import nickrout.lenslauncher.model.App;
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
        mLensView.setActivity(HomeActivity.this);
        loadApps(true);
    }

    private void loadApps(boolean isLoad) {
        new UpdateAppsTask(isLoad).execute();
    }

    @Override
    public void onBackPressed() {
        // Do Nothing
    }

    public static class AppsUpdatedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ObservableObject.getInstance().update();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        loadApps(false);
    }

    private class UpdateAppsTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog mProgressDialog;
        boolean mIsLoad;

        public UpdateAppsTask(boolean isLoad) {
            mProgressDialog = new ProgressDialog(HomeActivity.this);
            mIsLoad = isLoad;
        }

        @Override
        protected void onPreExecute() {
            if (mIsLoad) {
                mProgressDialog.setMessage(getString(R.string.progress_loading_apps));
                mProgressDialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            mPackageManager = getPackageManager();
            mApps = AppUtil.getApps(mPackageManager);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mLensView.setPackageManager(mPackageManager);
            if (mIsLoad) {
                mLensView.setApps(mApps);
                mProgressDialog.dismiss();
            } else {
                updateApps();
            }
            super.onPostExecute(result);
        }
    }

    private void updateApps() {
        if (mLensView != null) {
            if (mLensView.getApps() != null && mApps != null) {
                // App Removed
                if (mLensView.getApps().size() > mApps.size()) {
                    App changedApp = AppUtil.determineChangedApp(mApps, mLensView.getApps());
                    if (changedApp != null) {
                        mLensView.removeApp(changedApp);
                    }
                // App Added
                } else if (mApps.size() > mLensView.getApps().size()) {
                    App changedApp = AppUtil.determineChangedApp(mLensView.getApps(), mApps);
                    if (changedApp != null) {
                        mLensView.addApp(changedApp, mApps.indexOf(changedApp));
                    }
                // App Changed (eg. Name, Icon) - Need to Test
                // Remember to add <action android:name="android.intent.action.PACKAGE_CHANGED" /> to Manifest
                // Works, but breaks when two apps with same package name in mApps (eg. Dashlane / Dashlane Browser) ...
                } /*else {
                    boolean changedApp = false;
                    for (int i = 0; i < mApps.size(); i++) {
                        if (changedApp) {
                            break;
                        }
                        App potentialChangedApp = mApps.get(i);
                        for (int j = 0; j < mLensView.getApps().size(); j++) {
                            App oldApp = mLensView.getApps().get(j);
                            if (oldApp.getName().equals(potentialChangedApp.getName())) {
                                if (potentialChangedApp.getIconResId() != oldApp.getIconResId() ||
                                        !(potentialChangedApp.getLabel().equals(oldApp.getLabel()))) {
                                    mLensView.setApp(potentialChangedApp, j, i);
                                    changedApp = true;
                                }
                                break;
                            }
                        }
                    }
                }*/
            }
        }
    }
}
