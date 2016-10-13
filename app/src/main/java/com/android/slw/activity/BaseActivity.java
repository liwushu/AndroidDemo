package com.android.slw.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.android.slw.SlwApplication;


/**
 * Created by liwu.shu on 2016/8/22.
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView();
        initViews();
        bindListener();
    }

    protected abstract void setContentView();

    protected abstract void initViews();

    protected abstract void bindListener();

    protected void logStr(String tag,String str){
        Log.d(tag,str);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
