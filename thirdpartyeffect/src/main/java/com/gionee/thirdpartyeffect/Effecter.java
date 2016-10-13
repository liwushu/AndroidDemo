
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.IEffectView;

/**
 * 动画控制器接口类
 */
public abstract class Effecter {
    public abstract void setIEffectView(IEffectView ie);

    public abstract void start(boolean loop);

    public abstract void end();

    public abstract void setFactor(float factor);

    public abstract void onPageIndexChange();

    public abstract boolean isNeedClip();

    public abstract boolean isEnd();
}
