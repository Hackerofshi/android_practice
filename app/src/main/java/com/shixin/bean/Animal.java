package com.shixin.bean;

import android.util.Log;

public class Animal {

    public void create() {
        Master master = new Master(this);
    }

    public void type() {
        Log.i("TAG", "父类");
        testPrivate();
    }

    public void testPublic() {
        Log.i("TAG", "testPrivate");
    }

    private void testPrivate() {
        Log.i("TAG", "testPrivate");
    }

    protected void testProtect() {
        Log.i("TAG", "testProtect");
    }

    void testDefault() {
        Log.i("TAG", "testDefault");
    }

    //          同一个类  同一个包   不同包子类  不同包非子类
    // public    √         √         √            √
    // protect   √         √         √
    // default   √         √
    // private   √



}
