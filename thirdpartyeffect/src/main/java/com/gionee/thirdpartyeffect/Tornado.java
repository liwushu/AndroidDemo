
package com.gionee.thirdpartyeffect;

import android.graphics.PointF;

import com.gionee.thirdpartyeffect.utils.MathUtils;
import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Tornado {

    final static float DEFAULT_ANGLE = 67.5f;

    private static void build_tornado(ViewGroup3D icon, float rotaDegX,
            float rotaDeg, float ratio, float scale_x, float this_width,
            float colora) {
        PointF old_pos;
        old_pos = (PointF) icon.getTag();
        icon.setPosition(old_pos.x
                + (this_width / 2 - old_pos.x - icon.mOriginX) * ratio,
                old_pos.y);
        icon.setAlpha(colora);
        icon.setScale(scale_x, 1.0f);
        icon.setRotationAngle(rotaDegX * ratio, rotaDeg * ratio, 0);
    }

    private static void destroy_tornado(ViewGroup3D icon, float rotaDegX,
            float rotaDeg, float ratio, float scale_x, float this_width,
            float colora) {
        PointF old_pos;
        old_pos = (PointF) icon.getTag();
        icon.setPosition(this_width / 2 - icon.mOriginX
                + (old_pos.x - (this_width / 2 - icon.mOriginX)) * ratio,
                old_pos.y);
        icon.setAlpha(colora);
        icon.setScale(scale_x, 1.0f);
        icon.setRotationAngle(rotaDegX * (1 - ratio), rotaDeg * (1 - ratio), 0);
    }

    private static void rotate_tornado(ViewGroup3D icon, float degree,
            float rotaDegX, float rotaDeg, float des_rotaDeg, float ratio,
            float scale_x, float this_width) {
        PointF old_pos;
        old_pos = (PointF) icon.getTag();
        float color_a = 0f;
        float temp = -MathUtils.cosDeg(rotaDeg);

        if (temp < 0) {
            color_a = 1;
        } else {
            float des_temp = -MathUtils.cosDeg(des_rotaDeg);
            float des_a = (1 - des_temp) * 0.7f + 0.3f;

            if (degree > -0.21875)// -0.125~-0.21875
            {
                color_a = des_a * (degree - (-0.125f))
                        / (-0.21875f - (-0.125f));// 0~des_a
            } else if (degree < -0.78125f)// -0.875~-0.78125
            {
                color_a = des_a * (degree - (-0.875f))
                        / (-0.78125f - (-0.875f));// 0~des_a
            } else {
                color_a = (1 - temp) * 0.7f + 0.3f;
            }
        }
        icon.setAlpha(color_a);
        icon.setPosition(this_width / 2 - icon.mOriginX, old_pos.y);
        icon.setScale(scale_x, 1.0f);
        icon.setRotationAngle(rotaDegX, rotaDeg, 0);

    }

    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {
        ViewGroup3D icon;
        final int cur_countX = (cur_view).getCellCountX();
        final int cur_countY = (cur_view).getCellCountY();
        final float rotaDegConst = 180 / cur_countX;
        float rowAngleArrayX = 0;
        float rotaDeg = 0;
        float ratio = 0;
        float rowAngleStart;
        final float top_circle_radius = (float) (this_width) * 2 / 3;
        final float bottom_circle_radius = (float) top_circle_radius / 5;
        float scaleX = 0f;

        if (cur_countY > 4) {
            rowAngleStart = 30;
        } else {
            rowAngleStart = 40;
        }

        cur_view.setDrawOrder(true);
        next_view.setDrawOrder(false);

        int cur_size = cur_view.getChildCount();
        int next_size = next_view.getChildCount();

        if (degree <= 0 && degree > (-1 / 8f)) {
            ratio = (-8f) * degree;// 0~1
            for (int i = 0; i < cur_size; i++) {
                int icon_row_num = 0;
                int icon_column_num = 0;
                float icon_circle_radius = 0f;
                icon = cur_view.getChildAt(i);

                icon_row_num = icon.getRowNum();
                icon_column_num = icon.getColumnNum();
                icon_circle_radius = top_circle_radius
                        - (top_circle_radius - bottom_circle_radius)
                        * (icon_row_num) / (cur_countY - 1);

                if (icon_row_num == 0) {
                    icon_circle_radius -= bottom_circle_radius;
                }

                rotaDeg = ((float) (DEFAULT_ANGLE - (icon_column_num)
                        * rotaDegConst));// 67.5 22.5 -22.5 -67.5
                icon.setOriginZ(-icon_circle_radius);

                rowAngleArrayX = /*-rowAngleStart+*/rowAngleStart
                        / (cur_countY - 1) * icon_row_num;
                scaleX = 1.0f - (float) (icon_row_num) / (cur_countY) * ratio;
                build_tornado(icon, rowAngleArrayX, -rotaDeg, ratio, scaleX,
                        this_width, 1.0f);
                icon.endEffect();
            }

            for (int i = 0; i < next_size; i++) {
                int icon_row_num = 0;
                int icon_column_num = 0;
                float icon_circle_radius = 0f;

                icon = next_view.getChildAt(i);

                icon_row_num = icon.getRowNum();
                icon_column_num = icon.getColumnNum();
                icon_circle_radius = top_circle_radius
                        - (top_circle_radius - bottom_circle_radius)
                        * (icon_row_num) / (cur_countY - 1);

                if (icon_row_num == 0) {
                    icon_circle_radius -= bottom_circle_radius;
                }

                rotaDeg = (float) (DEFAULT_ANGLE - (icon_column_num)
                        * rotaDegConst - 180);
                icon.setOriginZ(-icon_circle_radius);
                rowAngleArrayX = rowAngleStart / (cur_countY - 1)
                        * icon_row_num;
                scaleX = 1.0f - (float) (icon_row_num) / (cur_countY) * ratio;
                build_tornado(icon, rowAngleArrayX, -rotaDeg, ratio, scaleX,
                        this_width, 0);
                icon.endEffect();
            }
        } else if (degree <= (-1 / 8f) && degree > (-7 / 8f)) {
            float des_degree = 0;
            float des_rotaDeg = 0;
            float des_ratio = 0;
            ratio = (-degree - 1 / 8f) * 8 / 6f;// 0~1

            for (int i = 0; i < cur_size; i++) {
                int icon_row_num = 0;
                int icon_column_num = 0;
                float icon_circle_radius = 0f;

                icon = cur_view.getChildAt(i);

                icon_row_num = icon.getRowNum();
                icon_column_num = icon.getColumnNum();
                icon_circle_radius = top_circle_radius
                        - (top_circle_radius - bottom_circle_radius)
                        * (icon_row_num) / (cur_countY - 1);

                /*
                 * if(icon_row_num == 0) { icon_circle_radius -=
                 * bottom_circle_radius; }
                 */

                rotaDeg = (float) (DEFAULT_ANGLE - (icon_column_num)
                        * rotaDegConst);
                rotaDeg = rotaDeg + ratio * 180;
                icon.setOriginZ(-icon_circle_radius/*
                                                    * top_circle_radius*icon.getY
                                                    * ()/cur_view.getHeight()
                                                    */);
                rowAngleArrayX = rowAngleStart / (cur_countY - 1)
                        * icon_row_num;
                float tt = -MathUtils.cosDeg(-rotaDeg);
                if (tt > 0) {
                    if (degree > -0.21875f)// -0.125~-0.21875
                    {
                        des_degree = -0.21875f;
                    } else if (degree < -0.78125f) {
                        des_degree = -0.78125f;
                    }
                    des_ratio = (-des_degree - 1 / 8f) * 8 / 6f;
                    des_rotaDeg = (float) (DEFAULT_ANGLE - (i % cur_countX)
                            * rotaDegConst);
                    des_rotaDeg = des_rotaDeg + des_ratio * 180;
                }
                scaleX = 1.0f - (float) (icon_row_num) / (cur_countY);
                rotate_tornado(icon, degree, rowAngleArrayX, -rotaDeg,
                        -des_rotaDeg, ratio, scaleX, this_width);
                icon.endEffect();
            }
            for (int i = 0; i < next_size; i++) {
                int icon_row_num = 0;
                int icon_column_num = 0;
                float icon_circle_radius = 0f;

                icon = next_view.getChildAt(i);

                icon_row_num = icon.getRowNum();
                icon_column_num = icon.getColumnNum();
                icon_circle_radius = top_circle_radius
                        - (top_circle_radius - bottom_circle_radius)
                        * (icon_row_num) / (cur_countY - 1);

                /*
                 * if(icon_row_num == 0) { icon_circle_radius -=
                 * bottom_circle_radius; }
                 */

                rotaDeg = (float) (DEFAULT_ANGLE - (icon_column_num)
                        * rotaDegConst - 180);
                rotaDeg = rotaDeg + ratio * 180;
                icon.setOriginZ(-icon_circle_radius/*
                                                    * top_circle_radius*icon.getY
                                                    * ()/next_view.getHeight()
                                                    */);
                rowAngleArrayX = rowAngleStart / (cur_countY - 1)
                        * icon_row_num;
                float tt = -MathUtils.cosDeg(-rotaDeg);
                if (tt > 0) {
                    if (degree > -0.21875f)// -0.125~-0.21875
                    {
                        des_degree = -0.21875f;
                    } else if (degree < -0.78125f) {
                        des_degree = -0.78125f;
                    }
                    des_ratio = (-des_degree - 1 / 8f) * 8 / 6f;
                    des_rotaDeg = (float) (DEFAULT_ANGLE - (i % cur_countX)
                            * rotaDegConst - 180);
                    des_rotaDeg = des_rotaDeg + des_ratio * 180;
                }
                scaleX = 1.0f - (float) (icon_row_num) / (cur_countY);
                rotate_tornado(icon, degree, rowAngleArrayX, -rotaDeg,
                        -des_rotaDeg, ratio, scaleX, this_width);
                icon.endEffect();
            }
        } else {
            ratio = -8f * degree - 7f;

            for (int i = 0; i < next_size; i++) {
                int icon_row_num = 0;
                int icon_column_num = 0;
                float icon_circle_radius = 0f;

                icon = next_view.getChildAt(i);

                icon_row_num = icon.getRowNum();
                icon_column_num = icon.getColumnNum();
                icon_circle_radius = top_circle_radius
                        - (top_circle_radius - bottom_circle_radius)
                        * (icon_row_num) / (cur_countY - 1);
                if (icon_row_num == 0) {
                    icon_circle_radius -= bottom_circle_radius;
                }
                rotaDeg = ((float) (DEFAULT_ANGLE - (icon_column_num)
                        * rotaDegConst));
                icon.setOriginZ(-icon_circle_radius/*
                                                    * top_circle_radius*icon.getY
                                                    * ()/next_view.getHeight()
                                                    */);

                rowAngleArrayX = rowAngleStart / (cur_countY - 1)
                        * icon_row_num;
                scaleX = 1.0f /*- (float)(icon_row_num)/(cur_countY)*/;
                destroy_tornado(icon, rowAngleArrayX, -rotaDeg, ratio, scaleX,
                        this_width, 1.0f);
                icon.endEffect();
            }
            for (int i = 0; i < cur_size; i++) {
                int icon_row_num = 0;
                int icon_column_num = 0;
                float icon_circle_radius = 0f;

                icon = cur_view.getChildAt(i);
                icon_row_num = icon.getRowNum();
                icon_column_num = icon.getColumnNum();

                icon_circle_radius = top_circle_radius
                        - (top_circle_radius - bottom_circle_radius)
                        * (icon_row_num) / (cur_countY - 1);
                if (icon_row_num == 0) {
                    icon_circle_radius -= bottom_circle_radius;
                }
                rotaDeg = (float) (DEFAULT_ANGLE - (icon_column_num)
                        * rotaDegConst);
                icon.setOriginZ(-icon_circle_radius/*
                                                    * top_circle_radius*icon.getY
                                                    * ()/cur_view.getHeight()
                                                    */);
                rowAngleArrayX = rowAngleStart / (cur_countY - 1)
                        * icon_row_num;
                scaleX = 1.0f/* - (float)(icon_row_num)/(cur_countY) */;
                destroy_tornado(icon, rowAngleArrayX, -rotaDeg, ratio, scaleX,
                        this_width, 0);
                icon.endEffect();
            }
        }

    }

}
