package com.nickrout.testlens;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by nicholasrout on 2016/03/28.
 */
public class LensView extends View {

    private Context mContext;
    private Paint mPaint;

    Bitmap mBitmap;

    private float mTouchX = -Float.MAX_VALUE;
    private float mTouchY = -Float.MAX_VALUE;

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

    private void init(Context context) {
        this.mContext = context;
        setBackground();
        setupPaint();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    private void setBackground() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            setBackgroundColor(mContext.getResources().getColor(R.color.color_transparent, null));
        } else {
            setBackgroundColor(mContext.getResources().getColor(R.color.color_transparent));
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
        drawGrid(canvas, 200);
        drawTouchPoint(canvas);
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
                invalidate();
                return true;
            }
            default: {
                return super.onTouchEvent(event);
            }
        }
    }

    private void drawGrid(Canvas canvas, int itemCount) {
        Grid grid = LensCalculator.calculateGrid(getContext(), getWidth(), getHeight(), itemCount);
        for (float y = 0.0f; y < (float) grid.getItemCountVertical(); y += 1.0f) {
            for (float x = 0.0f; x < (float) grid.getItemCountHorizontal(); x += 1.0f) {
                int currentItem = (int) (y * ((float) grid.getItemCountHorizontal()) + (x + 1.0f));
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
                    }
                    mPaint.setStyle(Paint.Style.FILL);
                    Rect src = new Rect(0, 0, mBitmap.getWidth()-1, mBitmap.getHeight()-1);
                    canvas.drawBitmap(mBitmap, src, rect, mPaint);
                }
            }
        }
    }
}