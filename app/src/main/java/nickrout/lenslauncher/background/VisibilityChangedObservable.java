package nickrout.lenslauncher.background;

import java.util.Observable;

public class VisibilityChangedObservable extends Observable {
    private static VisibilityChangedObservable instance = new VisibilityChangedObservable();

    public static VisibilityChangedObservable getInstance() {
        return instance;
    }

    private VisibilityChangedObservable() {
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