package nickrout.lenslauncher.util;

import java.util.Observable;

/**
 * Created by nicholasrout on 2016/06/23.
 */
public class SortedObservable extends Observable {
    private static SortedObservable instance = new SortedObservable();

    public static SortedObservable getInstance() {
        return instance;
    }

    private SortedObservable() {
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
