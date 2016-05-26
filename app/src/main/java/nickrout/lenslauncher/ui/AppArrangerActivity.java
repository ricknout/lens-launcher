package nickrout.lenslauncher.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nickrout.lenslauncher.R;
import nickrout.lenslauncher.adapter.ArrangerDragDropAdapter;
import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.util.AppUtil;

public class AppArrangerActivity extends BaseActivity {

    private static final String TAG = "AppArrangerActivity";

    @Bind(R.id.arranger_recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_arranger);
        ButterKnife.bind(this);

        List<App> appsList = AppUtil.getApps();

        if (appsList == null)
            Snackbar.make(mRecyclerView, "Unable To Load Apps List", Snackbar.LENGTH_LONG).show();
        else {
            ArrangerDragDropAdapter arrangerDragDropAdapter = new ArrangerDragDropAdapter(mRecyclerView, appsList);
            // arrangerDragDropAdapter.setHasStableIds(false);
            mRecyclerView.setAdapter(arrangerDragDropAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }

    }
}
