package com.android.slw.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.slw.R;

public class TestVisible2 extends BaseActivity implements View.OnClickListener{

    ViewGroup viewGroup;
    TextView tvShow,tvShowInvisible,tvShowGone;
    int[] resId = new int[]{R.drawable.test,R.drawable.test1,R.drawable.test2,R.drawable.test3,
            R.drawable.test4,R.drawable.test5,R.drawable.test6,R.drawable.test7};
    private int clickIndex;
    private Bitmap[] bitmaps = new Bitmap[resId.length];

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_test_view_visible);
    }

    @Override
    protected void initViews() {
        viewGroup = (ViewGroup)findViewById(R.id.view_group);
        tvShow = (TextView)findViewById(R.id.show);
        tvShowInvisible = (TextView)findViewById(R.id.show_invisible);
        tvShowGone = (TextView)findViewById(R.id.show_gone);
    }

    @Override
    protected void bindListener() {
        tvShow.setOnClickListener(this);
        tvShowInvisible.setOnClickListener(this);
        tvShowGone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.show:
                viewGroup.setVisibility(View.VISIBLE);
                break;
            case R.id.show_invisible:
                //viewGroup.setVisibility(View.INVISIBLE);
                invokeCreate();
                break;
            case R.id.show_gone:
                viewGroup.setVisibility(View.GONE);
                break;
        }
    }

    private void invokeCreate(){
        clickIndex++;
        if(clickIndex>=resId.length){
            clickIndex = clickIndex%resId.length;
        }
        bitmaps[clickIndex] = BitmapFactory.decodeResource(getResources(),resId[clickIndex]);
    }
}
