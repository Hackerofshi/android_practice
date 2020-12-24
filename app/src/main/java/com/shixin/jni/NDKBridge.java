package com.shixin.jni;

/**
 * @ProjectName: AnyeMonitor
 * @Package: com.anyemonitor.jni
 * @ClassName: NDKBridge
 * @Description: java类作用描述
 * @Author: shixin
 * @CreateDate: 2020/10/26 16:30
 * @UpdateUser: shixin：
 * @UpdateDate: 2020/10/26 16:30
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class NDKBridge {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public native float resultFrom(int a, int b, int c, int d);


    public native int[] floatToString(float f);
}
