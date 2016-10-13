
package com.gionee.thirdpartyeffect;

import com.gionee.thirdpartyeffect.view.EffecterDelegate;

/**
 * 动画工厂类
 */
public class EffecterFactory {

    public final static int EffectStyle_Classic = 1;
    public final static int EffectStyle_Fan = 2;
    public final static int EffectStyle_Jump = 3;
    public final static int EffectStyle_ElectricFan = 4;
    public final static int EffectStyle_Fadein = 5;
    public final static int EffectStyle_Wave = 6;
    public final static int EffectStyle_Cross_Flip = 7;
    public final static int EffectStyle_Box_Outside = 8;
    public final static int EffectStyle_Box_Inside = 9;
    public final static int EffectStyle_Flip = 10;
    public final static int EffectStyle_Galaxy_S3 = 11;
    public final static int EffectStyle_Upright = 12;
    public final static int EffectStyle_Press = 13;

    public final static int EffectStyle_Cylinder = 14;
    public final static int EffectStyle_Window = 15;
    public final static int EffectStyle_Shutter = 16;
    public final static int EffectStyle_Tornado = 17;
    public final static int EffectStyle_Ball = 18;
    public final static int EffectStyle_Binaries = 19;
    public final static int EffectStyle_Melt = 20;
    public final static int EffectStyle_Roll = 21;
    public final static int EffectStyle_Rotate = 22;
    public final static int EffectStyle_Wheel = 23;
    public final static int EffectStyle_Erase = 24;
    public final static int EffectStyle_Wind = 25;
    public final static int EffectStyle_Hump = 26;
    public final static int EffectStyle_Cross = 27;

    public final static int EffectStyle_Ceraunite = 28;
    public final static int EffectStyle_Snake = 29;
    public final static int EffectStyle_Random = 30;

    public static Effecter creator(int which) {
        switch (which) {
            case EffectStyle_Classic:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_DEFAULT);
            case EffectStyle_Fan:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_FAN);
            case EffectStyle_Jump:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_JUMP);
            case EffectStyle_ElectricFan:
                return new EffecterDelegate(
                        EffecterDelegate.GIONEE_EFFECT_ELECTRIC_FAN);
            case EffectStyle_Fadein:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_FADEIN);
            case EffectStyle_Wave:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_WAVE);
            case EffectStyle_Cylinder:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_CYLINDER);
            case EffectStyle_Shutter:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_BLIND);
            case EffectStyle_Cross_Flip:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_BIGFAN);
            case EffectStyle_Box_Outside:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_OUTBOX);
            case EffectStyle_Box_Inside:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_INBOX);
            case EffectStyle_Flip:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_FLIP);
            case EffectStyle_Galaxy_S3:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_GS3);
            case EffectStyle_Wheel:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_WHEEL);
            case EffectStyle_Ball:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_BALL);
            case EffectStyle_Binaries:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_BINARIES);
            case EffectStyle_Melt:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_MELT);
            case EffectStyle_Upright:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_UPRIGHT);
            case EffectStyle_Rotate:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_ROTATE);
            case EffectStyle_Press:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_PRESS);
            case EffectStyle_Roll:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_ROLL);
            case EffectStyle_Window:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_WINDOW);
            case EffectStyle_Tornado:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_TORNADO);
            case EffectStyle_Erase:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_ERASE);
            case EffectStyle_Wind:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_WIND);
            case EffectStyle_Hump:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_HUMP);
            case EffectStyle_Cross:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_CROSS);
            case EffectStyle_Ceraunite:
                return new EffecterDelegate(
                        EffecterDelegate.GIONEE_EFFECT_CERAUNITE);
            case EffectStyle_Snake:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_SNAKE);
                /*
                 * case EffectStyle_Random: return new EffecterRandom();
                 */
            default:
                return new EffecterDelegate(EffecterDelegate.GIONEE_EFFECT_OUTBOX);

        }
    }
}
