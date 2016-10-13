package com.android.slw.reflect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.android.slw.R;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity1 extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test0();
    }


    private void test0() {
        //构造一个服务器返回服务器返回
        String response = generateResponse();

        //得到返回类型
        Method method = Util.getMethod(Test.class, "getReturnType");
        Type type = method.getGenericReturnType();
        Object obj;
        System.out.println("type instanceof ParameterizedType: "+(type instanceof ParameterizedType));
        System.out.println("type: "+type);
        if (type instanceof ParameterizedType) {
            obj = JSON.parseObject(response, type, new Feature[0]);
        } else {
            Class returnClz =
                    Util.getReturnType(Test.class, "getReturnType");
            obj = JSON.parseObject(response,returnClz );
        }

        Paginator<Dog> p = (Paginator<Dog>) obj;
        try {
            Log.d(TAG, p.getData().get(0).getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String generateResponse() {
        Paginator<Dog> paginator = new Paginator<>();
        paginator.setPage(1);

        List<Dog> dogList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Dog dog = new Dog();
            dog.setAge(i + 3);
            dog.setName("小狗" + i);
            dogList.add(dog);
        }
        paginator.setData(dogList);
        return JSON.toJSONString(paginator);
    }
}
