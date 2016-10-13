
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class Erase {
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, float this_width) {
        ViewGroup3D icon;

        if (degree <= 0 && degree < -1 / 2f) {
            int next_view_colomn_num = (next_view).getCellCountX();

            cur_view.setVisible(false);
            next_view.setVisible(true);

            int next_size = next_view.getChildCount();

            for (int i = 0; i < next_size; i++) {
                int icon_column_num = 0;
                icon = next_view.getChildAt(i);

                icon_column_num = icon.getColumnNum();

                for (int column = 0; column < next_view_colomn_num; column++) {
                    if (column == icon_column_num) {
                        if (Math.abs(degree) * 2 < 1f
                                + (float) (next_view_colomn_num - column)
                                / (next_view_colomn_num)
                                && Math.abs(degree) * 2 > 1f
                                        + (next_view_colomn_num - column - 1f)
                                        / next_view_colomn_num) {
                            ExaminePreColumnStatusAndImplement(next_view,
                                    column + 1, true);// true means shown
                            ExaminePreColumnStatusAndImplement(next_view,
                                    column - 1, false);

                            float color_a = Math.abs(degree) * 2f
                                    * next_view_colomn_num
                                    - (2 * next_view_colomn_num - 1f - column);

                            icon.setAlpha(color_a);
                        }
                    }
                }

            }

            int cur_view_size = cur_view.getChildCount();
            for (int i = 0; i < cur_view_size; i++) {
                ViewGroup3D cur_view_icon = cur_view.getChildAt(i);
                {
                    float nextViewIconColor_a = 0f;
                    cur_view_icon.setAlpha(nextViewIconColor_a);
                }
            }
        } else {
            int cur_view_colomn_num = (cur_view).getCellCountX();

            next_view.setVisible(false);
            cur_view.setVisible(true);

            int cur_size = cur_view.getChildCount();

            for (int i = 0; i < cur_size; i++) {
                int icon_column_num = 0;
                icon = cur_view.getChildAt(i);

                icon_column_num = icon.getColumnNum();

                for (int column = 0; column < cur_view_colomn_num; column++) {
                    if (icon_column_num == column) {
                        if (Math.abs(degree) > (float) (cur_view_colomn_num
                                - column - 1)
                                / (2 * cur_view_colomn_num)
                                && Math.abs(degree) < (float) (cur_view_colomn_num - column)
                                        / (2 * cur_view_colomn_num)) {
                            ExaminePreColumnStatusAndImplement(cur_view,
                                    column + 1, false);
                            ExaminePreColumnStatusAndImplement(cur_view,
                                    column - 1, true);

                            float color_a = (cur_view_colomn_num
                                    - Math.abs(degree) * 2
                                    * cur_view_colomn_num - column);

                            icon.setAlpha(color_a);
                        }
                    }
                }
            }

            // set next view's color transparent
            int next_view_size = next_view.getChildCount();
            for (int i = 0; i < next_view_size; i++) {
                ViewGroup3D next_view_icon = next_view.getChildAt(i);
                {
                    float nextViewIconColor_a = 0f;
                    next_view_icon.setAlpha(nextViewIconColor_a);
                }
            }

        }
    }

    private static void ExaminePreColumnStatusAndImplement(
            ViewGroup3D cur_view, int pre_column, boolean is_shown_or_disappeard) {
        int cur_CountX = (cur_view).getCellCountX();
        float user_intend_color_a = -1f;

        if (pre_column < 0 || pre_column >= cur_CountX) {
            return;
        }

        if (is_shown_or_disappeard == true) // user wants show this icon
        {
            user_intend_color_a = 1f;
        } else {
            user_intend_color_a = 0f;
        }

        for (int index = 0; index < cur_view.getChildCount(); index++) {

            ViewGroup3D icon = cur_view.getChildAt(index);

            if (icon.getColumnNum() == pre_column) {
                icon.setAlpha(user_intend_color_a);
            }
        }
    }
}
