package com.android.slw.http;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.facebook.drawee.drawable.DrawableUtils;

/**
 * Created by lcqing on 8/18/16.
 */
public class ImageLoadingDrawable extends Drawable {

    final private int DEFAULT_RADIUS_DP= 20;
    final private int BACKGROUND_COLOR = 0xFF000000;
    final private int RING_COLOR = 0xffffffff;
    private Paint mRingBackgroundPaint;
    private int mRingBackgroundColor;
    // 画圆环的画笔
    private Paint mRingPaint;
    // 圆环颜色
    private int mRingColor;
    // 半径
    private float mRadius;
    // 圆环半径
    private float mRingRadius;
    // 圆环宽度
    private float mStrokeWidth;
    // 圆心x坐标
    private int mXCenter;
    // 圆心y坐标
    private int mYCenter;
    // 总进度
    private int mTotalProgress = 10000;
    // 当前进度
    private int mProgress;

    public ImageLoadingDrawable(Context context){
        initAttrs(context);
    }

    private void initAttrs(Context context) {
        mRadius = ((int)(context.getResources().getDisplayMetrics().density+0.5))*DEFAULT_RADIUS_DP;
        mStrokeWidth = 6;
        mRingBackgroundColor = BACKGROUND_COLOR;
        mRingColor = RING_COLOR;
        mRingRadius = mRadius - mStrokeWidth ;
        initVariable();
    }

    private void initVariable() {
        mRingBackgroundPaint = new Paint();
        mRingBackgroundPaint.setAntiAlias(true);
        mRingBackgroundPaint.setColor(mRingBackgroundColor);
        mRingBackgroundPaint.setStyle(Paint.Style.STROKE);
        mRingBackgroundPaint.setStrokeWidth(mStrokeWidth);
        mRingBackgroundPaint.setAlpha(51);

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);
        mRingPaint.setAlpha(178);
    }

    @Override
    public void draw(Canvas canvas) {
        drawBgBar(canvas,mTotalProgress,mRingBackgroundPaint);
        drawBar(canvas,mProgress,mRingPaint);
    }

    private void drawBar(Canvas canvas, int level, Paint paint) {
        if (level > 0 ) {
            Rect bound= getBounds();
            mXCenter = bound.centerX();
            mYCenter = bound.centerY();
            RectF bg = new RectF();
            bg.left = (mXCenter - mRadius);
            bg.top = (mYCenter - mRadius);
            bg.right = mRadius * 2 + (mXCenter - mRadius);
            bg.bottom = mRadius * 2 + (mYCenter - mRadius);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(bg, -90,  360, false, paint);
            RectF oval = new RectF();
            oval.left = (mXCenter - mRingRadius);
            oval.top = (mYCenter -  mRingRadius);
            oval.right = mRingRadius * 2 + (mXCenter -mRingRadius);
            oval.bottom = mRingRadius * 2 + (mYCenter -mRingRadius);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawArc(oval, -90, ((float) level / mTotalProgress) * 360, true, paint);

        }
    }

    private void drawBgBar(Canvas canvas, int level, Paint paint) {
        if (level > 0 ) {
            Rect bound= getBounds();
            mXCenter = bound.centerX();
            mYCenter = bound.centerY();
            RectF oval = new RectF();
            oval.left = (mXCenter - mRadius);
            oval.top = (mYCenter - mRadius);
            oval.right = mRadius * 2 + (mXCenter - mRadius);
            oval.bottom = mRadius * 2 + (mYCenter - mRadius);
            canvas.drawArc(oval, -90, ((float) level / mTotalProgress) * 360, true, paint); //
        }
    }

    @Override
    protected boolean onLevelChange(int level) {
        mProgress = level;
        System.out.println("level: "+level);
        if(level > 0 && level < 10000) {
            invalidateSelf();
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mRingPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mRingPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return DrawableUtils.getOpacityFromColor(this.mRingPaint.getColor());
    }
}
