package nickrout.lenslauncher.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.dragsortadapter.DragSortAdapter;
import com.makeramen.dragsortadapter.NoForegroundShadowBuilder;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nickrout.lenslauncher.R;
import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.model.AppPersistent;
import nickrout.lenslauncher.util.AppUtil;
import nickrout.lenslauncher.util.AppsSingleton;

/**
 * Created by rish on 26/5/16.
 */
public class ArrangerDragDropAdapter extends DragSortAdapter<ArrangerDragDropAdapter.MainViewHolder> {

    public static final String TAG = "ArrangerDragDropAdapter";

    private final List<App> mApps;
    private RecyclerView mRecyclerView;
    private Context mContext;

    public ArrangerDragDropAdapter(Context mContext, RecyclerView recyclerView, List<App> mApps) {
        super(recyclerView);
        this.mContext = mContext;
        this.mApps = mApps;
        this.mRecyclerView = recyclerView;
    }

    public App getItemForPosition(int position) {
        return mApps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mApps.get(position).getID();
    }

    @Override
    public int getPositionForId(long id) {
        for (int i = 0; i < mApps.size(); i++)
            if (mApps.get(i).getID() == ((int) id))
                return i;
        return -1;
    }

    @Override
    public boolean move(int fromPosition, int toPosition) {
        mApps.add(toPosition, mApps.remove(fromPosition));
        return true;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_app_arranger, parent, false);
        final MainViewHolder holder = new MainViewHolder(mContext, ArrangerDragDropAdapter.this, view);
        //view.setOnLongClickListener(holder);
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
        return super.getDraggingId();
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    @Override
    public void onDrop() {
        super.onDrop();
    }

    public List<App> getApps() {
        return mApps;
    }

    public static class MainViewHolder extends DragSortAdapter.ViewHolder implements View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {

        @Bind(R.id.element_app_container)
        CardView mContainer;

        @Bind(R.id.element_app_label)
        TextView mLabel;

        @Bind(R.id.element_app_icon)
        ImageView mIcon;

        @Bind(R.id.element_app_hide)
        ImageView mToggleAppVisibility;

        @Bind(R.id.element_app_menu)
        ImageView mMenu;

        private App mApp;
        private Context mContext;

        public MainViewHolder(Context context, final ArrangerDragDropAdapter adapter, View itemView) {
            super(adapter, itemView);
            ButterKnife.bind(this, itemView);
            this.mContext = context;
        }

        @Override
        public boolean onLongClick(@NonNull View v) {
            //startDrag();
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
            boolean isAppVisible =
                    AppPersistent.getAppVisibility(mApp.getPackageName().toString(), mApp.getName().toString());
            if (isAppVisible) {
                mToggleAppVisibility.setImageResource(R.drawable.ic_visibility_grey_24dp);
            } else {
                mToggleAppVisibility.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
            }
            if (mApp.getPackageName().toString().equals(mContext.getPackageName()))
                mToggleAppVisibility.setVisibility(View.INVISIBLE);
            else
                mToggleAppVisibility.setVisibility(View.VISIBLE);
            mContainer.postInvalidate();
        }

        public void toggleAppVisibility(App app) {
            this.mApp = app;
            boolean isAppVisible =
                    AppPersistent.getAppVisibility(mApp.getPackageName().toString(), mApp.getName().toString());
            AppPersistent.setAppVisibility(
                    mApp.getPackageName().toString(),
                    mApp.getName().toString(),
                    !isAppVisible);
            if (isAppVisible) {
                Snackbar.make(mContainer, mApp.getLabel() + " is now hidden", Snackbar.LENGTH_LONG).show();
                mToggleAppVisibility.setImageResource(R.drawable.ic_visibility_off_grey_24dp);
            } else {
                Snackbar.make(mContainer, mApp.getLabel() + " is now visible", Snackbar.LENGTH_LONG).show();
                mToggleAppVisibility.setImageResource(R.drawable.ic_visibility_grey_24dp);
            }
        }

        public void setOnClickListeners() {
            mToggleAppVisibility.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mApp != null) {
                        toggleAppVisibility(mApp);
                        AppsSingleton.getInstance().setNeedsUpdate(true);
                    } else {
                        Snackbar.make(mContainer, mContext.getString(R.string.error_app_not_found), Snackbar.LENGTH_LONG).show();
                    }
                    printAllPersistent();
                }
            });

            mMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(mContext, view);
                    popupMenu.setOnMenuItemClickListener(MainViewHolder.this);
                    if (mApp.getPackageName().equals("nickrout.lenslauncher")) {
                        popupMenu.inflate(R.menu.menu_app_lens_launcher);
                    } else {
                        popupMenu.inflate(R.menu.menu_app);
                    }
                    popupMenu.show();
                }
            });
        }

        public void printAllPersistent() {
            for (AppPersistent appPersistent : AppPersistent.listAll(AppPersistent.class)) {
                if (!appPersistent.isAppVisible())
                    Log.d(TAG, appPersistent.toString());
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_item_element_open:
                    AppUtil.launchComponent(mApp.getPackageName().toString(), mApp.getName().toString(), mContext);
                    return true;
                case R.id.menu_item_element_uninstall:
                    try {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + mApp.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(mContext, R.string.error_app_not_found, Toast.LENGTH_SHORT).show();
                    }
                    return true;
            }
            return false;
        }
    }
}