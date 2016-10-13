package com.android.slw.utils;

import android.util.Log;

/**
 * Created by liwu.shu on 2016/10/13.
 */
public class LogUtils {
    public static boolean isDebug = true;

    public static void d(String tag,String msg){
        if(isDebug){
            Log.i(tag,msg);
        }
    }
}
