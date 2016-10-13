
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Fan {
    public static void updateEffect(ViewGroup3D cur_view, ViewGroup3D next_view, float degree, float yScale,
            float this_width)
    {

        cur_view.setOrigin(cur_view.getWidth() / 2, -(cur_view.mOriginX));
        cur_view.setRotationZ(-degree * 90);

        next_view.setOrigin(next_view.getWidth() / 2, -(next_view.mOriginX));
        next_view.setRotationZ(-(degree + 1) * 90);

        cur_view.endEffect();
        next_view.endEffect();
    }
}
