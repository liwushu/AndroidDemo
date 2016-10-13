
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class FadeIn {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {

        cur_view.setPosition(degree * this_width, 0);
        float color_a = 1.0f;
        cur_view.setAlpha(color_a);
        cur_view.setScale(1.0f, 1.0f);

        float z = 0.6f + 0.4f * Math.abs(degree);
        next_view.setPosition(0, 0);
        next_view.setScale(z, z);
        next_view.setAlpha(Math.abs(degree));

        cur_view.endEffect();
        next_view.endEffect();

    }
}
