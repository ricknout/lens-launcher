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
    private long installDate;

    public App() {
    }

    public long getInstallDate() {
        return installDate;
    }

    public void setInstallDate(long installDate) {
        this.installDate = installDate;
    }

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

    @Override
    public String toString() {
        return "App{" +
                "mLabel=" + mLabel +
                ", mPackageName=" + mPackageName +
                ", mName=" + mName +
                ", mIconResId=" + mIconResId +
                ", mIcon=" + mIcon +
                ", installDate=" + installDate +
                '}';
    }

    public void setIcon(Bitmap icon) {
        mIcon = icon;
    }

}
