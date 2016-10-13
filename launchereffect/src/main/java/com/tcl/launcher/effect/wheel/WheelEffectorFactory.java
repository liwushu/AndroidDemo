package com.tcl.launcher.effect.wheel;

import com.tcl.launcher.effect.page.Effector;
import com.tcl.launcher.effect.page.IEffectFactory;

/**
 * Created by liwu.shu on 2016/8/29.
 */
public class WheelEffectorFactory implements IEffectFactory{
    @Override
    public Effector createEffector() {
        return new WheelEffector();
    }
}
