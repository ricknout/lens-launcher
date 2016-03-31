package com.nickrout.testlauncher;

import android.graphics.drawable.Drawable;

/**
 * Created by nicholasrout on 2016/03/28.
 */

public class AppDetail {

    private CharSequence mLabel;
    private CharSequence mName;
    private Drawable mIcon;

    public AppDetail() {}

    public AppDetail(CharSequence label, CharSequence name, Drawable icon) {
        this.mLabel = label;
        this.mName = name;
        this.mIcon = icon;
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
}
