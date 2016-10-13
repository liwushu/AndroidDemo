
package com.gionee.thirdpartyeffect.view;

import com.gionee.thirdpartyeffect.APageEase;
import com.gionee.thirdpartyeffect.Effecter;
import com.gionee.thirdpartyeffect.utils.BitampUtils;
import com.gionee.thirdpartyeffect.view.IEffectView.PageParams;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

/**
 * 动画进度控制器
 */
public class EffecterDelegate extends Effecter {

    /**
     * 动画类型
     */
    public static final int GIONEE_EFFECT_DEFAULT = 0; // 默认
    public static final int GIONEE_EFFECT_RANDOM = 1; // 随机
    public static final int GIONEE_EFFECT_SEQUENCE = 2; // 顺序
    public static final int GIONEE_EFFECT_BINARIES = 3;// 双子星
    public static final int GIONEE_EFFECT_BLIND = 4; // 百叶窗
    public static final int GIONEE_EFFECT_FAN = 5; // 风车
    public static final int GIONEE_EFFECT_BIGFAN = 6; // 十字翻转
    public static final int GIONEE_EFFECT_TORNADO = 7; // 龙卷风
    public static final int GIONEE_EFFECT_WHEEL = 8; // 车轮
    public static final int GIONEE_EFFECT_CYLINDER = 9;// 圆柱
    public static final int GIONEE_EFFECT_BALL = 10; // 球体
    public static final int GIONEE_EFFECT_CROSS = 11; // 交叉
    public static final int GIONEE_EFFECT_JUMP = 12; // 弹跳
    public static final int GIONEE_EFFECT_MELT = 13; // 溶解
    public static final int GIONEE_EFFECT_UPRIGHT = 14; // 垂直
    public static final int GIONEE_EFFECT_FLIP = 15; // 翻转
    public static final int GIONEE_EFFECT_SCROLL = 16; //
    public static final int GIONEE_EFFECT_ROTATE = 17; // 旋转
    public static final int GIONEE_EFFECT_WAVE = 18; // 波浪
    public static final int GIONEE_EFFECT_PRESS = 19; // 挤压
    public static final int GIONEE_EFFECT_INBOX = 20; // 内盒
    public static final int GIONEE_EFFECT_FADEIN = 21; // 层叠
    public static final int GIONEE_EFFECT_OUTBOX = 22; // 外盒
    public static final int GIONEE_EFFECT_GS3 = 23; // 默认(GS3高仿)
    public static final int GIONEE_EFFECT_ELASTICITY = 24; // 弹性
    public static final int GIONEE_EFFECT_ELECTRIC_FAN = 25; // 风扇

    public static final int GIONEE_EFFECT_ROLL = 26; // 卷帘
    public static final int GIONEE_EFFECT_WINDOW = 27; // 窗户
    public static final int GIONEE_EFFECT_ERASE = 28; // 擦除
    public static final int GIONEE_EFFECT_WIND = 29; // 暴风
    public static final int GIONEE_EFFECT_HUMP = 30; // 凸起
    public static final int GIONEE_EFFECT_CERAUNITE = 31; // 陨石
    public static final int GIONEE_EFFECT_SNAKE = 32; // 贪吃蛇

    IEffectView mEE;

    private View mCurPage = null;
    private View mNextPage = null;
    private View mPrePage = null;

    private Bitmap mCurPageShot = null;
    private Bitmap mNextPageShot = null;
    private Bitmap mPrePageShot = null;

    private ViewGroup3D meffectPrePage = new ViewGroup3D();
    private ViewGroup3D meffectCurPage = new ViewGroup3D();
    private ViewGroup3D meffectNextPage = new ViewGroup3D();

    private boolean mIsLoop = false;
    private boolean mIsEnd = false;

    private int mEffectType = 0;
    private boolean mIsNeedClip = false;

    private int[] mCLipCountInXY = new int[2];

    private final static int INVALID_INDEX = -1;

    private int mCurrentPageIndex = INVALID_INDEX;

    public EffecterDelegate(int type) {
        mEffectType = type;
        mIsNeedClip = isNeedClip(type);
        getClipCount(type, mCLipCountInXY);
    }

    @Override
    public void setIEffectView(IEffectView ie) {
        mEE = ie;
    }

    /**
     * 启动动画
     * 
     * @param bLoop 是否循环控制
     */
    @Override
    public void start(boolean bLoop) {
        IEffectView ee = mEE;
        int curPageIndex = ee.getCurPageIndex();
        if (curPageIndex != mCurrentPageIndex) {
            mCurrentPageIndex = curPageIndex;
        } else {
            if (!mIsEnd) {
                return;
            }
        }
        mIsEnd = false;
        mIsLoop = bLoop;
        Log.d("EffecterDelegate", "start()");
        int pageCount = ee.getPageCount();

        if (pageCount == 1) {
            mCurPage = null;
            return;
        }

        PageParams pageparams = ee.getPageParams();
        mCurPage = ee.getPageAt(curPageIndex);

        if (curPageIndex == 0 && mIsLoop) {
            if (pageCount - 1 != curPageIndex + 1) {
                mPrePage = ee.getPageAt(pageCount - 1);
                mNextPage = ee.getPageAt(curPageIndex + 1);
            } else {
                mNextPage = ee.getPageAt(curPageIndex + 1);
                mPrePage = mNextPage;
            }
        } else if (curPageIndex == pageCount - 1 && mIsLoop) {
            if (curPageIndex - 1 != 0) {
                mPrePage = ee.getPageAt(curPageIndex - 1);
                mNextPage = ee.getPageAt(0);
            } else {
                mPrePage = ee.getPageAt(curPageIndex - 1);
                mNextPage = mPrePage;
            }
        } else {
            mPrePage = ee.getPageAt(curPageIndex - 1);
            mNextPage = ee.getPageAt(curPageIndex + 1);
        }

        if (mIsNeedClip) {
            mCurPageShot = BitampUtils.getViewBitmap(mCurPage);
        }

        if (mPrePage != null) {
            mPrePage.setTranslationX(-pageparams.mWidth);
            if (mIsNeedClip) {
                mPrePageShot = BitampUtils.getViewBitmap(mPrePage);
            }
        }

        if (mNextPage != null) {
            mNextPage.setTranslationX(-pageparams.mWidth);
            if (mIsNeedClip) {
                mNextPageShot = BitampUtils.getViewBitmap(mNextPage);
            }
        }

        meffectPrePage.SetPageSView(mPrePage, mIsNeedClip, mCLipCountInXY);
        meffectCurPage.SetPageSView(mCurPage, mIsNeedClip, mCLipCountInXY);
        meffectNextPage.SetPageSView(mNextPage, mIsNeedClip, mCLipCountInXY);
    }

    /**
     * 动画结束
     */
    @Override
    public void end() {
        Log.d("EffecterDelegate", "end()");
        mIsEnd = true;
        mCurrentPageIndex = INVALID_INDEX;

        mCurPageShot = null;
        mNextPageShot = null;
        mPrePageShot = null;

        meffectPrePage.clearTransform();
        meffectCurPage.clearTransform();
        meffectNextPage.clearTransform();

        mIsLoop = false;
    }

    /**
     * 更新动画
     * 
     * @param factor 动画进度百分比
     */
    @Override
    public void setFactor(float factor) {
        if (mCurPage == null) {
            return;
        }

        float effect_factor = factor;
        if (factor > 0) {
            APageEase.setScrolldirection(false);
            effect_factor = factor - 1;
            meffectPrePage.setPageTransformation(mEE.getFirstTransformation());
            mEE.getFirstTransformation().mPagedShot = mPrePageShot;
            meffectCurPage.setPageTransformation(mEE.getSecondTransformation());
            mEE.getSecondTransformation().mPagedShot = mCurPageShot;
            APageEase.updateEffect(meffectPrePage, meffectCurPage, effect_factor, 0f,
                    mEffectType);
        } else {
            APageEase.setScrolldirection(true);
            effect_factor = factor;
            meffectCurPage.setPageTransformation(mEE.getFirstTransformation());
            mEE.getFirstTransformation().mPagedShot = mCurPageShot;
            meffectNextPage.setPageTransformation(mEE.getSecondTransformation());
            mEE.getSecondTransformation().mPagedShot = mNextPageShot;
            APageEase.updateEffect(meffectCurPage, meffectNextPage, effect_factor, 0f,
                    mEffectType);
        }

    }

    public void clearPageSView(View pageSView) {
        IEffectView ee = mEE;
        View curPageView = ee.getPageAt(ee.getCurPageIndex());
        if (pageSView != null && pageSView != curPageView) {
            pageSView.setTranslationX(0);
        }
    }

    @Override
    public void onPageIndexChange() {

        clearPageSView(mCurPage);
        clearPageSView(mNextPage);
        clearPageSView(mPrePage);

        IEffectView ee = mEE;
        int curPageIndex = ee.getCurPageIndex();
        int pageCount = ee.getPageCount();

        if (pageCount == 1) {
            mCurPage = null;
            mPrePage = null;
            mNextPage = null;
            return;
        }

        if (!mIsEnd) {
            mCurPage = ee.getPageAt(curPageIndex);
            if (curPageIndex == 0 && mIsLoop) {
                if (pageCount - 1 != curPageIndex + 1) {
                    mPrePage = ee.getPageAt(pageCount - 1);
                    mNextPage = ee.getPageAt(curPageIndex + 1);
                } else {
                    mNextPage = ee.getPageAt(curPageIndex + 1);
                    mPrePage = mNextPage;
                }
            } else if (curPageIndex == pageCount - 1 && mIsLoop) {
                if (curPageIndex - 1 != 0) {
                    mPrePage = ee.getPageAt(curPageIndex - 1);
                    mNextPage = ee.getPageAt(0);
                } else {
                    mPrePage = ee.getPageAt(curPageIndex - 1);
                    mNextPage = mPrePage;
                }
            } else {
                mPrePage = ee.getPageAt(curPageIndex - 1);
                mNextPage = ee.getPageAt(curPageIndex + 1);
            }

            meffectPrePage.SetPageSView(mPrePage, mIsNeedClip, mCLipCountInXY);
            meffectCurPage.SetPageSView(mCurPage, mIsNeedClip, mCLipCountInXY);
            meffectNextPage.SetPageSView(mNextPage, mIsNeedClip, mCLipCountInXY);
        }
    }

    private boolean isNeedClip(int type) {
        return (type == GIONEE_EFFECT_WHEEL
                || type == GIONEE_EFFECT_BALL
                || type == GIONEE_EFFECT_CYLINDER
                || type == GIONEE_EFFECT_BINARIES
                || type == GIONEE_EFFECT_BLIND
                || type == GIONEE_EFFECT_ROTATE
                || type == GIONEE_EFFECT_ROLL
                || type == GIONEE_EFFECT_WINDOW
                || type == GIONEE_EFFECT_TORNADO
                || type == GIONEE_EFFECT_ERASE
                || type == GIONEE_EFFECT_WIND
                || type == GIONEE_EFFECT_HUMP
                || type == GIONEE_EFFECT_CROSS
                || type == GIONEE_EFFECT_CERAUNITE
                || type == GIONEE_EFFECT_SNAKE);
    }

    private void getClipCount(int type, int[] xy) {
        switch (type) {
            case GIONEE_EFFECT_CYLINDER:
                xy[0] = 4;
                xy[1] = 1;
                break;
            case GIONEE_EFFECT_BALL:
                xy[0] = 6;
                xy[1] = 6;
                break;
            default:
                xy[0] = 4;
                xy[1] = 4;
        }
    }

    @Override
    public boolean isNeedClip() {
        return mIsNeedClip;
    }

    @Override
    public boolean isEnd() {
        return mIsEnd;
    }
}
