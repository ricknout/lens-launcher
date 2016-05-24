package nickrout.lenslauncher.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pavelsikun.vintagechroma.ChromaDialog;
import com.pavelsikun.vintagechroma.IndicatorMode;
import com.pavelsikun.vintagechroma.OnColorSelectedListener;
import com.pavelsikun.vintagechroma.colormode.ColorMode;

import java.util.ArrayList;

import nickrout.lenslauncher.R;
import nickrout.lenslauncher.util.IconPackManager;
import nickrout.lenslauncher.util.Settings;

/**
 * Created by nickrout on 2016/04/02.
 */
public class SettingsActivity extends BaseActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    private LensView mLensView;

    private AppCompatSeekBar mLensDiameter;
    private TextView mValueLensDiameter;
    private AppCompatSeekBar mMinIconSize;
    private TextView mValueMinIconSize;
    private AppCompatSeekBar mDistortionFactor;
    private TextView mValueDistortionFactor;
    private AppCompatSeekBar mScaleFactor;
    private TextView mValueScaleFactor;
    private ImageView mTouchSelectionColor;
    private LinearLayout mIconPackLayout;
    private TextView mSelectedIconPack;

    private SwitchCompat mVibrateAppHover;
    private SwitchCompat mVibrateAppLaunch;
    private SwitchCompat mShowNameAppHover;
    private SwitchCompat mShowTouchSelection;
    private SwitchCompat mShowNewAppTag;

    private ChromaDialog mChromaDialog;
    private MaterialDialog mIconPackChooserDialog;

    private Settings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mSettings = new Settings(this);
        setupViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        assignValues();
    }

    private void setupViews() {
        mLensView = (LensView) findViewById(R.id.lens_view_settings);
        mLensView.setDrawType(LensView.DrawType.CIRCLES);

        mLensDiameter = (AppCompatSeekBar) findViewById(R.id.seek_bar_lens_diameter);
        mLensDiameter.setMax(Settings.MAX_LENS_DIAMETER);
        mValueLensDiameter = (TextView) findViewById(R.id.value_lens_diameter);
        mLensDiameter.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int appropriateProgress = progress + Settings.MIN_LENS_DIAMETER;
                String lensDiameter = appropriateProgress + "dp";
                mValueLensDiameter.setText(lensDiameter);
                mSettings.save(Settings.KEY_LENS_DIAMETER, (float) appropriateProgress);
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
        mMinIconSize.setMax(Settings.MAX_MIN_ICON_SIZE);
        mValueMinIconSize = (TextView) findViewById(R.id.value_min_icon_size);
        mMinIconSize.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int appropriateProgress = progress + Settings.MIN_MIN_ICON_SIZE;
                String minIconSize = appropriateProgress + "dp";
                mValueMinIconSize.setText(minIconSize);
                mSettings.save(Settings.KEY_MIN_ICON_SIZE, (float) appropriateProgress);
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
        mDistortionFactor.setMax(Settings.MAX_DISTORTION_FACTOR);
        mValueDistortionFactor = (TextView) findViewById(R.id.value_distortion_factor);
        mDistortionFactor.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float appropriateProgress = (float) progress / 2.0f;
                String distortionFactor = appropriateProgress + "";
                mValueDistortionFactor.setText(distortionFactor);
                mSettings.save(Settings.KEY_DISTORTION_FACTOR, appropriateProgress);
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
        mScaleFactor.setMax(Settings.MAX_SCALE_FACTOR);
        mValueScaleFactor = (TextView) findViewById(R.id.value_scale_factor);
        mScaleFactor.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float appropriateProgress = (float) progress / 2.0f;
                String scaleFactor = appropriateProgress + "";
                mValueScaleFactor.setText(scaleFactor);
                mSettings.save(Settings.KEY_SCALE_FACTOR, appropriateProgress);
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
                mSettings.save(Settings.KEY_VIBRATE_APP_HOVER, isChecked);
                mLensView.invalidate();
            }
        });
        mVibrateAppLaunch = (SwitchCompat) findViewById(R.id.switch_vibrate_app_launch);
        mVibrateAppLaunch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettings.save(Settings.KEY_VIBRATE_APP_LAUNCH, isChecked);
                mLensView.invalidate();
            }
        });
        mShowNameAppHover = (SwitchCompat) findViewById(R.id.switch_show_name_app_hover);
        mShowNameAppHover.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettings.save(Settings.KEY_SHOW_NAME_APP_HOVER, isChecked);
            }
        });
        mShowTouchSelection = (SwitchCompat) findViewById(R.id.switch_show_touch_selection);
        mShowTouchSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettings.save(Settings.KEY_SHOW_TOUCH_SELECTION, isChecked);
            }
        });
        mShowNewAppTag = (SwitchCompat) findViewById(R.id.switch_show_new_app_tag);
        mShowNewAppTag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettings.save(Settings.KEY_SHOW_NEW_APP_TAG, isChecked);
            }
        });
        mTouchSelectionColor = (ImageView) findViewById(R.id.switch_show_touch_selection_color);
        mTouchSelectionColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });
        mIconPackLayout = (LinearLayout) findViewById(R.id.layout_icon_pack_chooser);
        mSelectedIconPack = (TextView) findViewById(R.id.textview_selected_icon_pack);
        mIconPackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIconPackChooserDialog();
            }
        });
    }

    private void assignValues() {


        mLensDiameter.setProgress((int) mSettings.getFloat(Settings.KEY_LENS_DIAMETER) - Settings.MIN_LENS_DIAMETER);
        String lensDiameter = (int) mSettings.getFloat(Settings.KEY_LENS_DIAMETER) + "dp";
        mValueLensDiameter.setText(lensDiameter);
        mMinIconSize.setProgress((int) mSettings.getFloat(Settings.KEY_MIN_ICON_SIZE) - Settings.MIN_MIN_ICON_SIZE);
        String minIconSize = (int) mSettings.getFloat(Settings.KEY_MIN_ICON_SIZE) + "dp";
        mValueMinIconSize.setText(minIconSize);
        mDistortionFactor.setProgress((int) (2.0f * mSettings.getFloat(Settings.KEY_DISTORTION_FACTOR)));
        String distortionFactor = mSettings.getFloat(Settings.KEY_DISTORTION_FACTOR) + "";
        mValueDistortionFactor.setText(distortionFactor);
        mScaleFactor.setProgress((int) (2.0f * mSettings.getFloat(Settings.KEY_SCALE_FACTOR)));
        String scaleFactor = mSettings.getFloat(Settings.KEY_SCALE_FACTOR) + "";
        mValueScaleFactor.setText(scaleFactor);

        mVibrateAppHover.setChecked(mSettings.getBoolean(Settings.KEY_VIBRATE_APP_HOVER));
        mVibrateAppLaunch.setChecked(mSettings.getBoolean(Settings.KEY_VIBRATE_APP_LAUNCH));
        mShowNameAppHover.setChecked(mSettings.getBoolean(Settings.KEY_SHOW_NAME_APP_HOVER));
        mShowTouchSelection.setChecked(mSettings.getBoolean(Settings.KEY_SHOW_TOUCH_SELECTION));
        mShowNewAppTag.setChecked(mSettings.getBoolean(Settings.KEY_SHOW_NEW_APP_TAG));

        mTouchSelectionColor.setImageDrawable(new ColorDrawable(Color.parseColor(mSettings.getString(Settings.KEY_TOUCH_SELECTION_COLOR))));

        mSelectedIconPack.setText(mSettings.getString(Settings.KEY_ICON_PACK_LABEL_NAME));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_show_apps:
                Intent homeIntent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                return true;
            case R.id.menu_item_about:
                Intent aboutIntent = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showColorPickerDialog() {
        mChromaDialog = new ChromaDialog.Builder()
                .initialColor(Color.parseColor(mSettings.getString(Settings.KEY_TOUCH_SELECTION_COLOR)))
                .colorMode(ColorMode.ARGB)
                .indicatorMode(IndicatorMode.HEX)
                .onColorSelected(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(@ColorInt int color) {
                        String hexColor = String.format("#%06X", (0xFFFFFFFF & color));
                        mSettings.save(Settings.KEY_TOUCH_SELECTION_COLOR, hexColor);
                        mTouchSelectionColor.setImageDrawable(new ColorDrawable(Color.parseColor(mSettings.getString(Settings.KEY_TOUCH_SELECTION_COLOR))));
                        mChromaDialog.dismiss();
                    }
                })
                .create();
        mChromaDialog.show(getSupportFragmentManager(), "ChromaDialog");
    }

    private void showIconPackChooserDialog() {
        final ArrayList<IconPackManager.IconPack> availableIconPacks = new IconPackManager().getAvailableIconPacksWithIcons(true, getApplication());
        final ArrayList<String> iconPackNames = new ArrayList<>();

        iconPackNames.add(getString(R.string.setting_default_icon_pack));
        for (int i = 0; i < availableIconPacks.size(); i++)
            iconPackNames.add(availableIconPacks.get(i).name);

        String selectedPackageName = mSettings.getString(Settings.KEY_ICON_PACK_LABEL_NAME);
        int selectedIndex = iconPackNames.indexOf(selectedPackageName);

        mIconPackChooserDialog = new MaterialDialog.Builder(SettingsActivity.this)
                .title(R.string.dialog_select_icon_pack)
                .items(iconPackNames)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        mSettings.save(Settings.KEY_ICON_PACK_LABEL_NAME, iconPackNames.get(which));
                        mSelectedIconPack.setText(iconPackNames.get(which));
                        mIconPackChooserDialog.dismiss();

                        /* Send broadcast to refresh the app drawer in background. */
                        Intent refreshHomeIntent = new Intent(SettingsActivity.this, HomeActivity.AppsUpdatedReceiver.class);
                        sendBroadcast(refreshHomeIntent);

                        return true;
                    }
                })
                .build();
        mIconPackChooserDialog.show();
    }
}