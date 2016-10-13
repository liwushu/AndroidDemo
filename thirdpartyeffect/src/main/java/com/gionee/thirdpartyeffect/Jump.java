
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Jump {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {
        float this_height = cur_view.getHeight();

        if (this_height == 0)
            this_height = next_view.getHeight();

        cur_view.setPosition(degree * this_width, degree * this_height);
        next_view.setPosition((degree + 1) * this_width, -(degree + 1)
                * this_height);

        cur_view.endEffect();
        next_view.endEffect();

    }
}
