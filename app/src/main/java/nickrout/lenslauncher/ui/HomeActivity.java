package nickrout.lenslauncher.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.ArrayList;

import nickrout.lenslauncher.R;
import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.util.AppUtil;

/**
 * Created by nickrout on 2016/04/02.
 */
public class HomeActivity extends BaseActivity {

    private LensView mLensView;
    private PackageManager mPackageManager;
    private ArrayList<App> mApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mLensView = (LensView) findViewById(R.id.lens_view_apps);
        loadApps();
    }

    private void loadApps() {
        mPackageManager = getPackageManager();
        mApps = AppUtil.getApps(mPackageManager);
        mLensView.setPackageManager(mPackageManager);
        mLensView.setApps(mApps);
    }

    @Override
    public void onBackPressed() {
        // Do Nothing
    }
}
