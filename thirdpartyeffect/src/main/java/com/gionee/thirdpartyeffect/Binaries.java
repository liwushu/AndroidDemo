
package com.gionee.thirdpartyeffect;

import android.graphics.PointF;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Binaries {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {
        ViewGroup3D icon;
        PointF pos;
        PointF old_pos;
        float radio;
        float this_height = cur_view.getHeight();

        if (this_height == 0) {
            this_height = next_view.getHeight();
        }

        pos = new PointF(this_width / 2.0f, this_height / 2.0f);
        if (degree <= 0 && degree > (-1 / 2f)) {
            radio = degree * (-2f);
        } else {
            radio = (degree + 1) * (2f);
        }

        cur_view.setPosition(degree * this_width, 0);
        int cur_size = cur_view.getChildCount();
        for (int i = 0; i < cur_size; i++) {
            if (i < cur_view.getChildCount()) {
                icon = cur_view.getChildAt(i);
                old_pos = (PointF) icon.getTag();
                icon.setPosition(old_pos.x + (pos.x - old_pos.x) * radio,
                        old_pos.y + (pos.y - old_pos.y) * radio);
                icon.endEffect();
            }
        }
        cur_view.endEffect();

        next_view.setPosition((degree + 1) * this_width, 0);
        int next_size = next_view.getChildCount();
        for (int i = 0; i < next_size; i++) {
            if (i < next_view.getChildCount()) {
                icon = next_view.getChildAt(i);
                old_pos = (PointF) icon.getTag();
                float newX = old_pos.x + (pos.x - old_pos.x) * radio;
                float newY = old_pos.y + (pos.y - old_pos.y) * radio;
                icon.setPosition(newX, newY);
                icon.endEffect();
            }
        }
        next_view.endEffect();
    }
}
