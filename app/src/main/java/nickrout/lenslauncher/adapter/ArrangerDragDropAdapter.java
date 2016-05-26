package nickrout.lenslauncher.adapter;

import android.graphics.Point;
import android.support.annotation.NonNull;
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

/**
 * Created by rish on 26/5/16.
 */
public class ArrangerDragDropAdapter extends DragSortAdapter<ArrangerDragDropAdapter.MainViewHolder> {

    public static final String TAG = "ArrangerDragDropAdapter";
    private final List<App> appData;

    public ArrangerDragDropAdapter(RecyclerView recyclerView, List<App> appData) {
        super(recyclerView);
        this.appData = appData;
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
        MainViewHolder holder = new MainViewHolder(this, view);
        view.setOnClickListener(holder);
        view.setOnLongClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        App app = appData.get(position);
        holder.mLabel.setText(app.getLabel());
        Log.d(TAG, "Setting Name = " + app.getLabel());

        // NOTE: check for getDraggingId() match to set an "invisible space" while dragging

        if (getDraggingId() == getPositionForId(position))
            Log.d(TAG, "shouldSetInvisibleSpace? " + getDraggingId() + " == " + getPositionForId(position));

        holder.mContainer.setVisibility(getDraggingId() == getPositionForId(position) ? View.INVISIBLE : View.VISIBLE);
        holder.mContainer.postInvalidate();
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
        Log.d(TAG, "Save the ordering of the apps here");
    }

    static class MainViewHolder extends DragSortAdapter.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @Bind(R.id.element_app_container)
        CardView mContainer;

        @Bind(R.id.element_app_label)
        TextView mLabel;

        @Bind(R.id.element_app_icon)
        ImageView mIcon;

        public MainViewHolder(DragSortAdapter adapter, View itemView) {
            super(adapter, itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(@NonNull View v) {

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