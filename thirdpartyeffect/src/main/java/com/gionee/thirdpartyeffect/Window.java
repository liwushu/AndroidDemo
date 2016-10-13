
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Window {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {
        ViewGroup3D icon;
        final int cur_countX = cur_view.getCellCountX();
        int cur_size = cur_view.getChildCount();

        for (int i = 0; i < cur_size; i++) {
            int icon_column_num = 0;

            icon = cur_view.getChildAt(i);
            icon_column_num = icon.getColumnNum();
            if (icon_column_num < cur_countX / 2) {
                float origin_posX = -icon.getX();
                icon.setCameraDistance(-this_width / 2);
                icon.setOrigin(origin_posX, icon.mOriginY);
                icon.setRotationY(-1 * Math.abs(degree) * 90);
            } else {
                float origin_posX = this_width - icon.getX();
                icon.setCameraDistance(-this_width / 2);
                icon.setOrigin(origin_posX, icon.mOriginY);
                icon.setRotationY(1 * Math.abs(degree) * 90);
            }

            icon.endEffect();
        }

        float scale = Math.abs(degree);
        float color_a = Math.abs(degree);
        next_view.setAlpha(color_a);
        next_view.setScale(scale, scale);
        next_view.endEffect();
    }
}
