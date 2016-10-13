package com.tcl.launcher.effect.page;

import android.view.View;

/**
 * Created by liwu.shu on 2016/8/27.
 */
public interface IViewEffect {

    static class PageParams {
        public int mWidth;
        public int mHeight;
    }

    /**
     * 设置动画控制器
     */
    void setEffector(Effector ef);

    Effector getEffector();

    int getCurPageIndex();

    View getPageAt(int index);

    int getPageCount();

    void update();

    PageTransformationInfo getFirstTransformation();

    PageTransformationInfo getSecondTransformation();

    PageParams getPageParams();
}
