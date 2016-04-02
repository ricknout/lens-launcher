package nickrout.lenslauncher.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        //drawTouchPoint(canvas);
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
                    float lensSize = getContext().getResources().getDimension(R.dimen.lens_size);
                    float shiftedCenterX = LensCalculator.shiftPoint(mTouchX, rect.centerX(), lensSize);
                    float shiftedCenterY = LensCalculator.shiftPoint(mTouchY, rect.centerY(), lensSize);
                    float scaledCenterX = LensCalculator.scalePoint(mTouchX, rect.centerX(), rect.width(), lensSize);
                    float scaledCenterY = LensCalculator.scalePoint(mTouchY, rect.centerY(), rect.height(), lensSize);
                    float newSize = LensCalculator.calculateSquareScaledSize(scaledCenterX, shiftedCenterX, scaledCenterY, shiftedCenterY);
                    if (LensCalculator.calculateDistance(mTouchX, rect.centerX(), mTouchY, rect.centerY()) <= lensSize / 2.0f) {
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
        performVibration();
    }

    private void performVibration() {
        if (mInsideRect) {
            if (mMustVibrate) {
                performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                mMustVibrate = false;
            }
        } else {
            mMustVibrate = true;
        }
    }
}
