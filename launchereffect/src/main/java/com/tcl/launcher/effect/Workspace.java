package com.tcl.launcher.effect;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.tcl.launcher.effect.page.ClipView;
import com.tcl.launcher.effect.page.EffectOrientation;
import com.tcl.launcher.effect.page.Effector;
import com.tcl.launcher.effect.page.EffectorVelocityTracker;
import com.tcl.launcher.effect.page.ISlidePageExControl;
import com.tcl.launcher.effect.page.IViewEffect;
import com.tcl.launcher.effect.page.PageListener;
import com.tcl.launcher.effect.page.PageTransformationInfo;
import com.tcl.launcher.effect.page.ScrollState;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by liwu.shu on 2016/8/25.
 */
public class Workspace extends ViewGroup implements IViewEffect {

    private static final int MIN_DISTANCE_FOR_FLING = 25; // dips
    private static final int CLOSE_ENOUGH = 2; // dp
    private static final int DEFAULT_MAX_DIS = 24; // dips;
    private static final int ANI_DURATION = 600;

    PageParams pageParams = new PageParams();
    private int mCurIndex=0;
    private boolean isBeingDragged;
    private boolean isUnableToDrag;
    private boolean isForbidenToDrag;
    //获取第一个点的index
    private int mActPointId;
    private float downX,downY,moveX,moveY,lastDownX,lastDownY;
    //滑动的最先距离
    private int mTouchSlop;
    //终止滑动的最小距离
    private int mCloseEnough ;
    private int mFlingDistance;
    private int mWhenExInterDefaultDis;

    private int mMinimumVelocity;
    private int mMaximumVelocity;

    //拖拽:垂直/水平
    private int mEffectOrientation = EffectOrientation.EFFECT_HORIZONTAL;
    private AtomicBoolean mLoop = new AtomicBoolean();

    private Scroller mScroller;
    int mScrollState = ScrollState.SCROLL_STATE_IDLE;

    private float mDeltaDiff;
    private float mAspectratio;
    private EffectorVelocityTracker mEffectVelocityTracker;
    private boolean mConsistencyInterceptVerifier = false;

    PageListener mPageListener;

    //拖拽效果
    private Effector mEffector;

    PageTransformationInfo firstPageTransformationInfo = new PageTransformationInfo();
    PageTransformationInfo secondPageTransformationInfo = new PageTransformationInfo();
    private ISlidePageExControl mISlidePageExControl;

    public Workspace(Context context) {
        this(context,null);
    }

    public Workspace(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Workspace(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setWillNotDraw(false);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        mLoop.set(true);
        float density = getResources().getDisplayMetrics().density;
        pageParams = new IViewEffect.PageParams();
        isBeingDragged = false;
        isUnableToDrag = false;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mTouchSlop = viewConfiguration.getScaledTouchSlop();
        mScroller = new Scroller(getContext());
        mCloseEnough = (int) (CLOSE_ENOUGH*density);
        mEffectVelocityTracker = new EffectorVelocityTracker();
        mFlingDistance = (int) (MIN_DISTANCE_FOR_FLING * density);
        mCloseEnough = (int) (CLOSE_ENOUGH * density);
        mWhenExInterDefaultDis = (int) (DEFAULT_MAX_DIS * density);
        mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    }

    public  void setISlidePageExControl(ISlidePageExControl slidePgeExControl){
        this.mISlidePageExControl = slidePgeExControl;
    }

    @Override
    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        setMeasuredDimension(getDefaultSize(0,widthMeasureSpec),getDefaultSize(0,heightMeasureSpec));
        int measureWidth = getMeasuredWidth();
        int measureHeight = getMeasuredHeight();
        if(pageParams != null){
            pageParams.mWidth = measureWidth;
            pageParams.mHeight = measureHeight;
        }
        mAspectratio = measureWidth * 1.0f / measureHeight;
        int childWidth = measureWidth - getPaddingLeft() - getPaddingRight();
        int childHeight = measureHeight - getPaddingTop() - getPaddingBottom();
        int count = getChildCount();
        if(count>0){
            for(int i=0;i<count;i++){
                View view = getChildAt(i);
                if(view.getVisibility() != View.GONE){
                    view.measure(MeasureSpec.makeMeasureSpec(childWidth,MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(childHeight,MeasureSpec.EXACTLY));
                }
            }
        }
    }

    @Override
    public void onLayout(boolean isChanged,int l,int t,int r,int b){
        int left;
        int offset = getWidth();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int count = getChildCount();
        for(int i = 0;i<count;i++){
            View child = getChildAt(i);
            left = offset+paddingLeft;
            child.layout(left,paddingTop,left+child.getMeasuredWidth(),paddingTop+child.getMeasuredHeight());
        }

        if(count>0&&mCurIndex>=0&&mCurIndex<count){
            View child = getChildAt(mCurIndex);
            child.setTranslationX(-pageParams.mWidth);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        final int action = event.getAction()&MotionEvent.ACTION_MASK;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                mActPointId = event.getPointerId(0);
                downX = lastDownX = (int) event.getX();
                downY = lastDownY = (int) event.getY();
                mScroller.computeScrollOffset();
                if(mScrollState == ScrollState.SCROLL_STATE_IDLE
                        && (mScroller.getFinalX()-mScroller.getCurrX()>mCloseEnough)){
                    mScroller.abortAnimation();
                    isBeingDragged = true;
                    mEffector.start(mLoop.get());
                }else{
                    isBeingDragged = false;
                    completeScroll();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                final int actPointIndex = mActPointId;
                float[] diff = new float[1];
                if(actPointIndex == -1){
                    break;
                }
                isBeingDragged = isDetermineDrag(event,diff);
                if(isBeingDragged){
                    if(performDrag(diff[0])){
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                break;
        }
        return isBeingDragged;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(getPageCount()<=1){
            return true;
        }
        if(mConsistencyInterceptVerifier){
            int action = event.getAction();
            if(action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL){
                mConsistencyInterceptVerifier = false;
                isUnableToDrag = false;
                mActPointId = -1;
                mEffectVelocityTracker.recycle();
            }
            return true;
        }
        if ((null != mISlidePageExControl && mISlidePageExControl
                .isBlockSlidePage())) {
            if (!mConsistencyInterceptVerifier) {
                int action = event.getAction();
                if (action != MotionEvent.ACTION_UP && action != MotionEvent.ACTION_CANCEL) {
                    mConsistencyInterceptVerifier = true;
                }
                isForbidenToDrag = true;
                // mIsBeingDragged = false;
                WhenExIntercepterAnimaToDefaultPostion();
            }
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
            return false;
        }

        boolean needsInvalidate = false;
        mEffectVelocityTracker.addVelocityTrack(event);
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                mScroller.abortAnimation();
                isBeingDragged = true;
                mEffector.start(mLoop.get());
                setScrollState(ScrollState.SCROLL_STATE_DRAGGING);
                // Remember where the motion event started
                lastDownX = downX = event.getX();
                lastDownY = downY = event.getY();
                mActPointId = event.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                if (!isBeingDragged) {
                    final int pointerIndex = event.findPointerIndex(mActPointId);
                    final int pointCount =event.getPointerCount();
                    if (pointerIndex == -1 || pointerIndex > pointCount - 1) {
                        break;
                    }
                    final float x = event.getX(pointerIndex);
                    final float xDiff = Math.abs(x - lastDownX);
                    final float y = event.getY(pointerIndex);
                    final float yDiff = Math.abs(y - lastDownY);
                    if (mEffectOrientation == EffectOrientation.Effect_VERTICAL) {
                        if (yDiff > mTouchSlop || yDiff > xDiff) {
                            isBeingDragged = true;
                            mEffector.start(mLoop.get());
                            lastDownY = y - downX > 0 ? downY
                                    + mTouchSlop : downY - mTouchSlop;
                            setScrollState(ScrollState.SCROLL_STATE_DRAGGING);
                        }
                    } else {
                        if (xDiff > mTouchSlop || xDiff > yDiff) {
                            isBeingDragged = true;
                            mEffector.start(mLoop.get());
                            lastDownY = x - downX > 0 ? downX
                                    + mTouchSlop : downX - mTouchSlop;
                            setScrollState(ScrollState.SCROLL_STATE_DRAGGING);
                        }
                    }
                }

                if (isBeingDragged && mActPointId != -1) {
                    final int activePointerIndex = event
                            .findPointerIndex(mActPointId);
                    if (activePointerIndex == -1) {
                        break;
                    }
                    final float x = lastDownX = event.getX(activePointerIndex);
                    final float y = lastDownY = event.getY(activePointerIndex);
                    if (mEffectOrientation == EffectOrientation.Effect_VERTICAL) {
                        needsInvalidate = performDrag(y);
                    } else {
                        needsInvalidate = performDrag(x);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isBeingDragged) {
                    final VelocityTracker velocityTracker = mEffectVelocityTracker.getVelocityTracker();
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity;
                    int totalDelta;
                    final int activePointerIndex = event
                            .findPointerIndex(mActPointId);
                    final float x = event.getX(activePointerIndex);
                    final float y = event.getY(activePointerIndex);
                    if (mEffectOrientation == EffectOrientation.Effect_VERTICAL) {
                        totalDelta = (int) (y - downY);
                        totalDelta *= mAspectratio;
                        initialVelocity = (int) velocityTracker
                                .getYVelocity(mActPointId);
                    } else {
                        totalDelta = (int) (x - downX);
                        initialVelocity = (int) velocityTracker
                                .getXVelocity(mActPointId);
                    }
                    totalDelta = (int) reviseDeltaX(totalDelta);
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
                    System.out.println("startX: "+totalDelta+"   dx: "+(slidePara * getWidth() - totalDelta));
                    mScroller
                            .startScroll(totalDelta, 0, slidePara * getWidth() - totalDelta, 0, ANI_DURATION);
                    setCurrentIndex(nextPage);
                    setScrollState(ScrollState.SCROLL_STATE_SETTLING);
                    mActPointId = -1;
                    endDrag();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (isBeingDragged) {
                    mActPointId = -1;
                    endDrag();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = event.getActionIndex();
                final float x = event.getX(index);
                lastDownX = x;
                mActPointId = event.getPointerId(index);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                lastDownX = event.getX(event.findPointerIndex(mActPointId));
                break;
        }
        if (needsInvalidate) {
            postInvalidate();
        }
        return true;
    }

    /**
     * 切割动画，会绘制{@link PageTransformationInfo#mPagedView}
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mScrollState != ScrollState.SCROLL_STATE_IDLE && !mEffector.isEnd() && mEffector.isNeedClip()) {
            System.out.println("shouldDrawClips(secondPageTransformationInfo): "+shouldDrawClips(secondPageTransformationInfo)+
                    "   shouldDrawClips(firstPageTransformationInfo): "+shouldDrawClips(firstPageTransformationInfo));
            if (shouldDrawClips(secondPageTransformationInfo)) {
                drawClip(canvas, secondPageTransformationInfo);
            }
            if (shouldDrawClips(firstPageTransformationInfo)) {
                drawClip(canvas, firstPageTransformationInfo);
            }
        }
    }

    /**
     * 非切割动画，会绘制,
     */
    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean more = false;

        if (mScrollState != ScrollState.SCROLL_STATE_IDLE && !mEffector.isEnd()) {
            if (mEffector.isNeedClip()) {
                return more;
            }
            if (child == firstPageTransformationInfo.getPagedView()) {
                System.out.println("mFirstPageTranformation: "+firstPageTransformationInfo.getPagedTransMatrix());
                if (!firstPageTransformationInfo.getPagedTransMatrix().isIdentity()
                        || firstPageTransformationInfo.isBmIsMatrixDirty()) {
                    canvas.save();
                    canvas.concat(firstPageTransformationInfo.getPagedTransMatrix());
                    more = super.drawChild(canvas, child, drawingTime);
                    canvas.restore();
                }
            } else if (child == secondPageTransformationInfo.getPagedView()) {
                System.out.println("secondPageTransformationInfo: "+secondPageTransformationInfo.getPagedTransMatrix());
                if (!secondPageTransformationInfo.getPagedTransMatrix().isIdentity()
                        || secondPageTransformationInfo.isBmIsMatrixDirty()) {
                    canvas.save();
                    canvas.concat(secondPageTransformationInfo.getPagedTransMatrix());
                    more = super.drawChild(canvas, child, drawingTime);
                    canvas.restore();
                }
            }
        } else {
            int index = getIndexOfChild(child);
            if (index == mCurIndex) {
                more = super.drawChild(canvas, child, drawingTime);
            }
        }
        return more;
    }

    private int getIndexOfChild(View child) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            if (child == getChildAt(i)) {
                return i;
            }
        }
        return 0;
    }

    private boolean shouldDrawClips(PageTransformationInfo info) {
        return info.getPagedView() != null &&
                info.getPagedView().getVisibility() == VISIBLE &&
                null != info.getClipViews() &&
                null != info.getPagedShot();
    }

    private void drawClip(Canvas canvas, PageTransformationInfo parentInfo) {
        int count = parentInfo.getClipViews().size();
        if (count != 0) {
            System.out.println("bmDrawOrder: "+parentInfo.isBmDrawOrder());
            if (parentInfo.isBmDrawOrder()) {
                for (int i = 0; i < count; i++) {
                    ClipView view = parentInfo.getClipViews()
                            .get(i);
                    canvas.save();
                    canvas.concat(parentInfo.getPagedTransMatrix());
                    canvas.translate(view.getDisRect().left,
                            view.getDisRect().top);
                    canvas.concat(view.getTransMatrix());
                    PageTransformationInfo.SRect.set(0, 0,
                            view.getWidth(), view.getHeight());
                    PageTransformationInfo.SPaint
                            .setAlpha((int) (parentInfo.getAlpha()
                                    * view.getAlpha() * 255));
                    canvas.drawBitmap(
                            parentInfo.getPagedShot(),
                            view.getDisRect(), PageTransformationInfo.SRect,
                            PageTransformationInfo.SPaint);
                    canvas.restore();
                }
            } else {
                for (int i = count - 1; i >= 0; i--) {
                    ClipView view = parentInfo.getClipViews()
                            .get(i);
                    canvas.save();
                    canvas.concat(parentInfo.getPagedTransMatrix());
                    canvas.translate(view.getDisRect().left,
                            view.getDisRect().top);
                    canvas.concat(view.getTransMatrix());
                    PageTransformationInfo.SRect.set(0, 0,
                            view.getWidth(), view.getHeight());
                    PageTransformationInfo.SPaint
                            .setAlpha((int) (parentInfo.getAlpha()
                                    * view.getAlpha() * 255));
                    canvas.drawBitmap(
                            parentInfo.getPagedShot(),
                            view.getDisRect(), PageTransformationInfo.SRect,
                            PageTransformationInfo.SPaint);
                    canvas.restore();
                }
            }
        }
    }


    private boolean isDetermineDrag(MotionEvent event,float[] diff){
        final int actPointId = mActPointId;
        int pointIndex = event.findPointerIndex(actPointId);
        moveX =  event.getX(pointIndex);
        moveY =  event.getY(pointIndex);
        float deltaX = moveX - lastDownX;
        float deltaY = moveY - lastDownY;
        float diffX = Math.abs(deltaX);
        float diffY = Math.abs(deltaY);

        if(mEffectOrientation == EffectOrientation.EFFECT_HORIZONTAL){
            if(diffX>mTouchSlop){
                diff[0] = diffX;
                if(diffX>diffY){
                    isBeingDragged = true;
                    mEffector.start(mLoop.get());
                    setScrollState(ScrollState.SCROLL_STATE_DRAGGING);
                    lastDownX = deltaX>0?downX+mTouchSlop:downX-mTouchSlop;
                }else{
                    isUnableToDrag = false;
                }
            }
        }else if(mEffectOrientation == EffectOrientation.Effect_VERTICAL){
            if(deltaY>mTouchSlop){
                diff[0] = diffY;
                if(diffY>diffX){
                    isBeingDragged = true;
                    mEffector.start(mLoop.get());
                    setScrollState(ScrollState.SCROLL_STATE_DRAGGING);
                    lastDownY = deltaY>0?downY+mTouchSlop:downY-mTouchSlop;
                }else
                    isUnableToDrag = false;
            }
        }
        return isBeingDragged;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActPointId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            lastDownX = ev.getX(newPointerIndex);
            mActPointId = ev.getPointerId(newPointerIndex);
            mEffectVelocityTracker.clear();
        }
    }


    private void endDrag() {
        isBeingDragged = false;
        isUnableToDrag = false;
        mEffectVelocityTracker.recycle();
    }

    private boolean performDrag(float x) {
        float deltaX = getDelta(x);
        mEffector.setFactor(getScrollFraction(deltaX));
        return true;
    }


    private float getScrollFraction(float deltaX) {
        float fraction = deltaX / getWidth();
        if (null != mPageListener) {
            mPageListener.onPageSlideFraction(fraction);
        }
        return fraction;
    }

    private float reviseDeltaX(float deltaX) {
        if (!mLoop.get()) {
            int compareValue = getWidth() / 2;
            if (mCurIndex == 0 && deltaX > 0) {
                deltaX = Math.min(compareValue, deltaX);
            } else if (mCurIndex == getPageCount() - 1 && deltaX < 0) {
                deltaX = Math.max(-compareValue, deltaX);
            }
        }
        return deltaX;
    }

    private float getDelta(float x) {
        float deltaX;
        if (mEffectOrientation == EffectOrientation.Effect_VERTICAL) {
            deltaX = x - downY;
            deltaX = deltaX * mAspectratio;
        } else {
            deltaX = x - downX;
        }
        mDeltaDiff = deltaX = reviseDeltaX(deltaX);
        return deltaX;
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


    private void completeScroll(){
        boolean needPopulate = mScrollState == ScrollState.SCROLL_STATE_SETTLING;
        if (needPopulate) {
            mScroller.abortAnimation();
            setScrollState(ScrollState.SCROLL_STATE_IDLE);
        }
    }


    private void setScrollState(int state){
        mScrollState = state;
    }

    private void setCurrentIndex(int item) {
        if (mCurIndex == item) {
            return;
        }
        if (mEffector.isNeedClip()) {
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

    private void WhenExIntercepterAnimaToDefaultPostion() {
        mScroller.abortAnimation();
        final float curDeltaX = mDeltaDiff;
        int tar = mWhenExInterDefaultDis;
        if (mDeltaDiff < 0) {
            tar = -mWhenExInterDefaultDis;
        }
        final float dex = tar - curDeltaX;
        ValueAnimator ani = new ValueAnimator();
        ani.setFloatValues(0, 1);
        ani.setDuration(100);
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float factor = (Float) animation.getAnimatedValue();
                mDeltaDiff = curDeltaX + dex * factor;
                mEffector.setFactor(getScrollFraction(mDeltaDiff));
                invalidate();
            }
        });
        ani.start();
    }


    @Override
    public void setEffector(Effector ef) {
        this.mEffector = ef;
        if (mEffector.isNeedClip()) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        } else {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        mEffector.setViewEffect(this);
    }

    public void setPageLoop(boolean isLoop) {
        mLoop.set(isLoop);
    }

    @Override
    public Effector getEffector() {
        return mEffector;
    }

    @Override
    public int getCurPageIndex() {
        return mCurIndex;
    }

    @Override
    public View getPageAt(int index) {
        return index<getChildCount()? getChildAt(index):null;
    }

    @Override
    public int getPageCount() {
        return getChildCount();
    }

    @Override
    public void update() {
        invalidate();
    }

    @Override
    public PageTransformationInfo getFirstTransformation() {
        return firstPageTransformationInfo;
    }

    @Override
    public PageTransformationInfo getSecondTransformation() {
        return secondPageTransformationInfo;
    }

    @Override
    public PageParams getPageParams() {
        return pageParams;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int newX = mScroller.getCurrX();
            float fraction = getScrollFraction(newX);
            mEffector.setFactor(fraction);
            postInvalidate();
        } else {
            if (!isBeingDragged) {
                if (mEffector != null &&!mEffector.isEnd()) {
                    mEffector.end();
                    mEffector.onPageIndexChange();
                }
                completeScroll();
            }
        }
    }
}
