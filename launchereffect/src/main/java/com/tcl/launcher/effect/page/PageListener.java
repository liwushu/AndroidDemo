package com.tcl.launcher.effect.page;

/**
 * Created by liwu.shu on 2016/8/27.
 * @description 翻页接口
 */
public interface PageListener {

    /**
     * 换页完成调用
     *
     * @param index 页码
     */
    public void onPageSelseted(int index);

    /**
     * 换页过程中状态改变
     *
     * @param state 页码 {@link #SCROLL_STATE_IDLE
     *            ,#SCROLL_STATE_DRAGGING,#SCROLL_STATE_SETTLING}
     */
    public void onPageSrcollStateChange(int state);

    /**
     * 换页进度
     *
     * @param fraction 0～1 0～-1
     */
    public void onPageSlideFraction(float fraction);
}
