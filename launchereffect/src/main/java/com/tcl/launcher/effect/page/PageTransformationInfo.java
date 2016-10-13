package com.tcl.launcher.effect.page;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import java.util.List;


/**
 * Created by liwu.shu on 2016/8/27.
 */
public class PageTransformationInfo {
    boolean bmIsMatrixDirty;
    boolean bmDrawOrder = true;
    float mAlpha;
    View mPagedView;
    Bitmap mPagedShot = null;
    final Matrix mPagedTransMatrix = new Matrix();

    public static final Paint SPaint = new Paint();
    static {
        SPaint.setAntiAlias(true);
    }
    public static final Rect SRect = new Rect();

    List<ClipView> mClipViews;

    public void resetAndrecyle() {
        mAlpha = 1.0f;
        mPagedView = null;
        mClipViews = null;
        mPagedShot = null;
        mPagedTransMatrix.reset();
    }

    public List<ClipView> getClipViews(){
        return mClipViews;
    }

    public void setClipViews(List<ClipView> clipViews){
        this.mClipViews = clipViews;
    }

    public static Rect getSRect() {
        return SRect;
    }

    public Matrix getPagedTransMatrix() {
        return mPagedTransMatrix;
    }

    public Bitmap getPagedShot() {
        return mPagedShot;
    }

    public void setPagedShot(Bitmap mPagedShot) {
        this.mPagedShot = mPagedShot;
    }

    public View getPagedView() {
        return mPagedView;
    }

    public void setPagedView(View mPagedView) {
        this.mPagedView = mPagedView;
    }

    public float getAlpha() {
        return mAlpha;
    }

    public void setAlpha(float mAlpha) {
        this.mAlpha = mAlpha;
    }

    public boolean isBmDrawOrder() {
        return bmDrawOrder;
    }

    public void setBmDrawOrder(boolean bmDrawOrder) {
        this.bmDrawOrder = bmDrawOrder;
    }

    public boolean isBmIsMatrixDirty() {
        return bmIsMatrixDirty;
    }

    public void setBmIsMatrixDirty(boolean bmIsMatrixDirty) {
        this.bmIsMatrixDirty = bmIsMatrixDirty;
    }
}
