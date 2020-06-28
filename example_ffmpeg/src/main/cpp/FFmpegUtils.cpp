#include <jni.h>

extern "C" {
#include <libavutil/avutil.h>

JNIEXPORT jstring JNICALL
Java_demo_simple_example_1ffmpeg_MainActivity_getVersion(JNIEnv *env, jclass clazz) {
    const char *version = av_version_info();
    return env->NewStringUTF(version);
}


}
