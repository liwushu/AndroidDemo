package com.android.slw.http;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slw.R;
import com.android.slw.activity.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;


public class TestRetrofitActivity extends BaseActivity implements View.OnClickListener{
    private static final String UPDATE_URL =  "\"http://tlauncher-api.tclclouds.com/tlauncher-api/api/\"";

    TextView tvShowMessage,tvStart,tvUpdate,tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_test_retrofit);
    }

    @Override
    protected void initViews() {
        tvShowMessage = (TextView)findViewById(R.id.show_message);
        tvStart = (TextView)findViewById(R.id.start);
        tvUpdate = (TextView)findViewById(R.id.update);
        tvTest = (TextView)findViewById(R.id.test);
    }

    @Override
    protected void bindListener() {
        tvStart.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);
        tvTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        int id = v.getId();
        switch (id){
            case R.id.start:
                invokeStart();
                break;
            case R.id.update:
                invokeUpdate();
                break;
            case R.id.test:
                invokeTest();
                break;
        }
    }

    private void invokeTest(){
        int left = tvTest.getLeft()+10;
        int top = tvTest.getTop()+10;
        int right = left+tvTest.getWidth();
        int bottom = top+tvTest.getWidth();

        tvTest.layout(left,top,right,bottom);
    }

    private void invokeStart(){
        Map<String,String>params = new HashMap<>();
        params.put("subscribe","false");
        HttpManager<HttpService>httpManager = new HttpManager(HttpService.class);
        httpManager.invokeLoad(new Subscriber<HttpBean<List<NewsCategory>>>() {
            @Override
            public void onCompleted() {
                Toast.makeText(TestRetrofitActivity.this,"onCompleted",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(TestRetrofitActivity.this,"onError",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpBean<List<NewsCategory>> listHttpBean) {
                StringBuilder sb = new StringBuilder();
                sb.append(listHttpBean.code);
                sb.append("\n");
                sb.append(listHttpBean.msg);
                sb.append("\n");
                for(int i=0;i<listHttpBean.data.size();i++){
                    sb.append(listHttpBean.data.get(i).toString());
                }
                tvShowMessage.setText(sb.toString());
            }
        },params,"loadNewsCategory");
    }

    private void invokeUpdate(){
        HttpManager<HttpBean<String>>httpManager = new HttpManager(HttpService.class);
        Map<String,String> params = new HashMap<>();
        params.put("inner_package_name", "com.tcl.mie.launcher.lite");
        params.put("inner_version_code", "1");
        httpManager.invokeLoad(new Subscriber<HttpBean<String>>() {
            @Override
            public void onCompleted() {
                Toast.makeText(TestRetrofitActivity.this,"onCompleted",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(TestRetrofitActivity.this,"onError",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(HttpBean<String> listHttpBean) {
                StringBuilder sb = new StringBuilder();
                sb.append(listHttpBean.code);
                sb.append("\n");
                sb.append(listHttpBean.msg);
                sb.append("\n");
                sb.append(listHttpBean.data);
                tvShowMessage.setText(sb.toString());
            }
        },params,"loadUpdateInfo");
    }
}
