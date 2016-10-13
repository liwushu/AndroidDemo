
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Blind {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {
        ViewGroup3D icon;

        int cur_size = cur_view.getChildCount();
        int next_size = next_view.getChildCount();

        float absDegree = Math.abs(degree);

        if (degree <= 0 && degree >= -1 / 2f) {
            cur_view.setVisible(true);
            next_view.setVisible(false);
            for (int i = 0; i < cur_size; i++) {
                icon = cur_view.getChildAt(i);
                icon.setCameraDistance(-this_width / 2);
                icon.setRotationY(2 * absDegree * 90);
                icon.endEffect();
            }
            for (int i = 0; i < next_size; i++) {
                icon = next_view.getChildAt(i);
                icon.setRotationY(0);
                icon.endEffect();
            }
        } else {
            next_view.setVisible(true);
            cur_view.setVisible(false);
            for (int i = 0; i < cur_size; i++) {
                icon = cur_view.getChildAt(i);
                icon.setRotationY(0);
                icon.endEffect();
            }
            for (int i = 0; i < next_size; i++) {
                icon = next_view.getChildAt(i);
                icon.setCameraDistance(-this_width / 2);
                icon.setRotationY((2 - 2 * absDegree) * (-90));
                icon.endEffect();
            }
        }
    }
}
