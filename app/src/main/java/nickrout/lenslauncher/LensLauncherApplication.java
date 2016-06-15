package nickrout.lenslauncher;

import com.orm.SugarApp;

import java.util.Observable;
import java.util.Observer;

import nickrout.lenslauncher.util.UpdateObservable;
import nickrout.lenslauncher.util.UpdateAppsTask;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by nicholasrout on 2016/06/12.
 */
public class LensLauncherApplication extends SugarApp implements Observer {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        UpdateObservable.getInstance().addObserver(this);
        updateApps();
    }

    @Override
    public void update(Observable observable, Object data) {
        updateApps();
    }

    private void updateApps() {
        new UpdateAppsTask(
                getPackageManager(),
                getApplicationContext(),
                this)
                .execute();
    }


}
