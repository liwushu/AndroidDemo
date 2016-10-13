package com.android.slw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Scroller;

/**
 * Created by liwu.shu on 2016/8/10.
 */
public class ScrollView extends ViewGroup {

    Context mc;
    Scroller mScroller;
    int touchSlop;
    int touchDownX,mLastDownX;
    int leftBorder,rightBorder;

    public ScrollView(Context context){
        this(context,null);
    }

    public ScrollView(Context context, AttributeSet attr){
        this(context,attr,0);
    }

    public ScrollView(Context context,AttributeSet attr,int defStyle){
        super(context,attr,defStyle);
        init(context);
    }

    private void init(Context mc){
        this.mc = mc;
        mScroller = new Scroller(mc);
        ViewConfiguration configuration = ViewConfiguration.get(mc);
        touchSlop = configuration.getScaledPagingTouchSlop();
    }


    @Override
    public void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int count = getChildCount();
        for(int i=0;i<count;i++){
            View child = getChildAt(i);
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    public void onLayout(boolean isChange,int l,int t,int r,int b){
        if(isChange){
            int count = getChildCount();
            for(int i=0;i<count;i++) {
                View child = getChildAt(i);
                int left = i*child.getMeasuredWidth();
                int top = 0;
                int right = (i+1)*child.getMeasuredWidth();
                int bottom = child.getMeasuredHeight();
                child.layout(left,top,right,bottom);
            }
            if(count>0){
                leftBorder = getChildAt(0).getLeft();
                rightBorder = getChildAt(count-1).getRight();
            }
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        int action = event.getAction()&MotionEvent.ACTION_MASK;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                touchDownX = mLastDownX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                touchDownX = (int)event.getX();
                System.out.println("Math.abs(touchDownX-mLastDownX): "+(Math.abs(touchDownX-mLastDownX))+"  touchSlop: "+touchSlop);
                if(Math.abs(touchDownX-mLastDownX)>touchSlop)
                    return true;
                mLastDownX = touchDownX;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction()&MotionEvent.ACTION_MASK;
        switch (action){
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_MOVE:
                touchDownX = (int) event.getX();
                int deltaX = mLastDownX-touchDownX;
                if(deltaX+getScrollX()<leftBorder){
                    scrollTo(leftBorder,0);
                    return true;
                }else if(deltaX+getScrollX()+getWidth()>rightBorder){
                    scrollTo(rightBorder-getWidth(),0);
                    return true;
                }
                scrollBy(deltaX,0);
                System.out.println("deltaX: "+deltaX);
                mLastDownX = touchDownX;
                break;
            case MotionEvent.ACTION_UP:
                int targetIndex = (getScrollX()+getWidth()/2)/getWidth();
                int dx = targetIndex*getWidth()-getScrollX();
                mScroller.startScroll(getScrollX(),0,dx,0);
                invalidate();
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll(){
        System.out.println("computScroll: "+mScroller.computeScrollOffset());
        if(mScroller != null) {
            if(mScroller.computeScrollOffset()){
                scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
                invalidate();
            }
        }
    }
}
