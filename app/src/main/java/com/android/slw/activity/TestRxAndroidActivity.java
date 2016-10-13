package com.android.slw.activity;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slw.R;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TestRxAndroidActivity extends BaseActivity implements View.OnClickListener{

    TextView tvTest;
    ImageView imageView;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_test_rx_android);
    }

    @Override
    protected void initViews() {
        //tvTest = (TextView)findViewById(R.id.test);
        imageView = (ImageView)findViewById(R.id.image);
    }

    @Override
    protected void bindListener() {
        tvTest.setOnClickListener(this);
    }

    private void invokeClick(){
        System.out.println("invokeClick: "+Thread.currentThread());
         Observable.create(new Observable.OnSubscribe<Integer>() {

             @Override
             public void call(Subscriber<? super Integer> subscriber) {
                 System.out.println("call: "+Thread.currentThread());
                 subscriber.onNext(1);
             }
         })
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final Integer integer) {
                System.out.println("onNext: "+Thread.currentThread());
                System.out.println("integer: "+integer.intValue());
                System.out.println("text: "+tvTest.getText().toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //tvTest.setText(tvTest.getText().toString()+integer);
                    }
                });
                tvTest.setText(tvTest.getText().toString()+integer);
                System.out.println("onNext finish");
            }
        });
    }

    @Override
    public void onClick(View view){
        int id = view.getId();
        switch (id){
            case R.id.test:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        invokeClick();
                    }
                }).start();
                break;
        }
    }
}
