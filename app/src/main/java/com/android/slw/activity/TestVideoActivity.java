package com.android.slw.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.android.slw.R;

import java.util.HashMap;

/**
 * Created by liwu.shu on 2016/9/7.
 */
public class TestVideoActivity extends BaseActivity implements View.OnClickListener {
    private static final String VIDEO_URL = "http://10.128.208.25/tools/androidsdk/gradle/test.mp4";
    VideoView video1,video2;
    Button btn1,btn2;
    ImageView image;
    ThumbnailUtils thumbnailUtils;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_video_layout);
    }

    @Override
    protected void initViews() {
        video1 = (VideoView)findViewById(R.id.video1);
        video2 = (VideoView)findViewById(R.id.video2);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        image = (ImageView)findViewById(R.id.image);
        createThumbnailBitmap();
        createVideoThumbnail(VIDEO_URL,300,300);
    }

    @Override
    protected void bindListener() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        video1.setVideoURI(Uri.parse(VIDEO_URL));
        video2.setVideoURI(Uri.parse(VIDEO_URL));
    }

    private void createThumbnailBitmap(){
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(VIDEO_URL,ThumbnailUtils.TARGET_SIZE_MINI_THUMBNAIL);
        System.out.println("bitmap: "+bitmap);
        if(bitmap != null){
            image.setImageBitmap(bitmap);
        }
    }

    private Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        if(bitmap != null){
            video1.setBackground(new BitmapDrawable(bitmap));
            video2.setBackground(new BitmapDrawable(bitmap));
        }
        return bitmap;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn1:
                video1.setBackground(null);
                video1.start();
                break;
            case R.id.btn2:
                video2.setBackground(null);
                video2.start();
                break;
        }
    }
}
