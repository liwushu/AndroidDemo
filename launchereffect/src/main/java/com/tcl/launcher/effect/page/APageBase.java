package com.tcl.launcher.effect.page;

import com.tcl.launcher.effect.pojo.ViewEffect3D;

/**
 * Created by liwu.shu on 2016/8/27.
 */
public abstract class APageBase {

    /**
     * 左右滑动指示
     */
    private boolean isScrollR2L = false;

    public void setScrolldirection(boolean isR2L) {
        this.isScrollR2L = isR2L;
    }

    public void updateEffect(ViewEffect3D curPage, ViewEffect3D nextPage,
                                      float rotationDegree, float yScale){
        float width = 0;
        float height =0;
        width = curPage.getWidth();
        if (width == 0) {
            width = nextPage.getWidth();
                if (width == 0) {
                    return;
                }
        }

        height = curPage.getHeight();
        if (height == 0) {
            height = nextPage.getHeight();
            if (height == 0) {
                return;
            }
        }
        update(curPage,nextPage,rotationDegree,yScale,width==0?height:width);
    }

    public abstract void update(ViewEffect3D curPage, ViewEffect3D nextPage,
                                float degree, float yScale,float width);
}
