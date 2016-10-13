
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Rotate {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {
        ViewGroup3D icon;
        float radio;

        int cur_size = cur_view.getChildCount();
        int next_size = next_view.getChildCount();

        if (degree <= 0 && degree > (-1 / 2f)) {
            radio = (2 * degree + 1) * 0.8f + 0.2f;
            cur_view.setVisible(true);
            next_view.setVisible(false);
        } else {
            radio = (-2 * degree - 1) * 0.8f + 0.2f;
            cur_view.setVisible(false);
            next_view.setVisible(true);
        }
        for (int i = 0; i < cur_size; i++) {
            if (i < cur_view.getChildCount()) {
                icon = cur_view.getChildAt(i);
                icon.setScale(radio, radio);
                icon.setRotationZ(degree * 360 % 360);

                icon.endEffect();
            }
        }
        for (int i = 0; i < next_size; i++) {
            if (i < next_view.getChildCount()) {
                icon = next_view.getChildAt(i);
                icon.setScale(radio, radio);
                icon.setRotationZ(degree * 360 % 360);

                icon.endEffect();
            }
        }

    }
}
