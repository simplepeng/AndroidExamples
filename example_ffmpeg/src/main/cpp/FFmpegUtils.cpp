#include <jni.h>
#include <libavcodec/avcodec.h>


extern "C" {
JNIEXPORT jstring JNICALL
Java_demo_simple_example_1ffmpeg_MainActivity_getVersion(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF(av_version_info());
}


}
