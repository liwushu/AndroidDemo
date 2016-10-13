package com.tcl.launcher.effect.ball;

import android.graphics.PointF;

import com.tcl.launcher.effect.page.Effector;
import com.tcl.launcher.effect.page.APageBase;
import com.tcl.launcher.effect.pojo.ViewEffect3D;
import com.tcl.launcher.effect.utils.MathUtils;

/**
 * Created by liwu.shu on 2016/8/29.
 */
public class BallEffector extends Effector {

    private final int mCameraDistance = -12;

    @Override
    public APageBase getPageBase() {
        return new APageBase() {
            @Override
            public void update(ViewEffect3D curPage, ViewEffect3D nextPage, float degree, float yScale, float this_width) {
                ViewEffect3D icon;
                final int cur_countX = (curPage).getCellCountX();
                final int cur_countY = (curPage).getCellCountY();
                final float rotaDegConst = 180 / cur_countX;
                float rotaDeg = 0;
                float ratio = 0;
                float rowAngleStart;
                float rowAngleArrayX = 0;
                float this_height = curPage.getHeight();
                final float cylinder_radius = this_width / 1.9f;

                if (this_height == 0) {
                    this_height = nextPage.getHeight();
                }

                rowAngleStart = 90 - 180.0f / cur_countY;

                curPage.setDrawOrder(true);
                nextPage.setDrawOrder(false);

                if (degree <= 0 && degree > (-1 / 8f)) {
                    ratio = (-8f) * degree;

                    for (int i = 0; i < curPage.getChildCount(); i++) {
                        int icon_column_num = 0;
                        int icon_row_num = 0;

                        icon = curPage.getChildAt(i);
                        icon_column_num = icon.getColumnNum();
                        icon_row_num = icon.getRowNum();

                        rotaDeg = 67.5f - (icon_column_num) * rotaDegConst;

                        rowAngleArrayX = -rowAngleStart + rowAngleStart * 2
                                / (cur_countY - 1) * icon_row_num;

                        icon.setRotationZ(-cylinder_radius);

                        build_ball(icon, rowAngleArrayX, -rotaDeg, ratio, this_width,
                                1.0f, this_height);

                        icon.endEffect();

                    }

                    for (int i = 0; i < nextPage.getChildCount(); i++) {
                        int icon_column_num = 0;
                        int icon_row_num = 0;

                        icon = nextPage.getChildAt(i);

                        icon_column_num = icon.getColumnNum();
                        icon_row_num = icon.getRowNum();

                        rotaDeg = (float) (67.5 - (icon_column_num) * rotaDegConst - 180);
                        rowAngleArrayX = -rowAngleStart + rowAngleStart * 2
                                / (cur_countY - 1) * icon_row_num;
                        icon.setRotationZ(-cylinder_radius);
                        build_ball(icon, rowAngleArrayX, -rotaDeg, ratio, this_width,
                                0, this_height);

                        icon.endEffect();

                    }

                } else if (degree <= (-1 / 8f) && degree > (-7 / 8f)) {
                    float des_degree = 0;
                    float des_rotaDeg = 0;
                    float des_ratio = 0;
                    ratio = (-degree - 1 / 8f) * 8 / 6f;

                    for (int i = 0; i < curPage.getChildCount(); i++) {
                        int icon_column_num = 0;
                        int icon_row_num = 0;

                        icon = curPage.getChildAt(i);

                        icon_column_num = icon.getColumnNum();
                        icon_row_num = icon.getRowNum();

                        rotaDeg = ((float) (67.5 - (icon_column_num) * rotaDegConst) + ratio * 180);

                        rowAngleArrayX = -rowAngleStart + rowAngleStart * 2
                                / (cur_countY - 1) * icon_row_num;

                        icon.setRotationZ(-cylinder_radius);

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

                    for (int i = 0; i < nextPage.getChildCount(); i++) {
                        int icon_column_num = 0;
                        int icon_row_num = 0;

                        icon = nextPage.getChildAt(i);

                        icon_column_num = icon.getColumnNum();
                        icon_row_num = icon.getRowNum();

                        rotaDeg = (float) (67.5 - (icon_column_num) * rotaDegConst
                                - 180 + ratio * 180);
                        rowAngleArrayX = -rowAngleStart + rowAngleStart * 2
                                / (cur_countY - 1) * icon_row_num;

                        icon.setRotationZ(-cylinder_radius);

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

                    for (int i = 0; i < curPage.getChildCount(); i++) {
                        int icon_column_num = 0;
                        int icon_row_num = 0;

                        icon = curPage.getChildAt(i);

                        icon_column_num = icon.getColumnNum();
                        icon_row_num = icon.getRowNum();

                        rotaDeg = ((float) (67.5 - (icon_column_num) * rotaDegConst) - 180);
                        rowAngleArrayX = -rowAngleStart + rowAngleStart * 2
                                / (cur_countY - 1) * icon_row_num;
                        icon.setRotationZ(-cylinder_radius);
                        destroy_ball(icon, rowAngleArrayX, -rotaDeg, ratio, this_width,
                                0, this_height);
                        icon.endEffect();

                    }

                    for (int i = 0; i < nextPage.getChildCount(); i++) {
                        int icon_column_num = 0;
                        int icon_row_num = 0;

                        icon = nextPage.getChildAt(i);

                        icon_column_num = icon.getColumnNum();
                        icon_row_num = icon.getRowNum();

                        rotaDeg = (float) (67.5 - (icon_column_num) * rotaDegConst);
                        rowAngleArrayX = -rowAngleStart + rowAngleStart * 2
                                / (cur_countY - 1) * icon_row_num;
                        icon.setRotationZ(-cylinder_radius);
                        destroy_ball(icon, rowAngleArrayX, -rotaDeg, ratio, this_width,
                                1.0f, this_height);
                        icon.endEffect();

                    }

                }
            }
        };
    }

    @Override
    public boolean isNeedClip() {
        return true;
    }

    private void build_ball(ViewEffect3D icon, float rotaDegX,
                                   float rotaDeg, float ratio, float this_width, float colora,
                                   float this_height) {
        PointF old_pos;
        old_pos = (PointF) icon.getTag();
        icon.setPosition(old_pos.x
                        + (this_width / 2 - old_pos.x - icon.mRotationX) * ratio,
                old_pos.y + (this_height / 2 - old_pos.y - icon.mRotationY)
                        * ratio);
        icon.setAlpha(colora);
        icon.setCameraDistance(mCameraDistance);
        icon.setRotationAngle(rotaDegX * ratio, rotaDeg * ratio, 0);
    }

    private void destroy_ball(ViewEffect3D icon, float rotaDegX,
                                     float rotaDeg, float ratio, float this_width, float colora,
                                     float this_height) {
        PointF old_pos;
        old_pos = (PointF) icon.getTag();
        icon.setPosition(this_width / 2 - icon.mRotationX
                        + (old_pos.x - (this_width / 2 - icon.mRotationX)) * ratio,
                this_height / 2 - icon.mRotationY
                        + (old_pos.y - (this_height / 2 - icon.mRotationY))
                        * ratio);

        icon.setAlpha(colora);
        icon.setCameraDistance(mCameraDistance);
        icon.setRotationAngle(rotaDegX * (1 - ratio), rotaDeg * (1 - ratio), 0);
    }

    private void rotate_ball(ViewEffect3D icon, float degree,
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
        icon.setPosition(this_width / 2 - icon.mRotationX, this_height / 2
                - icon.mRotationY);
        icon.setCameraDistance(mCameraDistance);
        icon.setRotationAngle(rotaDegX, rotaDeg, 0);
    }

    @Override
    public void getClipCount(int[] xy){
        xy[0] = 6;
        xy[1] = 6;
    }
}
