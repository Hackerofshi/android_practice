package com.shixin.bean;

import android.util.Log;

public class Animal {

    public void create() {
        Master master = new Master(this);
    }

    public void type() {
        Log.i("TAG", "父类");
    }

}
