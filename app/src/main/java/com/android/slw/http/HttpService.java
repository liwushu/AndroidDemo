package com.android.slw.http;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by liwu.shu on 2016/9/20.
 */
public interface HttpService {
    @GET("api/category/list")
    Observable<HttpBean<List<NewsCategory>>> loadNewsCategory(@QueryMap Map<String, String> paramMap);

    @FormUrlEncoded
    @POST("upgrade")
    Observable<HttpBean<String>> loadUpdateInfo(@FieldMap Map<String,String> paramMap);
}
