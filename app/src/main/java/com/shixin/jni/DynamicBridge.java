package com.shixin.jni;

import android.util.Log;

public class DynamicBridge {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    public String name = "ooo";

    public DynamicBridge(){

    }

    public void print() {
        Log.d("mmm", name);
    }
}
