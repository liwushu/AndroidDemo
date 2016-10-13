package com.android.slw.http;

import java.lang.reflect.Method;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liwu.shu on 2016/9/19.
 */
public class HttpManager<T> {
    static final private String BASE_URL = "http://tstream-india-test.api.tclclouds.com/";
    Retrofit retrofit;
    private String baseURL;
    T httpService;
    public HttpManager(Class<T> clz){
        this(BASE_URL,clz);
    }

    public HttpManager(String baseURL,Class<T> clz){
        this.baseURL = baseURL;
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseURL)
                .build();
        httpService = retrofit.create(clz);
    }

    public<E> void invokeLoad(Subscriber<E> subscriber, Map<String,String>params, String methodName){
        try {
            Method method= httpService.getClass().getMethod(methodName,Map.class);
            if(method != null){
                ((Observable<E>)method.invoke(httpService,params))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
