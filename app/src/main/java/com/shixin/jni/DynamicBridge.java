package com.shixin.jni;

public class DynamicBridge {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    public native int text(String message);


    public static native int static_text(String message);

}
