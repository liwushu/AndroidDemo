package com.tcl.launcher.effect.wheel;

import android.graphics.PointF;

import com.tcl.launcher.effect.page.Effector;
import com.tcl.launcher.effect.page.APageBase;
import com.tcl.launcher.effect.pojo.ViewEffect3D;

/**
 * Created by liwu.shu on 2016/8/29.
 */
public class WheelEffector extends Effector {

    @Override
    public APageBase getPageBase() {
        return new APageBase() {
            @Override
            public void update(ViewEffect3D cur_view, ViewEffect3D next_view, float degree, float yScale, float this_width) {
                ViewEffect3D icon;
                PointF pos;
                PointF old_pos;
                float this_height = cur_view.getHeight();
                float radio;
                float Radius = this_width / 3.0f;
                float Angle;
                float add_angle = 90f;
                float cur_line_add_angle = 0f;

                if (this_height == 0) {
                    this_height = next_view.getHeight();
                }
                cur_view.setPosition(degree * this_width, 0);
                int cur_size = cur_view.getChildCount();

                next_view.setPosition((degree + 1) * this_width, 0);
                int next_size = next_view.getChildCount();

                if (degree <= 0 && degree > (-1 / 3f)) {
                    radio = degree * (-3f);

                    for (int i = 0; i < cur_size; i++) {
                        int icon_column_num = 0;
                        icon = cur_view.getChildAt(i);
                        icon_column_num = icon.getColumnNum();

                        float width = icon.getWidth();
                        float height = icon.getHeight();
                        pos = new PointF(this_width / 2.0f - width / 2, this_height
                                / 2.0f - Radius - height / 2);
                        old_pos = (PointF) icon.getTag();
                        System.out.println("i: "+i+"  old_pos: "+old_pos.x+","+old_pos.y);
                        icon.setPosition(old_pos.x + (pos.x - old_pos.x) * radio,
                                old_pos.y + (pos.y - old_pos.y) * radio);
                        Angle = (add_angle + i * 360 / cur_size) % 360;
                        if (Angle > 180 && (icon_column_num == 0))
                            cur_line_add_angle = -360f;
                        Angle += cur_line_add_angle;
                        icon.setRotation(icon.getWidth() / 2, icon.getHeight() / 2
                                + Radius * radio);
                        icon.setRotationDegreeZ(Angle * radio);
                        icon.endEffect();
                    }
                    for (int i = 0; i < next_size; i++) {
                        icon = next_view.getChildAt(i);
                        float width = icon.getWidth();
                        float height = icon.getHeight();
                        pos = new PointF(this_width / 2.0f - width / 2, this_height
                                / 2.0f - Radius - height / 2);
                        old_pos = (PointF) icon.getTag();
                        icon.setPosition(old_pos.x + (pos.x - old_pos.x) * radio,
                                old_pos.y + (pos.y - old_pos.y) * radio);

                        Angle = (i * 360 / next_size) % 360;
                        if (Angle > 180 && (i % next_view.getCellCountX() == 0))
                            cur_line_add_angle = -360f;
                        Angle += cur_line_add_angle;
                        icon.setRotation(icon.getWidth() / 2, icon.getHeight() / 2
                                + Radius * radio);
                        icon.setRotationDegreeZ(Angle * radio);
                        icon.endEffect();
                    }
                } else if (degree > (-2 / 3f)) {
                    radio = (1 / 3f + degree) * (-3);

                    for (int i = 0; i < cur_size; i++) {
                        int icon_column_num = 0;

                        icon = cur_view.getChildAt(i);
                        icon_column_num = icon.getColumnNum();

                        float width = icon.getWidth();
                        float height = icon.getHeight();
                        pos = new PointF(this_width / 2.0f - width / 2, this_height
                                / 2.0f - Radius - height / 2);
                        old_pos = (PointF) icon.getTag();
                        icon.setPosition(pos.x, pos.y);

                        Angle = (i * 360 / cur_size) % 360;
                        if (Angle > 180 && (icon_column_num == 0))
                            cur_line_add_angle = -360f;
                        Angle += cur_line_add_angle;
                        icon.setRotation(icon.getWidth() / 2, icon.getHeight() / 2
                                + Radius);
                        icon.setRotationDegreeZ((add_angle + Angle) - add_angle * radio);
                        icon.endEffect();
                    }

                    for (int i = 0; i < next_size; i++) {
                        int icon_column_num = 0;
                        icon = next_view.getChildAt(i);
                        icon_column_num = icon.getColumnNum();

                        float width = icon.getWidth();
                        float height = icon.getHeight();
                        pos = new PointF(this_width / 2.0f - width / 2, this_height
                                / 2.0f - Radius - height / 2);
                        old_pos = (PointF) icon.getTag();
                        icon.setPosition(pos.x, pos.y);

                        Angle = (i * 360 / next_size) % 360;
                        if (Angle > 180 && (icon_column_num == 0))
                            cur_line_add_angle = -360f;
                        Angle += cur_line_add_angle;
                        icon.setRotation(icon.getWidth() / 2, icon.getHeight() / 2
                                + Radius);
                        icon.setRotationDegreeZ(Angle - add_angle * radio);
                        icon.endEffect();
                    }
                } else {
                    radio = (degree + 1) * (3f);

                    for (int i = 0; i < cur_size; i++) {
                        int icon_column_num = 0;
                        icon = cur_view.getChildAt(i);

                        icon_column_num = icon.getColumnNum();
                        float width = icon.getWidth();
                        float height = icon.getHeight();
                        pos = new PointF(this_width / 2.0f - width / 2, this_height
                                / 2.0f - Radius - height / 2);
                        old_pos = (PointF) icon.getTag();
                        icon.setPosition(old_pos.x + (pos.x - old_pos.x) * radio,
                                old_pos.y + (pos.y - old_pos.y) * radio);
                        Angle = (add_angle * 2 + i * 360 / cur_size) % 360;
                        if (Angle > 180 && (icon_column_num == 0))
                            cur_line_add_angle = -360f;
                        Angle += cur_line_add_angle;
                        icon.setRotation(icon.getWidth() / 2, icon.getHeight() / 2
                                + Radius * radio);
                        icon.setRotationDegreeZ(-Angle * radio);
                        icon.endEffect();
                    }
                    for (int i = 0; i < next_size; i++) {
                        int icon_column_num = 0;
                        icon = next_view.getChildAt(i);

                        icon_column_num = icon.getColumnNum();
                        float width = icon.getWidth();
                        float height = icon.getHeight();
                        pos = new PointF(this_width / 2.0f - width / 2, this_height
                                / 2.0f - Radius - height / 2);
                        old_pos = (PointF) icon.getTag();
                        icon.setPosition(old_pos.x + (pos.x - old_pos.x) * radio,
                                old_pos.y + (pos.y - old_pos.y) * radio);
                        Angle = (add_angle + i * 360 / next_size) % 360;
                        if (Angle > 180 && (icon_column_num == 0))
                            cur_line_add_angle = -360f;
                        Angle += cur_line_add_angle;
                        icon.setRotation(icon.getWidth() / 2, icon.getHeight() / 2
                                + Radius * radio);
                        icon.setRotationDegreeZ(-Angle * radio);
                        icon.endEffect();
                    }
                }
                cur_view.endEffect();
                next_view.endEffect();
            }

        };
    }

    @Override
    public boolean isNeedClip() {
        return true;
    }
}
