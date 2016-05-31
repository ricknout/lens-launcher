package nickrout.lenslauncher.model;

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
    private String mName;
    private long mOpenCount;
    private int mOrderNumber;
    private boolean mAppVisible;

    private static boolean DEFAULT_APP_VISIBILITY = true;
    private static int DEFAULT_ORDER_NUMBER = -1;
    private static long DEFAULT_OPEN_COUNT = 1;
    private static final String TAG = "AppPersistent";

    public AppPersistent(String packageName, String name, long openCount, int orderNumber, boolean appVisible) {
        this.mPackageName = packageName;
        this.mName = name;
        this.mOpenCount = openCount;
        this.mOrderNumber = orderNumber;
        this.mAppVisible = appVisible;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
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
                ", mName='" + mName + '\'' +
                ", mOpenCount=" + mOpenCount +
                ", mOrderNumber=" + mOrderNumber +
                ", mAppVisible=" + mAppVisible +
                '}';
    }

    public static void incrementAppCount(String packageName, String name) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mName")).eq(name)).first();
        if (appPersistent != null) {
            appPersistent.setOpenCount(appPersistent.getOpenCount() + 1);
            appPersistent.save();
        } else {
            AppPersistent newAppPersistent = new AppPersistent(packageName, name, DEFAULT_OPEN_COUNT, DEFAULT_ORDER_NUMBER, DEFAULT_APP_VISIBILITY);
            newAppPersistent.save();
        }
    }

    public static void setOrderNumberForPackage(String packageName, String name, int orderNumber) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mName")).eq(name)).first();
        if (appPersistent != null) {
            appPersistent.setOrderNumber(orderNumber);
            appPersistent.save();
        } else {
            AppPersistent newAppPersistent = new AppPersistent(packageName, name, DEFAULT_OPEN_COUNT, DEFAULT_ORDER_NUMBER, DEFAULT_APP_VISIBILITY);
            newAppPersistent.save();
        }
    }

    public static boolean getAppVisibilityForPackage(String name) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mName")).eq(name)).first();
        if (appPersistent != null) {
            return appPersistent.isAppVisible();
        } else {
            return true;
        }
    }

    public static void setAppVisibilityForPackage(String packageName, String name, boolean mHideApp) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mName")).eq(name)).first();
        if (appPersistent != null) {
            appPersistent.setAppVisible(mHideApp);
            appPersistent.save();
        } else {
            AppPersistent newAppPersistent = new AppPersistent(packageName, name, DEFAULT_OPEN_COUNT, DEFAULT_ORDER_NUMBER, mHideApp);
            newAppPersistent.save();
        }
    }

    public static long getOpenCountByName(String name) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mName")).eq(name)).first();
        if (appPersistent != null) {
            return appPersistent.getOpenCount();
        } else {
            return 0;
        }
    }

}