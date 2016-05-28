package nickrout.lenslauncher.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import nickrout.lenslauncher.R;
import nickrout.lenslauncher.adapter.ArrangerDragDropAdapter;
import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.model.AppPersistent;
import nickrout.lenslauncher.util.AppSorter;
import nickrout.lenslauncher.util.Settings;
import nickrout.lenslauncher.util.UpdateAppsTask;

public class AppArrangerActivity extends BaseActivity implements UpdateAppsTask.UpdateAppsTaskListener {

    private static final String TAG = "AppArrangerActivity";

    @Bind(R.id.arranger_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.arranger_sort_fab)
    FloatingActionButton mSortFab;

    private MaterialDialog mProgressDialog;
    private MaterialDialog mSortTypeChooserDialog;
    private ArrangerDragDropAdapter arrangerDragDropAdapter;

    private Settings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_arranger);
        ButterKnife.bind(this);
        mSettings = new Settings(this);

        loadApps(true);
    }

    @OnClick(R.id.arranger_sort_fab)
    public void onSortClicked() {

        final List<AppSorter.SORT_TYPE> sortTypes = new ArrayList<>(EnumSet.allOf(AppSorter.SORT_TYPE.class));

        AppSorter.SORT_TYPE selectedSortType = mSettings.getSortType();
        int selectedIndex = sortTypes.indexOf(selectedSortType);

        mSortTypeChooserDialog = new MaterialDialog.Builder(AppArrangerActivity.this)
                .title(R.string.setting_icon_pack)
                .items(sortTypes)
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        mSettings.save(sortTypes.get(which));

                        loadApps(true);

                        /* Send broadcast to refresh the app drawer in background. */
                        Intent refreshHomeIntent = new Intent(AppArrangerActivity.this, HomeActivity.AppsUpdatedReceiver.class);
                        sendBroadcast(refreshHomeIntent);

                        return true;
                    }
                })
                .show();

    }

    private void loadApps(boolean isLoad) {
        new UpdateAppsTask(getPackageManager(), getApplicationContext(), getApplication(), isLoad, AppArrangerActivity.this).execute();
    }

    @Override
    public void onUpdateAppsTaskPreExecute(boolean isLoad) {
        if (isLoad)
            showProgressDialog();
    }

    @Override
    public void onUpdateAppsTaskPostExecute(ArrayList<App> mApps, ArrayList<Bitmap> mAppIcons) {
        dismissProgressDialog();
        arrangerDragDropAdapter = new ArrangerDragDropAdapter(AppArrangerActivity.this, mRecyclerView, mApps);

        mRecyclerView.setAdapter(arrangerDragDropAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new MaterialDialog.Builder(this)
                    .content(R.string.progress_loading_apps)
                    .progress(true, 0)
                    .cancelable(false)
                    .canceledOnTouchOutside(false)
                    .show();
        }
    }

    private void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        saveToPersistence();
        super.onDestroy();
    }

    private void saveToPersistence() {
        final List<App> appData = arrangerDragDropAdapter.getAppData();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < appData.size(); i++)
                    AppPersistent.setOrderNumberForPackage(appData.get(i).getPackageName().toString(), i);
            }
        });
        thread.start();
        /* Send broadcast to refresh the app drawer in background. */
        Intent refreshHomeIntent = new Intent(AppArrangerActivity.this, HomeActivity.AppsUpdatedReceiver.class);
        sendBroadcast(refreshHomeIntent);
    }
}