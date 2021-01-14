package com.shixin.jni;

import android.util.Log;

public class DynamicBridge {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    public native int text(String message);

    public static native int static_text(String message);

    public String name = "ooo";

    public native void native_init();

    public DynamicBridge(){
        native_init();
    }

    public void print() {
        Log.d("mmm", name);
    }
}
