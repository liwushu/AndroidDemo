
package com.gionee.thirdpartyeffect;

import android.graphics.PointF;
import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Wind {

    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {
        ViewGroup3D icon;
        PointF old_pos;
        float move_interval_between_column = 80.0f;
        int is_next_column_following_moving = 0;
        float this_height = cur_view.getHeight();

        if (this_height == 0) {
            this_height = next_view.getHeight();
        }

        if (degree <= 0 && degree >= -1 / 2f) {
            int cur_items_CountX = (cur_view).getCellCountX();
            int cur_items_CountY = (cur_view).getCellCountY();
            int[][] sort_array = new int[cur_items_CountX][cur_items_CountY]; // 记录按照列数排好序的
                                                                              // item
                                                                              // index值

            next_view.setVisible(false);
            cur_view.setVisible(true);

            for (int i = 0; i < cur_view.getChildCount(); i++) {
                int icon_column_num = 0;
                int icon_row_num = 0;
                icon = cur_view.getChildAt(i);
                icon_column_num = icon.getColumnNum();
                icon_row_num = icon.getRowNum();

                sort_array[icon_column_num][icon_row_num] = i + 1; // +1
                                                                   // 是为了保证sort_array中初始值
                                                                   // 0的区别
            }

            for (int column = 0; column < cur_items_CountX; column++) {
                for (int row = 0; row < cur_items_CountY; row++) {
                    int item_idx = sort_array[column][row] - 1;
                    float ratio = 2 * degree;

                    float distance_should_move_x = Math.abs(ratio)
                            * (this_width + (cur_items_CountX - 1)
                                    * move_interval_between_column) - column
                            * move_interval_between_column;
                    float distance_should_move_y = /* 2.0f* */distance_should_move_x
                            * this_width / (this_height);

                    if (item_idx >= 0) {
                        icon = cur_view.getChildAt(item_idx);
                        old_pos = (PointF) icon.getTag();

                        if (column == 0 || is_next_column_following_moving == 1) {
                            if (row < cur_items_CountY / 2) {
                                icon.setPosition(old_pos.x
                                        + distance_should_move_x, old_pos.y
                                        - distance_should_move_y);
                            } else {
                                icon.setPosition(old_pos.x
                                        + distance_should_move_x, old_pos.y
                                        + distance_should_move_y);
                            }
                            icon.setRotationZ(-ratio * 720);
                            icon.endEffect();
                        }

                    }

                    if (row + 1 >= cur_items_CountY) {

                        if (distance_should_move_x >= move_interval_between_column) {
                            is_next_column_following_moving = 1;
                        } else {
                            is_next_column_following_moving = 0;

                            if (column < cur_items_CountX - 1) {
                                for (int i = 0; i < cur_items_CountY; i++) {
                                    int tmp_index = sort_array[column + 1][i] - 1;
                                    if (tmp_index >= 0) {
                                        ViewGroup3D item = cur_view
                                                .getChildAt(tmp_index);
                                        PointF item_old_pos = (PointF) item
                                                .getTag();
                                        item.setPosition(item_old_pos.x,
                                                item_old_pos.y);
                                        item.setRotationZ(0f);
                                        item.endEffect();
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } else // -1/2 ~ -1
        {

            int next_items_CountX = (next_view).getCellCountX();
            int next_items_CountY = (next_view).getCellCountY();
            int[][] sort_array = new int[next_items_CountX][next_items_CountY]; // 记录按照列数排好序的
                                                                                // item
                                                                                // index值

            cur_view.setVisible(false);
            next_view.setVisible(true);

            for (int i = 0; i < next_view.getChildCount(); i++) {
                int icon_column_num = 0;
                int icon_row_num = 0;
                icon = next_view.getChildAt(i);
                icon_column_num = icon.getColumnNum();
                icon_row_num = icon.getRowNum();

                sort_array[icon_column_num][icon_row_num] = i + 1; // +1
                                                                   // 是为了保证sort_array中初始值
                                                                   // 0的区别

            }

            for (int column = next_items_CountX - 1; column >= 0; column--) {
                for (int row = 0; row < next_items_CountY; row++) {
                    int item_idx = sort_array[column][row] - 1;
                    float ratio = 2 * Math.abs(1 + degree); // 1 --->0
                    float distance_should_move_x = Math.abs(ratio)
                            * (this_width + (next_items_CountX - 1)
                                    * move_interval_between_column)
                            - (next_items_CountX - 1 - column)
                            * move_interval_between_column;
                    float distance_should_move_y = distance_should_move_x
                            * this_width / (this_height);

                    if (item_idx >= 0) {
                        icon = next_view.getChildAt(item_idx);

                        old_pos = (PointF) icon.getTag();

                        if (column == next_items_CountX - 1
                                || is_next_column_following_moving == 1) {

                            if (row < next_items_CountY / 2) {
                                icon.setPosition(old_pos.x
                                        + distance_should_move_x, old_pos.y
                                        - distance_should_move_y);
                            } else {
                                icon.setPosition(old_pos.x
                                        + distance_should_move_x, old_pos.y
                                        + distance_should_move_y);
                            }
                            icon.setRotationZ(-ratio * 720);
                            icon.endEffect();
                        }
                    }

                    if (row + 1 >= next_items_CountY) {

                        if (distance_should_move_x >= move_interval_between_column) {
                            is_next_column_following_moving = 1;
                        } else {
                            is_next_column_following_moving = 0;

                            if (column > 0) {
                                for (int i = 0; i < next_items_CountY; i++) {
                                    int tmp_index = sort_array[column - 1][i] - 1;

                                    if (tmp_index >= 0) {
                                        ViewGroup3D item = next_view
                                                .getChildAt(tmp_index);
                                        PointF item_old_pos = (PointF) item
                                                .getTag();
                                        item.setPosition(item_old_pos.x,
                                                item_old_pos.y);
                                        item.setRotationZ(0f);
                                        item.endEffect();
                                    }
                                }
                            }

                        }
                    }

                }
            }
        }
    }

}
