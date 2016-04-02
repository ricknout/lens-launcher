package nickrout.lenslauncher.model;

import android.graphics.drawable.Drawable;

/**
 * Created by nickrout on 2016/04/02.
 */
public class App {

    private CharSequence mLabel;
    private CharSequence mName;
    private Drawable mIcon;

    public App() {}

    public App(String label, String name, Drawable icon) {
        mLabel = label;
        mName = name;
        mIcon = icon;
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

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
    }

    @Override
    public String toString() {
        return "App{" +
                "mLabel='" + mLabel + '\'' +
                ", mName='" + mName + '\'' +
                ", mIcon=" + mIcon +
                '}';
    }
}
