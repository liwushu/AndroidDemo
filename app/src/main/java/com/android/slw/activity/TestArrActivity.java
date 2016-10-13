package com.android.slw.activity;

import android.animation.AnimatorSet;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import com.android.slw.R;
import com.tcl.launcher.dialog.ApplyThemeDialog;
import com.tcl.launcher.dialog.DeleteConfirmDialog;
import com.tcl.launcher.dialog.DeleteDialog;
import com.tcl.launcher.dialog.NetworkErrorDialog;

public class TestArrActivity extends Activity implements View.OnClickListener {

    Button dialog1,dialog2,dialog3,dialog4,dialog5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_arr);
        initViews();
        bindListener();
    }

    private void initViews(){
        dialog1 = (Button)findViewById(R.id.dialog1);
        dialog2 = (Button)findViewById(R.id.dialog2);
        dialog3 = (Button)findViewById(R.id.dialog3);
        dialog4 = (Button)findViewById(R.id.dialog4);
        dialog5 = (Button)findViewById(R.id.dialog5);
    }

    private void bindListener(){
        dialog1.setOnClickListener(this);
        dialog2.setOnClickListener(this);
        dialog3.setOnClickListener(this);
        dialog4.setOnClickListener(this);
        dialog5.setOnClickListener(this);
    }


    @Override
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.dialog1:
                invokeDialog1();
                break;
            case R.id.dialog2:
                invokeDialog2();
                break;
            case R.id.dialog3:
                invokeDialog3();
                break;
            case R.id.dialog4:
                invokeDialog4();
                break;
            case R.id.dialog5:
                invokeAnimation();
                break;
        }
    }

    private void invokeDialog1(){
        ApplyThemeDialog dialog = new ApplyThemeDialog(TestArrActivity.this);
        dialog.show();
    }

    private void invokeDialog2(){
        DeleteDialog dialog = new DeleteDialog(TestArrActivity.this);
        dialog.show();
    }

    private void invokeDialog3(){
        NetworkErrorDialog dialog = new NetworkErrorDialog(TestArrActivity.this);
        dialog.show();
    }

    private void invokeDialog4(){
        DeleteConfirmDialog dialog = new DeleteConfirmDialog(TestArrActivity.this);
        dialog.show();
    }

    private void invokeAnimation(){
        ScaleAnimation scaleAnimation1 = new ScaleAnimation(1f,1.5f,1f,1.5f,0.5f,0.5f);
        scaleAnimation1.setDuration(1000);
        scaleAnimation1.setFillAfter(true);

        ScaleAnimation scaleAnimation2 = new ScaleAnimation(1f,1.5f,1f,1.5f,0.5f,0.5f);
        scaleAnimation2.setDuration(1000);
        scaleAnimation2.setFillAfter(true);

        ScaleAnimation scaleAnimation4 = new ScaleAnimation(1f,0.1f,1f,0.1f,0.5f,0.5f);
        scaleAnimation4.setDuration(1000);
        scaleAnimation4.setStartOffset(500);
        scaleAnimation4.setFillAfter(true);

        AnimationSet animatorSet = new AnimationSet(true);
        animatorSet.addAnimation(scaleAnimation1);
        animatorSet.addAnimation(scaleAnimation2);
        animatorSet.addAnimation(scaleAnimation4);
        animatorSet.setFillAfter(true);
        dialog4.startAnimation(animatorSet);
    }

}
