package nickrout.lenslauncher.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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
import com.afollestad.materialdialogs.color.ColorChooserDialog;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import nickrout.lenslauncher.R;
import nickrout.lenslauncher.util.AppsSingleton;
import nickrout.lenslauncher.util.IconPackManager;
import nickrout.lenslauncher.util.ObservableObject;
import nickrout.lenslauncher.util.Settings;

/**
 * Created by nickrout on 2016/04/02.
 */
public class SettingsActivity extends BaseActivity
        implements Observer, ColorChooserDialog.ColorCallback {

    private static final String TAG = "SettingsActivity";

    private LensView mLensView;

    private AppCompatSeekBar mMinIconSize;
    private TextView mValueMinIconSize;
    private AppCompatSeekBar mDistortionFactor;
    private TextView mValueDistortionFactor;
    private AppCompatSeekBar mScaleFactor;
    private TextView mValueScaleFactor;
    private AppCompatSeekBar mAnimationTime;
    private TextView mValueAnimationTime;
    private ImageView mHighlightColor;
    private LinearLayout mIconPackLayout;
    private TextView mSelectedIconPack;
    private TextView mSelectedAppSort;
    private LinearLayout mAppArrangerLayout;

    private SwitchCompat mVibrateAppHover;
    private SwitchCompat mVibrateAppLaunch;
    private SwitchCompat mShowNameAppHover;
    private SwitchCompat mShowTouchSelection;
    private SwitchCompat mShowNewAppTag;

    private ColorChooserDialog mHighlightColorDialog;
    private MaterialDialog mIconPackChooserDialog;

    private Settings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mSettings = new Settings(this);
        setupViews();
        ObservableObject.getInstance().addObserver(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        assignValues();
    }

    private void setupViews() {

        mLensView = (LensView) findViewById(R.id.lens_view_settings);
        mLensView.setDrawType(LensView.DrawType.CIRCLES);

        mMinIconSize = (AppCompatSeekBar) findViewById(R.id.seek_bar_min_icon_size);
        mMinIconSize.setMax(Settings.MAX_MIN_ICON_SIZE);
        mValueMinIconSize = (TextView) findViewById(R.id.value_min_icon_size);
        mMinIconSize.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int appropriateProgress = progress + (int) Settings.MIN_MIN_ICON_SIZE;
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
                float appropriateProgress = (float) progress / 2.0f + Settings.MIN_DISTORTION_FACTOR;
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
                float appropriateProgress = (float) progress / 2.0f + Settings.MIN_SCALE_FACTOR;
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
        mAnimationTime = (AppCompatSeekBar) findViewById(R.id.seek_bar_animation_time);
        mAnimationTime.setMax(Settings.MAX_ANIMATION_TIME);
        mValueAnimationTime = (TextView) findViewById(R.id.value_animation_time);
        mAnimationTime.setOnSeekBarChangeListener(new AppCompatSeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                long appropriateProgress = (long) progress / 2 + Settings.MIN_ANIMATION_TIME;
                String animationTime = appropriateProgress + "ms";
                mValueAnimationTime.setText(animationTime);
                mSettings.save(Settings.KEY_ANIMATION_TIME, appropriateProgress);
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
        mHighlightColor = (ImageView) findViewById(R.id.selector_highlight_color);
        mHighlightColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialog();
            }
        });
        mIconPackLayout = (LinearLayout) findViewById(R.id.layout_icon_pack_chooser);
        mSelectedIconPack = (TextView) findViewById(R.id.textview_selected_icon_pack);
        mSelectedAppSort = (TextView) findViewById(R.id.textview_selected_app_sort);
        mIconPackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIconPackChooserDialog();
            }
        });
        mAppArrangerLayout = (LinearLayout) findViewById(R.id.layout_app_arranger);
        mAppArrangerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, AppArrangerActivity.class));
            }
        });
    }

    private void assignValues() {

        mMinIconSize.setProgress((int) mSettings.getFloat(Settings.KEY_MIN_ICON_SIZE) - (int) Settings.MIN_MIN_ICON_SIZE);
        String minIconSize = (int) mSettings.getFloat(Settings.KEY_MIN_ICON_SIZE) + "dp";
        mValueMinIconSize.setText(minIconSize);
        mDistortionFactor.setProgress((int) (2.0f * (mSettings.getFloat(Settings.KEY_DISTORTION_FACTOR) - Settings.MIN_DISTORTION_FACTOR)));
        String distortionFactor = mSettings.getFloat(Settings.KEY_DISTORTION_FACTOR) + "";
        mValueDistortionFactor.setText(distortionFactor);
        mScaleFactor.setProgress((int) (2.0f * (mSettings.getFloat(Settings.KEY_SCALE_FACTOR) - Settings.MIN_SCALE_FACTOR)));
        String scaleFactor = mSettings.getFloat(Settings.KEY_SCALE_FACTOR) + "";
        mValueScaleFactor.setText(scaleFactor);
        mAnimationTime.setProgress((int) (2 * (mSettings.getLong(Settings.KEY_ANIMATION_TIME) - Settings.MIN_ANIMATION_TIME)));
        String animationTime = mSettings.getLong(Settings.KEY_ANIMATION_TIME) + "ms";
        mValueAnimationTime.setText(animationTime);
        setSelectedIconPackText();
        mSelectedAppSort.setText(getString(mSettings.getSortType().getDisplayNameResId()));
        setHighlightColorDrawable();
        mVibrateAppHover.setChecked(mSettings.getBoolean(Settings.KEY_VIBRATE_APP_HOVER));
        mVibrateAppLaunch.setChecked(mSettings.getBoolean(Settings.KEY_VIBRATE_APP_LAUNCH));
        mShowNameAppHover.setChecked(mSettings.getBoolean(Settings.KEY_SHOW_NAME_APP_HOVER));
        mShowTouchSelection.setChecked(mSettings.getBoolean(Settings.KEY_SHOW_TOUCH_SELECTION));
        mShowNewAppTag.setChecked(mSettings.getBoolean(Settings.KEY_SHOW_NEW_APP_TAG));
    }

    private void setHighlightColorDrawable() {
        GradientDrawable colorDrawable = new GradientDrawable();
        colorDrawable.setColor(Color.parseColor(mSettings.getString(Settings.KEY_TOUCH_SELECTION_COLOR)));
        colorDrawable.setCornerRadius(getResources().getDimension(R.dimen.radius_highlight_color_switch));
        mHighlightColor.setImageDrawable(colorDrawable);
    }

    private void setSelectedIconPackText() {
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
            case R.id.menu_item_reset_default:
                resetToDefault();
                assignValues();
                Snackbar.make(mLensView, getString(R.string.snackbar_reset_successful), Snackbar.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showColorPickerDialog() {
        mHighlightColorDialog = new ColorChooserDialog.Builder(this, R.string.setting_highlight_color)
                .titleSub(R.string.setting_highlight_color)
                .accentMode(true)
                .doneButton(R.string.md_done_label)
                .cancelButton(R.string.md_cancel_label)
                .backButton(R.string.md_back_label)
                .preselect(Color.parseColor(mSettings.getString(Settings.KEY_TOUCH_SELECTION_COLOR)))
                .dynamicButtonColor(false)
                .allowUserColorInputAlpha(false)
                .show();
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        String hexColor = String.format("#%06X", (0xFFFFFFFF & selectedColor));
        mSettings.save(Settings.KEY_TOUCH_SELECTION_COLOR, hexColor);
        setHighlightColorDrawable();
    }

    private void showIconPackChooserDialog() {
        final ArrayList<IconPackManager.IconPack> availableIconPacks = new IconPackManager().getAvailableIconPacksWithIcons(true, getApplication());
        final ArrayList<String> iconPackNames = new ArrayList<>();

        iconPackNames.add(getString(R.string.setting_default_icon_pack));
        for (int i = 0; i < availableIconPacks.size(); i++)
            iconPackNames.add(availableIconPacks.get(i).mName);

        String selectedPackageName = mSettings.getString(Settings.KEY_ICON_PACK_LABEL_NAME);
        int selectedIndex = iconPackNames.indexOf(selectedPackageName);

        mIconPackChooserDialog = new MaterialDialog.Builder(SettingsActivity.this)
                .title(R.string.setting_icon_pack)
                .items(iconPackNames)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        mSettings.save(Settings.KEY_ICON_PACK_LABEL_NAME, iconPackNames.get(which));
                        setSelectedIconPackText();
                        dismissIconPackChooserDialog();
                        AppsSingleton.getInstance().setNeedsUpdate(true);
                        /* Send broadcast to refresh the app drawer in background. */
                        Intent refreshHomeIntent = new Intent(SettingsActivity.this, HomeActivity.AppsUpdatedReceiver.class);
                        sendBroadcast(refreshHomeIntent);

                        return true;
                    }
                })
                .show();
    }

    private void dismissIconPackChooserDialog() {
        if (mIconPackChooserDialog != null && mIconPackChooserDialog.isShowing()) {
            mIconPackChooserDialog.dismiss();
        }
    }

    private void resetToDefault() {
        mSettings.save(Settings.KEY_MIN_ICON_SIZE, Settings.DEFAULT_MIN_ICON_SIZE);
        mSettings.save(Settings.KEY_DISTORTION_FACTOR, Settings.DEFAULT_DISTORTION_FACTOR);
        mSettings.save(Settings.KEY_SCALE_FACTOR, Settings.DEFAULT_SCALE_FACTOR);
        mSettings.save(Settings.KEY_ANIMATION_TIME, Settings.DEFAULT_ANIMATION_TIME);
        mSettings.save(Settings.KEY_VIBRATE_APP_HOVER, Settings.DEFAULT_VIBRATE_APP_HOVER);
        mSettings.save(Settings.KEY_VIBRATE_APP_LAUNCH, Settings.DEFAULT_VIBRATE_APP_LAUNCH);
        mSettings.save(Settings.KEY_SHOW_NAME_APP_HOVER, Settings.DEFAULT_SHOW_NAME_APP_HOVER);
        mSettings.save(Settings.KEY_SHOW_TOUCH_SELECTION, Settings.DEFAULT_SHOW_TOUCH_SELECTION);
        mSettings.save(Settings.KEY_SHOW_NEW_APP_TAG, Settings.DEFAULT_SHOW_NEW_APP_TAG);
        mSettings.save(Settings.KEY_TOUCH_SELECTION_COLOR, Settings.DEFAULT_TOUCH_SELECTION_COLOR);
        mSettings.save(Settings.KEY_ICON_PACK_LABEL_NAME, Settings.DEFAULT_ICON_PACK_LABEL_NAME);
        mSettings.save(Settings.KEY_SORT_TYPE, Settings.DEFAULT_SORT_TYPE);
    }

    @Override
    protected void onDestroy() {
        dismissIconPackChooserDialog();
        ObservableObject.getInstance().deleteObserver(this);
        super.onDestroy();
    }

    @Override
    public void update(Observable observable, Object data) {
        AppsSingleton.getInstance().setNeedsUpdate(true);
    }
}