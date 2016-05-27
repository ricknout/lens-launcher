package nickrout.lenslauncher.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nickrout.lenslauncher.R;
import nickrout.lenslauncher.adapter.ArrangerDragDropAdapter;
import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.model.AppPersistent;
import nickrout.lenslauncher.util.UpdateAppsTask;

public class AppArrangerActivity extends BaseActivity implements UpdateAppsTask.UpdateAppsTaskListener {

    private static final String TAG = "AppArrangerActivity";

    @Bind(R.id.arranger_recycler_view)
    RecyclerView mRecyclerView;

    private MaterialDialog mProgressDialog;
    private ArrangerDragDropAdapter arrangerDragDropAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_arranger);
        ButterKnife.bind(this);

        loadApps(true);
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