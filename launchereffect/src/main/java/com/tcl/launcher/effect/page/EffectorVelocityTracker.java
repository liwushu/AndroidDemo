package com.tcl.launcher.effect.page;

import android.view.MotionEvent;
import android.view.VelocityTracker;

/**
 * Created by liwu.shu on 2016/8/27.
 */
public class EffectorVelocityTracker {
    VelocityTracker mVelocityTracker;

    public void addVelocityTrack(MotionEvent event){
        if(mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(event);
    }

    public void clear(){
        if(mVelocityTracker != null)
            mVelocityTracker.clear();
    }

    public void recycle(){
        if(mVelocityTracker != null){
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public VelocityTracker getVelocityTracker(){
        return mVelocityTracker;
    }
}
