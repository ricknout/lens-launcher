package nickrout.lenslauncher.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import nickrout.lenslauncher.R;
import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.util.AppUtil;
import nickrout.lenslauncher.util.ObservableObject;
import nickrout.lenslauncher.util.Settings;

/**
 * Created by nickrout on 2016/04/02.
 */
public class HomeActivity extends BaseActivity implements Observer {

    private final static String TAG = "HomeActivity";

    private LensView mLensView;
    private PackageManager mPackageManager;
    private ArrayList<App> mApps;
    private ArrayList<Bitmap> mAppIcons;
    private Settings mSettings;

    private MaterialDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_home);
        ObservableObject.getInstance().addObserver(this);
        mLensView = (LensView) findViewById(R.id.lens_view_apps);
        mSettings = new Settings(getApplication());
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

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new MaterialDialog.Builder(this)
                    .content(R.string.progress_loading_apps)
                    .progress(true, 0)
                    .cancelable(false)
                    .canceledOnTouchOutside(false)
                    .show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    @Override
    public void update(Observable observable, Object data) {
        loadApps(false);
    }

    private class UpdateAppsTask extends AsyncTask<Void, Void, Void> {

        boolean mIsLoad;

        public UpdateAppsTask(boolean isLoad) {
            mIsLoad = isLoad;
        }

        @Override
        protected void onPreExecute() {
            if (mIsLoad) {
                showProgressDialog();
            }
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            mPackageManager = getPackageManager();
            ArrayList<App> apps = AppUtil.getApps(mPackageManager, HomeActivity.this, getApplication(),
                    mSettings.getString(Settings.KEY_ICON_PACK_LABEL_NAME),
                    mSettings.getSortType());
            mApps = new ArrayList<>();
            mAppIcons = new ArrayList<>();
            for (int i = 0; i < apps.size(); i++) {
                App app = apps.get(i);
                Bitmap appIcon = app.getIcon();
                if (appIcon != null) {
                    mApps.add(app);
                    mAppIcons.add(appIcon);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (HomeActivity.this.isDestroyed()) {
                    return;
                }
            } else {
                if (HomeActivity.this.isFinishing()) {
                    return;
                }
            }
            dismissProgressDialog();
            mLensView.setPackageManager(mPackageManager);
            mLensView.setApps(mApps, mAppIcons);

            super.onPostExecute(result);
        }
    }
}
