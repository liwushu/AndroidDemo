
package com.gionee.thirdpartyeffect;

import android.graphics.PointF;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Roll {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale,
            float this_width, boolean scroll_right_to_left) {
        if (scroll_right_to_left == true) {
            cur_view.setDrawOrder(true);
            drawRollEffect(cur_view, next_view, degree, true);
        } else {
            next_view.setDrawOrder(false);
            drawRollEffect(next_view, cur_view, 1 + degree, false);

        }
    }

    private static void drawRollEffect(ViewGroup3D c_view, ViewGroup3D n_view,
            float degree, boolean roll_from_right_to_left) {
        int CUR_VIEW_COLUMNS = (c_view).getCellCountX();
        int NEXT_VIEW_COLUMNS = (n_view).getCellCountX();

        float view_left_padding = 0f;

        float ratio = degree * CUR_VIEW_COLUMNS;
        float ratio_angle = Math.abs(ratio) * 90;
        int roll_stage_index = 0;
        ViewGroup3D item;

        if (ratio_angle <= 90) {
            roll_stage_index = 1;
        } else if (ratio_angle > 90 && ratio_angle <= 180) {
            roll_stage_index = 2;
        } else if (ratio_angle > 180 && ratio_angle < 270) {
            roll_stage_index = 3;
        } else if (ratio_angle > 270 && ratio_angle <= 360) {
            roll_stage_index = 4;
        }

        for (int index = 0; index < roll_stage_index; index++) {
            for (int item_idx = 0; item_idx < c_view.getChildCount(); item_idx++) {
                int c_column = 0;
                item = c_view.getChildAt(item_idx);
                c_column = item.getColumnNum();

                if (false == roll_from_right_to_left && c_column == index) {

                    if ((c_column == CUR_VIEW_COLUMNS - 1)
                            || (c_column == 0 && roll_stage_index == 4)) {
                    }
                } else if (true == roll_from_right_to_left
                        && c_column == (CUR_VIEW_COLUMNS - 1 - index)) {

                    if ((c_column == 0)
                            || (c_column == CUR_VIEW_COLUMNS - 1 && roll_stage_index == 4)) {
                    }
                } else {
                    continue;
                }

                rollActionStage(item, roll_stage_index - index, Math.abs(ratio)
                        - index, view_left_padding, roll_from_right_to_left);
            }
        }

        for (int keepIdx = roll_stage_index; keepIdx < CUR_VIEW_COLUMNS; keepIdx++) {
            for (int item_idx = 0; item_idx < c_view.getChildCount(); item_idx++) {
                int c_column = 0;
                item = c_view.getChildAt(item_idx);
                c_column = item.getColumnNum();

                if (false == roll_from_right_to_left && c_column == keepIdx) {
                } else if (true == roll_from_right_to_left
                        && c_column == (CUR_VIEW_COLUMNS - 1 - keepIdx)) {
                } else {
                    break;
                }

                PointF old_pos = (PointF) item.getTag();

                item.setPosition(old_pos.x, old_pos.y);
                item.setOrigin(item.getWidth() / 2f, item.getWidth() / 2f);
                item.setOriginZ(0f);
                item.setRotationAngle(0f, 0f, 0f);
                item.endEffect();

            }
        }

        for (int index = 0; index < n_view.getChildCount(); index++) {
            int item_column = 0;
            item = n_view.getChildAt(index);

            item_column = item.getColumnNum();

            float color_a = 0f;

            if (true == roll_from_right_to_left) {
                if (item_column > NEXT_VIEW_COLUMNS - roll_stage_index) {
                    color_a = 1.0f;
                } else if (item_column == NEXT_VIEW_COLUMNS - roll_stage_index) {
                    float colora = (float) (Math.abs(ratio) - (roll_stage_index - 1));
                    color_a = (colora > 0.5f) ? 2 * (colora - 0.5f) : 0f;
                } else {
                    color_a = 0f;
                }
            } else {
                if (item_column < roll_stage_index - 1) {
                    color_a = 1.0f;
                } else if (item_column == roll_stage_index - 1) {
                    float colora = (float) (Math.abs(ratio) - (roll_stage_index - 1));
                    color_a = (colora > 0.5f) ? 2 * (colora - 0.5f) : 0f;
                } else {
                    color_a = 0f;
                }
            }

            item.setAlpha(color_a);
            item.endEffect();

        }
    }

    private static void rollActionStage(ViewGroup3D item, int stage_index,
            float cur_ratio, float view_left_padding,
            boolean roll_from_right_to_left) {
        PointF old_pos;
        float cur_ratio_angle = Math.abs(cur_ratio) * 90;
        int coefficient = (roll_from_right_to_left == true) ? 1 : -1;

        old_pos = (PointF) item.getTag();

        switch (stage_index) {
            case 1:
                item.setPosition(old_pos.x, old_pos.y);
                item.setOriginZ(0f);
                item.setOrigin(item.getWidth() / 2 - coefficient
                        * (item.getWidth() / 2 + view_left_padding), item.mOriginY);
                item.setRotationY(-coefficient * cur_ratio_angle);
                break;
            case 2:
                item.setPosition(old_pos.x - coefficient * (item.getWidth() - 2),
                        old_pos.y);
                item.setOrigin(item.getWidth() / 2 - coefficient
                        * (item.getWidth() / 2 + view_left_padding), item.mOriginY);
                item.setCameraDistance(item.getWidth());
                item.setOriginZ(item.getWidth());
                item.setRotationY(-coefficient * cur_ratio_angle);
                break;
            case 3:
                item.setPosition(old_pos.x - coefficient
                        * (Math.abs(cur_ratio) - 1f) * item.getWidth(), old_pos.y);
                item.setOrigin(item.getWidth() / 2 - coefficient
                        * (item.getWidth() / 2 + view_left_padding), item.mOriginY);
                item.setCameraDistance(item.getWidth());
                item.setOriginZ(item.getWidth());
                item.setRotationY(-coefficient * cur_ratio_angle);
                break;
            case 4:
                item.setPosition(old_pos.x - coefficient * 4
                        * (item.getWidth() + view_left_padding), old_pos.y);
                item.setOrigin(item.getWidth() / 2 - coefficient
                        * (item.getWidth() / 2 + view_left_padding), item.mOriginY);
                item.setOriginZ(0f);
                item.setRotationY(-coefficient * cur_ratio_angle);
                break;
        }

        item.endEffect();
    }

}
