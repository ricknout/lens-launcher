package nickrout.lenslauncher.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
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

    private Context mContext;
    private Paint mPaint;

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
        init(context);
    }

    public LensView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LensView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setApps(ArrayList<App> apps) {
        mApps = apps;
        for (int i = 0; i < mApps.size(); i++) {
            App app = mApps.get(i);
            Bitmap appIcon = BitmapUtil.drawableToBitmap(app.getIcon());
            mAppIcons.add(appIcon);
        }
        invalidate();
    }

    public void setPackageManager(PackageManager packageManager) {
        mPackageManager = packageManager;
    }

    private void init(Context context) {
        mContext = context;
        mApps = new ArrayList<>();
        mAppIcons = new ArrayList<>();
        mDrawType = DrawType.APPS;
        setBackground();
        setupPaint();
    }

    private void setBackground() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            setBackgroundColor(mContext.getResources().getColor(R.color.colorTransparent, null));
        } else {
            setBackgroundColor(mContext.getResources().getColor(R.color.colorTransparent));
        }
    }

    private void setupPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawType == DrawType.APPS) {
            if (mApps != null) {
                drawGrid(canvas, mApps.size());
            }
            Settings settings = new Settings(getContext());
            if (settings.getBoolean(Settings.KEY_SHOW_TOUCH_SELECTION)) {
                drawTouchPoint(canvas);
            }
        } else if (mDrawType == DrawType.CIRCLES) {
            mTouchX = getWidth() / 2;
            mTouchY = getHeight() / 2;
            drawGrid(canvas, 100);
        }
    }

    private void drawTouchPoint(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mPaint.setStrokeWidth(5);
        mPaint.setAlpha(255);
        canvas.drawCircle(mTouchX, mTouchY, 100, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDrawType == DrawType.APPS) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    mTouchX = event.getX();
                    mTouchY = event.getY();
                    mSelectIndex = -1;
                    LensDiameterAnimation lensDiameterShowAnimation = new LensDiameterAnimation(true);
                    startAnimation(lensDiameterShowAnimation);
                    return true;
                }
                case MotionEvent.ACTION_MOVE: {
                    mTouchX = event.getX();
                    mTouchY = event.getY();
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
                    Settings settings = new Settings(getContext());
                    float lensDiameter = mLensDiameter;
                    if (mDrawType == DrawType.CIRCLES) {
                        lensDiameter = LensCalculator.convertDpToPixel(settings.getFloat(Settings.KEY_LENS_DIAMETER), getContext());
                    }
                    float shiftedCenterX = LensCalculator.shiftPoint(getContext(), mTouchX, rect.centerX(), lensDiameter);
                    float shiftedCenterY = LensCalculator.shiftPoint(getContext(), mTouchY, rect.centerY(), lensDiameter);
                    float scaledCenterX = LensCalculator.scalePoint(getContext(), mTouchX, rect.centerX(), rect.width(), lensDiameter);
                    float scaledCenterY = LensCalculator.scalePoint(getContext(), mTouchY, rect.centerY(), rect.height(), lensDiameter);
                    float newSize = LensCalculator.calculateSquareScaledSize(scaledCenterX, shiftedCenterX, scaledCenterY, shiftedCenterY);
                    if (LensCalculator.isFrameWithinLens(rect, mTouchX, mTouchY, lensDiameter)) {
                    // Old Method - calculates circular distance but causes some unwanted icon overlap
                    //if (LensCalculator.calculateDistance(mTouchX, rect.centerX(), mTouchY, rect.centerY()) <= lensDiameter / 2.0f) {
                        if (settings.getFloat(Settings.KEY_DISTORTION_FACTOR) > 0.0f && settings.getFloat(Settings.KEY_SCALE_FACTOR) > 0.0f) {
                            rect = LensCalculator.calculateRect(shiftedCenterX, shiftedCenterY, newSize);
                        } else if (settings.getFloat(Settings.KEY_DISTORTION_FACTOR) > 0.0f && settings.getFloat(Settings.KEY_SCALE_FACTOR) == 0.0f) {
                            rect = LensCalculator.calculateRect(shiftedCenterX, shiftedCenterY, rect.width());
                        }
                        if (LensCalculator.isInsideRect(mTouchX, mTouchY, rect)) {
                            mInsideRect = true;
                            selectIndex = currentIndex;
                            rectToSelect = rect;
                        }
                    }
                    mPaint.setStyle(Paint.Style.FILL);
                    if (mDrawType == DrawType.APPS) {
                        Bitmap appIcon = mAppIcons.get(currentIndex);
                        Rect src = new Rect(0, 0, appIcon.getWidth() - 1, appIcon.getHeight() - 1);
                        canvas.drawBitmap(appIcon, src, rect, mPaint);
                    } else if (mDrawType == DrawType.CIRCLES) {
                        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2.0f, mPaint);
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
        if (rectToSelect != null && mDrawType == DrawType.APPS && mApps != null) {
            Settings settings = new Settings(getContext());
            if (settings.getBoolean(Settings.KEY_SHOW_NAME_APP_HOVER)) {
                mPaint.setTextSize(getResources().getDimension(R.dimen.text_size_lens));
                mPaint.setTextAlign(Paint.Align.CENTER);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
                mPaint.setShadowLayer(5.0f, 5.0f, 5.0f, ContextCompat.getColor(getContext(), R.color.colorBlack));
                canvas.drawText((String) mApps.get(mSelectIndex).getLabel(), rectToSelect.centerX(), rectToSelect.bottom + getResources().getDimension(R.dimen.margin_lens_text), mPaint);
            }
        }
        mPaint.setShadowLayer(0, 0, 0, 0);
    }

    private void performHoverVibration() {
        if (mInsideRect) {
            if (mMustVibrate) {
                Settings settings = new Settings(getContext());
                if (settings.getBoolean(Settings.KEY_VIBRATE_APP_HOVER)) {
                    performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                }
                mMustVibrate = false;
            }
        } else {
            mMustVibrate = true;
        }
    }

    private void performLaunchVibration() {
        if (mPackageManager != null) {
            if (mSelectIndex >= 0) {
                if (mInsideRect) {
                    Settings settings = new Settings(getContext());
                    if (settings.getBoolean(Settings.KEY_VIBRATE_APP_LAUNCH)) {
                        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    }
                }
            }
        }
    }

    private class LensDiameterAnimation extends Animation {

        private static final float SPEED = 10.0f;

        private float mStart;
        private float mEnd;
        private boolean mShow;

        public LensDiameterAnimation(boolean show) {
            mStart = 10.0f;
            Settings settings = new Settings(getContext());
            mEnd = LensCalculator.convertDpToPixel(settings.getFloat(Settings.KEY_LENS_DIAMETER), getContext());
            mShow = show;
            setInterpolator(new AccelerateDecelerateInterpolator());
            float duration = Math.abs(mEnd - mStart) / SPEED;
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
                        if (mSelectIndex >= 0) {
                            AppUtil.launchApp(getContext(), mPackageManager, (String) mApps.get(mSelectIndex).getName());
                        }
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
            } else {
                mLensDiameter = mStart + (1.0f - interpolatedTime) * mEnd;
            }
            postInvalidate();
        }
    }
}
