package nickrout.lenslauncher;

import com.orm.SugarApp;

import java.util.Observable;
import java.util.Observer;

import nickrout.lenslauncher.util.SortAppsTask;
import nickrout.lenslauncher.util.SortedObservable;
import nickrout.lenslauncher.util.UpdateObservable;
import nickrout.lenslauncher.util.UpdateAppsTask;

/**
 * Created by nicholasrout on 2016/06/12.
 */
public class LensLauncherApplication extends SugarApp implements Observer {

    @Override
    public void onCreate() {
        super.onCreate();
        UpdateObservable.getInstance().addObserver(this);
        SortedObservable.getInstance().addObserver(this);
        updateApps();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof UpdateObservable) {
            updateApps();
        } else if (observable instanceof SortedObservable) {
            sortApps();
        }
    }

    private void updateApps() {
        new UpdateAppsTask(
                getPackageManager(),
                getApplicationContext(),
                this)
                .execute();
    }

    private void sortApps() {
        new SortAppsTask(
                getApplicationContext(),
                this)
                .execute();
    }
}
