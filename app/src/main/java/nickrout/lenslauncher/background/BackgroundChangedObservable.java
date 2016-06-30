package nickrout.lenslauncher.background;

import java.util.Observable;

public class BackgroundChangedObservable extends Observable {
    private static BackgroundChangedObservable instance = new BackgroundChangedObservable();

    public static BackgroundChangedObservable getInstance() {
        return instance;
    }

    private BackgroundChangedObservable() {
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