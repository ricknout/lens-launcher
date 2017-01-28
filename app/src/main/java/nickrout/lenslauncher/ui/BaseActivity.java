package nickrout.lenslauncher.ui;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import nickrout.lenslauncher.R;
import nickrout.lenslauncher.util.Settings;

/**
 * Created by nickrout on 2016/04/05.
 */
public class BaseActivity extends AppCompatActivity {

    protected Settings mSettings;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mSettings = new Settings(this);
        if (savedInstanceState == null) {
            updateNightMode();
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setTaskDescription();
    }

    private void setTaskDescription() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap appIconBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(
                    getString(R.string.app_name),
                    appIconBitmap,
                    ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
            setTaskDescription(taskDescription);
        }
    }

    protected void updateNightMode() {
        if (mSettings == null) {
            mSettings = new Settings(this);
        }
        getDelegate().setLocalNightMode(mSettings.getNightMode());
    }
}
