package nickrout.lenslauncher.ui;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import nickrout.lenslauncher.R;
import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.model.AppPersistent;
import nickrout.lenslauncher.AppsSingleton;
import nickrout.lenslauncher.background.LoadedObservable;

/**
 * Created by nickrout on 2016/04/02.
 */
public class HomeActivity extends BaseActivity implements Observer {

    private final static String TAG = "HomeActivity";

    @BindView(R.id.lens_view_apps)
    LensView mLensView;

    @BindView(R.id.progress_home)
    MaterialProgressBar mProgress;

    private PackageManager mPackageManager;
    private ArrayList<App> mApps;
    private ArrayList<Bitmap> mAppIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mPackageManager = getPackageManager();
        mLensView.setPackageManager(mPackageManager);
        assignApps(AppsSingleton.getInstance().getApps(), AppsSingleton.getInstance().getAppIcons());
        LoadedObservable.getInstance().addObserver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLensView.invalidate();
    }

    private void assignApps(ArrayList<App> apps, ArrayList<Bitmap> appIcons) {
        if (apps.size() == 0 || appIcons.size() == 0) {
            return;
        }
        mProgress.setVisibility(View.INVISIBLE);
        mLensView.setVisibility(View.VISIBLE);
        mApps = apps;
        mAppIcons = appIcons;
        removeHiddenApps();
        mLensView.setApps(mApps, mAppIcons);
    }

    private void removeHiddenApps() {
        for (int i = 0; i < mApps.size(); i++) {
            if (!AppPersistent.getAppVisibility(
                    mApps.get(i).getPackageName().toString(), mApps.get(i).getName().toString())) {
                mApps.remove(i);
                mAppIcons.remove(i);
                i--;
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Do Nothing
    }

    @Override
    protected void onDestroy() {
        LoadedObservable.getInstance().deleteObserver(this);
        super.onDestroy();
    }

    @Override
    public void update(Observable observable, Object data) {
        assignApps(AppsSingleton.getInstance().getApps(), AppsSingleton.getInstance().getAppIcons());
    }
}