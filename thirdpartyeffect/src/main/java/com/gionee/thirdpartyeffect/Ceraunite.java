
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

import android.graphics.PointF;

public class Ceraunite {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale,
            float this_width, boolean is_right_to_left) {
        float this_height = cur_view.getHeight();
        final int cur_countX = (cur_view).getCellCountX();
        final int cur_countY = (cur_view).getCellCountY();
        final int next_countX = (next_view).getCellCountX();
        final int next_countY = (next_view).getCellCountY();
        final int CUR_ROWS_COLUMNS = cur_countX + cur_countY - 1;
        final int NEXT_ROWS_COLUMNS = next_countX + next_countY - 1;
        final int CONST_VALUE = cur_countY - 1; // 4

        int[][] cur_sort_array = new int[cur_countX][cur_countY]; // 记录按照列数排好序的
                                                                  // item
                                                                  // index值
        int[][] next_sort_array = new int[next_countX][next_countY]; // 记录按照列数排好序的
                                                                     // item
                                                                     // index值

        if (this_height == 0) {
            this_height = next_view.getHeight();
        }

        for (int i = 0; i < cur_view.getChildCount(); i++) {
            int icon_column_num = 0;
            int icon_row_num = 0;
            ViewGroup3D icon = cur_view.getChildAt(i);
            icon_column_num = icon.getColumnNum();
            icon_row_num = icon.getRowNum();

            cur_sort_array[icon_column_num][icon_row_num] = i + 1; // +1
                                                                   // 是为了保证sort_array中初始值
                                                                   // 0的区别

        }

        for (int i = 0; i < next_view.getChildCount(); i++) {
            int icon_column_num = 0;
            int icon_row_num = 0;
            ViewGroup3D icon = next_view.getChildAt(i);
            icon_column_num = icon.getColumnNum();
            icon_row_num = icon.getRowNum();

            next_sort_array[icon_column_num][icon_row_num] = i + 1;
        }

        if (is_right_to_left == true) {

            if (degree <= 0 && degree >= -1 / 2f) {
                boolean is_next_icon_group_moving_in_turn = false;
                float ratio = degree * 2;

                cur_view.setVisible(true);//
                next_view.setVisible(false);

                for (int a = 0; a < NEXT_ROWS_COLUMNS; a++) {
                    if (a == 0) {
                        // 因为a =0,为第一组移动的icon
                        is_next_icon_group_moving_in_turn = true;
                    }

                    if (is_next_icon_group_moving_in_turn == false) {
                        // 前一组的icon还未移动完成，需要等待一个degree，等前一组的icon移动完成
                        break;
                    }

                    for (int column = 0; column < cur_countX; column++) {
                        for (int row = 0; row < cur_countY; row++) {
                            // (row-column) --> 4,3,2,1,0,-1,-2,-3,....
                            if (CONST_VALUE - a == row - column) {
                                int icon_idx = cur_sort_array[column][row] - 1/*
                                                                               * row
                                                                               * *
                                                                               * cur_countX
                                                                               * +
                                                                               * column
                                                                               */;
                                if (icon_idx >= 0) {
                                    // 目前本组的icon需要移动
                                    float tmp_ratio = Math.abs(ratio)
                                            * NEXT_ROWS_COLUMNS - a;

                                    ViewGroup3D icon = cur_view
                                            .getChildAt(icon_idx);
                                    PointF old_pos = (PointF) icon.getTag();

                                    if (tmp_ratio >= 0f && tmp_ratio < 1.0f) {
                                        ExaminePreGroupsAtOriginalPos(cur_view,
                                                cur_sort_array, CONST_VALUE - a
                                                        - 1, true);

                                        icon.setPosition(old_pos.x
                                                - (this_width) * tmp_ratio,
                                                old_pos.y + (this_height)
                                                        * tmp_ratio);
                                        is_next_icon_group_moving_in_turn = false;

                                        icon.endEffect();
                                    } else if (tmp_ratio >= 1.0f) {
                                        icon.setPosition(
                                                old_pos.x - this_width,
                                                old_pos.y + this_height);
                                        is_next_icon_group_moving_in_turn = true;

                                        icon.endEffect();
                                    } else {
                                        // tmp_ratio < 0
                                        // do nothing
                                    }

                                }
                            }
                        }
                    }
                }

            } else {
                //
                float ratio = 2 * degree + 1;
                boolean is_next_icon_group_moving_in_turn = false;

                // next_view hide
                for (int i = 0; i < next_view.getChildCount(); i++) {
                    if (i < next_view.getChildCount()) {
                        ViewGroup3D item = next_view.getChildAt(i);

                        float color_a = 0f;
                        item.setAlpha(color_a);
                    }
                }

                next_view.setVisible(true);// next_view.show();
                cur_view.setVisible(false);

                // 以下的icon是按a的顺序移动的
                for (int a = 0; a < NEXT_ROWS_COLUMNS; a++) {
                    if (a == 0) {
                        // 因为a =0,为第一组移动的icon
                        is_next_icon_group_moving_in_turn = true;
                    }

                    if (is_next_icon_group_moving_in_turn == false) {
                        // 前一组的icon还未移动完成，需要等待一个degree，等前一组的icon移动完成
                        break;
                    }

                    for (int column = 0; column < next_countX; column++) {
                        for (int row = 0; row < next_countY; row++) {
                            // (row-column) --> 4,3,2,1,0,-1,-2,-3,....
                            if (CONST_VALUE - a == row - column) {
                                int icon_idx = next_sort_array[column][row] - 1/*
                                                                                * row
                                                                                * *
                                                                                * next_countX
                                                                                * +
                                                                                * column
                                                                                */;

                                if (icon_idx >= 0) {

                                    // 目前本组的icon需要移动
                                    float tmp_ratio = Math.abs(ratio)
                                            * NEXT_ROWS_COLUMNS - a;

                                    ViewGroup3D icon = next_view
                                            .getChildAt(icon_idx);
                                    PointF old_pos = (PointF) icon.getTag();

                                    if (tmp_ratio >= 0f && tmp_ratio < 1.0f) {
                                        float color_a = 1.0f;
                                        icon.setAlpha(color_a);

                                        icon.setPosition(old_pos.x
                                                + (this_width) - (this_width)
                                                * tmp_ratio, old_pos.y
                                                - this_height + (this_height)
                                                * tmp_ratio);
                                        is_next_icon_group_moving_in_turn = false;

                                        icon.endEffect();
                                    } else if (tmp_ratio >= 1.0f) {
                                        float color_a = 1.0f;
                                        icon.setAlpha(color_a);

                                        icon.setPosition(old_pos.x, old_pos.y);
                                        is_next_icon_group_moving_in_turn = true;

                                        icon.endEffect();
                                    } else {
                                        // tmp_ratio < 0 , do nothing
                                    }

                                }
                            }
                        }
                    }
                }
            }

        } else {
            // from left to right

            if (degree <= 0 && degree <= -1 / 2f) {
                // 先消失
                float ratio = (1 + degree) * 2;
                boolean is_next_icon_group_moving_in_turn = false;

                cur_view.setVisible(false);
                next_view.setVisible(true);

                for (int a = NEXT_ROWS_COLUMNS - 1; a >= 0; a--) {
                    if (a == NEXT_ROWS_COLUMNS - 1) {
                        // 因为a = NEXT_ROWS_COLUMNS - 1,为第一组移动的icon
                        is_next_icon_group_moving_in_turn = true;
                    }

                    if (is_next_icon_group_moving_in_turn == false) {
                        // 前一组的icon还未移动完成，需要等待一个degree，等前一组的icon移动完成
                        break;
                    }

                    for (int column = 0; column < next_countX; column++) {
                        for (int row = 0; row < next_countY; row++) {
                            if (a == column + row) {
                                int icon_idx = next_sort_array[column][row] - 1/*
                                                                                * row
                                                                                * *
                                                                                * next_countX
                                                                                * +
                                                                                * column
                                                                                */;

                                if (icon_idx >= 0) {

                                    float tmp_ratio = Math.abs(ratio)
                                            * NEXT_ROWS_COLUMNS
                                            - (NEXT_ROWS_COLUMNS - a - 1);
                                    ViewGroup3D icon = next_view
                                            .getChildAt(icon_idx);
                                    PointF old_pos = (PointF) icon.getTag();

                                    if (tmp_ratio >= 0f && tmp_ratio < 1.0f) {
                                        // 先判断preGroup的icon是否在原先位置上
                                        ExaminePreGroupsAtOriginalPos(
                                                next_view, next_sort_array,
                                                a - 1, false);
                                        icon.setPosition(old_pos.x
                                                + (this_width) * tmp_ratio,
                                                old_pos.y + (this_height)
                                                        * tmp_ratio);
                                        is_next_icon_group_moving_in_turn = false;

                                        icon.endEffect();
                                    } else if (tmp_ratio >= 1.0f) {
                                        icon.setPosition(old_pos.x
                                                + (this_width), old_pos.y
                                                + (this_height));
                                        is_next_icon_group_moving_in_turn = true;

                                        icon.endEffect();
                                    } else {
                                        // tmp_ratio < 0 , do nothing
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                float ratio = (degree + 1 / 2f) * 2;
                boolean is_next_icon_group_moving_in_turn = false;

                // cur_view hide
                for (int i = 0; i < cur_view.getChildCount(); i++) {
                    if (i < cur_view.getChildCount()) {
                        ViewGroup3D item = cur_view.getChildAt(i);
                        float color_a = 0f;
                        item.setAlpha(color_a);
                    }
                }

                next_view.setVisible(false);
                cur_view.setVisible(true);

                for (int a = CUR_ROWS_COLUMNS - 1; a >= 0; a--) {
                    if (a == CUR_ROWS_COLUMNS - 1) {
                        // 因为a = NEXT_ROWS_COLUMNS - 1,为第一组移动的icon
                        is_next_icon_group_moving_in_turn = true;
                    }

                    if (is_next_icon_group_moving_in_turn == false) {
                        // 前一组的icon还未移动完成，需要等待一个degree，等前一组的icon移动完成
                        break;
                    }

                    for (int column = 0; column < cur_countX; column++) {
                        for (int row = 0; row < cur_countY; row++) {
                            if (a == column + row) {
                                int icon_idx = cur_sort_array[column][row] - 1/*
                                                                               * row
                                                                               * *
                                                                               * cur_countX
                                                                               * +
                                                                               * column
                                                                               */;

                                if (icon_idx >= 0) {

                                    float tmp_ratio = Math.abs(ratio)
                                            * NEXT_ROWS_COLUMNS
                                            - (NEXT_ROWS_COLUMNS - a - 1);
                                    ViewGroup3D icon = cur_view
                                            .getChildAt(icon_idx);
                                    PointF old_pos = (PointF) icon.getTag();

                                    if (tmp_ratio >= 0f && tmp_ratio < 1.0f) {
                                        float color_a = 1.0f;
                                        icon.setAlpha(color_a);

                                        icon.setPosition(old_pos.x
                                                - (this_width) + (this_width)
                                                * tmp_ratio, old_pos.y
                                                - this_height + (this_height)
                                                * tmp_ratio);
                                        is_next_icon_group_moving_in_turn = false;

                                        icon.endEffect();
                                    } else if (tmp_ratio >= 1.0f) {
                                        float color_a = 1.0f;
                                        icon.setAlpha(color_a);

                                        icon.setPosition(old_pos.x, old_pos.y);
                                        is_next_icon_group_moving_in_turn = true;

                                        icon.endEffect();
                                    } else {
                                        // tmp_ratio < 0 , do nothing
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        cur_view.endEffect();
        next_view.endEffect();

    }

    // 在scroll的过程中，当往回滑动时，需要保证之前消失的item恢复原来的位置
    private static void ExaminePreGroupsAtOriginalPos(ViewGroup3D cur_view,
            int[][] sort_array, int pre_group_id, boolean is_order) {
        ViewGroup3D cur_icon;
        int mCountX = (cur_view).getCellCountX();
        int mCountY = (cur_view).getCellCountY();
        final int CONST_VALUE = mCountY - 1; // 4
        final int VIEW_ROWS_COLUMNS = mCountX + mCountY - 1;

        if (is_order == false) {
            // 消失的组别数次序从大到小

            if (pre_group_id < 0) {
                return;
            }

            for (int i = 0; i < mCountX; i++) {
                for (int j = 0; j < mCountY; j++) {
                    int index = sort_array[i][j] - 1/* j*mCountX + i */;

                    if (index >= 0 && ((i + j) <= pre_group_id)) {
                        for (int groupId = pre_group_id; groupId >= 0; groupId--) {
                            if ((i + j) == groupId) {
                                cur_icon = cur_view.getChildAt(index);
                                PointF old_pos = (PointF) cur_icon.getTag();
                                cur_icon.setPosition(old_pos.x, old_pos.y);

                                cur_icon.endEffect();
                                break;
                            }
                        }
                    }
                }
            }

        } else {
            if (pre_group_id < CONST_VALUE - (VIEW_ROWS_COLUMNS - 1)) {
                return;
            }

            for (int i = 0; i < mCountX; i++) {
                for (int j = 0; j < mCountY; j++) {
                    int index = sort_array[i][j] - 1/* j*mCountX + i */;

                    if (index >= 0 && ((j - i) <= pre_group_id)) {
                        for (int groupId = pre_group_id; groupId >= (CONST_VALUE
                                - VIEW_ROWS_COLUMNS + 1); groupId--) {
                            if ((j - i) == groupId) {
                                cur_icon = cur_view.getChildAt(index);
                                PointF old_pos = (PointF) cur_icon.getTag();
                                {
                                    cur_icon.setPosition(old_pos.x, old_pos.y);
                                    cur_icon.endEffect();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}
