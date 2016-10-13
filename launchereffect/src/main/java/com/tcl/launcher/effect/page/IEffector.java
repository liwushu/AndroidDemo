package com.tcl.launcher.effect.page;

/**
 * Created by liwu.shu on 2016/8/27.
 */
interface IEffector {
    void setViewEffect(IViewEffect ie);

    void start(boolean loop);

    void end();

    void setFactor(float factor);

    void onPageIndexChange();

    boolean isNeedClip();

    boolean isEnd();
}
