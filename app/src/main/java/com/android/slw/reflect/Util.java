package com.android.slw.reflect;

import java.lang.reflect.Method;

/**
 * Author: Beta-Tan
 * CreateTime: 16/9/8
 * Description:
 */
public class Util {
    public static Class getReturnType(Class clz,String methodName) {
        Method[] methods = clz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method.getReturnType();
            }
        }
        return null;
    }

    public static Method getMethod(Class clz,String methodName) {
        Method[] methods = clz.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
}
