package com.android.slw.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liwu.shu on 2016/9/2.
 */
public class MyTransformationView extends View {

    Bitmap bitmap;
    Matrix matrix;
    Paint mPaint;


    public MyTransformationView(Context context) {
        this(context,null);
    }

    public MyTransformationView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyTransformationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        matrix = new Matrix();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        if(bitmap != null){
            canvas.drawBitmap(bitmap,matrix,mPaint);
        }

    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public Matrix getMatrix(){
        return matrix;
    }

}
