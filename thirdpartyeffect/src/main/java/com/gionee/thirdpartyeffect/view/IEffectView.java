
package com.gionee.thirdpartyeffect.view;

import com.gionee.thirdpartyeffect.Effecter;
import com.gionee.thirdpartyeffect.view.EasyViewGroup.PageTranformationInfo;

import android.view.View;

/**
 * 动画容器接口类
 */
public interface IEffectView {

    public static class PageParams {
        int mWidth;
        int mHeight;
    }

    /**
     * 设置动画控制器
     */
    public void setEffecter(Effecter ef);

    public Effecter getEffecter();

    public int getCurPageIndex();

    public View getPageAt(int index);

    public int getPageCount();

    public void update();

    public PageTranformationInfo getFirstTransformation();

    public PageTranformationInfo getSecondTransformation();

    public PageParams getPageParams();
}
