
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Inbox {

    final public static float DEFAULT_ANGLE = -60;

    public static void updateEffect(ViewGroup3D cur_view, ViewGroup3D next_view, float degree, float yScale,
            float this_width)
    {
        cur_view.mOriginX = this_width;
        cur_view.setPosition(degree * this_width, 0);
        cur_view.setRotationY(degree * DEFAULT_ANGLE);
        cur_view.setCameraDistance(-25);

        next_view.mOriginX = 0;
        next_view.setPosition((degree + 1) * this_width, 0);
        next_view.setRotationY((degree + 1) * DEFAULT_ANGLE);
        next_view.setCameraDistance(-25);

        cur_view.endEffect();
        next_view.endEffect();
    }
}
