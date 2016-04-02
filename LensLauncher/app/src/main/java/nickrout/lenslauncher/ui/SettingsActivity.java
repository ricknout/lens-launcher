package nickrout.lenslauncher.ui;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import nickrout.lenslauncher.R;

/**
 * Created by nickrout on 2016/04/02.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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
}
