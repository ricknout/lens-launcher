package nickrout.lenslauncher.ui;

import android.app.Activity;
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

/**
 * Created by nickrout on 2016/04/02.
 */
public class HomeActivity extends Activity {

    private LensView mLensView;
    private PackageManager mPackageManager;
    private ArrayList<App> mApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mLensView = (LensView) findViewById(R.id.lens_view_apps);
        loadApps();
        mLensView.setApps(mApps);
        mLensView.setPackageManager(mPackageManager);
    }

    private void loadApps(){
        mPackageManager = getPackageManager();
        mApps = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = mPackageManager.queryIntentActivities(intent, 0);
        for(ResolveInfo resolveInfo : availableActivities){
            App app = new App();
            app.setLabel(resolveInfo.loadLabel(mPackageManager));
            app.setName(resolveInfo.activityInfo.packageName);
            app.setIcon(resolveInfo.activityInfo.loadIcon(mPackageManager));
            mApps.add(app);
        }
        Collections.sort(mApps, new Comparator<App>() {
            @Override
            public int compare(App appOne, App appTwo) {
                return appOne.getLabel().toString().compareToIgnoreCase(appTwo.getLabel().toString());
            }
        });
    }
}
