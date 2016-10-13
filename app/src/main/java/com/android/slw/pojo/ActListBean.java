package com.android.slw.pojo;

/**
 * Created by liwu.shu on 2016/8/9.
 */
public class ActListBean {
    public Class<?> actClass;
    public String actName;

    public ActListBean(String actName,Class<?> actClass){
        this.actClass = actClass;
        this.actName = actName;
    }
}
