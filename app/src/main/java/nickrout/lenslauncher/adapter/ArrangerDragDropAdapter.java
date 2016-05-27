package nickrout.lenslauncher.adapter;

import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.dragsortadapter.DragSortAdapter;
import com.makeramen.dragsortadapter.NoForegroundShadowBuilder;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nickrout.lenslauncher.R;
import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.model.AppPersistent;

/**
 * Created by rish on 26/5/16.
 */
public class ArrangerDragDropAdapter extends DragSortAdapter<ArrangerDragDropAdapter.MainViewHolder> {

    public static final String TAG = "ArrangerDragDropAdapter";
    private final List<App> appData;
    private RecyclerView mRecyclerView;

    public ArrangerDragDropAdapter(RecyclerView recyclerView, List<App> appData) {
        super(recyclerView);
        this.appData = appData;
        this.mRecyclerView = recyclerView;
    }

    public App getItemForPosition(int position) {
        return appData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return appData.get(position).getID();
    }

    @Override
    public int getPositionForId(long id) {
        for (int i = 0; i < appData.size(); i++)
            if (appData.get(i).getID() == ((int) id))
                return i;
        return -1;
    }

    @Override
    public boolean move(int fromPosition, int toPosition) {
        appData.add(toPosition, appData.remove(fromPosition));
        return true;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.element_app_arranger, parent, false);
        final MainViewHolder holder = new MainViewHolder(ArrangerDragDropAdapter.this, view);
        view.setOnLongClickListener(holder);
        holder.setOnClickListeners();
        return holder;
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {
        App app = getItemForPosition(position);

        // NOTE: check for getDraggingId() match to set an "invisible space" while dragging
        holder.mContainer.setVisibility(getDraggingId() == getPositionForId(position) ? View.INVISIBLE : View.VISIBLE);
        holder.mContainer.postInvalidate();

        holder.setAppElement(app);
    }

    @Override
    public long getDraggingId() {
        if (super.getDraggingId() != -1)
            Log.d(TAG, "getDraggingId() called with: " + " " + super.getDraggingId());
        return super.getDraggingId();
    }

    @Override
    public int getItemCount() {
        return appData.size();
    }

    @Override
    public void onDrop() {
        super.onDrop();
    }

    public List<App> getAppData() {
        return appData;
    }

    public static class MainViewHolder extends DragSortAdapter.ViewHolder implements View.OnLongClickListener {

        @Bind(R.id.element_app_container)
        CardView mContainer;

        @Bind(R.id.element_app_label)
        TextView mLabel;

        @Bind(R.id.element_app_icon)
        ImageView mIcon;

        @Bind(R.id.element_app_hide)
        ImageView mHideApp;

        private App mApp;

        public MainViewHolder(final ArrangerDragDropAdapter adapter, View itemView) {
            super(adapter, itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public boolean onLongClick(@NonNull View v) {
            startDrag();
            return true;
        }

        @Override
        public View.DragShadowBuilder getShadowBuilder(View itemView, Point touchPoint) {
            return new NoForegroundShadowBuilder(itemView, touchPoint);
        }

        public void setAppElement(App app) {
            this.mApp = app;
            mLabel.setText(mApp.getLabel());
            mIcon.setImageBitmap(mApp.getIcon());
            boolean isAppVisible = AppPersistent.getAppVisibilityForPackage(mApp.getPackageName().toString());
            if (isAppVisible) {
                mHideApp.setImageResource(R.drawable.ic_visible);
            } else {
                mHideApp.setImageResource(R.drawable.ic_invisible);
                Log.d(TAG, "setAppElement() called with: " + "app = [" + app.toString() + "]");
            }
            mContainer.postInvalidate();
        }

        public void toggleAppVisibility(App app) {
            boolean isAppVisible = AppPersistent.getAppVisibilityForPackage(app.getPackageName().toString());
            AppPersistent.setAppVisibilityForPackage(app.getPackageName().toString(), !isAppVisible);
            if (isAppVisible) {
                Snackbar.make(mContainer, app.getLabel() + " is now hidden", Snackbar.LENGTH_LONG).show();
                mHideApp.setImageResource(R.drawable.ic_invisible);
            } else {
                Snackbar.make(mContainer, app.getLabel() + " is now visible", Snackbar.LENGTH_LONG).show();
                mHideApp.setImageResource(R.drawable.ic_visible);
            }
        }

        public void setOnClickListeners() {
            mHideApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    printAllPersistent();
                    if (mApp != null)
                        toggleAppVisibility(mApp);
                    else
                        Snackbar.make(mContainer, "Error in Opening App", Snackbar.LENGTH_LONG).show();
                }
            });
        }

        public void printAllPersistent() {
            for (AppPersistent appPersistent : AppPersistent.listAll(AppPersistent.class)) {
                if (!appPersistent.isAppVisible())
                    Log.d(TAG, appPersistent.toString());
            }
        }
    }
}