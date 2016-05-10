package nickrout.lenslauncher.model;

import android.graphics.Bitmap;

/**
 * Created by nickrout on 2016/04/02.
 */
public class App {

    private CharSequence mLabel;
    private CharSequence mPackageName;
    private CharSequence mName;
    private int mIconResId;
    private Bitmap mIcon;

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

    public CharSequence getPackageName() {
        return mPackageName;
    }

    public void setPackageName(CharSequence packageName) {
        mPackageName = packageName;
    }

    public CharSequence getName() {
        return mName;
    }

    public void setName(CharSequence name) {
        mName = name;
    }

    public int getIconResId() {
        return mIconResId;
    }

    public void setIconResId(int iconResId) {
        mIconResId = iconResId;
    }

    public Bitmap getIcon() {
        return mIcon;
    }

    public void setIcon(Bitmap icon) {
        mIcon = icon;
    }

    @Override
    public String toString() {
        return "App{" +
                "mLabel=" + mLabel +
                ", mPackageName=" + mPackageName +
                ", mName=" + mName +
                ", mIconResId=" + mIconResId +
                ", mIcon=" + mIcon +
                '}';
    }
}
