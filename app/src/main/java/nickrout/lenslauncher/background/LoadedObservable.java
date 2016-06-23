package nickrout.lenslauncher.background;

import java.util.Observable;

/**
 * Created by nicholasrout on 2016/06/12.
 */
public class LoadedObservable extends Observable {
    private static LoadedObservable instance = new LoadedObservable();

    public static LoadedObservable getInstance() {
        return instance;
    }

    private LoadedObservable() {
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