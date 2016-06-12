package nickrout.lenslauncher.util;

import java.util.Observable;

/**
 * Created by nickrout on 2016/04/08.
 */
public class UpdateObservable extends Observable {
    private static UpdateObservable instance = new UpdateObservable();

    public static UpdateObservable getInstance() {
        return instance;
    }

    private UpdateObservable() {
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