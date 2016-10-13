package com.android.slw.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.View;
import android.widget.TextView;

import com.android.slw.R;
import com.android.slw.view.MyTransformationView;

/**
 * Created by liwu.shu on 2016/9/2.
 */
public class MyTransformationActivity extends BaseActivity implements View.OnClickListener{

    MyTransformationView transformationView;
    TextView tvTrans,tvScale,tvRotate,tvSkew,tvSymmetry,tvScaleSmall;
    Bitmap bitmap;
    int midX,midY;
    float[] midMap = new float[2];

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_test_transformation);
    }

    @Override
    protected void initViews(){
        tvTrans = (TextView)findViewById(R.id.tv_trans);
        tvRotate = (TextView)findViewById(R.id.tv_rotate);
        tvScale = (TextView)findViewById(R.id.tv_scale);
        tvSkew = (TextView)findViewById(R.id.tv_skew);
        tvSymmetry = (TextView)findViewById(R.id.tv_symmetry);
        tvScaleSmall = (TextView)findViewById(R.id.tv_scale_small);
        transformationView = (MyTransformationView)findViewById(R.id.transformation);
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.test);
        midX = bitmap.getWidth()/2;
        midY = bitmap.getHeight()/2;
        transformationView.post(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight()*2, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap1);
                Matrix matrix = new Matrix();
                float[] values ={1f,0.0f,0.0f,0.0f,-1f,0.0f,0.0f,0.0f,1.0f};
                matrix.setValues(values);
                System.out.println("matrix:" + matrix.toString());
                Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                        matrix, true);
                canvas.drawBitmap(bitmap,0,0,null);
                canvas.drawBitmap(dstbmp,0,bitmap.getHeight(),null);
                canvas.setBitmap(null);
                transformationView.setBitmap(bitmap);
            }
        });
    }

    @Override
    protected void bindListener() {
        tvTrans.setOnClickListener(this);
        tvScale.setOnClickListener(this);
        tvRotate.setOnClickListener(this);
        tvSkew.setOnClickListener(this);
        tvSymmetry.setOnClickListener(this);
        tvScaleSmall.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.tv_trans:
                invokeTrans();
                break;
            case R.id.tv_rotate:
                invokeRotate();
                break;
            case R.id.tv_scale:
                invokeScale();
                break;
            case R.id.tv_skew:
                invokeSkew();
                break;
            case R.id.tv_symmetry:
                invokeSymmetry();
                break;
            case R.id.tv_scale_small:
                invokeScaleSmall();
                break;
        }
    }

    private void invokeTrans(){
        Matrix matrix = transformationView.getMatrix();
        matrix.preTranslate(10,10);
        refresh();
    }

    private void invokeScale(){
        Matrix matrix = transformationView.getMatrix();
        midMap[0] = midX;
        midMap[1] = midY;
        matrix.mapPoints(midMap);
        matrix.preScale(1.2f,1.2f, midMap[0],midMap[1]);
        refresh();
    }

    private void invokeScaleSmall(){
        Matrix matrix = transformationView.getMatrix();
        midMap[0] = midX;
        midMap[1] = midY;
        matrix.mapPoints(midMap);
        matrix.preScale(0.8f,0.8f,midMap[0],midMap[1]);
        refresh();
    }

    private void invokeRotate(){
        Matrix matrix = transformationView.getMatrix();
        midMap[0] = midX;
        midMap[1] = midY;
        matrix.mapPoints(midMap);
        matrix.preRotate(10);
        refresh();
    }

    private void invokeSkew(){
        Matrix matrix = transformationView.getMatrix();
        matrix.preSkew(0f,0.1f);
        refresh();
    }

    private void invokeSymmetry(){
        Matrix matrix = transformationView.getMatrix();
        float[] values ={-1f,0.0f,0.0f,0.0f,1f,0.0f,0.0f,0.0f,1.0f};
        matrix.setValues(values);
        //transformationView.setSMatrix(matrix);
        refresh();
    }

    private void refresh(){
        System.out.println("width: "+transformationView.getWidth()+"  height: "+transformationView.getHeight());
        transformationView.invalidate();
        System.out.println("matrix: "+transformationView.getMatrix());
    }
}
