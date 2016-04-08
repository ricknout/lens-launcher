package nickrout.lenslauncher.util;

import java.util.Observable;

/**
 * Created by nickrout on 2016/04/08.
 */
public class ObservableObject extends Observable {
    private static ObservableObject instance = new ObservableObject();

    public static ObservableObject getInstance() {
        return instance;
    }

    private ObservableObject() {
    }

    public void updateValue(Object data) {
        synchronized (this) {
            setChanged();
            notifyObservers(data);
        }
    }
}