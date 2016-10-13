package com.android.slw;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.slw.activity.MyTransformationActivity;
import com.android.slw.activity.TestArrActivity;
import com.android.slw.activity.TestBackgroundActivity;
import com.android.slw.activity.TestEffectActivity;
import com.android.slw.activity.TestFrescoActivity;
import com.android.slw.activity.TestHandlerActivity;
import com.android.slw.activity.TestLayoutActivity;
import com.android.slw.activity.TestRxAndroidActivity;
import com.android.slw.activity.TestScrollActivity;
import com.android.slw.activity.TestVideoActivity;
import com.android.slw.activity.TestViewVisible;
import com.android.slw.activity.TestVisible2;
import com.android.slw.activity.TestVolleyActivity;
import com.android.slw.activity.TestWidgetActivity;
import com.android.slw.adapter.AbstractAdapter;
import com.android.slw.adapter.ActListAdapter;
import com.android.slw.http.TestRetrofitActivity;
import com.android.slw.job.TestJobSchedule;
import com.android.slw.job.TestJobScheduleService;
import com.android.slw.pojo.ActListBean;
import com.android.slw.reflect.MainActivity1;
import com.android.slw.xutils.TestXutilsActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AbstractAdapter.OnClickListener {

    private final Class<?>[] actClasses = new Class[]{
            TestArrActivity.class, TestScrollActivity.class, TestJobSchedule.class,TestWidgetActivity.class,
            TestLayoutActivity.class,TestEffectActivity.class,MyTransformationActivity.class, TestVideoActivity.class,MainActivity1.class,
            TestRxAndroidActivity.class,TestBackgroundActivity.class,TestViewVisible.class,TestRetrofitActivity.class,TestVolleyActivity.class,
            TestXutilsActivity.class,TestVisible2.class,TestFrescoActivity.class, TestHandlerActivity.class,
    };

    RecyclerView recyclerView;
    ActListAdapter actListAdapter ;
    List<ActListBean> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initVies();
    }

    private List<ActListBean> getDataList(){
        dataList = new ArrayList<>();
        for(int i= 0;i<actClasses.length;i++){
            dataList.add(new ActListBean(actClasses[i].getSimpleName(),actClasses[i]));
        }
        return dataList;
    }

    private void initVies(){
        recyclerView = (RecyclerView)findViewById(R.id.act_list);
        actListAdapter = new ActListAdapter(this,getDataList());
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(actListAdapter);
        actListAdapter.setOnClickListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                System.out.println("newState: "+newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println("dx: "+dx+"   dy: "+dy);
            }
        });
    }

    @Override
    public void onItemClick(int position){
        jumpToActivity(dataList.get(position).actClass);
    }

    private void jumpToActivity(Class<?> actClass){
        Intent intent = new Intent();
        intent.setClass(this,actClass);
        startActivity(intent);
    }

}
