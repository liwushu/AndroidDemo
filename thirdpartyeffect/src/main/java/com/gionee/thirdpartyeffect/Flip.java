
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Flip {
    public static void updateEffect(ViewGroup3D cur_view, ViewGroup3D next_view, float degree, float yScale,
            float this_width)
    {
        cur_view.setPosition(0, 0);
        next_view.setPosition(0, 0);

        if (degree <= 0 && degree >= -1 / 2f)
        {
            cur_view.setVisible(true);
            next_view.setVisible(false);
            cur_view.setRotationY(degree * 180);
            next_view.setRotationY((degree + 1) * 180);
            float color_a = (0.5f - Math.abs(degree)) * 2;
            cur_view.setAlpha(color_a);
            cur_view.setCameraDistance(-cur_view.getWidth() / 2);
        }
        else
        {
            cur_view.setVisible(false);
            next_view.setVisible(true);
            cur_view.setRotationY(degree * 180);
            next_view.setRotationY((degree + 1) * 180);
            float color_a = (Math.abs(degree) - 0.5f) * 2;
            next_view.setAlpha(color_a);
            next_view.setCameraDistance(-next_view.getWidth() / 2);
        }

        cur_view.endEffect();
        next_view.endEffect();

    }
}
