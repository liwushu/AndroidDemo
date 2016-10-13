
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.EffecterDelegate;
import com.gionee.thirdpartyeffect.view.ViewGroup3D;

public class APageEase {

    /**
     * 左右滑动指示
     */
    public static boolean is_scroll_from_right_to_left = false;

    public static void setScrolldirection(boolean is_right_to_left) {
        is_scroll_from_right_to_left = is_right_to_left;
    }

    /**
     * 按动画类型更新动画
     */
    public static void updateEffect(ViewGroup3D cur_view,
            ViewGroup3D next_view, float degree, float yScale, int type) {
        float this_width = 0;
        if (type != EffecterDelegate.GIONEE_EFFECT_UPRIGHT) {
            this_width = cur_view.getWidth();
            if (this_width == 0) {
                this_width = next_view.getWidth();
                if (this_width == 0) {
                    return;
                }
            }
        } else {
            this_width = cur_view.getHeight();
            if (this_width == 0) {
                this_width = next_view.getHeight();
                if (this_width == 0) {
                    return;
                }
            }
        }
        if (next_view == cur_view)
            type = EffecterDelegate.GIONEE_EFFECT_DEFAULT;

        switch (type) {
            case EffecterDelegate.GIONEE_EFFECT_DEFAULT:
            default:
                Default.updateEffect(cur_view, next_view, degree, yScale,
                        this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_BINARIES:
                Binaries.updateEffect(cur_view, next_view, degree, yScale,
                        this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_FAN:
                Fan.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_WHEEL:
                Wheel.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_BLIND:
                Blind.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_BIGFAN:
                Bigfan.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_TORNADO:
                Tornado.updateEffect(cur_view, next_view, degree, yScale,
                        this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_CYLINDER:
                Cylinder.updateEffect(cur_view, next_view, degree, yScale,
                        this_width);
                break;
            /*
             * case EffecterDelegate.GIONEE_EFFECT_ELASTICITY:
             * Elasticity.updateEffect(cur_view, next_view, degree,
             * original_degree_whn_touch_up, yScale,
             * this_width,is_anim_effect_shown_whn_touch_up); break;
             */
            case EffecterDelegate.GIONEE_EFFECT_BALL:
                Ball.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_CROSS:
                Cross.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_JUMP:
                Jump.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_MELT:
                Melt.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_UPRIGHT:
                Upright.updateEffect(cur_view, next_view, degree, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_FLIP:
                Flip.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            /*
             * case EffecterDelegate.GIONEE_EFFECT_SCROLL:
             * Scroll.updateEffect(cur_view, next_view, degree, yScale,
             * this_width); break;
             */
            case EffecterDelegate.GIONEE_EFFECT_ROTATE:
                Rotate.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_WAVE:
                Wave.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_PRESS:
                Press.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_OUTBOX:
                Outbox.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_INBOX:
                Inbox.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_FADEIN:
                FadeIn.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_ELECTRIC_FAN:
                ElectricFan.updateEffect(cur_view, next_view, degree, yScale,
                        this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_GS3:
                GS3.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_CERAUNITE:
                Ceraunite.updateEffect(cur_view, next_view, degree, yScale,
                        this_width, is_scroll_from_right_to_left);
                break;
            case EffecterDelegate.GIONEE_EFFECT_WINDOW:
                Window.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_ROLL:
                Roll.updateEffect(cur_view, next_view, degree, yScale, this_width,
                        is_scroll_from_right_to_left);
                break;
            case EffecterDelegate.GIONEE_EFFECT_ERASE:
                Erase.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_WIND:
                Wind.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
            case EffecterDelegate.GIONEE_EFFECT_HUMP:
                Hump.updateEffect(cur_view, next_view, degree, yScale, this_width,
                        is_scroll_from_right_to_left);
                break;
            case EffecterDelegate.GIONEE_EFFECT_SNAKE:
                Snake.updateEffect(cur_view, next_view, degree, yScale, this_width);
                break;
        }
    }
}
