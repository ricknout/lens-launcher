package nickrout.lenslauncher.background;

import java.util.Observable;

/**
 * Created by nickrout on 2016/04/08.
 */
public class UpdatedObservable extends Observable {
    private static UpdatedObservable instance = new UpdatedObservable();

    public static UpdatedObservable getInstance() {
        return instance;
    }

    private UpdatedObservable() {
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