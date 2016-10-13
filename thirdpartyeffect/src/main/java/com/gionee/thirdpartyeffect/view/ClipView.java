
package com.gionee.thirdpartyeffect.view;

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

    public void setAlpha(float alpha) {
        mAlpha = alpha;
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
