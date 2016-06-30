package nickrout.lenslauncher.util;

/**
 * Created by rish on 22/5/16.
 */

import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import nickrout.lenslauncher.R;

public class IconPackManager {

    private static final String TAG = "IconPackManager";

    private Application mApplication;

    public class IconPack {

        public IconPack() {
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setFilterBitmap(true);
            mPaint.setDither(true);
        }

        public String mPackageName;
        public String mName;

        private boolean mLoaded = false;
        private HashMap<String, String> mPackagesDrawables = new HashMap<String, String>();

        private List<Bitmap> mBackImages = new ArrayList<Bitmap>();
        private Bitmap mMaskImage = null;
        private Bitmap mFrontImage = null;
        private float mFactor = 1.0f;

        private Paint mPaint;

        Resources mIconPackRes = null;

        public void load() {
            // Load appfilter.xml from the icon pack package
            PackageManager pm = mApplication.getPackageManager();
            try {
                XmlPullParser xpp = null;
                mIconPackRes = pm.getResourcesForApplication(mPackageName);
                int appfilterid = mIconPackRes.getIdentifier("appfilter", "xml", mPackageName);
                if (appfilterid > 0) {
                    xpp = mIconPackRes.getXml(appfilterid);
                } else {
                    // No resource found, try to open it from assets folder
                    try {
                        InputStream appfilterstream = mIconPackRes.getAssets().open("appfilter.xml");

                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        factory.setNamespaceAware(true);
                        xpp = factory.newPullParser();
                        xpp.setInput(appfilterstream, "utf-8");
                    } catch (IOException e1) {
                        Log.d(TAG, "No appfilter.xml file");
                    }
                }

                if (xpp != null) {
                    int eventType = xpp.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.getName().equals("iconback")) {
                                for (int i = 0; i < xpp.getAttributeCount(); i++) {
                                    if (xpp.getAttributeName(i).startsWith("img")) {
                                        String drawableName = xpp.getAttributeValue(i);
                                        Bitmap iconback = loadBitmap(drawableName);
                                        if (iconback != null)
                                            mBackImages.add(iconback);
                                    }
                                }
                            } else if (xpp.getName().equals("iconmask")) {
                                if (xpp.getAttributeCount() > 0 && xpp.getAttributeName(0).equals("img1")) {
                                    String drawableName = xpp.getAttributeValue(0);
                                    mMaskImage = loadBitmap(drawableName);
                                }
                            } else if (xpp.getName().equals("iconupon")) {
                                if (xpp.getAttributeCount() > 0 && xpp.getAttributeName(0).equals("img1")) {
                                    String drawableName = xpp.getAttributeValue(0);
                                    mFrontImage = loadBitmap(drawableName);
                                }
                            } else if (xpp.getName().equals("scale")) {
                                if (xpp.getAttributeCount() > 0 && xpp.getAttributeName(0).equals("factor")) {
                                    try {
                                        mFactor = Float.valueOf(xpp.getAttributeValue(0));
                                    } catch (NumberFormatException e) {
                                        mFactor = 1.0f;
                                        e.printStackTrace();
                                    }
                                }
                            } else if (xpp.getName().equals("item")) {
                                String componentName = null;
                                String drawableName = null;

                                for (int i = 0; i < xpp.getAttributeCount(); i++) {
                                    if (xpp.getAttributeName(i).equals("component")) {
                                        componentName = xpp.getAttributeValue(i);
                                    } else if (xpp.getAttributeName(i).equals("drawable")) {
                                        drawableName = xpp.getAttributeValue(i);
                                    }
                                }
                                if (!mPackagesDrawables.containsKey(componentName))
                                    mPackagesDrawables.put(componentName, drawableName);
                            }
                        }
                        eventType = xpp.next();
                    }
                }
                mLoaded = true;
            } catch (PackageManager.NameNotFoundException e) {
                Log.d(TAG, "Cannot load icon pack");
            } catch (XmlPullParserException e) {
                Log.d(TAG, "Cannot parse icon pack appfilter.xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Bitmap loadBitmap(String drawableName) {
            int id = mIconPackRes.getIdentifier(drawableName, "drawable", mPackageName);
            if (id > 0) {
                Drawable bitmap = mIconPackRes.getDrawable(id);
                if (bitmap instanceof BitmapDrawable)
                    return ((BitmapDrawable) bitmap).getBitmap();
            }
            return null;
        }

        public Bitmap getIconForPackage(String appPackageName, Bitmap defaultBitmap) {
            if (!mLoaded) {
                load();
            }

            PackageManager pm = mApplication.getPackageManager();
            Intent launchIntent = pm.getLaunchIntentForPackage(appPackageName);
            String componentName = null;
            if (launchIntent != null)
                componentName = pm.getLaunchIntentForPackage(appPackageName).getComponent().toString();
            String drawable = mPackagesDrawables.get(componentName);
            if (drawable != null) {
                Bitmap bitmap = loadBitmap(drawable);
                if (bitmap == null) {
                    return generateBitmap(defaultBitmap);
                }
                return bitmap;
            } else {
                // Try to get a resource with the component filename
                if (componentName != null) {
                    int start = componentName.indexOf("{") + 1;
                    int end = componentName.indexOf("}", start);
                    if (end > start) {
                        drawable = componentName.substring(start, end).toLowerCase(Locale.getDefault()).replace(".", "_").replace("/", "_");
                        if (mIconPackRes.getIdentifier(drawable, "drawable", mPackageName) > 0)
                            return loadBitmap(drawable);
                    }
                }
            }
            return generateBitmap(defaultBitmap);
        }

        private Bitmap generateBitmap(Bitmap defaultBitmap) {
            // No need to go through below process id defaultBitmap is null
            if (defaultBitmap == null) {
                return null;
            }
            // If no back images, return default app icon
            if (mBackImages.size() == 0) {
                return defaultBitmap;
            }
            // Get a random back image
            Bitmap backImage = getMostAppropriateBackImage(defaultBitmap);
            int backImageWidth = backImage.getWidth();
            int backImageHeight = backImage.getHeight();
            // Create a bitmap for the result
            Bitmap result;
            try {
                result = Bitmap.createBitmap(backImageWidth, backImageHeight, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return null;
            }
            // Instantiate a canvas to combine the icon / background
            Canvas canvas = new Canvas(result);
            // Draw the background first
            canvas.drawBitmap(backImage, 0, 0, null);
            // Create rects for scaling the default bitmap
            Rect srcRect = new Rect(
                    0,
                    0,
                    defaultBitmap.getWidth(),
                    defaultBitmap.getHeight()
            );
            float scaledWidth = mFactor * ((float) backImageWidth);
            float scaledHeight = mFactor * ((float) backImageHeight);
            RectF destRect = new RectF(
                    ((float) backImageWidth) / 2.0f - scaledWidth / 2.0f,
                    ((float) backImageHeight) / 2.0f - scaledHeight / 2.0f,
                    ((float) backImageWidth) / 2.0f + scaledWidth / 2.0f,
                    ((float) backImageHeight) / 2.0f + scaledHeight / 2.0f
            );
            // Handle mask image
            if (mMaskImage != null) {
                // First get mask bitmap
                Bitmap mask;
                try {
                    mask = Bitmap.createBitmap(backImageWidth, backImageHeight, Bitmap.Config.ARGB_8888);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    return null;
                }
                // Make a temp mask canvas
                Canvas maskCanvas = new Canvas(mask);
                // Draw the bitmap with mask into the result
                maskCanvas.drawBitmap(
                        defaultBitmap, srcRect, destRect, mPaint
                );
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
                maskCanvas.drawBitmap(mMaskImage, 0, 0, mPaint);
                mPaint.setXfermode(null);
                canvas.drawBitmap(mask, 0, 0, mPaint);
            } else {
                // Draw the scaled bitmap without mask
                canvas.drawBitmap(
                        defaultBitmap, srcRect, destRect, mPaint
                );
            }
            // Draw the front image
            if (mFrontImage != null) {
                canvas.drawBitmap(mFrontImage, 0, 0, mPaint);
            }
            // Return result
            return result;
        }

        private Bitmap getMostAppropriateBackImage(Bitmap defaultBitmap) {
            if (mBackImages.size() == 1) {
                return mBackImages.get(0);
            }
            @ColorInt int defaultPaletteColor = ColorUtil.getPaletteColorFromBitmap(defaultBitmap);
            float defaultHueColor = ColorUtil.getHueColorFromColor(defaultPaletteColor);
            float difference = Float.MAX_VALUE;
            int index = 0;
            for (int i = 0; i < mBackImages.size(); i++) {
                @ColorInt int backPaletteColor = ColorUtil.getPaletteColorFromBitmap(mBackImages.get(i));
                float backHueColor = ColorUtil.getHueColorFromColor(backPaletteColor);
                if (Math.abs(defaultHueColor - backHueColor) < difference) {
                    difference = Math.abs(defaultHueColor - backHueColor);
                    index = i;
                }
            }
            return mBackImages.get(index);
        }
    }

    private ArrayList<IconPack> mIconPacks = null;

    public ArrayList<IconPack> getAvailableIconPacksWithIcons(boolean forceReload, Application application) {
        mApplication = application;

        if (mIconPacks == null || forceReload) {
            mIconPacks = new ArrayList<>();

            PackageManager pm = mApplication.getPackageManager();

            /**
             * Currently Lens Launcher supports all icon packs supported by GoLauncher / EX
             * Create a set by filter the list to contain unique icon packs only
             */
            List<ResolveInfo> rinfo = new ArrayList<>();
            for (String launcher : mApplication.getResources().getStringArray(R.array.icon_pack_launchers)) {
                rinfo.addAll(pm.queryIntentActivities(new Intent(launcher), PackageManager.GET_META_DATA));
            }

            for (ResolveInfo ri : rinfo) {
                IconPack ip = new IconPack();
                ip.mPackageName = ri.activityInfo.packageName;

                ApplicationInfo ai = null;
                try {
                    ai = pm.getApplicationInfo(ip.mPackageName, PackageManager.GET_META_DATA);
                    ip.mName = mApplication.getPackageManager().getApplicationLabel(ai).toString();
                    mIconPacks.add(ip);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return mIconPacks;
    }
}