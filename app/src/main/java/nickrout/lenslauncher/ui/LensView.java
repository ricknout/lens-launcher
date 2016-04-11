package nickrout.lenslauncher.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.ArrayList;

import nickrout.lenslauncher.R;
import nickrout.lenslauncher.model.App;
import nickrout.lenslauncher.model.Grid;
import nickrout.lenslauncher.util.AppUtil;
import nickrout.lenslauncher.util.BitmapUtil;
import nickrout.lenslauncher.util.LensCalculator;
import nickrout.lenslauncher.util.Settings;

/**
 * Created by nickrout on 2016/04/02.
 */
public class LensView extends View {

    private Activity mActivity;

    private Paint mPaintIcons;
    private Paint mPaintCircles;
    private Paint mPaintTouchSelection;
    private Paint mPaintText;

    private float mTouchX = -Float.MAX_VALUE;
    private float mTouchY = -Float.MAX_VALUE;

    private boolean mInsideRect = false;
    private boolean mMustVibrate = true;
    private int mSelectIndex;

    private ArrayList<App> mApps;
    private ArrayList<Bitmap> mAppIcons;
    private PackageManager mPackageManager;

    private float mLensDiameter = 10.0f;
    private boolean mLensDiameterHiding = false;
    private final float mMinimumLensSize = 10.0f;
    private final float mBaseAnimationSpeed = 15.0f;

    private final int mNumberOfCircles = 100;

    private Settings mSettings;

    public enum DrawType {
        APPS,
        CIRCLES
    }

    private DrawType mDrawType;

    public void setDrawType(DrawType drawType) {
        mDrawType = drawType;
        invalidate();
    }

    public LensView(Context context) {
        super(context);
        init();
    }

    public LensView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LensView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void setApps(ArrayList<App> apps) {
        mApps = apps;
        new LoadIconsTask().execute();
    }

    public ArrayList<App> getApps() {
        return mApps;
    }

    public void addApp(App app, int index) {
        Bitmap appIcon = BitmapUtil.packageNameToBitmap(mPackageManager, (String) app.getName());
        if (appIcon != null) {
            mApps.add(index, app);
            mAppIcons.add(index, appIcon);
            invalidate();
        }

    }

    public void removeApp(App app) {
        for (int i = 0; i < mApps.size(); i++) {
            if (mApps.get(i).getName().equals(app.getName())) {
                mApps.remove(i);
                mAppIcons.remove(i);
                invalidate();
                break;
            }
        }
    }

    public void setPackageManager(PackageManager packageManager) {
        mPackageManager = packageManager;
    }

    private void init() {
        mApps = new ArrayList<>();
        mAppIcons = new ArrayList<>();
        mDrawType = DrawType.APPS;
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTransparent));
        setupPaints();
        mSettings = new Settings(getContext());
    }

    private void setupPaints() {
        mPaintIcons = new Paint();
        mPaintIcons.setAntiAlias(true);
        mPaintIcons.setStyle(Paint.Style.FILL);
        mPaintIcons.setFilterBitmap(true);
        mPaintIcons.setDither(true);

        mPaintCircles = new Paint();
        mPaintCircles.setAntiAlias(true);
        mPaintCircles.setStyle(Paint.Style.FILL);
        mPaintCircles.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        mPaintTouchSelection = new Paint();
        mPaintTouchSelection.setAntiAlias(true);
        mPaintTouchSelection.setStyle(Paint.Style.STROKE);
        mPaintTouchSelection.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mPaintTouchSelection.setStrokeWidth(getResources().getDimension(R.dimen.stroke_width_touch_selection));

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        mPaintText.setShadowLayer(getResources().getDimension(R.dimen.shadow_text),
                                  getResources().getDimension(R.dimen.shadow_text),
                                  getResources().getDimension(R.dimen.shadow_text),
                                  ContextCompat.getColor(getContext(), R.color.colorShadow));
        mPaintText.setTextSize(getResources().getDimension(R.dimen.text_size_lens));
        mPaintText.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawType == DrawType.APPS) {
            if (mApps != null) {
                drawGrid(canvas, mApps.size());
            }
            if (mSettings.getBoolean(Settings.KEY_SHOW_TOUCH_SELECTION)) {
                drawTouchPoint(canvas);
            }
        } else if (mDrawType == DrawType.CIRCLES) {
            mTouchX = getWidth() / 2;
            mTouchY = getHeight() / 2;
            drawGrid(canvas, mNumberOfCircles);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDrawType == DrawType.APPS) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    if (event.getX() < 0.0f) {
                        mTouchX = 0.0f;
                    } else {
                        mTouchX = event.getX();
                    }
                    if (event.getY() < 0.0f) {
                        mTouchY = 0.0f;
                    } else {
                        mTouchY = event.getY();
                    }
                    mSelectIndex = -1;
                    LensDiameterAnimation lensDiameterShowAnimation = new LensDiameterAnimation(true);
                    startAnimation(lensDiameterShowAnimation);
                    return true;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (event.getX() < 0.0f) {
                        mTouchX = 0.0f;
                    } else {
                        mTouchX = event.getX();
                    }
                    if (event.getY() < 0.0f) {
                        mTouchY = 0.0f;
                    } else {
                        mTouchY = event.getY();
                    }
                    invalidate();
                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    performLaunchVibration();
                    LensDiameterAnimation lensDiameterHideAnimation = new LensDiameterAnimation(false);
                    startAnimation(lensDiameterHideAnimation);
                    return true;
                }
                default: {
                    return super.onTouchEvent(event);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void drawTouchPoint(Canvas canvas) {
        canvas.drawCircle(mTouchX, mTouchY, getResources().getDimension(R.dimen.radius_touch_selection), mPaintTouchSelection);
    }

    private void drawGrid(Canvas canvas, int itemCount) {
        Grid grid = LensCalculator.calculateGrid(getContext(), getWidth(), getHeight(), itemCount);
        mInsideRect = false;
        int selectIndex = -1;
        float offset = LensCalculator.calculateGridOffset(grid, getHeight());
        RectF rectToSelect = null;

        for (float y = 0.0f; y < (float) grid.getItemCountVertical(); y += 1.0f) {
            for (float x = 0.0f; x < (float) grid.getItemCountHorizontal(); x += 1.0f) {

                int currentItem = (int) (y * ((float) grid.getItemCountHorizontal()) + (x + 1.0f));
                int currentIndex = currentItem - 1;

                if (currentItem <= grid.getItemCount() || mDrawType == DrawType.CIRCLES) {
                    RectF rect = new RectF();
                    rect.left = (x + 1.0f) * grid.getSpacingHorizontal() + x * grid.getItemSize();
                    rect.top = offset + (y + 1.0f) * grid.getSpacingVertical() + y * grid.getItemSize();
                    rect.right = rect.left + grid.getItemSize();
                    rect.bottom = rect.top + grid.getItemSize();

                    float lensDiameter = mLensDiameter;
                    if (mDrawType == DrawType.CIRCLES) {
                        // Animation does not affect lens size for Settings mode
                        lensDiameter = LensCalculator.convertDpToPixel(mSettings.getFloat(Settings.KEY_LENS_DIAMETER), getContext());
                    }

                    if (LensCalculator.isRectWithinLens(rect, mTouchX, mTouchY, lensDiameter)) {
                    // Old Method - calculates circular distance but causes some unwanted icon overlap
                    //if (LensCalculator.calculateDistance(mTouchX, rect.centerX(), mTouchY, rect.centerY()) <= lensDiameter / 2.0f) {

                        float shiftedCenterX = LensCalculator.shiftPoint(getContext(), mTouchX, rect.centerX(), lensDiameter);
                        float shiftedCenterY = LensCalculator.shiftPoint(getContext(), mTouchY, rect.centerY(), lensDiameter);
                        float scaledCenterX = LensCalculator.scalePoint(getContext(), mTouchX, rect.centerX(), rect.width(), lensDiameter);
                        float scaledCenterY = LensCalculator.scalePoint(getContext(), mTouchY, rect.centerY(), rect.height(), lensDiameter);
                        float newSize = LensCalculator.calculateSquareScaledSize(scaledCenterX, shiftedCenterX, scaledCenterY, shiftedCenterY);

                        if (mSettings.getFloat(Settings.KEY_DISTORTION_FACTOR) > 0.0f && mSettings.getFloat(Settings.KEY_SCALE_FACTOR) > 0.0f) {
                            rect = LensCalculator.calculateRect(shiftedCenterX, shiftedCenterY, newSize);
                        } else if (mSettings.getFloat(Settings.KEY_DISTORTION_FACTOR) > 0.0f && mSettings.getFloat(Settings.KEY_SCALE_FACTOR) == 0.0f) {
                            rect = LensCalculator.calculateRect(shiftedCenterX, shiftedCenterY, rect.width());
                        }

                        if (LensCalculator.isInsideRect(mTouchX, mTouchY, rect)) {
                            mInsideRect = true;
                            selectIndex = currentIndex;
                            rectToSelect = rect;
                        }
                    }

                    if (mDrawType == DrawType.APPS) {
                        drawAppIcon(canvas, rect, currentIndex);
                    } else if (mDrawType == DrawType.CIRCLES) {
                        drawCircle(canvas, rect);
                    }
                }
            }
        }

        if (selectIndex >= 0) {
            if (selectIndex != mSelectIndex) {
                mMustVibrate = true;
            } else {
                mMustVibrate = false;
            }
        } else {
            mMustVibrate = false;
        }

        if (!mLensDiameterHiding) {
            mSelectIndex = selectIndex;
        }

        if (mDrawType == DrawType.APPS) {
            performHoverVibration();
        }

        if (rectToSelect != null && mDrawType == DrawType.APPS && mApps != null && mSelectIndex >= 0) {
            drawAppName(canvas, rectToSelect);
        }
    }

    private void drawAppIcon(Canvas canvas, RectF rect, int index) {
        if (index < mAppIcons.size()) {
            Bitmap appIcon = mAppIcons.get(index);
            Rect src = new Rect(0, 0, appIcon.getWidth(), appIcon.getHeight());
            canvas.drawBitmap(appIcon, src, rect, mPaintIcons);
        }
    }

    private void drawCircle(Canvas canvas, RectF rect) {
        canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2.0f, mPaintCircles);
    }

    private void drawAppName(Canvas canvas, RectF rect) {
        if (mSettings.getBoolean(Settings.KEY_SHOW_NAME_APP_HOVER)) {
            canvas.drawText((String) mApps.get(mSelectIndex).getLabel(),
                    rect.centerX(),
                    rect.bottom + getResources().getDimension(R.dimen.margin_lens_text),
                    mPaintText);
        }
    }

    private void performHoverVibration() {
        if (mInsideRect) {
            if (mMustVibrate) {
                if (mSettings.getBoolean(Settings.KEY_VIBRATE_APP_HOVER)) {
                    performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                }
                mMustVibrate = false;
            }
        } else {
            mMustVibrate = true;
        }
    }

    private void performLaunchVibration() {
        if (mInsideRect) {
            if (mSettings.getBoolean(Settings.KEY_VIBRATE_APP_LAUNCH)) {
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
        }
    }

    private void launchApp() {
        if (mPackageManager != null && mApps != null && mSelectIndex >= 0 ) {
            AppUtil.launchApp(getContext(), mPackageManager, (String) mApps.get(mSelectIndex).getName());
        }
    }

    private class LensDiameterAnimation extends Animation {

        private float mStart;
        private float mEnd;
        private boolean mShow;

        public LensDiameterAnimation(boolean show) {
            mStart = mMinimumLensSize;
            mEnd = LensCalculator.convertDpToPixel(mSettings.getFloat(Settings.KEY_LENS_DIAMETER), getContext());
            mShow = show;
            setInterpolator(new AccelerateDecelerateInterpolator());
            float speed = (mSettings.getFloat(Settings.KEY_LENS_DIAMETER) / (float) Settings.MAX_LENS_DIAMETER) * mBaseAnimationSpeed;
            float duration = Math.abs(mEnd - mStart) / speed;
            setDuration((long) duration);
            setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (!mShow) {
                        mLensDiameterHiding = true;
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!mShow) {
                        launchApp();
                        mTouchX = -Float.MAX_VALUE;
                        mTouchY = -Float.MAX_VALUE;
                        mLensDiameterHiding = false;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (mShow) {
                mLensDiameter = mStart + interpolatedTime * mEnd;
                mPaintTouchSelection.setAlpha((int) (255.0f * interpolatedTime));
            } else {
                mLensDiameter = mStart + (1.0f - interpolatedTime) * mEnd;
                mPaintTouchSelection.setAlpha((int) (255.0f * (1.0f - interpolatedTime)));
            }
            postInvalidate();
        }
    }

    private class LoadIconsTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog mProgressDialog;

        public LoadIconsTask() {
            if (mActivity != null) {
                mProgressDialog = new ProgressDialog(mActivity);
            }
        }

        @Override
        protected void onPreExecute() {
            if (mProgressDialog != null) {
                mProgressDialog.setMessage(getContext().getString(R.string.progress_loading_icons));
                mProgressDialog.show();
            }
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            mAppIcons = new ArrayList<>();
            for (int i = 0; i < mApps.size(); i++) {
                App app = mApps.get(i);
                //Bitmap appIcon = BitmapUtil.packageNameToBitmap(mPackageManager, (String) app.getName());
                //Bitmap appIcon = BitmapUtil.drawableToBitmap(app.getIcon());
                Bitmap appIcon = app.getIcon();
                if (appIcon != null) {
                    mAppIcons.add(appIcon);
                } else {
                    mApps.remove(app);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            invalidate();
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            super.onPostExecute(result);
        }
    }
}
