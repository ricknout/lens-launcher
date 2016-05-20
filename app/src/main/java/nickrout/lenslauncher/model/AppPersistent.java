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

    public AppPersistent(String mPackageName, long mOpenCount) {
        this.mPackageName = mPackageName;
        this.mOpenCount = mOpenCount;
    }

    public String getmPackageName() {
        return mPackageName;
    }

    public void setmPackageName(String mPackageName) {
        this.mPackageName = mPackageName;
    }

    public long getmOpenCount() {
        return mOpenCount;
    }

    public void setmOpenCount(long mOpenCount) {
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
            appPersistent.setmOpenCount(appPersistent.getmOpenCount() + 1);
            appPersistent.save();
            Log.d("AppPersistent", "Updated appCount by +1");
        } else {
            Log.d("AppPersistent", "Unable to find mPackageName, creating new and setting incrementCountTo 0");
            AppPersistent newAppPersistent = new AppPersistent(mPackageName, 1); /* Set default incremented count to 1*/
            newAppPersistent.save();
        }
    }

    public static long getOpenCountByPackageName(String mPackageName) {
        AppPersistent appPersistent = Select.from(AppPersistent.class).where(Condition.prop(NamingHelper.toSQLNameDefault("mPackageName")).eq(mPackageName)).first();
        if (appPersistent != null) {
            return appPersistent.getmOpenCount();
        } else {
            Log.d("AppPersistent", "Unable to find mPackageName, returning 0");
            return 0;
        }
    }
}