
package com.gionee.thirdpartyeffect.view;

import android.view.View;

/**
 * 子View 与 子View切割块的接口类
 */
public interface ICompatibleView {
    public float getX();

    public float getY();

    public int getWidth();

    public int getHeight();

    public void setVisibility(int vis);

    public void setAlpha(float alpha);

    public View getNotClipView();

    public int getRowNum();

    public int getColumnNum();
}
