package nickrout.lenslauncher.background;

import java.util.Observable;

/**
 * Created by nicholasrout on 2016/06/23.
 */
public class EditedObservable extends Observable {
    private static EditedObservable instance = new EditedObservable();

    public static EditedObservable getInstance() {
        return instance;
    }

    private EditedObservable() {
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
