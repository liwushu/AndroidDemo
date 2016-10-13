package com.tcl.launcher.effect.pojo;

import android.view.View;

import com.tcl.launcher.effect.page.ICompatibleView;

/**
 * 应用切割动画时，子View 包装类。
 */
public class NotClipView implements ICompatibleView {

    View mView;

    public NotClipView(View view) {
        mView = view;
    }

    @Override
    public float getX() {
        if (mView != null) {
            return mView.getX();
        }
        return 0;
    }

    @Override
    public float getY() {
        if (mView != null) {
            return mView.getY();
        }
        return 0;
    }

    @Override
    public int getWidth() {
        if (mView != null) {
            return mView.getWidth();
        }
        return 0;
    }

    @Override
    public int getHeight() {
        if (mView != null) {
            return mView.getHeight();
        }
        return 0;
    }

    @Override
    public void setVisibility(int vis) {
        if (mView != null) {
            mView.setVisibility(vis);
        }
    }

    @Override
    public void setAlpha(float alpha) {
        if (mView != null) {
            mView.setAlpha(alpha);
        }
    }

    @Override
    public View getNotClipView() {
        return mView;
    }

    @Override
    public int getRowNum() {
        return 0;
    }

    @Override
    public int getColumnNum() {
        return 0;
    }
}
