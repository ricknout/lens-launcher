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
    private long mOpenCount;
    private int mOrderNumber;
    private boolean mHideApp;

    public AppPersistent(String mPackageName, long mOpenCount, int mOrderNumber, boolean mHideApp) {
        this.mPackageName = mPackageName;
        this.mOpenCount = mOpenCount;
        this.mOrderNumber = mOrderNumber;
        this.mHideApp = mHideApp;
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

    public boolean isHideApp() {
        return mHideApp;
    }

    public void setHideApp(boolean mHideApp) {
        this.mHideApp = mHideApp;
    }

    @Override
    public String toString() {
        return "AppPersistent{" +
                "mPackageName='" + mPackageName + '\'' +
                ", mOpenCount=" + mOpenCount +
                ", mOrderNumber=" + mOrderNumber +
                '}';
    }

    public static void incrementAppCount(String mPackageName) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mPackageName")).eq(mPackageName)).first();
        if (appPersistent != null) {
            appPersistent.setOpenCount(appPersistent.getOpenCount() + 1);
            appPersistent.save();
        } else {
            AppPersistent newAppPersistent = new AppPersistent(mPackageName, 1, -1, false); /* Set default incremented count to 1*/
            newAppPersistent.save();
        }
    }

    public static void setOrderNumberForPackage(String mPackageName, int mOrderNumber) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mPackageName")).eq(mPackageName)).first();
        if (appPersistent != null) {
            appPersistent.setOrderNumber(mOrderNumber);
            appPersistent.save();
        } else {
            AppPersistent newAppPersistent = new AppPersistent(mPackageName, 1, -1, false); /* Set default incremented count to 1*/
            newAppPersistent.save();
        }
    }

    public static boolean getHideAppForPackage(String mPackageName) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mPackageName")).eq(mPackageName)).first();
        if (appPersistent != null) {
            return appPersistent.isHideApp();
        } else {
            return false;
        }
    }

    public static void setHideAppForPackage(String mPackageName, boolean mHideApp) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mPackageName")).eq(mPackageName)).first();
        if (appPersistent != null) {
            appPersistent.setHideApp(mHideApp);
            appPersistent.save();
        } else {
            AppPersistent newAppPersistent = new AppPersistent(mPackageName, 1, -1, false); /* Set default incremented count to 1*/
            newAppPersistent.save();
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