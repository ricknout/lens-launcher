package nickrout.lenslauncher.model;

/**
 * Created by nickrout on 2016/04/02.
 */
public class Grid {

    private int mItemCount;
    private int mItemCountHorizontal;
    private int mItemCountVertical;
    private float mItemSize;
    private float mSpacingHorizontal;
    private float mSpacingVertical;

    public Grid() {}

    public int getItemCount() {
        return mItemCount;
    }

    public void setItemCount(int itemCount) {
        mItemCount = itemCount;
    }

    public int getItemCountHorizontal() {
        return mItemCountHorizontal;
    }

    public void setItemCountHorizontal(int itemCountHorizontal) {
        mItemCountHorizontal = itemCountHorizontal;
    }

    public int getItemCountVertical() {
        return mItemCountVertical;
    }

    public void setItemCountVertical(int itemCountVertical) {
        mItemCountVertical = itemCountVertical;
    }

    public float getItemSize() {
        return mItemSize;
    }

    public void setItemSize(float itemSize) {
        mItemSize = itemSize;
    }

    public float getSpacingHorizontal() {
        return mSpacingHorizontal;
    }

    public void setSpacingHorizontal(float spacingHorizontal) {
        mSpacingHorizontal = spacingHorizontal;
    }

    public float getSpacingVertical() {
        return mSpacingVertical;
    }

    public void setSpacingVertical(float spacingVertical) {
        mSpacingVertical = spacingVertical;
    }

    @Override
    public String toString() {
        return "Grid{" +
                "mItemCount=" + mItemCount +
                ", mItemCountHorizontal=" + mItemCountHorizontal +
                ", mItemCountVertical=" + mItemCountVertical +
                ", mItemSize=" + mItemSize +
                ", mSpacingHorizontal=" + mSpacingHorizontal +
                ", mSpacingVertical=" + mSpacingVertical +
                '}';
    }
}
