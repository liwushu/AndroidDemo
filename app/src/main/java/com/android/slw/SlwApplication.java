package com.android.slw;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by liwu.shu on 2016/9/21.
 */
public class SlwApplication extends Application {

    private RefWatcher refWatcher;
    private Bitmap bitmap;

    public static RefWatcher getRefWatcher(Context context){
        SlwApplication application = (SlwApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }

    public void setBitmap(Bitmap saveBitmap){
        this.bitmap = saveBitmap;
    }
}
