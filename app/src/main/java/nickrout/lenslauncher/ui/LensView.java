package nickrout.lenslauncher.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
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
import nickrout.lenslauncher.model.AppPersistent;
import nickrout.lenslauncher.model.Grid;
import nickrout.lenslauncher.util.AppUtil;
import nickrout.lenslauncher.util.LensCalculator;
import nickrout.lenslauncher.util.Settings;

/**
 * Created by nickrout on 2016/04/02.
 */
public class LensView extends View {

    private static final String TAG = "LensView";

    private Paint mPaintIcons;
    private Paint mPaintCircles;
    private Paint mPaintTouchSelection;
    private Paint mPaintText;
    private Paint mPaintNewAppTag;

    private float mTouchX = -Float.MAX_VALUE;
    private float mTouchY = -Float.MAX_VALUE;

    private boolean mInsideRect = false;
    private boolean mMustVibrate = true;
    private int mSelectIndex;

    private ArrayList<App> mApps;
    private ArrayList<Bitmap> mAppIcons;
    private PackageManager mPackageManager;

    private float mAnimationMultiplier = 0.0f;
    private boolean mAnimationHiding = false;

    private int mNumberOfCircles;

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

    public void setApps(ArrayList<App> apps, ArrayList<Bitmap> appIcons) {
        mApps = apps;
        mAppIcons = appIcons;
        invalidate();
    }

    public void setPackageManager(PackageManager packageManager) {
        mPackageManager = packageManager;
    }

    private void init() {
        mApps = new ArrayList<>();
        mAppIcons = new ArrayList<>();
        mDrawType = DrawType.APPS;
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorTransparent));
        mSettings = new Settings(getContext());
        setupPaints();
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
        mPaintTouchSelection.setColor(Color.parseColor(mSettings.getString(Settings.KEY_TOUCH_SELECTION_COLOR)));
        mPaintTouchSelection.setStrokeWidth(getResources().getDimension(R.dimen.stroke_width_touch_selection));

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        mPaintText.setTextSize(getResources().getDimension(R.dimen.text_size_lens));
        mPaintText.setTextAlign(Paint.Align.CENTER);

        mPaintNewAppTag = new Paint();
        mPaintNewAppTag.setAntiAlias(true);
        mPaintNewAppTag.setStyle(Paint.Style.FILL);
        mPaintNewAppTag.setColor(Color.parseColor(mSettings.getString(Settings.KEY_TOUCH_SELECTION_COLOR)));
        mPaintNewAppTag.setDither(true);
        mPaintNewAppTag.setShadowLayer(getResources().getDimension(R.dimen.shadow_text),
                getResources().getDimension(R.dimen.shadow_text),
                getResources().getDimension(R.dimen.shadow_text),
                ContextCompat.getColor(getContext(), R.color.colorShadow));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawType == DrawType.APPS) {
            mPaintNewAppTag.setColor(Color.parseColor(mSettings.getString(Settings.KEY_TOUCH_SELECTION_COLOR)));
            if (mApps != null) {
                drawGrid(canvas, mApps.size());
            }
            if (mSettings.getBoolean(Settings.KEY_SHOW_TOUCH_SELECTION)) {
                drawTouchSelection(canvas);
            }
        } else if (mDrawType == DrawType.CIRCLES) {
            mNumberOfCircles = getWidth() / 10;
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
                    LensAnimation lensShowAnimation = new LensAnimation(true);
                    startAnimation(lensShowAnimation);
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
                    LensAnimation lensHideAnimation = new LensAnimation(false);
                    startAnimation(lensHideAnimation);
                    return true;
                }
                default: {
                    return super.onTouchEvent(event);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void drawTouchSelection(Canvas canvas) {
        canvas.drawCircle(mTouchX, mTouchY, getResources().getDimension(R.dimen.radius_touch_selection), mPaintTouchSelection);
    }

    private void drawGrid(Canvas canvas, int itemCount) {
        int systemOffset = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mDrawType != DrawType.CIRCLES) {
            systemOffset = (int) getResources().getDimension(R.dimen.margin_system);
        }
        Log.d(TAG, "Height = " + getHeight());
        Grid grid = LensCalculator.calculateGrid(getContext(), getWidth(), getHeight(), itemCount, systemOffset);
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

                    float animationMultiplier;
                    if (mDrawType == DrawType.APPS) {
                        animationMultiplier = mAnimationMultiplier;
                    } else {
                        animationMultiplier = 1.0f;
                    }

                    if (mTouchX >= 0 && mTouchY >= 0) {
                        float shiftedCenterX = LensCalculator.shiftPoint(getContext(), mTouchX, rect.centerX(), getWidth(), animationMultiplier);
                        float shiftedCenterY = LensCalculator.shiftPoint(getContext(), mTouchY, rect.centerY(), getHeight(), animationMultiplier);
                        float scaledCenterX = LensCalculator.scalePoint(getContext(), mTouchX, rect.centerX(), rect.width(), getWidth(), animationMultiplier);
                        float scaledCenterY = LensCalculator.scalePoint(getContext(), mTouchY, rect.centerY(), rect.height(), getHeight(), animationMultiplier);
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

        if (!mAnimationHiding) {
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

            /**
             * Check if the app was installed Settings.SHOW_NEW_APP_TAG_DURATION ago, and if it has been opened since.
             * If not, drawNewAppTag()
             */
            if ((mApps.get(index).getInstallDate() >= (System.currentTimeMillis() - Settings.SHOW_NEW_APP_TAG_DURATION)
                    && (AppPersistent.getAppOpenCount(
                    mApps.get(index).getPackageName().toString(), mApps.get(index).getName().toString()) == 0))) {
                drawNewAppTag(canvas, rect);
            }
        }
    }

    private void drawCircle(Canvas canvas, RectF rect) {
        canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2.0f, mPaintCircles);
    }

    private void drawAppName(Canvas canvas, RectF rect) {
        if (mSettings.getBoolean(Settings.KEY_SHOW_NAME_APP_HOVER)) {
            canvas.drawText((String) mApps.get(mSelectIndex).getLabel(),
                    rect.centerX(),
                    rect.top - getResources().getDimension(R.dimen.margin_lens_text),
                    mPaintText);
        }
    }

    private void drawNewAppTag(Canvas canvas, RectF rect) {
        if (mSettings.getBoolean(Settings.KEY_SHOW_NEW_APP_TAG)) {
            canvas.drawCircle(rect.centerX(),
                    rect.bottom + getResources().getDimension(R.dimen.margin_new_app_tag),
                    getResources().getDimension(R.dimen.radius_new_app_tag),
                    mPaintNewAppTag);
        }
    }

    private void performHoverVibration() {
        if (mInsideRect) {
            if (mMustVibrate) {
                if (mSettings.getBoolean(Settings.KEY_VIBRATE_APP_HOVER) && !mAnimationHiding) {
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
        if (mPackageManager != null && mApps != null && mSelectIndex >= 0) {
            AppUtil.launchComponent(
                    (String) mApps.get(mSelectIndex).getPackageName(),
                    (String) mApps.get(mSelectIndex).getName(),
                    getContext());
            AppPersistent.incrementAppCount(
                    (String) mApps.get(mSelectIndex).getPackageName(),
                    (String) mApps.get(mSelectIndex).getName());
        }
    }

    private class LensAnimation extends Animation {

        private boolean mShow;

        public LensAnimation(boolean show) {
            mShow = show;
            setInterpolator(new AccelerateDecelerateInterpolator());
            setDuration(mSettings.getLong(Settings.KEY_ANIMATION_TIME));
            setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    if (!mShow) {
                        mAnimationHiding = true;
                        mPaintText.clearShadowLayer();
                    } else {
                        mAnimationHiding = false;
                    }
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!mShow) {
                        launchApp();
                        mTouchX = -Float.MAX_VALUE;
                        mTouchY = -Float.MAX_VALUE;
                        mAnimationHiding = false;
                    } else {
                        mPaintText.setShadowLayer(
                                getResources().getDimension(R.dimen.shadow_text),
                                getResources().getDimension(R.dimen.shadow_text),
                                getResources().getDimension(R.dimen.shadow_text),
                                ContextCompat.getColor(getContext(), R.color.colorShadow));
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
                mAnimationMultiplier = interpolatedTime;
                mPaintTouchSelection.setColor(Color.parseColor(mSettings.getString(Settings.KEY_TOUCH_SELECTION_COLOR)));
                mPaintTouchSelection.setAlpha((int) (255.0f * interpolatedTime));
                mPaintText.setAlpha((int) (255.0f * interpolatedTime));
            } else {
                mAnimationMultiplier = 1.0f - interpolatedTime;
                mPaintTouchSelection.setColor(Color.parseColor(mSettings.getString(Settings.KEY_TOUCH_SELECTION_COLOR)));
                mPaintTouchSelection.setAlpha((int) (255.0f * (1.0f - interpolatedTime)));
                mPaintText.setAlpha((int) (255.0f * (1.0f - interpolatedTime)));
            }
            postInvalidate();
        }
    }
}
