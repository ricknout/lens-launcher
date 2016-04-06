package nickrout.lenslauncher.ui;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import nickrout.lenslauncher.R;
import nickrout.lenslauncher.util.Settings;

/**
 * Created by nickrout on 2016/04/02.
 */
public class SettingsActivity extends BaseActivity {

    private LensView mLensView;

    private AppCompatSeekBar mLensDiameter;
    private TextView mValueLensDiameter;
    private AppCompatSeekBar mMinIconSize;
    private TextView mValueMinIconSize;
    private AppCompatSeekBar mDistortionFactor;
    private TextView mValueDistortionFactor;
    private AppCompatSeekBar mScaleFactor;
    private TextView mValueScaleFactor;

    private SwitchCompat mVibrateAppHover;
    private SwitchCompat mVibrateAppLaunch;
    private SwitchCompat mShowNameAppHover;
    private SwitchCompat mShowTouchSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        assignValues();
    }

    private void setupViews() {
        Settings settings = new Settings(getBaseContext());

        mLensView = (LensView) findViewById(R.id.lens_view_settings);
        mLensView.setDrawType(LensView.DrawType.CIRCLES);

        mLensDiameter = (AppCompatSeekBar) findViewById(R.id.seek_bar_lens_diameter);
        mLensDiameter.setMax(settings.MAX_LENS_DIAMETER);
        mValueLensDiameter = (TextView) findViewById(R.id.value_lens_diameter);
        mLensDiameter.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int appropriateProgress = progress + Settings.MIN_LENS_DIAMETER;
                String lensDiameter = appropriateProgress + "dp";
                mValueLensDiameter.setText(lensDiameter);
                Settings settings = new Settings(getBaseContext());
                settings.save(Settings.KEY_LENS_DIAMETER, (float) appropriateProgress);
                mLensView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mMinIconSize = (AppCompatSeekBar) findViewById(R.id.seek_bar_min_icon_size);
        mMinIconSize.setMax(settings.MAX_MIN_ICON_SIZE);
        mValueMinIconSize = (TextView) findViewById(R.id.value_min_icon_size);
        mMinIconSize.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int appropriateProgress = progress + Settings.MIN_MIN_ICON_SIZE;
                String minIconSize = appropriateProgress + "dp";
                mValueMinIconSize.setText(minIconSize);
                Settings settings = new Settings(getBaseContext());
                settings.save(Settings.KEY_MIN_ICON_SIZE, (float) appropriateProgress);
                mLensView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mDistortionFactor = (AppCompatSeekBar) findViewById(R.id.seek_bar_distortion_factor);
        mDistortionFactor.setMax(settings.MAX_DISTORTION_FACTOR);
        mValueDistortionFactor = (TextView) findViewById(R.id.value_distortion_factor);
        mDistortionFactor.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float appropriateProgress = (float) progress / 2.0f;
                String distortionFactor = appropriateProgress + "";
                mValueDistortionFactor.setText(distortionFactor);
                Settings settings = new Settings(getBaseContext());
                settings.save(Settings.KEY_DISTORTION_FACTOR, appropriateProgress);
                mLensView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mScaleFactor = (AppCompatSeekBar) findViewById(R.id.seek_bar_scale_factor);
        mScaleFactor.setMax(settings.MAX_SCALE_FACTOR);
        mValueScaleFactor = (TextView) findViewById(R.id.value_scale_factor);
        mScaleFactor.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float appropriateProgress = (float) progress / 2.0f;
                String scaleFactor = appropriateProgress + "";
                mValueScaleFactor.setText(scaleFactor);
                Settings settings = new Settings(getBaseContext());
                settings.save(Settings.KEY_SCALE_FACTOR, appropriateProgress);
                mLensView.invalidate();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mVibrateAppHover = (SwitchCompat) findViewById(R.id.switch_vibrate_app_hover);
        mVibrateAppHover.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings settings = new Settings(getBaseContext());
                settings.save(Settings.KEY_VIBRATE_APP_HOVER, isChecked);
                mLensView.invalidate();
            }
        });
        mVibrateAppLaunch = (SwitchCompat) findViewById(R.id.switch_vibrate_app_launch);
        mVibrateAppLaunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings settings = new Settings(getBaseContext());
                settings.save(Settings.KEY_VIBRATE_APP_LAUNCH, isChecked);
                mLensView.invalidate();
            }
        });
        mShowNameAppHover = (SwitchCompat) findViewById(R.id.switch_show_name_app_hover);
        mShowNameAppHover.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings settings = new Settings(getBaseContext());
                settings.save(Settings.KEY_SHOW_NAME_APP_HOVER, isChecked);
            }
        });
        mShowTouchSelection = (SwitchCompat) findViewById(R.id.switch_show_touch_selection);
        mShowTouchSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings settings = new Settings(getBaseContext());
                settings.save(Settings.KEY_SHOW_TOUCH_SELECTION, isChecked);
            }
        });
    }

    private void assignValues() {
        Settings settings = new Settings(getBaseContext());

        mLensDiameter.setProgress((int) settings.getFloat(Settings.KEY_LENS_DIAMETER) - settings.MIN_LENS_DIAMETER);
        String lensDiameter = (int) settings.getFloat(Settings.KEY_LENS_DIAMETER) + "dp";
        mValueLensDiameter.setText(lensDiameter);
        mMinIconSize.setProgress((int) settings.getFloat(Settings.KEY_MIN_ICON_SIZE) - settings.MIN_MIN_ICON_SIZE);
        String minIconSize = (int) settings.getFloat(Settings.KEY_MIN_ICON_SIZE) + "dp";
        mValueMinIconSize.setText(minIconSize);
        mDistortionFactor.setProgress((int) (2.0f * settings.getFloat(Settings.KEY_DISTORTION_FACTOR)));
        String distortionFactor = settings.getFloat(Settings.KEY_DISTORTION_FACTOR) + "";
        mValueDistortionFactor.setText(distortionFactor);
        mScaleFactor.setProgress((int) (2.0f * settings.getFloat(Settings.KEY_SCALE_FACTOR)));
        String scaleFactor = settings.getFloat(Settings.KEY_SCALE_FACTOR) + "";
        mValueScaleFactor.setText(scaleFactor);

        mVibrateAppHover.setChecked(settings.getBoolean(Settings.KEY_VIBRATE_APP_HOVER));
        mVibrateAppLaunch.setChecked(settings.getBoolean(Settings.KEY_VIBRATE_APP_LAUNCH));
        mShowNameAppHover.setChecked(settings.getBoolean(Settings.KEY_SHOW_NAME_APP_HOVER));
        mShowTouchSelection.setChecked(settings.getBoolean(Settings.KEY_SHOW_TOUCH_SELECTION));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_about:
                // TODO - Show About Screen / Fragment / View
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
