package nickrout.lenslauncher.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
        mPackageManager = getPackageManager();
        mApps = AppUtil.getApps(mPackageManager);
        mLensView.setPackageManager(mPackageManager);
        mLensView.setApps(mApps);
    }
}
