package com.tcl.launcher.effect.classical;

import com.tcl.launcher.effect.page.Effector;
import com.tcl.launcher.effect.page.IEffectFactory;

/**
 * @description:create default effect
 * Created by liwu.shu on 2016/8/27.
 */
public class DefaultEffectFactory implements IEffectFactory {
    @Override
    public Effector createEffector() {
        return new DefaultEffector();
    }
}
