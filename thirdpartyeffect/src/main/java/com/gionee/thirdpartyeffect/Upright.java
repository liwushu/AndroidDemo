
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Upright {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float this_width) {
        cur_view.setPosition(0, degree * this_width);
        float cur_color_a = 1 - Math.abs(degree);
        cur_view.setAlpha(cur_color_a);

        next_view.setPosition(0, (degree + 1) * this_width);
        float next_color_a = Math.abs(degree);
        next_view.setAlpha(next_color_a);

        cur_view.endEffect();
        next_view.endEffect();
    }
}
