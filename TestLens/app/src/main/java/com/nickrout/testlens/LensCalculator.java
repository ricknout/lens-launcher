package com.nickrout.testlens;

import android.content.Context;
import android.graphics.RectF;

/**
 * Created by nicholasrout on 2016/03/31.
 */
public class LensCalculator {

    public static Grid calculateGrid(Context context, int screenWidth, int screenHeight, int itemCount) {
        Grid grid = new Grid();
        grid.setItemCount(itemCount);
        double multiplier = Math.sqrt((double) itemCount);
        int itemCountHorizontal = (int) Math.ceil(multiplier * ((double) screenWidth / (double) screenHeight));
        grid.setItemCountHorizontal(itemCountHorizontal);
        int itemCountVertical = (int) Math.ceil(multiplier * ((double) screenHeight / (double) screenWidth));
        grid.setItemCountVertical(itemCountVertical);
        float itemSize = context.getResources().getDimension(R.dimen.item_size_min);
        grid.setItemSize(itemSize);
        float spacingHorizontal = (((float) screenWidth) - ((float) itemCountHorizontal * itemSize)) / ((float) (itemCountHorizontal + 1));
        grid.setSpacingHorizontal(spacingHorizontal);
        float spacingVertical = (((float) screenHeight) - ((float) itemCountVertical * itemSize)) / ((float) (itemCountVertical + 1));
        grid.setSpacingVertical(spacingVertical);
        return grid;
    }

    public static double calculateDistance(float x1, float x2, float y1, float y2) {
        return Math.sqrt(Math.pow((double)(x2 - x1), 2) + Math.pow((double)(y2 - y1), 2));
    }

    public static float shiftPoint(float lensPosition, float itemPosition, float boundary) {
        if(lensPosition < 0) {
            return itemPosition;
        }
        float shiftedPosition = itemPosition;
        float a = Math.abs(lensPosition - itemPosition);
        float x = a / (boundary / 2.0f);
        float y = ((1.0f + LensSettings.DISTORTION_FACTOR) * x) / (1.0f + (LensSettings.DISTORTION_FACTOR * x));
        float newDistanceFromCenter = (boundary / 2.0f) * y;
        if ((lensPosition + boundary / 2.0f) >= itemPosition && (lensPosition - boundary / 2) <= itemPosition) {
            if (lensPosition > itemPosition) {
                shiftedPosition = lensPosition - newDistanceFromCenter;
            } else if (lensPosition < itemPosition) {
                shiftedPosition = lensPosition + newDistanceFromCenter;
            }
        }
        return shiftedPosition;
    }

    public static float scalePoint(float lensPosition, float itemPosition, float itemSize, float boundary) {
        if(lensPosition < 0) {
            return itemSize;
        }
        float scaledPosition = itemPosition;
        if (lensPosition > itemPosition) {
            itemPosition = itemPosition - LensSettings.SCALE_FACTOR * (itemSize / 2.0f);
        } else {
            itemPosition = itemPosition + LensSettings.SCALE_FACTOR * (itemSize / 2.0f);
        }
        float a = Math.abs(lensPosition - itemPosition);
        float x = a / (boundary / 2.0f);
        float y = ((1.0f + LensSettings.DISTORTION_FACTOR) * x) / (1.0f + (LensSettings.DISTORTION_FACTOR * x));
        float scaledDistanceFromCenter = (boundary / 2.0f) * y;
        if ((lensPosition + boundary / 2.0f) >= itemPosition && (lensPosition - boundary / 2) <= itemPosition) {
            if (lensPosition > itemPosition) {
                scaledPosition = lensPosition - scaledDistanceFromCenter;
            } else if (lensPosition < itemPosition) {
                scaledPosition = lensPosition + scaledDistanceFromCenter;
            }
        }
        return scaledPosition;
    }

    public static float calculateSquareScaledSize(float scaledPositionX, float shiftedPositionX, float scaledPositionY, float shiftedPositionY) {
        return 2.0f * Math.min(Math.abs(scaledPositionX - shiftedPositionX), Math.abs(scaledPositionY - shiftedPositionY));
    }

    public static RectF calculateRect(float newCenterX, float newCenterY, float newSize) {
        RectF newRect = new RectF(newCenterX - newSize / 2.0f,
                                  newCenterY - newSize / 2.0f,
                                  newCenterX + newSize / 2.0f,
                                  newCenterY + newSize / 2.0f);
        return newRect;
    }
}
