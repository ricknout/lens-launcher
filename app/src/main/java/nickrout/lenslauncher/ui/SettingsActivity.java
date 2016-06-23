package nickrout.lenslauncher.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.color.ColorChooserDialog;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nickrout.lenslauncher.R;
import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.util.AppSorter;
import nickrout.lenslauncher.AppsSingleton;
import nickrout.lenslauncher.background.BroadcastReceivers;
import nickrout.lenslauncher.util.IconPackManager;
import nickrout.lenslauncher.util.LauncherUtil;
import nickrout.lenslauncher.background.LoadedObservable;
import nickrout.lenslauncher.util.Settings;

/**
 * Created by nicholasrout on 2016/06/07.
 */
public class SettingsActivity extends BaseActivity
        implements Observer, ColorChooserDialog.ColorCallback {

    private static final String TAG = "SettingsActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.fab_sort)
    FloatingActionButton mSortFab;

    @OnClick(R.id.fab_sort)
    public void onSortClicked() {
        showSortTypeDialog();
    }

    private FragmentPagerAdapter mPagerAdapter;

    private Settings mSettings;
    private ArrayList<App> mApps;
    private MaterialDialog mSortTypeDialog;
    private MaterialDialog mIconPackDialog;
    private MaterialDialog mBackgroundDialog;
    private ColorChooserDialog mBackgroundColorDialog;
    private ColorChooserDialog mHighlightColorDialog;

    public interface LensInterface {
        void onDefaultsReset();
    }
    private LensInterface mLensInterface;
    public void setLensInterface(LensInterface lensInterface) {
        mLensInterface = lensInterface;
    }

    public interface AppsInterface {
        void onDefaultsReset();
        void onAppsUpdated(ArrayList<App> apps);
    }
    private AppsInterface mAppsInterface;
    public void setAppsInterface(AppsInterface appsInterface) {
        mAppsInterface = appsInterface;
    }

    public interface SettingsInterface {
        void onDefaultsReset();
        void onValuesUpdated();
    }
    private SettingsInterface mSettingsInterface;
    public void setSettingsInterface(SettingsInterface settingsInterface) {
        mSettingsInterface = settingsInterface;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mSortFab.hide();
        setSupportActionBar(mToolbar);
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), SettingsActivity.this);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        mSortFab.show();
                        break;
                    default:
                        mSortFab.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mSettings = new Settings(this);
        mApps = AppsSingleton.getInstance().getApps();
        LoadedObservable.getInstance().addObserver(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (AppsSingleton.getInstance().doesNeedUpdate()) {
            AppsSingleton.getInstance().setNeedsUpdate(false);
            sendEditAppsBroadcast();
        }
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
                if (LauncherUtil.isLauncherDefault(getApplication())) {
                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory(Intent.CATEGORY_HOME);
                    startActivity(homeIntent);
                } else {
                    Intent homeIntent = new Intent(SettingsActivity.this, HomeActivity.class);
                    startActivity(homeIntent);
                }
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.menu_item_about:
                Intent aboutIntent = new Intent(SettingsActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            case R.id.menu_item_reset_default_settings:
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        if (mLensInterface != null) {
                            mLensInterface.onDefaultsReset();
                        }
                        break;
                    case 1:
                        if (mAppsInterface != null) {
                            mAppsInterface.onDefaultsReset();
                        }
                         break;
                    case 2:
                        if (mSettingsInterface != null) {
                            mSettingsInterface.onDefaultsReset();
                        }
                        break;
                }
                Snackbar.make(mToolbar, getString(R.string.snackbar_reset_successful), Snackbar.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        mApps = AppsSingleton.getInstance().getApps();
        if (mAppsInterface != null) {
            mAppsInterface.onAppsUpdated(mApps);
        }
    }

    private void sendUpdateAppsBroadcast() {
        Intent refreshAppsIntent = new Intent(SettingsActivity.this, BroadcastReceivers.AppsUpdatedReceiver.class);
        sendBroadcast(refreshAppsIntent);
    }

    private void sendEditAppsBroadcast() {
        Intent editAppsIntent = new Intent(SettingsActivity.this, BroadcastReceivers.AppsEditedReceiver.class);
        sendBroadcast(editAppsIntent);
    }

    private void showSortTypeDialog() {
        final List<AppSorter.SortType> sortTypes = new ArrayList<>(EnumSet.allOf(AppSorter.SortType.class));
        final List<String> sortTypeStrings = new ArrayList<>();
        for (int i = 0; i < sortTypes.size(); i++) {
            sortTypeStrings.add(getApplicationContext().getString(sortTypes.get(i).getDisplayNameResId()));
        }
        AppSorter.SortType selectedSortType = mSettings.getSortType();
        int selectedIndex = sortTypes.indexOf(selectedSortType);
        mSortTypeDialog = new MaterialDialog.Builder(SettingsActivity.this)
                .title(R.string.setting_sort_apps)
                .items(sortTypeStrings)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        mSettings.save(sortTypes.get(which));
                        sendEditAppsBroadcast();
                        return true;
                    }
                })
                .show();
    }

    public void showIconPackDialog() {
        final ArrayList<IconPackManager.IconPack> availableIconPacks =
                new IconPackManager().getAvailableIconPacksWithIcons(true, getApplication());
        final ArrayList<String> iconPackNames = new ArrayList<>();
        iconPackNames.add(getString(R.string.setting_default_icon_pack));
        for (int i = 0; i < availableIconPacks.size(); i++) {
            if (iconPackNames.size() > 0 && !iconPackNames.contains(availableIconPacks.get(i).mName)) {
                iconPackNames.add(availableIconPacks.get(i).mName);
            }
        }
        String selectedPackageName = mSettings.getString(Settings.KEY_ICON_PACK_LABEL_NAME);
        int selectedIndex = iconPackNames.indexOf(selectedPackageName);
        mIconPackDialog = new MaterialDialog.Builder(SettingsActivity.this)
                .title(R.string.setting_icon_pack)
                .items(iconPackNames)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        mSettings.save(Settings.KEY_ICON_PACK_LABEL_NAME, iconPackNames.get(which));
                        if (mSettingsInterface != null) {
                            mSettingsInterface.onValuesUpdated();
                        }
                        sendUpdateAppsBroadcast();
                        return true;
                    }
                })
                .show();
    }

    public void showHomeLauncherChooser() {
        LauncherUtil.resetPreferredLauncherAndOpenChooser(getApplicationContext());
    }

    public void showBackgroundDialog() {
        String[] availableBackgrounds = getResources().getStringArray(R.array.backgrounds);
        final ArrayList<String> backgroundNames = new ArrayList<>();
        for (int i = 0; i < availableBackgrounds.length; i++) {
            backgroundNames.add(availableBackgrounds[i]);
        }
        String selectedBackground = mSettings.getString(Settings.KEY_BACKGROUND);
        int selectedIndex = backgroundNames.indexOf(selectedBackground);
        mBackgroundDialog = new MaterialDialog.Builder(SettingsActivity.this)
                .title(R.string.setting_background)
                .items(R.array.backgrounds)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        String selection = backgroundNames.get(which);
                        if (selection.equals("Wallpaper")) {
                            mSettings.save(Settings.KEY_BACKGROUND, selection);
                            if (mSettingsInterface != null) {
                                mSettingsInterface.onValuesUpdated();
                            }
                            dismissBackgroundDialog();
                            showWallpaperPicker();

                        } else if (selection.equals("Color")) {
                            dismissBackgroundDialog();
                            showBackgroundColorDialog();
                        }
                        return true;
                    }
                })
                .show();
    }

    public void showWallpaperPicker() {
        Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
        startActivity(Intent.createChooser(intent, "Select Wallpaper"));
    }

    public void showBackgroundColorDialog() {
        mBackgroundColorDialog = new ColorChooserDialog.Builder(this, R.string.setting_background_color)
                .titleSub(R.string.setting_background_color)
                .accentMode(false)
                .doneButton(R.string.md_done_label)
                .cancelButton(R.string.md_cancel_label)
                .backButton(R.string.md_back_label)
                .preselect(Color.parseColor(mSettings.getString(Settings.KEY_BACKGROUND_COLOR)))
                .dynamicButtonColor(false)
                .allowUserColorInputAlpha(false)
                .show();
    }

    public void showHighlightColorDialog() {
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
        if (mBackgroundColorDialog!= null && mBackgroundColorDialog.getId() == dialog.getId()) {
            mSettings.save(Settings.KEY_BACKGROUND, "Color");
            mSettings.save(Settings.KEY_BACKGROUND_COLOR, hexColor);
        } else if (mHighlightColorDialog!= null && mHighlightColorDialog.getId() == dialog.getId()) {
            mSettings.save(Settings.KEY_TOUCH_SELECTION_COLOR, hexColor);
        }
        if (mSettingsInterface != null) {
            mSettingsInterface.onValuesUpdated();
        }
    }

    private void dismissSortTypeDialog() {
        if (mSortTypeDialog != null && mSortTypeDialog.isShowing()) {
            mSortTypeDialog.dismiss();
        }
    }

    private void dismissIconPackDialog() {
        if (mIconPackDialog != null && mIconPackDialog.isShowing()) {
            mIconPackDialog.dismiss();
        }
    }

    private void dismissBackgroundDialog() {
        if (mBackgroundDialog != null && mBackgroundDialog.isShowing()) {
            mBackgroundDialog.dismiss();
        }
    }

    private void dismissAllDialogs() {
        dismissSortTypeDialog();
        dismissIconPackDialog();
        dismissBackgroundDialog();
        // Color dialogs do not need to be dismissed
    }

    @Override
    protected void onDestroy() {
        dismissAllDialogs();
        LoadedObservable.getInstance().deleteObserver(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private class FragmentPagerAdapter extends FragmentStatePagerAdapter {

        private static final int NUM_PAGES = 3;

        private Context mContext;

        public FragmentPagerAdapter(FragmentManager fragmentManager, Context context) {
            super(fragmentManager);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    LensFragment lensFragment = LensFragment.newInstance();
                    return lensFragment;
                case 1:
                    AppsFragment appsFragment = AppsFragment.newInstance();
                    return appsFragment;
                case 2:
                    SettingsFragment settingsFragment = SettingsFragment.newInstance();
                    return settingsFragment;
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mContext.getResources().getString(R.string.tab_lens);
                case 1:
                    return mContext.getResources().getString(R.string.tab_apps);
                case 2:
                    return mContext.getResources().getString(R.string.tab_settings);
            }
            return super.getPageTitle(position);
        }
    }
}
