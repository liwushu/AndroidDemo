
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Press {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {
        float this_height = cur_view.getHeight();

        if (this_height == 0) {
            this_height = next_view.getHeight();
        }

        cur_view.setOrigin(this_width, this_height / 2);
        cur_view.setPosition(degree * this_width, 0);
        cur_view.setScale((1 + degree), 1f);

        next_view.setOrigin(0, this_height / 2);
        next_view.setPosition((degree + 1) * this_width, 0);
        next_view.setScale(Math.abs(degree), 1f);

        cur_view.endEffect();
        next_view.endEffect();

    }
}
