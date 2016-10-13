
package com.gionee.thirdpartyeffect.utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

/**
 * 转化View为bitmap
 */
public class BitampUtils {

    /**
     * 转化View为bitmap
     * 
     * @param v 待转化的view
     * @return Bitamp 返回的 View 的cacheView
     */
    public static Bitmap getViewBitmap(View v) {
        long starttime = System.currentTimeMillis();
        v.clearFocus();
        v.setPressed(false);

        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            Log.e("BitampUtils", "failed getViewBitmap(" + v + ")",
                    new RuntimeException());
            return null;
        }

        long used = System.currentTimeMillis() - starttime;
        Log.d("BitampUtils", "time used=" + used);
        return cacheBitmap;
    }
}
