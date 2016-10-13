package com.android.slw.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by liwu.shu on 2016/8/9.
 */
public abstract class AbstractAdapter<E,V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    protected List<E> dataList;

//    public AbstractAdapter(List<E> dataList){
//        this.dataList = dataList;
//    }

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(V holder, int position) {
        onBindData(holder,position);
    }

    @Override
    public int getItemCount() {
        return dataList == null ?0:dataList.size();
    }

    @Override
    public int getItemViewType(int position){
        return getViewType(position);
    }

    protected E getItemDate(int position){
        return dataList.get(position);
    }

    public abstract V getViewHolder(ViewGroup parent, int viewType);
    public abstract int getViewType(int position);
    public abstract void onBindData(V holder,int position);

    protected OnClickListener listener;

    public void setOnClickListener(OnClickListener  listener){
        this.listener = listener;
    }

    public static interface OnClickListener{
        void onItemClick(int position);
    }
}
