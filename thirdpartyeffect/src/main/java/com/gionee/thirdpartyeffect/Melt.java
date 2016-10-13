
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Melt {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {
        if (degree <= 0 && degree >= -1 / 2f) {
            cur_view.setVisible(true);
            next_view.setVisible(false);
            float color_a = 1f - 2f * Math.abs(degree);
            cur_view.setAlpha(color_a);

            next_view.endEffect();
            cur_view.endEffect();
        } else {
            cur_view.setVisible(false);
            next_view.setVisible(true);
            float color_a = 2f * Math.abs(degree) - 1f;
            next_view.setAlpha(color_a);

            cur_view.endEffect();
            next_view.endEffect();
        }
    }
}
