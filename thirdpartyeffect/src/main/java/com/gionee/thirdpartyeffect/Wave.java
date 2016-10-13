
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Wave {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {

        cur_view.setOrigin(cur_view.getWidth(), cur_view.getHeight() / 2);
        cur_view.setPosition(degree * this_width, 0);
        cur_view.setScale((1 - Math.abs(degree)) * 0.8f + 0.2f,
                (1 - Math.abs(degree)) * 0.8f + 0.2f);

        next_view.setOrigin(0, next_view.getHeight() / 2);
        next_view.setPosition((degree + 1) * this_width, 0);
        next_view.setScale(Math.abs(degree) * 0.8f + 0.2f,
                Math.abs(degree) * 0.8f + 0.2f);

        cur_view.endEffect();
        next_view.endEffect();

    }
}
