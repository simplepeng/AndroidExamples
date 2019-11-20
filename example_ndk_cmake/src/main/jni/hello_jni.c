#include <jni.h>

const char *package = "me/simple/example_ndk_cmake/MainActivity";

JNIEXPORT jstring JNICALL Java_me_simple_example_1ndk_1cmake_MainActivity_callJniMethod(
        JNIEnv *env, jclass clazz
) {
    const char *message = "string from jni c";
    return (*env)->NewStringUTF(env, message);
}

JNIEXPORT void JNICALL
Java_me_simple_example_1ndk_1cmake_MainActivity_jniCallJavaMethod(
        JNIEnv *env, jclass clazz) {
//    jclass jclazz = (*env)->FindClass(env, package);
    jclass jclazz = (*env)->GetObjectClass(env,clazz);
    if (jclazz == NULL){
        return;
    }

    const char *sign = "(java/lang/String;)V";
    jmethodID jmethodId = (*env)->GetMethodID(env, jclazz, "toast", sign);
    if (jmethodId == NULL)return;

    const char *message = "method called by c";
    jstring jstring1 = (*env)->NewStringUTF(env,message);
    (*env)->CallVoidMethod(env, clazz, jmethodId, jstring1);

}