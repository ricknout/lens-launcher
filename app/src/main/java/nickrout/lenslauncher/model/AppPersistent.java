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

    public AppPersistent(String mPackageName, long mOpenCount) {
        this.mPackageName = mPackageName;
        this.mOpenCount = mOpenCount;
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

    @Override
    public String toString() {
        return "AppPersistent{" +
                "mPackageName='" + mPackageName + '\'' +
                ", mOpenCount=" + mOpenCount +
                '}';
    }

    public static void incrementAppCount(String mPackageName) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mPackageName")).eq(mPackageName)).first();
        if (appPersistent != null) {
            appPersistent.setOpenCount(appPersistent.getOpenCount() + 1);
            appPersistent.save();
        } else {
            AppPersistent newAppPersistent = new AppPersistent(mPackageName, 1); /* Set default incremented count to 1*/
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