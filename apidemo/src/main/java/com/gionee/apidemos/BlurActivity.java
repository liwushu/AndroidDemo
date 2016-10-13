package com.gionee.apidemos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.gionee.widget.blur.GioneeBlur;

public class BlurActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    ImageView content;
    SeekBar mDirectionSeekBar;
    int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blur);
        content = (ImageView) findViewById(R.id.content);
        mDirectionSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mDirectionSeekBar.setOnSeekBarChangeListener(this);
    }

    private Handler mHandler = new Handler();
    private boolean mIsBlurable = true;

    public void onClick(final View view) {
         content.setBackgroundResource(R.drawable.desktop);
         mDirectionSeekBar.setProgress(0);
        /*GioneeBlur blur = GioneeBlur.getInstance();
        blur.generateBlurBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.home_bg),
                getResources(), 5, new GioneeBlur.BitmapCallback() {

                    @Override
                    public void onComplete(Bitmap completeBmp) {
                        File file = new File("/data/misc/gionee/" + "image.png");
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        FileOutputStream out;
                        try {
                            out = new FileOutputStream(file);
                            if (completeBmp.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                                out.flush();
                                out.close();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                */
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        Log.d("cng", "progress = " + progress);
        this.progress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d("cng", "onStopTrackingTouch = ");
        GioneeBlur blur = GioneeBlur.getInstance();
        blur.generateBlurDrawable(R.drawable.desktop, getResources(), 1 + 4 * progress / 100,
                new GioneeBlur.DrawableCallback() {

                    @Override
                    public void onComplete(final Drawable completeDrawable) {
                        Log.d("GioneeBlur", "completeDrawable = " + (completeDrawable == null) + ",this = "
                                + this);
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
}
