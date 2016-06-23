package nickrout.lenslauncher.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.util.ArrayList;

import nickrout.lenslauncher.model.App;

/**
 * Created by nicholasrout on 2016/06/23.
 */
public class SortAppsTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private Application mApplication;
    private Settings mSettings;

    private ArrayList<App> mApps;
    private ArrayList<Bitmap> mAppIcons;

    public SortAppsTask(Context context,
                        Application application) {
        mContext = context;
        mApplication = application;
        mSettings = new Settings(mContext);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<App> apps = AppsSingleton.getInstance().getApps();
        AppSorter.sort(apps, mSettings.getSortType());
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
        AppsSingleton.getInstance().setApps(mApps);
        AppsSingleton.getInstance().setAppIcons(mAppIcons);
        Intent appsLoadedIntent = new Intent(mApplication, BroadcastReceivers.AppsLoadedReceiver.class);
        mApplication.sendBroadcast(appsLoadedIntent);
        super.onPostExecute(result);
    }
}
