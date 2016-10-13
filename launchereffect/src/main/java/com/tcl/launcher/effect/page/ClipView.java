package com.tcl.launcher.effect.page;

/**
 * Created by liwu.shu on 2016/8/27.
 */

import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.View;

/**
 * 子View的切割块
 */
public class ClipView implements ICompatibleView {

    int mRowIndex;
    int mColumnIndex;
    int mPrivateFlag = 0;
    float mAlpha = 1.0f;
    Rect mDisRect = new Rect();
    Matrix mTransMatrix = new Matrix();

    public int getRowIndex() {
        return mRowIndex;
    }

    public void setRowIndex(int mRowIndex) {
        this.mRowIndex = mRowIndex;
    }

    public int getColumnIndex() {
        return mColumnIndex;
    }

    public void setColumnIndex(int mColumnIndex) {
        this.mColumnIndex = mColumnIndex;
    }

    public float getAlpha() {
        return mAlpha;
    }

    public void setAlpha(float mAlpha) {
        this.mAlpha = mAlpha;
    }

    public int getPrivateFlag() {
        return mPrivateFlag;
    }

    public void setPrivateFlag(int mPrivateFlag) {
        this.mPrivateFlag = mPrivateFlag;
    }

    public Rect getDisRect() {
        return mDisRect;
    }

    public void setDisRect(Rect mDisRect) {
        this.mDisRect = mDisRect;
    }

    public Matrix getTransMatrix() {
        return mTransMatrix;
    }

    public void setTransMatrix(Matrix mTransMatrix) {
        this.mTransMatrix = mTransMatrix;
    }

    @Override
    public float getX() {
        return mDisRect.left;
    }

    @Override
    public float getY() {
        return mDisRect.top;
    }

    @Override
    public int getWidth() {
        return mDisRect.right - mDisRect.left;
    }

    @Override
    public int getHeight() {
        return mDisRect.bottom - mDisRect.top;
    }

    @Override
    public void setVisibility(int vis) {
        mPrivateFlag |= vis;
    }

    public int getVisibility() {
        if ((mPrivateFlag & View.VISIBLE) != 0) {
            return View.VISIBLE;
        }
        return View.INVISIBLE;
    }

    @Override
    public View getNotClipView() {
        return null;
    }

    @Override
    public int getRowNum() {
        return mRowIndex;
    }

    @Override
    public int getColumnNum() {
        return mColumnIndex;
    }


}