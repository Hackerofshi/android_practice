package com.shixin.kotlin.dsl;

public class TestJava {

    //单方法的回调，这里返回Runnable类型的实例
    interface SingleFunListener {
        public Runnable singleFunction(int k);
    }

    //设置方法
    public void setSingleFunctionListener(SingleFunListener singleFunctionListener){
    }
}
