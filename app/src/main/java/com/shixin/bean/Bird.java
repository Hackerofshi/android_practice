package com.shixin.bean;

import android.util.Log;

public class Bird extends Animal {
    @Override
    public void type() {
        Log.i("TAG", "鸟类 ");
        testDefault();
        testProtect();
    }
}
