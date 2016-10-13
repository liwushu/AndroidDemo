package com.android.slw.activity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.slw.R;
import com.android.slw.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestHandlerActivity extends BaseActivity {

    @BindView(R.id.ui_handler)
    TextView uiHandler;
    @BindView(R.id.thread_handler)
    Button threadHandler;

    Handler uHandler;
    Handler tHandler;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_test_handler);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                tHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg){
                        switch(msg.what){
                            case 3:
                                LogUtils.d("flying","3: "+Thread.currentThread().getName());
                                break;
                        }
                    }
                };
                Looper.loop();
            }
        }).start();

        uHandler = new Handler(){

            @Override
            public void handleMessage(Message msg){
                switch(msg.what){
                    case 1:
                        LogUtils.d("flying","currentThread:"+Thread.currentThread().getName().toString());
                        break;
                    case 2:
                        break;
                }
            }
        };
    }

    @Override
    protected void bindListener() {

    }

    @OnClick({R.id.thread_handler,R.id.ui_handler})
    public void onClick(View view){
        int id = view.getId();
        LogUtils.d("flying","onClick");
        switch (id){
            case R.id.ui_handler:
                Message msg = Message.obtain();
                msg.what = 1;
                uHandler.sendMessage(msg);
                break;
            case R.id.thread_handler:
                Message msg1 = Message.obtain();
                msg1.what = 3;
                tHandler.sendMessage(msg1);
                break;
        }
    }

}
