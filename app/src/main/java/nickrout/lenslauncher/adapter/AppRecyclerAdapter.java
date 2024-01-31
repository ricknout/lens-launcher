package nickrout.lenslauncher.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nickrout.lenslauncher.R;
import nickrout.lenslauncher.background.BroadcastReceivers;
import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.model.AppPersistent;
import nickrout.lenslauncher.ui.SettingsActivity;
import nickrout.lenslauncher.util.AppUtil;

public class AppRecyclerAdapter extends RecyclerView.Adapter {

    public static final String TAG = "AppRecyclerAdapter";

    private Context mContext;
    private final List<App> mApps;

    public AppRecyclerAdapter(Context mContext, List<App> mApps) {
        this.mContext = mContext;
        this.mApps = mApps;
    }

    public App getItemForPosition(int position) {
        return mApps.get(position);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    @Override
    public long getItemId(int position) {
        return mApps.get(position).getId();
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_item_app, parent, false);
        final AppViewHolder holder = new AppViewHolder(view, mContext);
        holder.setOnClickListeners();
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        App app = getItemForPosition(position);
        if (app == null) {
            return;
        }
        AppViewHolder appViewHolder = (AppViewHolder) holder;
        appViewHolder.setAppElement(app);
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

        @BindView(R.id.element_app_container)
        CardView mContainer;

        @BindView(R.id.element_app_label)
        TextView mLabel;

        @BindView(R.id.element_app_icon)
        ImageView mIcon;

        @BindView(R.id.element_app_hide)
        ImageView mToggleAppVisibility;

        @BindView(R.id.element_app_menu)
        ImageView mMenu;

        private App mApp;
        private Context mContext;

        public AppViewHolder(View itemView, Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mContext = context;
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
            if (mApp.getPackageName().toString().equals("nickrout.lenslauncher")) {
                mToggleAppVisibility.setVisibility(View.INVISIBLE);
            } else {
                mToggleAppVisibility.setVisibility(View.VISIBLE);
            }
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

        private void sendChangeAppsVisibilityBroadcast() {
            if (mContext == null) {
                return;
            }
            if (!(mContext instanceof SettingsActivity)) {
                return;
            }
            SettingsActivity settingsActivity = (SettingsActivity) mContext;
            Intent changeAppsVisibilityIntent = new Intent(settingsActivity, BroadcastReceivers.AppsVisibilityChangedReceiver.class);
            settingsActivity.sendBroadcast(changeAppsVisibilityIntent);
        }

        public void setOnClickListeners() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppUtil.launchComponent(
                            mContext,
                            mApp.getPackageName().toString(), mApp.getName().toString(),
                            itemView, new Rect(0, 0, itemView.getMeasuredWidth(), itemView.getMeasuredHeight()));
                }
            });
            mToggleAppVisibility.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mApp != null) {
                        sendChangeAppsVisibilityBroadcast();
                        toggleAppVisibility(mApp);
                    } else {
                        Snackbar.make(mContainer, mContext.getString(R.string.error_app_not_found), Snackbar.LENGTH_LONG).show();
                    }
                }
            });

            mMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(mContext, view);
                    popupMenu.setOnMenuItemClickListener(AppViewHolder.this);
                    popupMenu.inflate(R.menu.menu_app);
                    popupMenu.show();
                }
            });
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_item_element_app_info:
                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + mApp.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(mContext, R.string.error_app_not_found, Toast.LENGTH_SHORT).show();
                    }
                    return true;
                case R.id.menu_item_element_uninstall:
                    try {
                        Intent intent = new Intent(Intent.ACTION_DELETE);
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
