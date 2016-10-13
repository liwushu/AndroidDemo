
package com.gionee.thirdpartyeffect.view;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.gionee.thirdpartyeffect.Effecter;
import com.gionee.thirdpartyeffect.EffecterFactory;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.RemoteViews.RemoteView;
import android.widget.Scroller;

/**
 * 显示容器
 */
@RemoteView
public class EasyViewGroup extends ViewGroup implements IEffectView {

    private final String TAG = "EasyViewGroup";
    private static final boolean DEBUG = false;
    /**
     * 换页过程中状态
     */
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SETTLING = 2;

    int mScrollState = SCROLL_STATE_IDLE;

    /**
     * 手势识别模式
     */
    public final static int HORIENZON = 1;
    public final static int VERTICAL = 2;

    private int mOrientation = HORIENZON;
    private float mAspectratio;

    private static final int INVALID_POINTER = -1;
    private static final int ANI_DURATION = 600;

    private int mCurIndex;
    private int mCurEffecterType;

    Effecter mEffecter;

    /**
     * 子View属性变化的载体
     */
    PageTranformationInfo mFirstPageTranformation = new PageTranformationInfo();
    PageTranformationInfo mSecondPageTranformation = new PageTranformationInfo();

    PageParams mPageParams = new PageParams();

    private VelocityTracker mVelocityTracker;
    private Scroller mScroller;

    private float mInitialMotionX;
    private float mInitialMotionY;
    private float mLastMotionX;
    private int mActivePointerId;
    private float mLastMotionY;
    private boolean mIsBeingDragged;
    private boolean mIsUnableToDrag;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private int mFlingDistance;
    private int mCloseEnough;
    private int mWhenExInterDefaultDis;
    private PageListener mPageListener;
    private AtomicBoolean mLoop = new AtomicBoolean();

    private static final int MIN_DISTANCE_FOR_FLING = 25; // dips
    private static final int CLOSE_ENOUGH = 2; // dp
    private static final int DEFAULTMAXDIS = 24; // dips;

    private ISlidePageExControl mISlidePageExControl;
    private boolean mConsistencyIntercepterVerifier = false;
    private boolean mIsForbidenToDrag = false;
    private float mDetalX = 0;

    /**
     * 换页接口
     */
    public interface PageListener {
        /**
         * 换页完成调用
         * 
         * @param index 页码
         */
        public void onPageSelseted(int index);

        /**
         * 换页过程中状态改变
         * 
         * @param state 页码 {@link #SCROLL_STATE_IDLE
         *            ,#SCROLL_STATE_DRAGGING,#SCROLL_STATE_SETTLING}
         */
        public void onPageSrcollStateChange(int state);

        /**
         * 换页进度
         * 
         * @param fraction 0～1 0～-1
         */
        public void onPageSlideFraction(float fraction);
    }

    public interface ISlidePageExControl {
        public boolean isBlockSlidePage();
    }

    private static final Interpolator sInterpolator = new Interpolator() {
        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };

    static class PageTranformationInfo {

        boolean bmIsMatrixDirty;
        boolean bmDrawOrder = true;
        float mAlpha;
        View mPagedView;
        Bitmap mPagedShot = null;
        final Matrix mPagedTransMatrix = new Matrix();

        static final Paint SPaint = new Paint();
        static {
            SPaint.setAntiAlias(true);
        }
        static final Rect SRect = new Rect();

        List<ClipView> mClipViews;

        public void resetAndrecyle() {
            mAlpha = 1.0f;
            mPagedView = null;
            mClipViews = null;
            mPagedShot = null;
            mPagedTransMatrix.reset();
        }
    }

    public EasyViewGroup(Context context) {
        super(context);
        initViewPager();
    }

    public EasyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViewPager();
    }

    void initViewPager() {
        setWillNotDraw(false);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        mLoop.set(true);
        final Context context = getContext();
        mScroller = new Scroller(context, sInterpolator);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop() / 2;
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

        final float density = context.getResources().getDisplayMetrics().density;
        mFlingDistance = (int) (MIN_DISTANCE_FOR_FLING * density);
        mCloseEnough = (int) (CLOSE_ENOUGH * density);
        mWhenExInterDefaultDis = (int) (DEFAULTMAXDIS * density);
        setEffecterType(EffecterFactory.EffectStyle_Box_Outside);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));

        final int measuredWidth = getMeasuredWidth();
        final int measuredheight = getMeasuredHeight();

        if (null != mPageParams) {
            mPageParams.mWidth = measuredWidth;
            mPageParams.mHeight = measuredheight;
        }
        mAspectratio = measuredWidth * 1.0f / measuredheight;

        int childWidthSize = getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight();
        int childHeightSize = getMeasuredHeight() - getPaddingTop()
                - getPaddingBottom();

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                child.measure(MeasureSpec.makeMeasureSpec(childWidthSize,
                        MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
                        childHeightSize, MeasureSpec.EXACTLY));
            }
        }
    }

    /**
     * 所有子View都放置在显示区域的右侧，然后当前显示View向左偏移一个屏幕
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int offset = getWidth();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                left = offset + paddingLeft;
                child.layout(left, paddingTop, left + child.getMeasuredWidth(),
                        paddingTop + child.getMeasuredHeight());
            }
        }
        if (count > 0 && mCurIndex >= 0 && mCurIndex < count) {
            View child = getChildAt(mCurIndex);
            child.setTranslationX(-mPageParams.mWidth);
        }

    }

    public void setScrollState(int newState) {
        if (mScrollState == newState) {
            return;
        }
        mScrollState = newState;
        if (DEBUG)
            Log.d(TAG, "setScrollState() mScrollState=" + mScrollState);
        if (null != mPageListener) {
            mPageListener.onPageSrcollStateChange(mScrollState);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (getPageCount() <= 1
                || (null != mISlidePageExControl && mISlidePageExControl
                        .isBlockSlidePage()) || mIsForbidenToDrag) {
            return false;
        }

        if (DEBUG)
            Log.v(TAG, "onInterceptTouchEvent MotionEvent=" + ev.getAction());
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        System.out.println("onInterceptTouchEvent: "+action);
        // Always take care of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL
                || action == MotionEvent.ACTION_UP) {
            // Release the drag.
            if (DEBUG)
                Log.v(TAG, "Intercept done!");
            mIsBeingDragged = false;
            mIsUnableToDrag = false;
            mActivePointerId = INVALID_POINTER;

            if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
            return false;
        }

        // Nothing more to do here if we have decided whether or not we
        // are dragging.
        if (action != MotionEvent.ACTION_DOWN) {
            if (mIsBeingDragged) {
                if (DEBUG)
                    Log.v(TAG, "Intercept returning true!");
                return true;
            }
            if (mIsUnableToDrag) {
                if (DEBUG)
                    Log.v(TAG, "Intercept returning false!");
                return false;
            }
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    // If we don't have a valid id, the touch down wasn't on
                    // content.
                    break;
                }

                final int pointerIndex = ev.findPointerIndex(activePointerId);
                final float x = ev.getX(pointerIndex);
                final float dx = x - mLastMotionX;
                final float xDiff = Math.abs(dx);
                final float y = ev.getY(pointerIndex);
                final float dy = y - mLastMotionY;
                final float yDiff = Math.abs(y - mLastMotionY);
                if (DEBUG)
                    Log.v(TAG, "Moved x to " + x + "," + y + " diff=" + xDiff + ","
                            + yDiff);

                if (mOrientation == VERTICAL) {
                    if (yDiff > mTouchSlop && yDiff > xDiff) {
                        if (DEBUG)
                            Log.v(TAG, "Starting drag!");
                        mIsBeingDragged = true;
                        mEffecter.start(mLoop.get());
                        setScrollState(SCROLL_STATE_DRAGGING);
                        mLastMotionY = dy > 0 ? mInitialMotionY + mTouchSlop :
                                mInitialMotionY - mTouchSlop;
                    } else {
                        if (xDiff > mTouchSlop) {
                            if (DEBUG)
                                Log.v(TAG, "Starting unable to drag!");
                            mIsUnableToDrag = true;
                        }
                    }
                    if (mIsBeingDragged) {
                        if (performDrag(y)) {
                            invalidate();
                        }
                    }
                } else {
                    if (xDiff > mTouchSlop && xDiff > yDiff) {
                        if (DEBUG)
                            Log.v(TAG, "Starting drag!");
                        mIsBeingDragged = true;
                        mEffecter.start(mLoop.get());
                        setScrollState(SCROLL_STATE_DRAGGING);
                        mLastMotionX = dx > 0 ? mInitialMotionX + mTouchSlop :
                                mInitialMotionX - mTouchSlop;
                    } else {
                        if (yDiff > mTouchSlop) {
                            if (DEBUG)
                                Log.v(TAG, "Starting unable to drag!");
                            mIsUnableToDrag = true;
                        }
                    }
                    if (mIsBeingDragged) {
                        if (performDrag(x)) {
                            invalidate();
                        }
                    }
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                /*
                 * Remember location of down touch. ACTION_DOWN always refers to
                 * pointer index 0.
                 */
                mLastMotionX = mInitialMotionX = ev.getX();
                mLastMotionY = mInitialMotionY = ev.getY();
                mActivePointerId = ev.getPointerId(0);
                mIsUnableToDrag = false;

                mScroller.computeScrollOffset();
                if (mScrollState == SCROLL_STATE_SETTLING &&
                        Math.abs(mScroller.getFinalX() - mScroller.getCurrX()) > mCloseEnough) {
                    // Let the user 'catch' the pager as it animates.
                    mScroller.abortAnimation();
                    mIsBeingDragged = true;
                    mEffecter.start(mLoop.get());
                    setScrollState(SCROLL_STATE_DRAGGING);
                } else {
                    mIsBeingDragged = false;
                    completeScroll();
                }

                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        /*
         * The only time we want to intercept motion events is if we are in the
         * drag mode.
         */
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (getPageCount() <= 1) {
            return true;
        }

        if (mConsistencyIntercepterVerifier) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                mConsistencyIntercepterVerifier = false;
                mActivePointerId = INVALID_POINTER;
                mIsUnableToDrag = false;
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
            }
            return true;
        }

        if ((null != mISlidePageExControl && mISlidePageExControl
                .isBlockSlidePage())) {
            if (!mConsistencyIntercepterVerifier) {
                int action = ev.getAction();
                if (action != MotionEvent.ACTION_UP && action != MotionEvent.ACTION_CANCEL) {
                    mConsistencyIntercepterVerifier = true;
                }
                mIsForbidenToDrag = true;
                // mIsBeingDragged = false;
                WhenExIntercepterAnimaToDefaultPostion();
            }
            return true;
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0) {
            return false;
        }

        boolean needsInvalidate = false;

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        final int action = ev.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                mScroller.abortAnimation();
                mIsBeingDragged = true;
                mEffecter.start(mLoop.get());
                setScrollState(SCROLL_STATE_DRAGGING);
                // Remember where the motion event started
                mLastMotionX = mInitialMotionX = ev.getX();
                mLastMotionY = mInitialMotionY = ev.getY();
                mActivePointerId = ev.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                if (!mIsBeingDragged) {
                    final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                    final int pointCount =ev.getPointerCount();
                    if (pointerIndex == -1 || pointerIndex > pointCount - 1) {
                        break;
                    }
                    final float x = ev.getX(pointerIndex);
                    final float xDiff = Math.abs(x - mLastMotionX);
                    final float y = ev.getY(pointerIndex);
                    final float yDiff = Math.abs(y - mLastMotionY);
                    if (DEBUG)
                        Log.v(TAG, "Moved x to " + x + "," + y + " diff=" + xDiff + "," + yDiff);
                    if (mOrientation == VERTICAL) {
                        if (yDiff > mTouchSlop || yDiff > xDiff) {
                            if (DEBUG)
                                Log.v(TAG, "Starting drag!");
                            mIsBeingDragged = true;
                            mEffecter.start(mLoop.get());
                            mLastMotionY = y - mInitialMotionY > 0 ? mInitialMotionY
                                    + mTouchSlop : mInitialMotionY - mTouchSlop;
                            setScrollState(SCROLL_STATE_DRAGGING);
                        }
                    } else {
                        if (xDiff > mTouchSlop || xDiff > yDiff) {
                            if (DEBUG)
                                Log.v(TAG, "Starting drag!");
                            mIsBeingDragged = true;
                            mEffecter.start(mLoop.get());
                            mLastMotionX = x - mInitialMotionX > 0 ? mInitialMotionX
                                    + mTouchSlop : mInitialMotionX - mTouchSlop;
                            setScrollState(SCROLL_STATE_DRAGGING);
                        }
                    }
                }

                if (mIsBeingDragged && mActivePointerId != INVALID_POINTER) {
                    final int activePointerIndex = ev
                            .findPointerIndex(mActivePointerId);
                    if (activePointerIndex == -1) {
                        break;
                    }
                    final float x = mLastMotionX = ev.getX(activePointerIndex);
                    final float y = mLastMotionY = ev.getY(activePointerIndex);
                    if (DEBUG)
                        Log.v(TAG, "Moved x to " + x + "," + y);
                    if (mOrientation == VERTICAL) {
                        needsInvalidate = performDrag(y);
                    } else {
                        needsInvalidate = performDrag(x);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity;
                    int totalDelta;
                    final int activePointerIndex = ev
                            .findPointerIndex(mActivePointerId);
                    final float x = ev.getX(activePointerIndex);
                    final float y = ev.getY(activePointerIndex);
                    if (mOrientation == VERTICAL) {
                        totalDelta = (int) (y - mInitialMotionY);
                        totalDelta *= mAspectratio;
                        initialVelocity = (int) velocityTracker
                                .getYVelocity(mActivePointerId);
                    } else {
                        totalDelta = (int) (x - mInitialMotionX);
                        initialVelocity = (int) velocityTracker
                                .getXVelocity(mActivePointerId);
                    }
                    totalDelta = (int) reviseDetalX(totalDelta);
                    float pageOffset = totalDelta * 1.0f / getWidth();

                    int nextPage = determineTargetPage(mCurIndex, pageOffset, initialVelocity,
                            totalDelta);
                    int slidePara = mCurIndex - nextPage;
                    if (mLoop.get()) {
                        if (totalDelta > 0 && slidePara < 0) {
                            slidePara = 1;
                        } else if (totalDelta < 0 && slidePara > 0) {
                            slidePara = -1;
                        }
                    }
                    mScroller
                            .startScroll(totalDelta, 0, slidePara * getWidth() - totalDelta, 0, ANI_DURATION);
                    setCurrentIndex(nextPage);
                    setScrollState(SCROLL_STATE_SETTLING);
                    mActivePointerId = INVALID_POINTER;
                    endDrag();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged) {
                    mActivePointerId = INVALID_POINTER;
                    endDrag();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = ev.getActionIndex();
                final float x = ev.getX(index);
                mLastMotionX = x;
                mActivePointerId = ev.getPointerId(index);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                mLastMotionX = ev.getX(ev.findPointerIndex(mActivePointerId));
                break;
        }
        if (needsInvalidate) {
            postInvalidate();
        }
        return true;
    }

    private void endDrag() {
        mIsBeingDragged = false;
        mIsUnableToDrag = false;

        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mLastMotionX = ev.getX(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
            if (mVelocityTracker != null) {
                mVelocityTracker.clear();
            }
        }
    }

    private float getScrollFraction(float detalX) {
        float fraction = detalX / getWidth();
        if (null != mPageListener) {
            mPageListener.onPageSlideFraction(fraction);
        }
        return fraction;
    }

    /**
     * invalidate()调用绘制
     */
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int newX = mScroller.getCurrX();
            float fraction = getScrollFraction(newX);
            mEffecter.setFactor(fraction);
            postInvalidate();
        } else {
            if (!mIsBeingDragged) {
                if (!mEffecter.isEnd()) {
                    mEffecter.end();
                    mEffecter.onPageIndexChange();
                }
                completeScroll();
            }
        }
    }

    private void completeScroll() {
        boolean needPopulate = mScrollState == SCROLL_STATE_SETTLING;
        if (needPopulate) {
            mScroller.abortAnimation();
            setScrollState(SCROLL_STATE_IDLE);
        }
    }

    private boolean performDrag(float x) {
        float detalX = getDetalX(x);
        mEffecter.setFactor(getScrollFraction(detalX));
        return true;
    }

    private float getDetalX(float x) {
        float detalX;
        if (mOrientation == VERTICAL) {
            detalX = x - mInitialMotionY;
            detalX = detalX * mAspectratio;
        } else {
            detalX = x - mInitialMotionX;
        }
        mDetalX = detalX = reviseDetalX(detalX);
        return detalX;
    }

    private float reviseDetalX(float detalx) {
        if (!mLoop.get()) {
            int compareValue = getWidth() / 2;
            if (mCurIndex == 0 && detalx > 0) {
                detalx = Math.min(compareValue, detalx);
            } else if (mCurIndex == getPageCount() - 1 && detalx < 0) {
                detalx = Math.max(-compareValue, detalx);
            }
        }
        return detalx;
    }

    private void setCurrentIndex(int item) {
        if (mCurIndex == item) {
            return;
        }
        if (mEffecter.isNeedClip()) {
            int oldpre = mCurIndex - 1;
            int oldnext = mCurIndex + 1;
            if (mLoop.get()) {
                if (oldpre < 0) {
                    oldpre = getPageCount() - 1;
                }
                if (oldnext > getPageCount() - 1) {
                    oldnext = 0;
                }
            }
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                if (i != oldpre && i != oldnext && i != mCurIndex) {
                    getChildAt(i).destroyDrawingCache();
                }
            }
        }
        mCurIndex = item;
        if (null != mPageListener) {
            mPageListener.onPageSelseted(mCurIndex);
        }
    }

    private int determineTargetPage(int currentPage, float pageOffset,
            int velocity, int deltaX) {
        int targetPage;
        if (Math.abs(deltaX) > mFlingDistance
                && Math.abs(velocity) > mMinimumVelocity) {
            targetPage = velocity > 0 ? currentPage - 1 : currentPage + 1;
        } else if (Math.abs(pageOffset) > 0.5f && deltaX < 0) {
            targetPage = currentPage + 1;
        } else if (Math.abs(pageOffset) > 0.5f && deltaX > 0) {
            targetPage = currentPage - 1;
        } else {
            targetPage = currentPage;
        }
        if (mLoop.get()) {
            if (targetPage < 0) {
                targetPage = getPageCount() - 1;
            } else if (targetPage > getPageCount() - 1) {
                targetPage = 0;
            }
        }
        targetPage = Math.max(0, Math.min(targetPage, getPageCount() - 1));
        return targetPage;
    }

    @Override
    public void setEffecter(Effecter ef) {
        mEffecter = ef;
        if (mEffecter.isNeedClip()) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } else {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        mEffecter.setIEffectView(this);
    }

    @Override
    public Effecter getEffecter() {
        return mEffecter;
    }

    @Override
    public View getPageAt(int index) {
        return getChildAt(index);
    }

    @Override
    public int getPageCount() {
        return getChildCount();
    }

    public void setEffecterType(int type) {
        mCurEffecterType = type;
        setEffecter(EffecterFactory.creator(type));
    }

    public int getEffecterType() {
        return mCurEffecterType;
    }

    @Override
    public void update() {
        invalidate();
    }

    @Override
    public int getCurPageIndex() {
        return mCurIndex;
    }

    private int getIndexofChild(View child) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            if (child == getChildAt(i)) {
                return i;
            }
        }
        return 0;
    }

    private boolean shouldDrawClips(PageTranformationInfo info) {
        return info.mPagedView != null &&
                info.mPagedView.getVisibility() == VISIBLE &&
                null != info.mClipViews &&
                null != info.mPagedShot;
    }

    private void drawClip(Canvas canvas, PageTranformationInfo parentInfo) {
        int count = parentInfo.mClipViews.size();
        if (count != 0) {
            if (parentInfo.bmDrawOrder) {
                for (int i = 0; i < count; i++) {
                    ClipView view = parentInfo.mClipViews
                            .get(i);
                    canvas.save();
                    canvas.concat(parentInfo.mPagedTransMatrix);
                    canvas.translate(view.mDisRect.left,
                            view.mDisRect.top);
                    canvas.concat(view.mTransMatrix);
                    PageTranformationInfo.SRect.set(0, 0,
                            view.getWidth(), view.getHeight());
                    PageTranformationInfo.SPaint
                            .setAlpha((int) (parentInfo.mAlpha
                                    * view.mAlpha * 255));
                    canvas.drawBitmap(
                            parentInfo.mPagedShot,
                            view.mDisRect, PageTranformationInfo.SRect,
                            PageTranformationInfo.SPaint);
                    canvas.restore();
                }
            } else {
                for (int i = count - 1; i >= 0; i--) {
                    ClipView view = parentInfo.mClipViews
                            .get(i);
                    canvas.save();
                    canvas.concat(parentInfo.mPagedTransMatrix);
                    canvas.translate(view.mDisRect.left,
                            view.mDisRect.top);
                    canvas.concat(view.mTransMatrix);
                    PageTranformationInfo.SRect.set(0, 0,
                            view.getWidth(), view.getHeight());
                    PageTranformationInfo.SPaint
                            .setAlpha((int) (parentInfo.mAlpha
                                    * view.mAlpha * 255));
                    canvas.drawBitmap(
                            parentInfo.mPagedShot,
                            view.mDisRect, PageTranformationInfo.SRect,
                            PageTranformationInfo.SPaint);
                    canvas.restore();
                }
            }
        }
    }

    /**
     * 切割动画，会绘制{@link PageTranformationInfo#mClipViews}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mScrollState != SCROLL_STATE_IDLE && !mEffecter.isEnd() && mEffecter.isNeedClip()) {
            System.out.println("shouldDrawClips(mSecondPageTranformation): "+shouldDrawClips(mSecondPageTranformation)
                                +"  shouldDrawClips(mFirstPageTranformation):  "+shouldDrawClips(mFirstPageTranformation));
            if (shouldDrawClips(mSecondPageTranformation)) {
                drawClip(canvas, mSecondPageTranformation);
            }
            if (shouldDrawClips(mFirstPageTranformation)) {
                drawClip(canvas, mFirstPageTranformation);
            }
        }
    }

    /**
     * 非切割动画，会绘制{@link #mFirstPageTranformation},
     * {@link #mSecondPageTranformation}
     */
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean more = false;
        if (mScrollState != SCROLL_STATE_IDLE && !mEffecter.isEnd()) {
            if (mEffecter.isNeedClip()) {
                return more;
            }
            if (DEBUG)
                Log.d(TAG, "drawChild(Canvas canvas, View child, long drawingTime) mEffecter");
            if (child == mFirstPageTranformation.mPagedView) {
                if (!mFirstPageTranformation.mPagedTransMatrix.isIdentity()
                        || mFirstPageTranformation.bmIsMatrixDirty) {
                    canvas.save();
                    canvas.concat(mFirstPageTranformation.mPagedTransMatrix);
                    more = super.drawChild(canvas, child, drawingTime);
                    canvas.restore();
                }
            } else if (child == mSecondPageTranformation.mPagedView) {
                if (!mSecondPageTranformation.mPagedTransMatrix.isIdentity()
                        || mSecondPageTranformation.bmIsMatrixDirty) {
                    canvas.save();
                    canvas.concat(mSecondPageTranformation.mPagedTransMatrix);
                    more = super.drawChild(canvas, child, drawingTime);
                    canvas.restore();
                }
            }
        } else {
            int index = getIndexofChild(child);
            if (index == mCurIndex) {
                more = super.drawChild(canvas, child, drawingTime);
            }
        }
        return more;
    }

    @Override
    public PageTranformationInfo getFirstTransformation() {
        return mFirstPageTranformation;
    }

    @Override
    public PageTranformationInfo getSecondTransformation() {
        return mSecondPageTranformation;
    }

    public void setPageListener(PageListener listen) {
        mPageListener = listen;
    }

    public void setPageLoop(boolean isLoop) {
        mLoop.set(isLoop);
    }

    public void setGestureOrientation(int orientation) {
        if (orientation == VERTICAL) {
            mOrientation = VERTICAL;
        } else {
            mOrientation = HORIENZON;
        }
    }

    public void setISlidePageExControl(ISlidePageExControl con) {
        mISlidePageExControl = con;
    }

    public void slideToPreviousPage() {
        int targetPage;
        targetPage = mCurIndex - 1;
        if (mLoop.get()) {
            if (targetPage < 0) {
                targetPage = getPageCount() - 1;
            } else if (targetPage > getPageCount() - 1) {
                targetPage = 0;
            }
        }
        targetPage = Math.max(0, Math.min(targetPage, getPageCount() - 1));
        int slidePara = mCurIndex - targetPage;

        if (mLoop.get() && (slidePara < 0)) {
            slidePara = 1;
        }
        mEffecter.start(mLoop.get());
        int totalDelta = (int) mDetalX;
        mScroller.startScroll(totalDelta, 0, slidePara * getWidth() - totalDelta, 0, ANI_DURATION);
        mDetalX = 0;
        setCurrentIndex(targetPage);
        setScrollState(SCROLL_STATE_SETTLING);
        mIsForbidenToDrag = false;
        mIsBeingDragged = false;
        invalidate();
    }

    public void slideToNextPage() {
        int targetPage;
        targetPage = mCurIndex + 1;
        if (mLoop.get()) {
            if (targetPage < 0) {
                targetPage = getPageCount() - 1;
            } else if (targetPage > getPageCount() - 1) {
                targetPage = 0;
            }
        }
        targetPage = Math.max(0, Math.min(targetPage, getPageCount() - 1));
        int slidePara = mCurIndex - targetPage;

        if (mLoop.get() && (slidePara > 0)) {
            slidePara = -1;
        }
        int totalDelta = (int) mDetalX;
        mEffecter.start(mLoop.get());
        mScroller.startScroll(totalDelta, 0, slidePara * getWidth() - totalDelta, 0, ANI_DURATION);
        mDetalX = 0;
        setCurrentIndex(targetPage);
        setScrollState(SCROLL_STATE_SETTLING);
        mIsForbidenToDrag = false;
        mIsBeingDragged = false;
        invalidate();
    }

    public void slideToDefaultPage() {
        if (DEBUG)
            Log.d(TAG, "slideToDefaultPage mDetalX=" + mDetalX);
        int totalDelta = (int) mDetalX;
        mEffecter.start(mLoop.get());
        mScroller.startScroll(totalDelta, 0, -totalDelta, 0, ANI_DURATION);
        mDetalX = 0;
        setScrollState(SCROLL_STATE_SETTLING);
        mIsForbidenToDrag = false;
        mIsBeingDragged = false;
        invalidate();
    }

    @Override
    public PageParams getPageParams() {
        return mPageParams;
    }

    @Override
    public void setScrollX(int value) {
        // do nothing
    }

    public void setWhenExIntercepterMaxDis(int dis) {
        mWhenExInterDefaultDis = dis;
    }

    private void WhenExIntercepterAnimaToDefaultPostion() {
        if (DEBUG)
            Log.v(TAG,
                    "WhenExIntercepterAnimaToDefaultPostion() mDetalX=" + mDetalX);
        mScroller.abortAnimation();
        final float curDetalX = mDetalX;
        int tar = mWhenExInterDefaultDis;
        if (mDetalX < 0) {
            tar = -mWhenExInterDefaultDis;
        }
        final float dex = tar - curDetalX;
        ValueAnimator ani = new ValueAnimator();
        ani.setFloatValues(0, 1);
        ani.setDuration(100);
        ani.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float factor = (Float) animation.getAnimatedValue();
                mDetalX = curDetalX + dex * factor;
                if (DEBUG)
                    Log.d(TAG, "WhenExIntercepterAnimaToDefaultPostion mDetalX=" + mDetalX);
                mEffecter.setFactor(getScrollFraction(mDetalX));
                invalidate();
            }
        });
        ani.start();
    }
}
