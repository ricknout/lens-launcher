package nickrout.lenslauncher.model;

import android.graphics.drawable.Drawable;

/**
 * Created by nickrout on 2016/04/02.
 */
public class App {

    private CharSequence mLabel;
    private CharSequence mName;

    public App() {}

    public App(String label, String name) {
        mLabel = label;
        mName = name;
    }

    public CharSequence getLabel() {
        return mLabel;
    }

    public void setLabel(CharSequence label) {
        mLabel = label;
    }

    public CharSequence getName() {
        return mName;
    }

    public void setName(CharSequence name) {
        mName = name;
    }

    @Override
    public String toString() {
        return "App{" +
                "mLabel=" + mLabel +
                ", mName=" + mName +
                '}';
    }
}
