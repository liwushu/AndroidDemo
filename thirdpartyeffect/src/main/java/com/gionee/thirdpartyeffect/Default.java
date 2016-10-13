
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Default {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {

        cur_view.setPosition(degree * this_width, 0);
        next_view.setPosition((degree + 1) * this_width, 0);
        cur_view.endEffect();
        next_view.endEffect();
    }
}
