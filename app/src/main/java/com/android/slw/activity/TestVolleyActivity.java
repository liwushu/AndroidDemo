package com.android.slw.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.slw.R;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by liwu.shu on 2016/9/20.
 */
public class TestVolleyActivity extends BaseActivity implements View.OnClickListener{

    private static final String UPDATE_URL =  "\"http://tlauncher-api.tclclouds.com/tlauncher-api/api/\"";

    TextView tvShowMessage,tvStart,tvUpdate,tvTest;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_test_volley);
    }

    @Override
    protected void initViews() {
        tvShowMessage = (TextView)findViewById(R.id.show_message);
        tvStart = (TextView)findViewById(R.id.start);
        tvUpdate = (TextView)findViewById(R.id.update);
        tvTest = (TextView)findViewById(R.id.test);
        imageView = (ImageView)findViewById(R.id.image);
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
                //invokeUpdate();
                break;
            case R.id.test:
                //invokeTest();
                break;
        }
    }

    private void invokeStart(){
        RequestQueue queue = Volley.newRequestQueue(TestVolleyActivity.this);
        StringRequest stringRequest = new StringRequest("http://www.baidu.com", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("response: "+response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error: "+error.getMessage());
            }
        });
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://m.weather.com.cn/data/101010100.html",
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                System.out.println("response: "+response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error: "+error.getMessage());
            }
        });

        ImageRequest imageRequest = new ImageRequest("http://imgt6.bdstatic.com/it/u=2,887966933&fm=19&gp=0.jpg",
                new Response.Listener<Bitmap>()
                {

                    @Override
                    public void onResponse(Bitmap response)
                    {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, null);
        queue.add(stringRequest);
        queue.add(jsonObjectRequest);
        queue.add(imageRequest);
    }
}
