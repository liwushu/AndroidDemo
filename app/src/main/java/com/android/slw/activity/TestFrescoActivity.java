package com.android.slw.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slw.R;
import com.android.slw.http.ImageLoadingDrawable;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class TestFrescoActivity extends BaseActivity implements View.OnClickListener {

    SimpleDraweeView simpleDraweeView1;
    SimpleDraweeView simpleDraweeView2;
    TextView tvStart;
    TextView tvStop;
    //Uri assetUrl2 = Uri.parse("http://10.128.208.25/tools/img/test_2m.gif");
    Uri assetUrl1 = Uri.parse("http://tcl-icloudcdn.tclclouds.com/tlauncher/20161012/08/04/33/0c46cb1c4d8342c297ecfc4e4502a71b.bmp");
    Uri assetUrl2 = Uri.parse("http://bena-test.s3.amazonaws.com/thumbnail/2016/10/12/6cddda878d204eb794df14ded26466f6.gif");

    @Override
    protected void setContentView() {
        Fresco.initialize(TestFrescoActivity.this);
        setContentView(R.layout.activity_test_fresco);
    }

    @Override
    protected void initViews() {
        simpleDraweeView1 = (SimpleDraweeView)findViewById(R.id.img1);
        simpleDraweeView2 = (SimpleDraweeView)findViewById(R.id.img2);
        simpleDraweeView1.setAspectRatio(1.33f);
        simpleDraweeView2.setAspectRatio(1.33f);
        tvStart = (TextView)findViewById(R.id.start);
        tvStop = (TextView)findViewById(R.id.stop);
        //assetUrl = Uri.parse("50234d40b6ffa3b585d96e23984352995de3393e54d6df3a16b89a276f3ca3af_1.gif");
        //invokeStart();
        getLauncherList();
        bindListener();

    }

    @Override
    protected void bindListener() {
        tvStart.setOnClickListener(this);
        tvStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.start:
                invokeStart();
                break;
            case R.id.stop:
                invokeStop();
                break;
        }
    }

    private void invokeStart() {
        Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
        GenericDraweeHierarchy hierarchy = simpleDraweeView1.getHierarchy();

                PipelineDraweeControllerBuilder builder =
                Fresco.newDraweeControllerBuilder()
                        .setAutoPlayAnimations(false); // 设置加载图片完成后是否直接进行播放
        builder.setControllerListener(new ControllerListener<ImageInfo>() {
            @Override
            public void onSubmit(String id, Object callerContext) {
                System.out.println("onSubmit");
            }

            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                if(animatable != null){
                    //animatable.start();
                }
            }

            @Override
            public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                System.out.println("onIntermediateImageSet");
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                System.out.println("onIntermediateImageFailed");
            }

            @Override
            public void onFailure(String id, Throwable throwable) {

            }

            @Override
            public void onRelease(String id) {
                System.out.println("onRelease");
            }
        });
        builder.setLowResImageRequest(ImageRequest.fromUri("http://10.128.208.25/tools/img/test3.png"));
        //builder.setImageRequest(ImageRequest.fromUri("http://bena-test.s3.amazonaws.com/thumbnail/2016/10/12/6cddda878d204eb794df14ded26466f6.gif"));
        DraweeController draweeController = builder.build();
                hierarchy.setProgressBarImage(new ImageLoadingDrawable(getApplicationContext()), null);
        simpleDraweeView1.setHierarchy(hierarchy);
        simpleDraweeView1.setController(draweeController);

        GenericDraweeHierarchy hierarchy2 = simpleDraweeView2.getHierarchy();
        DraweeController draweeController2 =
                Fresco.newDraweeControllerBuilder()
                        .setUri(assetUrl2)
                        .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                        .build();

        hierarchy2.setProgressBarImage(new ImageLoadingDrawable(getApplicationContext()), null);
        simpleDraweeView2.setHierarchy(hierarchy2);
        simpleDraweeView2.setController(draweeController2);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    URL url = new URL("http://bena-test.s3.amazonaws.com/thumbnail/2016/10/12/6cddda878d204eb794df14ded26466f6.gif");
//                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                    httpURLConnection.setRequestMethod("GET");
//                    httpURLConnection.connect();
//                    InputStream stream = httpURLConnection.getInputStream();
//                    long totalSize = 0;
//                    byte[] buffer = new byte[1024];
//                    while(true){
//                        int size = stream.read(buffer,0,1024);
//                        if(size <=0)
//                            break;
//                        totalSize += size;
//                        System.out.println("totalSize: "+totalSize);
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();

    }
    private void invokeStop(){
        GenericDraweeHierarchy hierarchy = simpleDraweeView1.getHierarchy();
        DraweeController draweeController =
                Fresco.newDraweeControllerBuilder()
                        .setUri(assetUrl1)
                        .setAutoPlayAnimations(false) // 设置加载图片完成后是否直接进行播放
                        .build();
        simpleDraweeView1.setController(draweeController);
        //hierarchy.setProgressBarImage(new ProgressBarDrawable(), null);
        //simpleDraweeView1.setHierarchy(hierarchy);

        GenericDraweeHierarchy hierarchy2 = simpleDraweeView2.getHierarchy();
        DraweeController draweeController2 =
                Fresco.newDraweeControllerBuilder()
                        .setUri(assetUrl2)
                        .setAutoPlayAnimations(false) // 设置加载图片完成后是否直接进行播放
                        .build();
        simpleDraweeView2.setController(draweeController2);
    }


    public void getLauncherList(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        //查找所有的
        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent, 0);
        System.out.println("resolveInfoList: "+resolveInfoList.size());
        for(ResolveInfo info:resolveInfoList){
            System.out.println("info: "+info.activityInfo.packageName);
        }
        //查找默认的
        ResolveInfo info = getPackageManager().resolveActivity(intent,PackageManager.MATCH_DEFAULT_ONLY);
        System.out.println("info: "+info);
    }
}
