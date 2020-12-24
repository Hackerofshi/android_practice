#include <jni.h>
#include <string>
#include<sstream>

union Speed {
    float num_f;
    unsigned char num_c[4];
};

union Speed1 {
    float num_f;
    char num_c[4];
} s1;

extern "C"
JNIEXPORT jfloat JNICALL
Java_com_shixin_jni_NDKBridge_resultFrom(JNIEnv *env, jobject thiz, jint a, jint b, jint c,
                                              jint d) {

    Speed s{};
    s.num_c[0] = a;
    s.num_c[1] = b;
    s.num_c[2] = c;
    s.num_c[3] = d;
    float f = s.num_f;

    return f;
}

extern "C"
JNIEXPORT jintArray JNICALL
Java_com_shixin_jni_NDKBridge_floatToString(JNIEnv *env, jobject thiz, jfloat f) {
    s1.num_f = f;
    char *pat = &s1.num_c[3];
    jintArray array = env->NewIntArray(4);
    jint j;
    jint buff[4];
    for (j = 0; j < 4; j++) {
        buff[j] = s1.num_c[j];
    }
    (env)->SetIntArrayRegion( array, 0, 4, buff);
    return array;
}