package nickrout.lenslauncher.model;

import android.graphics.Bitmap;
import androidx.annotation.ColorInt;

/**
 * Created by nickrout on 2016/04/02.
 */
public class App {

    private int mId;
    private CharSequence mLabel;
    private CharSequence mPackageName;
    private CharSequence mName;
    private int mIconResId;
    private Bitmap mIcon;
    private long mInstallDate;
    private @ColorInt int mPaletteColor;

    public App() {
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
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

    public long getInstallDate() {
        return mInstallDate;
    }

    public void setInstallDate(long mInstallDate) {
        this.mInstallDate = mInstallDate;
    }

    public int getPaletteColor() {
        return mPaletteColor;
    }

    public void setPaletteColor(int paletteColor) {
        mPaletteColor = paletteColor;
    }

    @Override
    public String toString() {
        return "App{" +
                "mId=" + mId +
                ", mLabel=" + mLabel +
                ", mPackageName=" + mPackageName +
                ", mName=" + mName +
                ", mIconResId=" + mIconResId +
                ", mIcon=" + mIcon +
                ", mInstallDate=" + mInstallDate +
                ", mPaletteColor=" + mPaletteColor +
                '}';
    }
}