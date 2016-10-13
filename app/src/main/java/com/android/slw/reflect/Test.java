package com.android.slw.reflect;

/**
 * Author: Beta-Tan
 * CreateTime: 16/9/8
 * Description:
 */
public class Test {


    //这里有个方法 得到接口的返回类型
    //private TestPaginator getReturnType() {
//        return null;
//    }

    //这里有个方法 得到接口的返回类型
    private Paginator<Dog> getReturnType() {
        return null;
    }

    public Class<Paginator> getResult(){
        return Paginator.class;
    }
}

class TestPaginator extends Paginator<Dog>{

}
