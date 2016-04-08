package nickrout.lenslauncher.model;

/**
 * Created by nickrout on 2016/04/08.
 */
public class AppChange {

    public enum ChangeType {
        ADD,
        REMOVE
    }

    private ChangeType mChangeType;
    private String mPackageName;

    public AppChange() {}

    public AppChange(ChangeType changeType, String packageName) {
        mChangeType = changeType;
        mPackageName = packageName;
    }

    public ChangeType getChangeType() {
        return mChangeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.mChangeType = changeType;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }
}
