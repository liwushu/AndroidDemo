package com.tcl.launcher.effect.page;

import android.view.View;

/**
 * Created by liwu.shu on 2016/8/27.
 */
public interface ICompatibleView {
    float getX();

    float getY();

    int getWidth();

    int getHeight();

    void setVisibility(int vis);

    void setAlpha(float alpha);

    View getNotClipView();

    int getRowNum();

    int getColumnNum();
}
