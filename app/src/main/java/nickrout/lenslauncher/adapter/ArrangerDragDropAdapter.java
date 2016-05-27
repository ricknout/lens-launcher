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
        MainViewHolder holder = new MainViewHolder(ArrangerDragDropAdapter.this, view);
        view.setOnLongClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {
        final App app = appData.get(position);
        holder.mLabel.setText(app.getLabel());
        Log.d(TAG, "Setting Name = " + app.getLabel());

        // NOTE: check for getDraggingId() match to set an "invisible space" while dragging

        if (getDraggingId() == getPositionForId(position))
            Log.d(TAG, "shouldSetInvisibleSpace? " + getDraggingId() + " == " + getPositionForId(position));

        holder.mContainer.setVisibility(getDraggingId() == getPositionForId(position) ? View.INVISIBLE : View.VISIBLE);
        holder.mContainer.postInvalidate();

        holder.mHideApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleAppVisibility(app, holder);
            }
        });
    }

    @Override
    public long getDraggingId() {
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

    private void toggleAppVisibility(App app, MainViewHolder holder) {
        boolean isAppVisible = AppPersistent.getHideAppForPackage(app.getPackageName().toString());
        AppPersistent.setHideAppForPackage(app.getPackageName().toString(), !isAppVisible);
        if (!isAppVisible) {
            Snackbar.make(holder.mContainer, app.getLabel() + " is now hidden", Snackbar.LENGTH_LONG).show();
            holder.mHideApp.setImageResource(R.drawable.ic_invisible);
        } else {
            Snackbar.make(holder.mContainer, app.getLabel() + " is now visible", Snackbar.LENGTH_LONG).show();
            holder.mHideApp.setImageResource(R.drawable.ic_visible);
        }
    }

    static class MainViewHolder extends DragSortAdapter.ViewHolder implements View.OnLongClickListener {

        @Bind(R.id.element_app_container)
        CardView mContainer;

        @Bind(R.id.element_app_label)
        TextView mLabel;

        @Bind(R.id.element_app_icon)
        ImageView mIcon;

        @Bind(R.id.element_app_hide)
        ImageView mHideApp;

        private ArrangerDragDropAdapter arrangerDragDropAdapter;

        public MainViewHolder(final ArrangerDragDropAdapter adapter, View itemView) {
            super(adapter, itemView);
            ButterKnife.bind(this, itemView);
            this.arrangerDragDropAdapter = adapter;
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

    }
}