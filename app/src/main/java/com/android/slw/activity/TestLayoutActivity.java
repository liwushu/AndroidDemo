package com.android.slw.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.slw.R;

public class TestLayoutActivity extends BaseActivity implements View.OnClickListener{

    TextView tvFullScreen;
    TextView tvWrap;
    LinearLayout group;
    Button btnClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_test_layout);
    }

    @Override
    protected void initViews() {
        tvFullScreen = (TextView)findViewById(R.id.full_screen);
        tvWrap = (TextView)findViewById(R.id.tv_wrap);
        group = (LinearLayout)findViewById(R.id.group);
        btnClick = (Button)findViewById(R.id.click);
        group.measure(0,0);
        logStr("measure_group","measureWidth: "+group.getMeasuredWidth()+"  measureheight: "+group.getMeasuredHeight());
        tvFullScreen.measure(0,0);
        logStr("measure_tvFullScreen","measureWidth: "+tvFullScreen.getMeasuredWidth()+"  measureheight: "+tvFullScreen.getMeasuredHeight());
        tvWrap.measure(0,0);
        logStr("measure_tvWrap","measureWidth: "+tvWrap.getMeasuredWidth()+"  measureheight: "+tvWrap.getMeasuredHeight());

    }

    @Override
    protected void bindListener() {
        btnClick.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.click:
                invokeClick();
                break;
        }
    }

    private void invokeClick(){
        logStr("click","group_Width: "+group.getWidth()+"  height: "+group.getHeight());
        logStr("click","tvWrap_Width: "+tvWrap.getWidth()+"  height: "+tvWrap.getHeight());
    }
}
