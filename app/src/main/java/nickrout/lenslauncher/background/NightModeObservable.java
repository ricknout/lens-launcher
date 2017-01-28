package nickrout.lenslauncher.background;

import java.util.Observable;

/**
 * Created by nicholasrout on 2017/01/15.
 */

public class NightModeObservable extends Observable {
    private static NightModeObservable instance = new NightModeObservable();

    public static NightModeObservable getInstance() {
        return instance;
    }

    private NightModeObservable() {
    }

    public void updateValue(Object data) {
        synchronized (this) {
            setChanged();
            notifyObservers(data);
        }
    }

    public void update() {
        updateValue(null);
    }
}
