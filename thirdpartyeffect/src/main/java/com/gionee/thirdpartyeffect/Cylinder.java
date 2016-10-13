
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.utils.MathUtils;
import com.gionee.thirdpartyeffect.view.ViewGroup3D;

import android.graphics.PointF;

public class Cylinder {

    private static void build_cylinder(ViewGroup3D icon, float rotaDeg,
            float ratio, float this_width, float colora) {
        PointF old_pos;
        old_pos = (PointF) icon.getTag();
        icon.setPosition(old_pos.x
                + (this_width / 2 - old_pos.x - icon.mOriginX) * ratio,
                old_pos.y);
        icon.setCameraDistance(-20);
        icon.setAlpha(colora);
        icon.setRotationY(rotaDeg * ratio);
    }

    private static void destroy_cylinder(ViewGroup3D icon, float rotaDeg,
            float ratio, float this_width, float colora) {
        PointF old_pos;
        old_pos = (PointF) icon.getTag();
        icon.setPosition(this_width / 2 - icon.mOriginX
                + (old_pos.x - (this_width / 2 - icon.mOriginX)) * ratio,
                old_pos.y);
        icon.setCameraDistance(-20);
        icon.setAlpha(colora);
        icon.setRotationY(rotaDeg * (1 - ratio));
    }

    private static void rotate_cylinder(ViewGroup3D icon, float degree,
            float rotaDeg, float des_rotaDeg, float ratio, float this_width) {
        PointF old_pos;
        old_pos = (PointF) icon.getTag();
        float color_a = 0f;

        float temp = -MathUtils.cosDeg(rotaDeg);

        if (temp < 0) {
            color_a = 1f;
        } else {
            float des_temp = -MathUtils.cosDeg(des_rotaDeg);
            float des_a = (1 - des_temp) * 0.7f + 0.3f;

            if (degree > -0.21875)// -0.125~-0.21875
            {
                color_a = des_a * (degree - (-0.125f))
                        / (-0.21875f - (-0.125f));
                ;
            } else if (degree < -0.78125f)// -0.875~-0.78125
            {
                color_a = des_a * (degree - (-0.875f))
                        / (-0.78125f - (-0.875f));
            } else {
                color_a = (1 - temp) * 0.7f + 0.3f;
            }
        }
        icon.setAlpha(color_a);
        icon.setCameraDistance(-20);
        icon.setPosition(this_width / 2 - icon.mOriginX, old_pos.y);
        icon.setRotationY(rotaDeg);
    }

    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {
        ViewGroup3D icon;
        final int cur_countX = cur_view.getCellCountX();
        final float rotaDegConst = 180 / cur_countX;
        float rotaDeg = 0;
        float ratio = 0;
        final float cylinder_radius = (float) (this_width / 2);

        int cur_size = cur_view.getChildCount();
        int icon_column_num = 0;
        // cur_view.setOriginZ(-cylinder_radius);

        int next_size = next_view.getChildCount();
        // next_view.setOriginZ(-cylinder_radius);

        cur_view.setDrawOrder(true);
        next_view.setDrawOrder(false);

        if (degree <= 0 && degree > (-1 / 8f)) {
            ratio = (-8f) * degree;// 0~1

            for (int i = 0; i < cur_size; i++) {

                icon = cur_view.getChildAt(i);
                icon_column_num = icon.getColumnNum();

                rotaDeg = ((float) (67.5 - (icon_column_num) * rotaDegConst));// 67.5
                                                                              // 22.5
                                                                              // -22.5
                                                                              // -67.5

                icon.setOriginZ(-cylinder_radius);
                build_cylinder(icon, -rotaDeg, ratio, this_width, 1.0f);

                icon.endEffect();
            }

            for (int i = 0; i < next_size; i++) {

                icon = next_view.getChildAt(i);
                icon_column_num = icon.getColumnNum();

                rotaDeg = (float) (67.5 - (icon_column_num) * rotaDegConst - 180);// 67.5
                                                                                  // 22.5
                                                                                  // -22.5
                                                                                  // -67.5

                icon.setOriginZ(-cylinder_radius);
                build_cylinder(icon, -rotaDeg, ratio, this_width, 0f);

                icon.endEffect();
            }

        } else if (degree <= (-1 / 8f) && degree > (-7 / 8f)) {

            float des_degree = 0;
            float des_rotaDeg = 0;
            float des_ratio = 0;
            ratio = (-degree - 1 / 8f) * 8 / 6f;// 0~1

            for (int i = 0; i < cur_size; i++) {
                icon = cur_view.getChildAt(i);
                icon_column_num = icon.getColumnNum();

                rotaDeg = ((float) (67.5 - (icon_column_num) * rotaDegConst));// 67.5
                                                                              // 22.5
                                                                              // -22.5
                                                                              // -67.5
                rotaDeg = rotaDeg + ratio * 180;
                icon.setOriginZ(-cylinder_radius);
                float tt = -MathUtils.cosDeg(-rotaDeg);
                if (tt > 0) {
                    if (degree > -0.21875f)// -0.125~-0.21875
                    {
                        des_degree = -0.21875f;
                    } else if (degree < -0.78125f) {
                        des_degree = -0.78125f;
                    }
                    des_ratio = (-des_degree - 1 / 8f) * 8 / 6f;

                    des_rotaDeg = ((float) (67.5 - (icon_column_num)
                            * rotaDegConst));// 67.5 22.5 -22.5 -67.5

                    des_rotaDeg = des_rotaDeg + des_ratio * 180;
                }
                rotate_cylinder(icon, degree, -rotaDeg, -des_rotaDeg, ratio,
                        this_width);

                icon.endEffect();
            }
            for (int i = 0; i < next_size; i++) {
                icon = next_view.getChildAt(i);
                icon_column_num = icon.getColumnNum();

                rotaDeg = (float) (67.5 - (icon_column_num) * rotaDegConst - 180);// 67.5
                                                                                  // 22.5
                                                                                  // -22.5
                                                                                  // -67.5

                rotaDeg = rotaDeg + ratio * 180;
                icon.setOriginZ(-cylinder_radius);
                float tt = -MathUtils.cosDeg(-rotaDeg);
                if (tt > 0) {
                    if (degree > -0.21875f)// -0.125~-0.21875
                    {
                        des_degree = -0.21875f;
                    } else if (degree < -0.78125f) {
                        des_degree = -0.78125f;
                    }
                    des_ratio = (-des_degree - 1 / 8f) * 8 / 6f;

                    des_rotaDeg = (float) (67.5 - (icon_column_num)
                            * rotaDegConst - 180);// 67.5 22.5 -22.5 -67.5

                    des_rotaDeg = des_rotaDeg + des_ratio * 180;
                }
                rotate_cylinder(icon, degree, -rotaDeg, -des_rotaDeg, ratio,
                        this_width);

                icon.endEffect();
            }
        } else {
            ratio = -8f * degree - 7f;

            for (int i = 0; i < next_size; i++) {

                icon = next_view.getChildAt(i);
                icon_column_num = icon.getColumnNum();

                rotaDeg = (float) (67.5 - (icon_column_num) * rotaDegConst);// 67.5
                                                                            // 22.5
                                                                            // -22.5
                                                                            // -67.5

                icon.setOriginZ(-cylinder_radius);
                destroy_cylinder(icon, -rotaDeg, ratio, this_width, 1.0f);

                icon.endEffect();
            }
            for (int i = 0; i < cur_size; i++) {
                icon = cur_view.getChildAt(i);
                icon_column_num = icon.getColumnNum();

                rotaDeg = ((float) (67.5 - (icon_column_num) * rotaDegConst));// 67.5
                                                                              // 22.5
                                                                              // -22.5
                                                                              // -67.5

                icon.setOriginZ(-cylinder_radius);
                destroy_cylinder(icon, -rotaDeg, ratio, this_width, 0f);

                icon.endEffect();
            }
        }

    }
}
