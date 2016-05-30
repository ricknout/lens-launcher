package nickrout.lenslauncher.util;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.graphics.Palette;

import nickrout.lenslauncher.model.App;

/**
 * Created by nicholasrout on 2016/05/28.
 */
public class ColorUtil {

    public static @ColorInt int getPaletteColorFromApp(App app) {
        Palette palette;
        try {
            palette = Palette.from(app.getIcon()).generate();
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
        float[] hsvValues = new float[3];
        Color.colorToHSV(app.getPaletteColor(), hsvValues);
        return hsvValues[0];
    }
}
