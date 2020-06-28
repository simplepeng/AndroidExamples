#include <jni.h>


extern "C" {
JNIEXPORT jstring JNICALL
Java_demo_simple_example_1ffmpeg_MainActivity_getVersion(JNIEnv *env, jclass clazz) {
    char *version = "hello";
    return env->NewStringUTF(version);
}


}
