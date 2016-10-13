package com.tcl.launcher.effect.classical;

import com.tcl.launcher.effect.page.Effector;
import com.tcl.launcher.effect.page.APageBase;
import com.tcl.launcher.effect.pojo.ViewEffect3D;

/**
 * Created by liwu.shu on 2016/8/27.
 */
public class DefaultEffector extends Effector {
    @Override
    public APageBase getPageBase() {
        return new APageBase() {
            @Override
            public void update(ViewEffect3D curPage, ViewEffect3D nextPage, float degree, float yScale, float width) {
                curPage.setPosition(degree * width, 0);
                nextPage.setPosition((degree + 1) * width, 0);
                curPage.endEffect();
                nextPage.endEffect();
            }
        };
    }

    @Override
    public boolean isNeedClip() {
        return false;
    }
}
