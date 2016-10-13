
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;
import android.graphics.PointF;

public class Hump {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale,
            float this_width, boolean is_right_to_left) {
        float this_height = cur_view.getHeight();

        if (this_height == 0) {
            this_height = next_view.getHeight();
        }

        if (is_right_to_left == true) {
            if (degree <= 0 && degree >= -3 / 4f) {
                float ratio = -4f * degree / 3f; // 0--> 1

                next_view.setVisible(false);
                cur_view.setVisible(true);
                // cur_view gather together
                build_gather_together(cur_view, ratio, this_width, this_height,
                        1.0f);
                cur_view.setScale(1.0f + ratio / 2, 1.0f + ratio / 2);

            } else {
                // degree: -3/4 ~ -1
                float ratio = 4 * (1 + degree);// 1->0
                float color_a = ratio;
                // cur_view.setColor(color);
                cur_view.setAlpha(color_a);

                cur_view.setScale(2.0f - ratio / 2, 2.0f - ratio / 2);

                float color1_a = 1 - ratio;
                // next_view.setColor(color1);
                next_view.setAlpha(color1_a);
                next_view.setScale(1.0f - 0.4f * ratio, 1.0f - 0.4f * ratio);
            }

            cur_view.endEffect();
            next_view.endEffect();
        } else {

            if (degree <= 0 && degree <= -1 / 4f) {
                float ratio = 4 * (1 + degree) / 3; // 0--> 1

                next_view.setVisible(true);
                cur_view.setVisible(false);
                // cur_view gather together
                next_view.setScale(1.0f + ratio / 2, 1.0f + ratio / 2);
                build_gather_together(next_view, ratio, this_width,
                        this_height, 1.0f);

            } else {
                // degree: -1/4 ~ 0
                float ratio = -4 * degree;// 1->0
                float color_a = ratio;
                // next_view.setColor(color);
                next_view.setAlpha(color_a);
                next_view.setScale(2.0f - ratio / 2, 2.0f - ratio / 2);

                float color1_a = 1 - ratio;
                // cur_view.setColor(color1);
                cur_view.setAlpha(color1_a);
                cur_view.setScale(1.0f - 0.4f * ratio, 1.0f - 0.4f * ratio);
            }

            cur_view.endEffect();
            next_view.endEffect();
        }
    }

    private static void build_gather_together(ViewGroup3D view, float ratio,
            float this_width, float this_height, float color_a) {
        ViewGroup3D icon;
        int view_countX = (view).getCellCountX(); // columns
        int view_countY = (view).getCellCountY(); // columns

        float section_ratio = ratio;

        final float rotaDegConst = 90 / view_countX;
        float rotaDeg = 0;
        float rowAngleStart;
        float rowAngleArrayX = 0;
        final float cylinder_radius = this_width / 1.8f;

        if (view_countY > 4) {
            rowAngleStart = 45;
        } else {
            rowAngleStart = 35;
        }

        for (int index = 0; index < view.getChildCount(); index++) {
            PointF old_pos;
            int column = 0, row = 0;
            icon = view.getChildAt(index);
            column = icon.getColumnNum();
            row = icon.getRowNum();

            rotaDeg = ((float) (34.75 - (column % view_countX) * rotaDegConst));
            rowAngleArrayX = -rowAngleStart + rowAngleStart * 2
                    / (view_countY - 1) * row;
            icon.setOriginZ(-cylinder_radius);

            old_pos = (PointF) icon.getTag();
            if (Math.abs(ratio) <= 1.0f / 4.0f) {
                section_ratio = 2 * ratio;
            } else {
                section_ratio = 1.0f / 2.0f + (ratio - 1.0f / 4.0f) * 2.0f
                        / 3.0f;
            }
            icon.setPosition(old_pos.x
                    + (this_width / 2 - old_pos.x - icon.mOriginX)
                    * section_ratio, old_pos.y
                    + (this_height / 2 - old_pos.y - icon.mOriginY)
                    * section_ratio);

            icon.setAlpha(color_a);
            icon.setRotationAngle(rowAngleArrayX * section_ratio, -rotaDeg
                    * section_ratio, 0);
            icon.endEffect();

        }

    }
}
