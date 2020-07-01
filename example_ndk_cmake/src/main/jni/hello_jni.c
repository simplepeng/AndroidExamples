#include <jni.h>
#include <stdio.h>
#include "android_log.h"

//#define LOG_TGA "example_ndk"
//#define log_d(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TGA, __VA_ARGS__);

const char *package = "me/simple/example_ndk_cmake/MainActivity";
const char *class_name = "me/simple/example_ndk_cmake/Test";

JNIEXPORT jstring JNICALL Java_me_simple_example_1ndk_1cmake_MainActivity_callJniMethod(
        JNIEnv *env, jclass clazz
) {
    const char *message = "string from jni c";
    return (*env)->NewStringUTF(env, message);
}

JNIEXPORT void JNICALL
Java_me_simple_example_1ndk_1cmake_MainActivity_jniCallJavaMethod(
        JNIEnv *env, jclass this) {

    const char *class_name = "me/simple/example_ndk_cmake/Test";
    //根据类路径找到类对象
    jclass jclazz = (*env)->FindClass(env, class_name);
    //jclass jclazz = (*env)->GetObjectClass(env, this);
    //获取类的空参构造方法
    const char *construct_method = "<init>";
    jmethodID construct = (*env)->GetMethodID(env, jclazz, construct_method, "()V");
    //创建该类的实例
    jobject jobject1 = (*env)->NewObject(env, jclazz, construct);
    //找到要调用的方法id
    const char *method = "log";
    const char *sign = "(Ljava/lang/String;)V";
    jmethodID jmethodId = (*env)->GetMethodID(env, jclazz, method, sign);
    //调用改类的方法
    const char *message = "method called by c";
    jstring jstring1 = (*env)->NewStringUTF(env, message);
    (*env)->CallVoidMethod(env, jobject1, jmethodId, jstring1);
    //释放引用
    (*env)->DeleteLocalRef(env, jclazz);
    (*env)->DeleteLocalRef(env, jobject1);
    (*env)->DeleteLocalRef(env, jstring1);
}

JNIEXPORT void JNICALL
Java_me_simple_example_1ndk_1cmake_MainActivity_native_1jniCallJavaStaticMethod(
        JNIEnv *env,
        jclass clazz) {

    jclass jclazz = (*env)->FindClass(env, class_name);

    const char *method = "staticLog";
    const char *sign = "(Ljava/lang/String;)V";
    jmethodID jmethodId = (*env)->GetStaticMethodID(env, jclazz, method, sign);

    const char *message = "message from c";
    jstring jmessage = (*env)->NewStringUTF(env, message);
    (*env)->CallStaticVoidMethod(env, jclazz, jmethodId, jmessage);
}

JNIEXPORT void JNICALL
Java_me_simple_example_1ndk_1cmake_MainActivity_native_1callJavaFiled(
        JNIEnv *env, jclass clazz, jobject testObj) {
    //根据类路径找到类对象
//    jclass jclazz = (*env)->FindClass(env, class_name);
    jclass jclazz = (*env)->GetObjectClass(env, testObj);
    //获取变量字段的id
    const char *filed = "a";
    const char *sign = "I";
    jfieldID jfieldId = (*env)->GetFieldID(env, jclazz, filed, sign);
    //获取字段的值
    jint jint1 = (*env)->GetIntField(env, testObj, jfieldId);
    log_d("get filed == %d", jint1);
    //更改字段的值
    (*env)->SetIntField(env, testObj, jfieldId, 3);
    jint1 = (*env)->GetIntField(env, testObj, jfieldId);
    log_d("set filed == %d", jint1);
}

JNIEXPORT void JNICALL
Java_me_simple_example_1ndk_1cmake_MainActivity_native_1callJavaStaticFiled(
        JNIEnv *env,
        jclass clazz) {
    //根据类路径找到类对象
    jclass jclass1 = (*env)->FindClass(env, class_name);
    //获取static变量字段的id
    const char *filed = "b";
    const char *sign = "Ljava/lang/String;";
    jfieldID jfieldId = (*env)->GetStaticFieldID(env, jclass1, filed, sign);
    //获取staic字段的值
    jstring jstring1 = (jstring) (*env)->GetStaticObjectField(env, jclass1, jfieldId);
    //jstring转换为字符指针
    const char *filed_string = (*env)->GetStringUTFChars(env, jstring1, 0);
    log_d("get string filed == %s", filed_string);
}

JNIEXPORT void JNICALL
Java_me_simple_example_1ndk_1cmake_MainActivity_native_1jniCallJavaConstruct(
        JNIEnv *env,
        jclass clazz) {
    //根据类路径找到类对象
    jclass jclazz = (*env)->FindClass(env, class_name);
    //获取类的有参构造方法
    const char *construct_method = "<init>";
    const char *sign = "(ILjava/lang/String;)V";
    jmethodID construct = (*env)->GetMethodID(env, jclazz, construct_method, sign);
    //创建该类的实例
    jint arg_a = 10;
    jstring arg_b = (*env)->NewStringUTF(env, "hello world");
    jobject jobject1 = (*env)->NewObject(env, jclazz, construct, arg_a, arg_b);

}

JNIEXPORT void JNICALL
Java_me_simple_example_1ndk_1cmake_MainActivity_native_1throwException(JNIEnv *env, jclass clazz) {
    jclass e_class = (*env)->FindClass(env, "java/lang/Exception");
    (*env)->ThrowNew(env, e_class, "throw exception from c");
}

JNIEXPORT void JNICALL
Java_me_simple_example_1ndk_1cmake_MainActivity_native_1jniCheckException(JNIEnv *env,
                                                                          jclass clazz) {
    jclass jclass1 = (*env)->FindClass(env, "java/lang/Exception");

    const char *method = "a";
    const char *sign = "b";
    jfieldID  jfieldId = (*env)->GetFieldID(env,jclass1,method,sign);

    //检测异常是否发生
    jthrowable e =  (*env)->ExceptionOccurred(env);
    if (e != NULL){
        log_d("异常发生");
        //清除异常，需要和ExceptionOccurred成对出现
        (*env)->ExceptionClear(env);
    }
}
