package com.nickrout.testlauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;

import java.util.ArrayList;

/**
 * Created by nicholasrout on 2016/03/28.
 */
public class AppAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, String, AppDetail, String> {

    private Context mContext;
    private PackageManager mPackageManager;

    public AppAdapter(Context context, PackageManager packageManager, ArrayList<AppDetail> apps) {
        this.mContext = context;
        this.mPackageManager = packageManager;
        setItems(apps);
    }

    public void updateItems(ArrayList<AppDetail> apps) {
        setItems(apps);
        notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View appView = inflater.inflate(R.layout.recycler_item_app, parent, false);
        return new AppViewHolder(appView, mContext);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Do Nothing
    }

    @Override protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        AppDetail app = getItem(position);
        AppViewHolder appViewHolder = (AppViewHolder) holder;
        appViewHolder.render(app);
    }

    @Override protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Do Nothing
    }

    private class AppViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;
        private RelativeLayout mLayout;
        private ImageView mIcon;
        private TextView mLabel;
        private TextView mName;

        public AppViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            mLayout = (RelativeLayout) itemView.findViewById(R.id.layout_app);
            mIcon = (ImageView) itemView.findViewById(R.id.image_view_icon);
            mLabel = (TextView) itemView.findViewById(R.id.text_view_label);
            mName = (TextView) itemView.findViewById(R.id.text_view_name);
        }

        public void render(AppDetail app) {
            mLayout.setOnClickListener(new OnAppClick(mContext, app.getName().toString()));
            mIcon.setImageDrawable(app.getIcon());
            mLabel.setText(app.getLabel());
            mName.setText(app.getName());
        }
    }

    private class OnAppClick implements View.OnClickListener {

        private Context mContext;
        private String mAppName;

        public OnAppClick(Context context, String appName) {
            this.mContext = context;
            this.mAppName = appName;
        }

        @Override
        public void onClick(View v) {
            Intent intent = mPackageManager.getLaunchIntentForPackage(mAppName);
            mContext.startActivity(intent);
        }
    }
}
