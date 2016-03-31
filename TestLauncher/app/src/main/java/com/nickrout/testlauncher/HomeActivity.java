package com.nickrout.testlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by nicholasrout on 2016/03/28.
 */
public class HomeActivity extends Activity {

    private PackageManager mPackageManager;
    private ArrayList<AppDetail> mApps;
    private RecyclerView mAppRecyclerView;
    private AppAdapter mAppAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadApps();
        loadRecyclerView();
    }

    private void loadApps(){
        mPackageManager = getPackageManager();
        mApps = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = mPackageManager.queryIntentActivities(intent, 0);
        for(ResolveInfo resolveInfo : availableActivities){
            AppDetail app = new AppDetail();
            app.setLabel(resolveInfo.loadLabel(mPackageManager));
            app.setName(resolveInfo.activityInfo.packageName);
            app.setIcon(resolveInfo.activityInfo.loadIcon(mPackageManager));
            mApps.add(app);
        }
        Collections.sort(mApps, new Comparator<AppDetail>() {
            @Override
            public int compare(AppDetail appDetailOne, AppDetail appDetailTwo) {
                return appDetailOne.getLabel().toString().compareToIgnoreCase(appDetailTwo.getLabel().toString());
            }
        });
    }

    private void loadRecyclerView() {
        mAppRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_apps);
        mAppRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        mAppRecyclerView.setLayoutManager(layoutManager);
        mAppAdapter = new AppAdapter(getBaseContext(), mPackageManager, mApps);
        mAppRecyclerView.setAdapter(mAppAdapter);
    }
}
