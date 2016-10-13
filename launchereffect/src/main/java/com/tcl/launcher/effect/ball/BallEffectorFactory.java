package com.tcl.launcher.effect.ball;

import com.tcl.launcher.effect.page.Effector;
import com.tcl.launcher.effect.page.IEffectFactory;

/**
 * Created by liwu.shu on 2016/8/29.
 */
public class BallEffectorFactory implements IEffectFactory {

    @Override
    public Effector createEffector() {
        return new BallEffector();
    }
}
