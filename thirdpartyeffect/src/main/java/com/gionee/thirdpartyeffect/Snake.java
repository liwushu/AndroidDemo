
package com.gionee.thirdpartyeffect;

import android.graphics.PointF;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Snake {

    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {

        final int cur_countX = (cur_view).getCellCountX();
        final int cur_countY = (cur_view).getCellCountY();

        float item_begin_pos_x = -1f;
        float item_begin_pos_y = -1f;
        float item_end_pos_x = -1f;
        float item_end_pos_y = -1f;
        float item_width = -1f;
        float item_height = -1f;
        float item_padding_x = -1f;
        float item_padding_y = -1f;
        float CUR_VIEW_GROUP_WIDTH = this_width;

        // 表示该icon移动到退出界面总共所需要移动的距离
        float icon_total_moving_distance = 0f;

        // 先确定 各个icon之间的布局的间距 item_padding_x，item_padding_y
        int view_size = cur_view.getChildCount() > next_view.getChildCount() ? cur_view
                .getChildCount() : next_view.getChildCount();

        for (int index = 0; index < view_size; index++) {
            ViewGroup3D CView_item = null;
            ViewGroup3D NView_item = null;

            if (view_size == 0) {
                item_begin_pos_x = 0;
                item_begin_pos_y = 0;
                item_width = cur_view.getWidth() / 4;
                item_height = cur_view.getHeight() / 4;
                break;
            }

            if (index < cur_view.getChildCount()) {
                CView_item = cur_view.getChildAt(index);

            }

            if (index < next_view.getChildCount()) {
                NView_item = next_view.getChildAt(index);
            }

            if (CView_item != null || NView_item != null) {
                ViewGroup3D view = (CView_item != null) ? CView_item
                        : NView_item;
                if (view != null) {
                    int column = view.getColumnNum();
                    int row = view.getRowNum();

                    item_width = view.getWidth();
                    item_height = view.getHeight();

                    if (row == 0 && item_begin_pos_y == -1) {
                        item_begin_pos_y = ((PointF) view.getTag()).y;
                    }

                    if (row == cur_countX - 1 && item_end_pos_y == -1) {
                        item_end_pos_y = ((PointF) view.getTag()).y;
                    }

                    if (column == 0 && item_begin_pos_x == -1) {
                        item_begin_pos_x = ((PointF) view.getTag()).x;
                    }

                    if (column == cur_countY - 1 && item_end_pos_x == -1) {
                        item_end_pos_x = ((PointF) view.getTag()).x;
                    }
                }
            }

        }

        if (item_width == -1f) {
            item_width = cur_view.getWidth() / 4;
            item_height = cur_view.getHeight() / 4;
        }
        if (-1.0f == item_padding_x) {
            item_padding_x = 0f;
        }
        if (-1.0f == item_padding_y) {
            item_padding_y = 0f;
        }

        if (item_begin_pos_x == -1) {
            item_begin_pos_x = 0;
        }

        if (item_begin_pos_y == -1) {
            item_begin_pos_y = 0;
        }

        if (item_end_pos_x == -1) {
            item_end_pos_x = item_begin_pos_x + (cur_countX - 1)
                    * (item_width + item_padding_x);
        }
        if (item_end_pos_y == -1.0f) {
            item_end_pos_y = item_begin_pos_y + (cur_countY - 1)
                    * (item_height + item_padding_y);
        }

        if (cur_countY % 2 == 1)// 总行数是奇数还是偶数
            icon_total_moving_distance = (item_end_pos_y - item_begin_pos_y)
                    + (cur_countY) * (item_end_pos_x - item_begin_pos_x)
                    + (item_begin_pos_x + item_width + 5.0f);
        else
            icon_total_moving_distance = (item_end_pos_y - item_begin_pos_y)
                    + (cur_countY) * (item_end_pos_x - item_begin_pos_x)
                    + (CUR_VIEW_GROUP_WIDTH - item_end_pos_x + 5.0f);

        SnakeEffectMovingRight2Left(cur_view, degree, item_begin_pos_x,
                item_begin_pos_y, item_end_pos_x, item_end_pos_y,
                item_padding_x, item_padding_y, icon_total_moving_distance);
        SnakeEffectMovingLeft2Right(next_view, 1 + degree, item_begin_pos_x,
                item_begin_pos_y, item_end_pos_x, item_end_pos_y,
                item_padding_x, item_padding_y, icon_total_moving_distance);

        cur_view.endEffect();
        next_view.endEffect();

    }

    private static void SnakeEffectMovingRight2Left(ViewGroup3D cur_view,
            float degree, float item_begin_pos_x, float item_begin_pos_y,
            float item_end_pos_x, float item_end_pos_y, float item_padding_x,
            float item_padding_y, float icon_total_moving_distance) {

        ViewGroup3D icon;
        PointF old_pos;

        final int cur_countY = (cur_view).getCellCountY();

        float item_height = 0f;
        int cur_view_size = cur_view.getChildCount();
        float ratio = /* 1.1f * */degree;

        for (int index = 0; index < cur_view_size; index++) {
            if (index < cur_view.getChildCount()) {
                int original_row_num = 0;// (index -
                                         // index%cur_countX)/cur_countX;

                icon = cur_view.getChildAt(index);
                original_row_num = icon.getRowNum();

                item_height = icon.getHeight();
                old_pos = (PointF) icon.getTag();

                // 表示此ratio下，按照规律icon需要移动如下的位移
                float icon_moving_dis_ex = Math.abs(ratio)
                        * icon_total_moving_distance;

                // 计算移动上述的位移，能将该Icon移动到哪个位置，
                if (original_row_num % 2 == 0) {
                    // 开始位置是偶数行，从右往左移动，
                    float icon_mv_dis_tmp = 0f;
                    // float icon_pos_x = 0f;
                    float icon_pos_y = 0f;
                    float icon_mv_dis_tmp_old = 0f;
                    int accumulate_ratio = 0;

                    for (int ix = 0; ix < 2 * cur_countY - 1; ix++) {
                        if (ix % 2 == 0 && ix != 0)
                            accumulate_ratio++;

                        if (ix % 4 == 0) {
                            // 偶数行，从右往左移动，包含第 0，4，8...行
                            float calc_end_pos_x = 0f;
                            if (ix == 0) {
                                accumulate_ratio = 0;
                                icon_mv_dis_tmp_old = 0f;
                                icon_mv_dis_tmp += old_pos.x - item_begin_pos_x;
                                icon_pos_y = old_pos.y;
                                calc_end_pos_x = old_pos.x;
                            } else {
                                icon_mv_dis_tmp_old = icon_mv_dis_tmp;
                                icon_mv_dis_tmp += item_end_pos_x
                                        - item_begin_pos_x;
                                icon_pos_y = old_pos.y + (accumulate_ratio)
                                        * (item_height + item_padding_y);
                                calc_end_pos_x = item_end_pos_x;
                            }

                            if (icon_mv_dis_tmp >= icon_moving_dis_ex) {
                                // 那么此时icon只需要在所在行从右往左移动即可
                                icon.setPosition(
                                        item_begin_pos_x
                                                + calc_end_pos_x
                                                - (icon_moving_dis_ex - icon_mv_dis_tmp_old),
                                        icon_pos_y);
                                icon.setScale(0.8f, 0.8f);
                                icon.endEffect();
                                // 退出该算法
                                break;
                            } else {
                                // 判断此时是否时最后一行
                                int cur_row = original_row_num + ix / 2;
                                if (cur_row == cur_countY - 1) {
                                    // 此时是最后一行了，继续往左移动出屏幕
                                    icon.setPosition(
                                            item_begin_pos_x
                                                    + calc_end_pos_x
                                                    - (icon_moving_dis_ex - icon_mv_dis_tmp_old),
                                            icon_pos_y);
                                    icon.setScale(0.8f, 0.8f);
                                    icon.endEffect();
                                    // 退出该算法
                                    break;
                                }
                            }
                        } else if (ix % 4 == 1) {
                            // 在Y方向上移动，从上往下移动,进入奇数行
                            icon_mv_dis_tmp_old = icon_mv_dis_tmp;

                            icon_mv_dis_tmp += item_height + item_padding_y;

                            if (icon_mv_dis_tmp >= icon_moving_dis_ex) {
                                icon_pos_y = old_pos.y + (accumulate_ratio)
                                        * (item_height + item_padding_y)
                                        + icon_moving_dis_ex
                                        - icon_mv_dis_tmp_old;
                                icon.setPosition(item_begin_pos_x, icon_pos_y);
                                icon.setScale(0.8f, 0.8f);
                                icon.endEffect();
                                break;
                            } else {
                                // 进入第三阶段，
                            }
                        } else if (ix % 4 == 2) {
                            // 奇数行，从左到右移动
                            icon_mv_dis_tmp_old = icon_mv_dis_tmp;
                            icon_pos_y = old_pos.y + (accumulate_ratio)
                                    * (item_height + item_padding_y);

                            icon_mv_dis_tmp += item_end_pos_x
                                    - item_begin_pos_x;

                            if (icon_mv_dis_tmp >= icon_moving_dis_ex) {
                                icon.setPosition(item_begin_pos_x
                                        + icon_moving_dis_ex
                                        - icon_mv_dis_tmp_old, icon_pos_y);
                                icon.setScale(0.8f, 0.8f);
                                icon.endEffect();
                                break;
                            } else {
                                // 判断此时是否时最后一行
                                int cur_row = original_row_num + ix / 2;
                                if (cur_row == cur_countY - 1) {
                                    // 此时是最后一行了，继续往左移动出屏幕
                                    icon.setPosition(item_begin_pos_x
                                            + icon_moving_dis_ex
                                            - icon_mv_dis_tmp_old, icon_pos_y);
                                    icon.setScale(0.8f, 0.8f);
                                    icon.endEffect();
                                    // 退出该算法
                                    break;
                                }
                            }
                        } else {
                            // Y方向移动，从上到下,进入偶数行
                            icon_mv_dis_tmp_old = icon_mv_dis_tmp;

                            icon_mv_dis_tmp += item_height + item_padding_y;

                            if (icon_mv_dis_tmp > icon_moving_dis_ex) {
                                icon_pos_y = old_pos.y + (accumulate_ratio)
                                        * (item_height + item_padding_y)
                                        + icon_moving_dis_ex
                                        - icon_mv_dis_tmp_old;
                                icon.setPosition(item_end_pos_x, icon_pos_y);
                                icon.setScale(0.8f, 0.8f);
                                icon.endEffect();
                                break;
                            } else {
                                // 进入第五阶段，
                            }
                        }
                    }
                } else {
                    // 开始位置是奇数行，从左往右移动
                    float icon_mv_dis_tmp = 0f;
                    float icon_pos_y = 0f;
                    float icon_mv_dis_tmp_old = 0f;
                    int accumulate_ratio = 0;

                    for (int ix = 0; ix < 2 * cur_countY - 3; ix++) {
                        if (ix % 2 == 0 && ix != 0)
                            accumulate_ratio++;

                        if (ix % 4 == 0) {
                            // 奇数行，从左往右移动，包含第1，5，9...行
                            float calc_end_pos_x = 0f;
                            if (ix == 0) {
                                accumulate_ratio = 0;
                                icon_mv_dis_tmp += item_end_pos_x - old_pos.x;
                                icon_pos_y = old_pos.y;
                                calc_end_pos_x = old_pos.x;
                            } else {
                                icon_mv_dis_tmp_old = icon_mv_dis_tmp;
                                icon_mv_dis_tmp += item_end_pos_x
                                        - item_begin_pos_x;
                                icon_pos_y = old_pos.y + (accumulate_ratio)
                                        * (item_height + item_padding_y);
                                calc_end_pos_x = item_begin_pos_x;
                            }

                            if (icon_mv_dis_tmp >= icon_moving_dis_ex) {
                                // 那么此时icon只需要在所在行从右往左移动即可
                                icon.setPosition(calc_end_pos_x
                                        + icon_moving_dis_ex
                                        - icon_mv_dis_tmp_old, icon_pos_y);
                                icon.setScale(0.8f, 0.8f);
                                icon.endEffect();
                                // 退出该算法
                                break;
                            } else {
                                // 判断此时是否时最后一行
                                int cur_row = original_row_num + ix / 2;
                                if (cur_row == cur_countY - 1) {
                                    // 此时是最后一行了，继续往左移动出屏幕
                                    icon.setPosition(calc_end_pos_x
                                            + icon_moving_dis_ex
                                            - icon_mv_dis_tmp_old, icon_pos_y);
                                    icon.setScale(0.8f, 0.8f);
                                    icon.endEffect();
                                    // 退出该算法
                                    break;
                                }
                            }
                        } else if (ix % 4 == 1) {
                            // 在Y方向上移动，从上往下移动,进入偶数行，
                            icon_mv_dis_tmp_old = icon_mv_dis_tmp;

                            icon_mv_dis_tmp += item_height + item_padding_y;

                            if (icon_mv_dis_tmp >= icon_moving_dis_ex) {
                                icon_pos_y = old_pos.y + (accumulate_ratio)
                                        * (item_height + item_padding_y)
                                        + icon_moving_dis_ex
                                        - icon_mv_dis_tmp_old;
                                icon.setPosition(item_end_pos_x, icon_pos_y);
                                icon.setScale(0.8f, 0.8f);
                                icon.endEffect();
                                break;
                            } else {
                                // 进入第三阶段，
                            }
                        } else if (ix % 4 == 2) {
                            // 偶数行，从右往左移动，包含第2，6，10...行
                            icon_mv_dis_tmp_old = icon_mv_dis_tmp;
                            icon_pos_y = old_pos.y + (accumulate_ratio)
                                    * (item_height + item_padding_y);

                            icon_mv_dis_tmp += item_end_pos_x
                                    - item_begin_pos_x;

                            if (icon_mv_dis_tmp >= icon_moving_dis_ex) {
                                icon.setPosition(
                                        item_begin_pos_x
                                                + item_end_pos_x
                                                - (icon_moving_dis_ex - icon_mv_dis_tmp_old),
                                        icon_pos_y);
                                icon.setScale(0.8f, 0.8f);
                                icon.endEffect();
                                break;
                            } else {
                                // 判断此时是否时最后一行
                                int cur_row = original_row_num + ix / 2;
                                if (cur_row == cur_countY - 1) {
                                    // 此时是最后一行了，继续往左移动出屏幕
                                    icon.setPosition(
                                            item_begin_pos_x
                                                    + item_end_pos_x
                                                    - (icon_moving_dis_ex - icon_mv_dis_tmp_old),
                                            icon_pos_y);
                                    icon.setScale(0.8f, 0.8f);
                                    icon.endEffect();
                                    // 退出该算法
                                    break;
                                }
                            }
                        } else // ix%4 == 3
                        {
                            // Y方向移动，从上到下,进入奇数行
                            icon_mv_dis_tmp_old = icon_mv_dis_tmp;

                            icon_mv_dis_tmp += item_height + item_padding_y;

                            if (icon_mv_dis_tmp > icon_moving_dis_ex) {
                                icon_pos_y = old_pos.y + (accumulate_ratio)
                                        * (item_height + item_padding_y)
                                        + icon_moving_dis_ex
                                        - icon_mv_dis_tmp_old;
                                icon.setPosition(item_begin_pos_x, icon_pos_y);
                                icon.setScale(0.8f, 0.8f);
                                icon.endEffect();
                                break;
                            } else {
                                // 进入第五阶段，
                            }
                        }
                    }
                }
            }
        }

    }

    private static void SnakeEffectMovingLeft2Right(ViewGroup3D cur_view,
            float degree, float item_begin_pos_x, float item_begin_pos_y,
            float item_end_pos_x, float item_end_pos_y, float item_padding_x,
            float item_padding_y, float icon_total_moving_distance) {

        ViewGroup3D icon;
        PointF old_pos;

        final int cur_countY = (cur_view).getCellCountY();

        float item_height = 0f;
        int cur_view_size = cur_view.getChildCount();
        float ratio = /* 1.1f * */degree;

        for (int index = 0; index < cur_view_size; index++) {
            if (index < cur_view.getChildCount()) {
                int original_row_num = 0;// (index -
                                         // index%cur_countX)/cur_countX;
                // int current_row_num = 0;

                icon = cur_view.getChildAt(index);

                original_row_num = icon.getRowNum();

                item_height = icon.getHeight();
                old_pos = (PointF) icon.getTag();

                // if(original_row_num%2 == 0)
                {
                    // 表示此ratio下，按照规律icon需要移动如下的位移
                    float icon_moving_dis_ex = Math.abs(ratio)
                            * icon_total_moving_distance;

                    // 计算移动上述的位移，能将该Icon移动到哪个位置，
                    if (original_row_num % 2 == 0) {
                        // 开始位置是偶数行，从左往右移动，
                        float icon_mv_dis_tmp = 0f;
                        float icon_pos_y = 0f;
                        float icon_mv_dis_tmp_old = 0f;
                        int accumulate_ratio = 0;

                        for (int ix = 0; ix < 2 * cur_countY - 1; ix++) {
                            if (ix % 2 == 0 && ix != 0)
                                accumulate_ratio++;

                            if (ix % 4 == 0) {
                                // 偶数行，从左到右移动，包含
                                float calc_end_pos_x = 0f;
                                if (ix == 0) {
                                    accumulate_ratio = 0;
                                    icon_mv_dis_tmp_old = 0f;
                                    icon_mv_dis_tmp += item_end_pos_x
                                            - old_pos.x;
                                    icon_pos_y = old_pos.y;
                                    calc_end_pos_x = old_pos.x;
                                } else {
                                    icon_mv_dis_tmp_old = icon_mv_dis_tmp;
                                    icon_mv_dis_tmp += item_end_pos_x
                                            - item_begin_pos_x;
                                    icon_pos_y = old_pos.y - (accumulate_ratio)
                                            * (item_height + item_padding_y);
                                    calc_end_pos_x = item_begin_pos_x;
                                }

                                if (icon_mv_dis_tmp >= icon_moving_dis_ex) {
                                    // 那么此时icon只需要在所在行从右往左移动即可
                                    icon.setPosition(
                                            calc_end_pos_x
                                                    + (icon_moving_dis_ex - icon_mv_dis_tmp_old),
                                            icon_pos_y);
                                    icon.setScale(0.8f, 0.8f);
                                    icon.endEffect();
                                    // 退出该算法
                                    break;
                                } else {
                                    // 判断此时是否时第一行
                                    int cur_row = original_row_num - ix / 2;
                                    if (cur_row == 0) {
                                        // 此时是第一行了，继续往右移动出屏幕
                                        icon.setPosition(
                                                calc_end_pos_x
                                                        + (icon_moving_dis_ex - icon_mv_dis_tmp_old),
                                                icon_pos_y);
                                        icon.setScale(0.8f, 0.8f);
                                        icon.endEffect();
                                        // 退出该算法
                                        break;
                                    }
                                }
                            } else if (ix % 4 == 1) {
                                // 在Y方向上移动，从下往上移动,进入奇数行
                                icon_mv_dis_tmp_old = icon_mv_dis_tmp;

                                icon_mv_dis_tmp += item_height + item_padding_y;

                                if (icon_mv_dis_tmp >= icon_moving_dis_ex) {
                                    icon_pos_y = old_pos.y
                                            - (accumulate_ratio)
                                            * (item_height + item_padding_y)
                                            - (icon_moving_dis_ex - icon_mv_dis_tmp_old);
                                    icon.setPosition(item_end_pos_x, icon_pos_y);
                                    icon.setScale(0.8f, 0.8f);
                                    icon.endEffect();
                                    break;
                                }
                            } else if (ix % 4 == 2) {
                                // 奇数行，从右往左移动
                                icon_mv_dis_tmp_old = icon_mv_dis_tmp;
                                icon_pos_y = old_pos.y - (accumulate_ratio)
                                        * (item_height + item_padding_y);

                                icon_mv_dis_tmp += item_end_pos_x
                                        - item_begin_pos_x;

                                if (icon_mv_dis_tmp >= icon_moving_dis_ex) {
                                    icon.setPosition(
                                            item_begin_pos_x
                                                    + item_end_pos_x
                                                    - (icon_moving_dis_ex - icon_mv_dis_tmp_old),
                                            icon_pos_y);
                                    icon.setScale(0.8f, 0.8f);
                                    icon.endEffect();
                                    break;
                                } else {
                                    // 此处无需判断，因为是奇数行 ！=0
                                }
                            } else {
                                // Y方向移动，从下往上,进入偶数行
                                icon_mv_dis_tmp_old = icon_mv_dis_tmp;

                                icon_mv_dis_tmp += item_height + item_padding_y;

                                if (icon_mv_dis_tmp > icon_moving_dis_ex) {
                                    icon_pos_y = old_pos.y
                                            - (accumulate_ratio)
                                            * (item_height + item_padding_y)
                                            - (icon_moving_dis_ex - icon_mv_dis_tmp_old);
                                    icon.setPosition(item_begin_pos_x,
                                            icon_pos_y);
                                    icon.setScale(0.8f, 0.8f);
                                    icon.endEffect();
                                    break;
                                }
                            }
                        }
                    } else {
                        // 开始位置是奇数行，从右往左移动，
                        float icon_mv_dis_tmp = 0f;
                        float icon_pos_y = 0f;
                        float icon_mv_dis_tmp_old = 0f;
                        int accumulate_ratio = 0;

                        for (int ix = 0; ix < 2 * cur_countY - 1; ix++) {
                            if (ix % 2 == 0 && ix != 0)
                                accumulate_ratio++;

                            if (ix % 4 == 0) {
                                // 奇数行，从右往左移动
                                float calc_end_pos_x = 0f;
                                if (ix == 0) {
                                    accumulate_ratio = 0;
                                    icon_mv_dis_tmp_old = 0f;
                                    icon_mv_dis_tmp += old_pos.x
                                            - item_begin_pos_x;
                                    icon_pos_y = old_pos.y;
                                    calc_end_pos_x = old_pos.x;
                                } else {
                                    icon_mv_dis_tmp_old = icon_mv_dis_tmp;
                                    icon_mv_dis_tmp += item_end_pos_x
                                            - item_begin_pos_x;
                                    icon_pos_y = old_pos.y - (accumulate_ratio)
                                            * (item_height + item_padding_y);
                                    calc_end_pos_x = item_end_pos_x;
                                }

                                if (icon_mv_dis_tmp >= icon_moving_dis_ex) {
                                    // 那么此时icon只需要在所在行从右往左移动即可
                                    icon.setPosition(
                                            item_begin_pos_x
                                                    + calc_end_pos_x
                                                    - (icon_moving_dis_ex - icon_mv_dis_tmp_old),
                                            icon_pos_y);
                                    icon.setScale(0.8f, 0.8f);
                                    icon.endEffect();
                                    // 退出该算法
                                    break;
                                } else {
                                    // 此处无需判断，因为是奇数行 ！= 0

                                }
                            } else if (ix % 4 == 1) {
                                // 在Y方向上移动，从下往上移动,进入偶数行
                                icon_mv_dis_tmp_old = icon_mv_dis_tmp;

                                icon_mv_dis_tmp += item_height + item_padding_y;

                                if (icon_mv_dis_tmp >= icon_moving_dis_ex) {
                                    icon_pos_y = old_pos.y
                                            - (accumulate_ratio)
                                            * (item_height + item_padding_y)
                                            - (icon_moving_dis_ex - icon_mv_dis_tmp_old);
                                    icon.setPosition(item_begin_pos_x,
                                            icon_pos_y);
                                    icon.setScale(0.8f, 0.8f);
                                    icon.endEffect();
                                    break;
                                }
                            } else if (ix % 4 == 2) {
                                // 偶数行，从左往右移动
                                icon_mv_dis_tmp_old = icon_mv_dis_tmp;
                                icon_pos_y = old_pos.y - (accumulate_ratio)
                                        * (item_height + item_padding_y);

                                icon_mv_dis_tmp += item_end_pos_x
                                        - item_begin_pos_x;

                                if (icon_mv_dis_tmp >= icon_moving_dis_ex) {
                                    icon.setPosition(
                                            item_begin_pos_x
                                                    + (icon_moving_dis_ex - icon_mv_dis_tmp_old),
                                            icon_pos_y);
                                    icon.setScale(0.8f, 0.8f);
                                    icon.endEffect();
                                    break;
                                } else {
                                    // 判断此时是否时第一行
                                    int cur_row = original_row_num - ix / 2;
                                    if (cur_row == 0) {
                                        // 此时是第一行了，继续往右移动出屏幕
                                        icon.setPosition(
                                                item_begin_pos_x
                                                        + (icon_moving_dis_ex - icon_mv_dis_tmp_old),
                                                icon_pos_y);
                                        icon.setScale(0.8f, 0.8f);
                                        icon.endEffect();
                                        // 退出该算法
                                        break;
                                    }
                                }
                            } else {
                                // Y方向移动，从下往上,进入奇数行
                                icon_mv_dis_tmp_old = icon_mv_dis_tmp;

                                icon_mv_dis_tmp += item_height + item_padding_y;

                                if (icon_mv_dis_tmp > icon_moving_dis_ex) {
                                    icon_pos_y = old_pos.y
                                            - (accumulate_ratio)
                                            * (item_height + item_padding_y)
                                            - (icon_moving_dis_ex - icon_mv_dis_tmp_old);
                                    icon.setPosition(item_end_pos_x, icon_pos_y);
                                    icon.setScale(0.8f, 0.8f);
                                    icon.endEffect();
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
