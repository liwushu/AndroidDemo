
package com.gionee.thirdpartyeffect;

import android.graphics.PointF;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Cross {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {
        ViewGroup3D icon;
        PointF old_pos;
        int cur_size = cur_view.getChildCount();
        int next_size = next_view.getChildCount();

        for (int i = 0; i < cur_size; i++) {
            if (i < cur_view.getChildCount()) {
                int item_row_idx = 0;
                icon = cur_view.getChildAt(i);
                item_row_idx = icon.getRowNum();

                old_pos = (PointF) icon.getTag();

                float item_color_a = 1.0f - Math.abs(degree);
                icon.setAlpha(item_color_a);

                if (item_row_idx % 2 == 0) {
                    icon.setPosition(old_pos.x + this_width * Math.abs(degree),
                            icon.getY());
                } else {
                    icon.setPosition(old_pos.x - this_width * Math.abs(degree),
                            icon.getY());
                }
                icon.endEffect();
            }
        }

        for (int i = 0; i < next_size; i++) {
            if (i < next_view.getChildCount()) {
                int item_row_idx = 0;
                icon = next_view.getChildAt(i);
                item_row_idx = icon.getRowNum();

                old_pos = (PointF) icon.getTag();

                float item_color_a = Math.abs(degree);
                icon.setAlpha(item_color_a);

                if (item_row_idx % 2 == 0) {
                    icon.setPosition(
                            old_pos.x + this_width * Math.abs(degree + 1),
                            icon.getY());
                } else {
                    icon.setPosition(
                            old_pos.x - this_width * Math.abs(degree + 1),
                            icon.getY());
                }

                icon.endEffect();
            }
        }

    }
}
