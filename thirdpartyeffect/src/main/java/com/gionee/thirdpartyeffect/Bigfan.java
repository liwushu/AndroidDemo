package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Bigfan {
    public static void updateEffect(ViewGroup3D cur_view, ViewGroup3D next_view,
            float degree, float yScale, float this_width) {
    	
        float color_a = 0f;

        cur_view.setRotationY(degree * 90);
        color_a = 1f - Math.abs(degree);
        cur_view.setAlpha(color_a);
        cur_view.setCameraDistance(-25);

        color_a = Math.abs(degree);
        next_view.setRotationY((degree + 1) * 90);
        next_view.setAlpha(color_a); 
        next_view.setCameraDistance(-25);
        
        cur_view.endEffect();
        next_view.endEffect();     

    }
}
