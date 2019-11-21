#ifndef ANDROIDEXAMPLES_ANDROID_LOG_H
#define ANDROIDEXAMPLES_ANDROID_LOG_H

#include <android/log.h>

#define TAG "example_ndk"
#define log_v(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)
#define log_d(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define log_i(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define log_w(...) __android_log_print(ANDROID_LOG_WARN, TAG, __VA_ARGS__)
#define log_e(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

#endif //ANDROIDEXAMPLES_ANDROID_LOG_H
