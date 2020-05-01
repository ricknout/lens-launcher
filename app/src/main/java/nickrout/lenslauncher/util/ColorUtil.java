package nickrout.lenslauncher.util;

import android.graphics.Bitmap;
import android.graphics.Color;
import androidx.annotation.ColorInt;
import androidx.palette.graphics.Palette;

import nickrout.lenslauncher.model.App;

/**
 * Created by nicholasrout on 2016/05/28.
 */
public class ColorUtil {

    public static @ColorInt int getPaletteColorFromApp(App app) {
        return getPaletteColorFromBitmap(app.getIcon());
    }

    public static @ColorInt int getPaletteColorFromBitmap(Bitmap bitmap) {
        Palette palette;
        try {
            palette = Palette.from(bitmap).generate();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return Color.BLACK;
        }
        if (palette.getSwatches().size() > 0) {
            int swatchIndex = 0;
            for (int i = 1; i < palette.getSwatches().size(); i++) {
                if (palette.getSwatches().get(i).getPopulation()
                        > palette.getSwatches().get(swatchIndex).getPopulation()) {
                    swatchIndex = i;
                }
            }
            return palette.getSwatches().get(swatchIndex).getRgb();
        } else {
            return Color.BLACK;
        }
    }

    public static float getHueColorFromApp(App app) {
        return getHueColorFromColor(app.getPaletteColor());
    }

    public static float getHueColorFromColor(@ColorInt int color) {
        float[] hsvValues = new float[3];
        Color.colorToHSV(color, hsvValues);
        return hsvValues[0];
    }
}
