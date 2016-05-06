package nickrout.lenslauncher.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

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
    private ArrayList<Bitmap> mAppIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ObservableObject.getInstance().addObserver(this);
        mLensView = (LensView) findViewById(R.id.lens_view_apps);
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

        final ProgressDialog mProgressDialog = new ProgressDialog(HomeActivity.this);;
        boolean mIsLoad;

        public UpdateAppsTask(boolean isLoad) {
            mProgressDialog.setCanceledOnTouchOutside(false);
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
            mAppIcons = new ArrayList<>();
            for (int i = 0; i < mApps.size(); i++) {
                App app = mApps.get(i);
                Bitmap appIcon = app.getIcon();
                if (appIcon != null) {
                    mAppIcons.add(appIcon);
                } else {
                    mApps.remove(app);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mLensView.setPackageManager(mPackageManager);
            mLensView.setApps(mApps, mAppIcons);
            if (mIsLoad) {
                mProgressDialog.dismiss();
            }
            super.onPostExecute(result);
        }
    }
}
