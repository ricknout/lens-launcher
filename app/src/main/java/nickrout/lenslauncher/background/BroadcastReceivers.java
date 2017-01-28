package nickrout.lenslauncher.background;

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
            UpdatedObservable.getInstance().update();
        }
    }

    public static class AppsEditedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            EditedObservable.getInstance().update();
        }
    }

    public static class AppsVisibilityChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            VisibilityChangedObservable.getInstance().update();
        }
    }

    public static class AppsLoadedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            LoadedObservable.getInstance().update();
        }
    }

    public static class BackgroundChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            BackgroundChangedObservable.getInstance().update();
        }
    }

    public static class NightModeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NightModeObservable.getInstance().update();
        }
    }
}
