
package com.gionee.thirdpartyeffect;

import android.graphics.PointF;

import com.gionee.thirdpartyeffect.utils.MathUtils;
import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Ball {

    private static final int mCameraDistance = -12;

    private static void build_ball(ViewGroup3D icon, float rotaDegX,
            float rotaDeg, float ratio, float this_width, float colora,
            float this_height) {
        PointF old_pos;
        old_pos = (PointF) icon.getTag();
        icon.setPosition(old_pos.x
                + (this_width / 2 - old_pos.x - icon.mOriginX) * ratio,
                old_pos.y + (this_height / 2 - old_pos.y - icon.mOriginY)
                        * ratio);
        icon.setAlpha(colora);
        icon.setCameraDistance(mCameraDistance);
        icon.setRotationAngle(rotaDegX * ratio, rotaDeg * ratio, 0);
    }

    private static void destroy_ball(ViewGroup3D icon, float rotaDegX,
            float rotaDeg, float ratio, float this_width, float colora,
            float this_height) {
        PointF old_pos;
        old_pos = (PointF) icon.getTag();
        icon.setPosition(this_width / 2 - icon.mOriginX
                + (old_pos.x - (this_width / 2 - icon.mOriginX)) * ratio,
                this_height / 2 - icon.mOriginY
                        + (old_pos.y - (this_height / 2 - icon.mOriginY))
                        * ratio);

        icon.setAlpha(colora);
        icon.setCameraDistance(mCameraDistance);
        icon.setRotationAngle(rotaDegX * (1 - ratio), rotaDeg * (1 - ratio), 0);
    }

    private static void rotate_ball(ViewGroup3D icon, float degree,
            float rotaDegX, float rotaDeg, float des_rotaDeg, float ratio,
            float this_width, float this_height) {
        float color_a;
        float temp = MathUtils.cosDeg(rotaDeg) * MathUtils.cosDeg(rotaDegX);
        if (temp > 0) {
            color_a = 1;
        } else {
            float des_temp = MathUtils.cosDeg(des_rotaDeg)
                    * MathUtils.cosDeg(rotaDegX);
            float des_a = (1 + des_temp) * 0.7f + 0.3f;
            if (degree > -0.21875)// -0.125~-0.21875
            {
                color_a = des_a * (degree - (-0.125f))
                        / (-0.21875f - (-0.125f));// 0~des_a
            } else if (degree < -0.78125f)// -0.875~-0.78125
            {
                color_a = des_a * (degree - (-0.875f))
                        / (-0.78125f - (-0.875f));// 0~des_a
            } else {
                color_a = (1 + temp) * 0.7f + 0.3f;
            }
        }
        icon.setAlpha(color_a);
        icon.setPosition(this_width / 2 - icon.mOriginX, this_height / 2
                - icon.mOriginY);
        icon.setCameraDistance(mCameraDistance);
        icon.setRotationAngle(rotaDegX, rotaDeg, 0);
    }

    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {
        ViewGroup3D icon;
        final int cur_countX = (cur_view).getCellCountX();
        final int cur_countY = (cur_view).getCellCountY();
        final float rotaDegConst = 180 / cur_countX;
        float rotaDeg = 0;
        float ratio = 0;
        float rowAngleStart;
        float rowAngleArrayX = 0;
        float this_height = cur_view.getHeight();
        final float cylinder_radius = this_width / 1.9f;

        if (this_height == 0) {
            this_height = next_view.getHeight();
        }

        rowAngleStart = 90 - 180.0f / cur_countY;

        cur_view.setDrawOrder(true);
        next_view.setDrawOrder(false);

        if (degree <= 0 && degree > (-1 / 8f)) {
            ratio = (-8f) * degree;

            for (int i = 0; i < cur_view.getChildCount(); i++) {
                int icon_column_num = 0;
                int icon_row_num = 0;

                icon = cur_view.getChildAt(i);
                icon_column_num = icon.getColumnNum();
                icon_row_num = icon.getRowNum();

                rotaDeg = 67.5f - (icon_column_num) * rotaDegConst;

                rowAngleArrayX = -rowAngleStart + rowAngleStart * 2
                        / (cur_countY - 1) * icon_row_num;

                icon.setOriginZ(-cylinder_radius);

                build_ball(icon, rowAngleArrayX, -rotaDeg, ratio, this_width,
                        1.0f, this_height);

                icon.endEffect();

            }

            for (int i = 0; i < next_view.getChildCount(); i++) {
                int icon_column_num = 0;
                int icon_row_num = 0;

                icon = next_view.getChildAt(i);

                icon_column_num = icon.getColumnNum();
                icon_row_num = icon.getRowNum();

                rotaDeg = (float) (67.5 - (icon_column_num) * rotaDegConst - 180);
                rowAngleArrayX = -rowAngleStart + rowAngleStart * 2
                        / (cur_countY - 1) * icon_row_num;
                icon.setOriginZ(-cylinder_radius);
                build_ball(icon, rowAngleArrayX, -rotaDeg, ratio, this_width,
                        0, this_height);

                icon.endEffect();

            }

        } else if (degree <= (-1 / 8f) && degree > (-7 / 8f)) {
            float des_degree = 0;
            float des_rotaDeg = 0;
            float des_ratio = 0;
            ratio = (-degree - 1 / 8f) * 8 / 6f;

            for (int i = 0; i < cur_view.getChildCount(); i++) {
                int icon_column_num = 0;
                int icon_row_num = 0;

                icon = cur_view.getChildAt(i);

                icon_column_num = icon.getColumnNum();
                icon_row_num = icon.getRowNum();

                rotaDeg = ((float) (67.5 - (icon_column_num) * rotaDegConst) + ratio * 180);

                rowAngleArrayX = -rowAngleStart + rowAngleStart * 2
                        / (cur_countY - 1) * icon_row_num;

                icon.setOriginZ(-cylinder_radius);

                float tt = MathUtils.cosDeg(rotaDeg)
                        * MathUtils.cosDeg(rowAngleArrayX);
                if (tt < 0) {
                    if (degree > -0.21875f)// -0.125~-0.21875
                    {
                        des_degree = -0.21875f;
                    } else if (degree < -0.78125f) {
                        des_degree = -0.78125f;
                    }
                    des_ratio = (-des_degree - 1 / 8f) * 8 / 6f;
                    des_rotaDeg = ((float) (67.5 - (icon_column_num)
                            * rotaDegConst) + des_ratio * 180);
                }
                rotate_ball(icon, degree, rowAngleArrayX, -rotaDeg,
                        -des_rotaDeg, ratio, this_width, this_height);
                icon.endEffect();

            }

            for (int i = 0; i < next_view.getChildCount(); i++) {
                int icon_column_num = 0;
                int icon_row_num = 0;

                icon = next_view.getChildAt(i);

                icon_column_num = icon.getColumnNum();
                icon_row_num = icon.getRowNum();

                rotaDeg = (float) (67.5 - (icon_column_num) * rotaDegConst
                        - 180 + ratio * 180);
                rowAngleArrayX = -rowAngleStart + rowAngleStart * 2
                        / (cur_countY - 1) * icon_row_num;

                icon.setOriginZ(-cylinder_radius);

                float tt = MathUtils.cosDeg(rotaDeg)
                        * MathUtils.cosDeg(rowAngleArrayX);
                if (tt < 0) {
                    if (degree > -0.21875f)// -0.125~-0.21875
                    {
                        des_degree = -0.21875f;
                    } else if (degree < -0.78125f) {
                        des_degree = -0.78125f;
                    }
                    des_ratio = (-des_degree - 1 / 8f) * 8 / 6f;
                    des_rotaDeg = (float) (67.5 - (icon_column_num)
                            * rotaDegConst - 180 + des_ratio * 180);
                }
                rotate_ball(icon, degree, rowAngleArrayX, -rotaDeg,
                        -des_rotaDeg, ratio, this_width, this_height);
                icon.endEffect();

            }

        } else {
            ratio = -8f * degree - 7f;

            for (int i = 0; i < cur_view.getChildCount(); i++) {
                int icon_column_num = 0;
                int icon_row_num = 0;

                icon = cur_view.getChildAt(i);

                icon_column_num = icon.getColumnNum();
                icon_row_num = icon.getRowNum();

                rotaDeg = ((float) (67.5 - (icon_column_num) * rotaDegConst) - 180);
                rowAngleArrayX = -rowAngleStart + rowAngleStart * 2
                        / (cur_countY - 1) * icon_row_num;
                icon.setOriginZ(-cylinder_radius);
                destroy_ball(icon, rowAngleArrayX, -rotaDeg, ratio, this_width,
                        0, this_height);
                icon.endEffect();

            }

            for (int i = 0; i < next_view.getChildCount(); i++) {
                int icon_column_num = 0;
                int icon_row_num = 0;

                icon = next_view.getChildAt(i);

                icon_column_num = icon.getColumnNum();
                icon_row_num = icon.getRowNum();

                rotaDeg = (float) (67.5 - (icon_column_num) * rotaDegConst);
                rowAngleArrayX = -rowAngleStart + rowAngleStart * 2
                        / (cur_countY - 1) * icon_row_num;
                icon.setOriginZ(-cylinder_radius);
                destroy_ball(icon, rowAngleArrayX, -rotaDeg, ratio, this_width,
                        1.0f, this_height);
                icon.endEffect();

            }

        }

    }
}
