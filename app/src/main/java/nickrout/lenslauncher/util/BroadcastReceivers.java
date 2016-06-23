package nickrout.lenslauncher.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by nicholasrout on 2016/06/12.
 */
public class BroadcastReceivers {

    public static class AppsUpdatedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            UpdateObservable.getInstance().update();
        }
    }

    public static class AppsSortedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SortedObservable.getInstance().update();
        }
    }

    public static class AppsLoadedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LoadedObservable.getInstance().update();
        }
    }
}
