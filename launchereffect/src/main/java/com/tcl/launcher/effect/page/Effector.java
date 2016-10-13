package com.tcl.launcher.effect.page;

import android.graphics.Bitmap;
import android.view.View;

import com.tcl.launcher.effect.pojo.ViewEffect3D;
import com.tcl.launcher.effect.utils.BitampUtils;

/**
 * Created by liwu.shu on 2016/8/27.
 */
public abstract class Effector implements IEffector {

    IViewEffect viewEffect;
    private View mCurPage = null;
    private View mNextPage = null;
    private View mPrePage = null;

    private Bitmap mCurPageShot = null;
    private Bitmap mNextPageShot = null;
    private Bitmap mPrePageShot = null;

    private ViewEffect3D mEffectPrePage = new ViewEffect3D();
    private ViewEffect3D mEffectCurPage = new ViewEffect3D();
    private ViewEffect3D mEffectNextPage = new ViewEffect3D();

    private boolean mIsLoop = false;
    private boolean mIsEnd = false;

    private boolean mIsNeedClip = false;

    private int[] mCLipCountInXY = new int[2];

    private final static int INVALID_INDEX = -1;

    private int mCurrentPageIndex = INVALID_INDEX;

    @Override
    public void setViewEffect(IViewEffect viewEffect) {
        this.viewEffect = viewEffect;
        getClipCount(mCLipCountInXY);
        mIsNeedClip = isNeedClip();
    }

    @Override
    public void start(boolean loop) {
        IViewEffect ee = viewEffect;
        int curPageIndex = ee.getCurPageIndex();
        if (curPageIndex != mCurrentPageIndex) {
            mCurrentPageIndex = curPageIndex;
        } else {
            if (!mIsEnd) {
                return;
            }
        }
        mIsEnd = false;
        mIsLoop = loop;
        int pageCount = ee.getPageCount();

        if (pageCount == 1) {
            mCurPage = null;
            return;
        }

        IViewEffect.PageParams pageparams = ee.getPageParams();
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

        mEffectPrePage.setPageViews(mPrePage, mIsNeedClip, mCLipCountInXY);
        mEffectCurPage.setPageViews(mCurPage, mIsNeedClip, mCLipCountInXY);
        mEffectNextPage.setPageViews(mNextPage, mIsNeedClip, mCLipCountInXY);
    }

    @Override
    public void end() {
        mIsEnd = true;
        mCurrentPageIndex = INVALID_INDEX;

        mCurPageShot = null;
        mNextPageShot = null;
        mPrePageShot = null;

        mEffectPrePage.clearTransform();
        mEffectCurPage.clearTransform();
        mEffectNextPage.clearTransform();

        mIsLoop = false;
    }

    @Override
    public void setFactor(float factor) {
        if (mCurPage == null) {
            return;
        }
        float effect_factor = factor;
        APageBase pageBase = getPageBase();
        if (effect_factor > 0) {
            pageBase.setScrolldirection(false);
            effect_factor = factor - 1;
            mEffectPrePage.setPageTransformationInfo(viewEffect.getFirstTransformation());
            viewEffect.getFirstTransformation().mPagedShot = mPrePageShot;
            mEffectCurPage.setPageTransformationInfo(viewEffect.getSecondTransformation());
            viewEffect.getSecondTransformation().mPagedShot = mCurPageShot;
            pageBase.updateEffect(mEffectPrePage, mEffectCurPage, effect_factor, 0f);
        } else {
            pageBase.setScrolldirection(true);
            effect_factor = factor;
            mEffectCurPage.setPageTransformationInfo(viewEffect.getFirstTransformation());
            viewEffect.getFirstTransformation().mPagedShot = mCurPageShot;
            mEffectNextPage.setPageTransformationInfo(viewEffect.getSecondTransformation());
            viewEffect.getSecondTransformation().mPagedShot = mNextPageShot;
            pageBase.updateEffect(mEffectCurPage, mEffectNextPage, effect_factor, 0f);
        }
    }

    public void clearPageSView(View pageSView) {
        IViewEffect ee = viewEffect;
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

        IViewEffect ee = viewEffect;
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

            mEffectPrePage.setPageViews(mPrePage, mIsNeedClip, mCLipCountInXY);
            mEffectCurPage.setPageViews(mCurPage, mIsNeedClip, mCLipCountInXY);
            mEffectNextPage.setPageViews(mNextPage, mIsNeedClip, mCLipCountInXY);
        }
    }


    @Override
    public boolean isEnd() {
        return mIsEnd;
    }

    public abstract APageBase getPageBase();

    public abstract boolean isNeedClip();


    public void getClipCount(int[] xy){
        xy[0] = 4;
        xy[1] = 4;
    }
}
