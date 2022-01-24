package com.shixin.inject.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ListenerInvocationHandler implements InvocationHandler {

    private Object obj ;
    private Method myMethod;

    public ListenerInvocationHandler(Object obj, Method myMethod) {
        this.obj      = obj;
        this.myMethod = myMethod;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return myMethod.invoke(o,objects);
    }
}
