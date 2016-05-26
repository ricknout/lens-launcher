package nickrout.lenslauncher.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import nickrout.lenslauncher.util.ObservableObject;
import nickrout.lenslauncher.util.UpdateAppsTask;

/**
 * Created by nickrout on 2016/04/02.
 */
public class HomeActivity extends BaseActivity implements Observer, UpdateAppsTask.UpdateAppsTaskListener {

    private final static String TAG = "HomeActivity";

    private LensView mLensView;
    private PackageManager mPackageManager;
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
        mPackageManager = getPackageManager();
        loadApps(true);

    }

    private void loadApps(boolean isLoad) {
        new UpdateAppsTask(mPackageManager, getApplicationContext(), getApplication(), isLoad, HomeActivity.this).execute();
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

    @Override
    public void onUpdateAppsTaskPreExecute(boolean mIsLoad) {
        if (mIsLoad) {
            showProgressDialog();
        }
    }

    @Override
    public void onUpdateAppsTaskPostExecute(ArrayList<App> mApps, ArrayList<Bitmap> mAppIcons) {
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
    }
}