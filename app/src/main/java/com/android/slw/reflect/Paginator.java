package com.android.slw.reflect;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Beta-Tan
 * CreateTime: 16/9/8
 * Description: 一个分页类
 */
public class Paginator<T> implements Serializable {

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    private int page;
    private List<T> data;


}
