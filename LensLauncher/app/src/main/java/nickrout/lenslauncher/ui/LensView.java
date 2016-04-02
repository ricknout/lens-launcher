package nickrout.lenslauncher.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

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
        mPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mApps != null) {
            drawGrid(canvas, mApps.size());
        }
        Settings settings = new Settings(getContext());
        if (settings.getBoolean(Settings.KEY_SHOW_TOUCH_SELECTION)) {
            drawTouchPoint(canvas);
        }
    }

    private void drawTouchPoint(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setAlpha(255);
        canvas.drawCircle(mTouchX, mTouchY, 100, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                mTouchX = event.getX();
                mTouchY = event.getY();
                mSelectIndex = -1;
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                mTouchX = event.getX();
                mTouchY = event.getY();
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                mTouchX = -Float.MAX_VALUE;
                mTouchY = -Float.MAX_VALUE;
                launchApp();
                invalidate();
                return true;
            }
            default: {
                return super.onTouchEvent(event);
            }
        }
    }

    private void launchApp() {
        if (mPackageManager != null) {
            if (mSelectIndex >= 0) {
                if (mInsideRect) {
                    Settings settings = new Settings(getContext());
                    if (settings.getBoolean(Settings.KEY_VIBRATE_APP_LAUNCH)) {
                        performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    }
                    AppUtil.launchApp(getContext(), mPackageManager, (String) mApps.get(mSelectIndex).getName());
                }
            }
        }
    }

    private void drawGrid(Canvas canvas, int itemCount) {
        Grid grid = LensCalculator.calculateGrid(getContext(), getWidth(), getHeight(), itemCount);
        mInsideRect = false;
        for (float y = 0.0f; y < (float) grid.getItemCountVertical(); y += 1.0f) {
            for (float x = 0.0f; x < (float) grid.getItemCountHorizontal(); x += 1.0f) {
                int currentItem = (int) (y * ((float) grid.getItemCountHorizontal()) + (x + 1.0f));
                int currentIndex = currentItem - 1;
                if (currentItem <= grid.getItemCount()) {
                    RectF rect = new RectF();
                    rect.left = (x + 1.0f) * grid.getSpacingHorizontal() + x * grid.getItemSize();
                    rect.top = (y + 1.0f) * grid.getSpacingVertical() + y * grid.getItemSize();
                    rect.right = rect.left + grid.getItemSize();
                    rect.bottom = rect.top + grid.getItemSize();
                    Settings settings = new Settings(getContext());
                    float lensDiameter = LensCalculator.convertDpToPixel(settings.getFloat(Settings.KEY_LENS_DIAMETER), getContext());
                    float shiftedCenterX = LensCalculator.shiftPoint(getContext(), mTouchX, rect.centerX(), lensDiameter);
                    float shiftedCenterY = LensCalculator.shiftPoint(getContext(), mTouchY, rect.centerY(), lensDiameter);
                    float scaledCenterX = LensCalculator.scalePoint(getContext(), mTouchX, rect.centerX(), rect.width(), lensDiameter);
                    float scaledCenterY = LensCalculator.scalePoint(getContext(), mTouchY, rect.centerY(), rect.height(), lensDiameter);
                    float newSize = LensCalculator.calculateSquareScaledSize(scaledCenterX, shiftedCenterX, scaledCenterY, shiftedCenterY);
                    if (LensCalculator.calculateDistance(mTouchX, rect.centerX(), mTouchY, rect.centerY()) <= lensDiameter / 2.0f) {
                        rect = LensCalculator.calculateRect(shiftedCenterX, shiftedCenterY, newSize);
                        if (LensCalculator.isInsideRect(mTouchX, mTouchY, rect)) {
                            mInsideRect = true;
                            mSelectIndex = currentIndex;
                        }
                    }
                    mPaint.setStyle(Paint.Style.FILL);
                    Bitmap appIcon = mAppIcons.get(currentIndex);
                    Rect src = new Rect(0, 0, appIcon.getWidth() - 1, appIcon.getHeight() - 1);
                    canvas.drawBitmap(appIcon, src, rect, mPaint);
                }
            }
        }
        performHoverVibration();
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
}
