package com.android.slw.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.android.slw.R;

/**
 * Created by liwu.shu on 2016/8/9.
 */
public class ActViewHolder extends RecyclerView.ViewHolder {
    public TextView itemView;

    public ActViewHolder(View rootView) {
        super(rootView);
        itemView = (TextView)rootView.findViewById(R.id.act_name);
    }
}
