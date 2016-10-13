package com.gionee.apidemos;

import com.gionee.widget.blur.GioneeBlur;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class TopLayerBlurActivty extends Activity {

    private ImageView mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blur_toplayer);
        mContent = (ImageView) findViewById(R.id.content);
    }

    private Handler mHandler = new Handler();

    private void blur(boolean deleteStatusBar) {
        GioneeBlur blur = GioneeBlur.getInstance();
        Bitmap top = GioneeBlur.getScreenshot(getWindowManager().getDefaultDisplay().getWidth(),
                getWindowManager().getDefaultDisplay().getHeight(), this, deleteStatusBar);
        Log.d("GioneeBlur", "top = " + (top == null));
        blur.generateBlurDrawable(top, getResources(), 3, new GioneeBlur.DrawableCallback() {

            @Override
            public void onComplete(final Drawable completeDrawable) {
                Log.d("GioneeBlur", "completePic = " + (completeDrawable == null) + ",this = " + this);
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        mContent.setBackground(completeDrawable);
                        mContent.invalidate();
                    }
                });
            }
        });
    }

    public void onClickStatusbar(View view) {
        blur(false);
    }

    public void onClick(View view) {
        blur(true);
    }

    public void onReset(View view) {
        mContent.setBackgroundResource(R.drawable.blur_bg);
    }
}
