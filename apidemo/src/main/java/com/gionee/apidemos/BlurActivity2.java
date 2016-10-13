package com.gionee.apidemos;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import com.gionee.widget.blur.GioneeBlur;

public class BlurActivity2 extends Activity {
    ImageView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        content = new ImageView(this);
        content.setBackgroundResource(R.drawable.blur_bg);
        setContentView(content);
    }

    private Handler mHandler = new Handler();

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("GioneeBlur", "onResume");
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                GioneeBlur blur = GioneeBlur.getInstance();
                blur.generateBlurDrawable(content, getResources(), 51, new GioneeBlur.DrawableCallback() {

                    @Override
                    public void onComplete(final Drawable completeDrawable) {
                        Log.d("GioneeBlur", "completePic = " + (completeDrawable == null) + ",this = " + this);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                content.setBackground(completeDrawable);
                                content.invalidate();
                            }
                        });
                    }
                });
            }
        }, 200);
    }
}
