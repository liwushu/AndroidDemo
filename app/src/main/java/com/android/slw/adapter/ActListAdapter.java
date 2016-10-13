package com.android.slw.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.slw.R;
import com.android.slw.pojo.ActListBean;
import com.android.slw.view.ActViewHolder;

import java.util.List;

/**
 * Created by liwu.shu on 2016/8/9.
 */
public class ActListAdapter extends AbstractAdapter<ActListBean,ActViewHolder> {

    Context mc;

    public ActListAdapter(Context mc,List<ActListBean> dataList){
        this.mc = mc;
        this.dataList=dataList;
    }

    @Override
    public ActViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mc).inflate(R.layout.recycler_item_layout,parent,false);
        return new ActViewHolder(root);
    }

    @Override
    public int getViewType(int position) {
        return 0;
    }


    @Override
    public void onBindData(ActViewHolder holder, int position) {
        ActListBean actListBean = getItemDate(position);
        holder.itemView.setText(actListBean.actName);
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                if(listener != null)
                    listener.onItemClick(position);
            }
        });
    }
}
