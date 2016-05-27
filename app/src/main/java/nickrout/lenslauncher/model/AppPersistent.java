package nickrout.lenslauncher.model;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.orm.util.NamingHelper;

/**
 * Created by rish on 20/5/16.
 */
public class AppPersistent extends SugarRecord {

    /* Required Default Constructor */
    public AppPersistent() {
    }

    private String mPackageName;
    private long mOpenCount;
    private int mOrderNumber;
    private boolean mAppVisible;

    private static boolean DEFAULT_APP_VISIBILITY = true;
    private static int DEFAULT_ORDER_NUMBER = -1;
    private static long DEFAULT_OPEN_COUNT = 1;
    private static final String TAG = "AppPersistent";

    public AppPersistent(String mPackageName, long mOpenCount, int mOrderNumber, boolean mAppVisible) {
        this.mPackageName = mPackageName;
        this.mOpenCount = mOpenCount;
        this.mOrderNumber = mOrderNumber;
        this.mAppVisible = mAppVisible;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public long getOpenCount() {
        return mOpenCount;
    }

    public void setOpenCount(long mOpenCount) {
        this.mOpenCount = mOpenCount;
    }

    public int getOrderNumber() {
        return mOrderNumber;
    }

    public void setOrderNumber(int mOrderNumber) {
        this.mOrderNumber = mOrderNumber;
    }

    public boolean isAppVisible() {
        return mAppVisible;
    }

    public void setAppVisible(boolean mAppVisible) {
        this.mAppVisible = mAppVisible;
    }

    @Override
    public String toString() {
        return "AppPersistent{" +
                "mPackageName='" + mPackageName + '\'' +
                ", mOpenCount=" + mOpenCount +
                ", mOrderNumber=" + mOrderNumber +
                ", mAppVisible=" + mAppVisible +
                '}';
    }

    public static void incrementAppCount(String mPackageName) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mPackageName")).eq(mPackageName)).first();
        if (appPersistent != null) {
            appPersistent.setOpenCount(appPersistent.getOpenCount() + 1);
            appPersistent.save();
        } else {
            AppPersistent newAppPersistent = new AppPersistent(mPackageName, DEFAULT_OPEN_COUNT, DEFAULT_ORDER_NUMBER, DEFAULT_APP_VISIBILITY);
            newAppPersistent.save();
        }
    }

    public static void setOrderNumberForPackage(String mPackageName, int mOrderNumber) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mPackageName")).eq(mPackageName)).first();
        if (appPersistent != null) {
            appPersistent.setOrderNumber(mOrderNumber);
            appPersistent.save();
        } else {
            AppPersistent newAppPersistent = new AppPersistent(mPackageName, DEFAULT_OPEN_COUNT, DEFAULT_ORDER_NUMBER, DEFAULT_APP_VISIBILITY);
            newAppPersistent.save();
        }
    }

    public static boolean getAppVisibilityForPackage(String mPackageName) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mPackageName")).eq(mPackageName)).first();
        if (appPersistent != null) {
            Log.d(TAG, mPackageName + " Returning " + appPersistent.isAppVisible());
            return appPersistent.isAppVisible();
        } else {
            Log.d(TAG, mPackageName + " Not found, returning true");
            return true;
        }
    }

    public static void setAppVisibilityForPackage(String mPackageName, boolean mHideApp) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mPackageName")).eq(mPackageName)).first();
        if (appPersistent != null) {
            appPersistent.setAppVisible(mHideApp);
            appPersistent.save();
            Log.d(TAG, "Saved " + mPackageName + " visibility to " + mHideApp);
        } else {
            AppPersistent newAppPersistent = new AppPersistent(mPackageName, DEFAULT_OPEN_COUNT, DEFAULT_ORDER_NUMBER, DEFAULT_APP_VISIBILITY);
            newAppPersistent.save();
            Log.d(TAG, "Created newAppPersistent");
        }
    }

    public static long getOpenCountByPackageName(String mPackageName) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mPackageName")).eq(mPackageName)).first();
        if (appPersistent != null) {
            return appPersistent.getOpenCount();
        } else {
            return 0;
        }
    }

}