#include <jni.h>

extern "C" {
#include <libavutil/avutil.h>
#include "libavcodec/avcodec.h"
#include "android/log.h"
#include "libavformat/avformat.h"
#include "libavutil/error.h"
#include <libavutil/imgutils.h>
#include <libswscale/swscale.h>

#define logDebug(...) __android_log_print(ANDROID_LOG_DEBUG,"MainActivity",__VA_ARGS__)


JNIEXPORT jstring JNICALL
Java_demo_simple_example_1ffmpeg_MainActivity_getVersion(JNIEnv *env, jclass clazz) {
    const char *version = av_version_info();
    return env->NewStringUTF(version);
}

jobject createBitmap(JNIEnv *env,
                     AVFrame *pFrame,
                     int width, int height) {


    jclass bitmapCls = env->FindClass("android/graphics/Bitmap");
    jmethodID createBitmapFunction = env->GetStaticMethodID(bitmapCls,
                                                            "createBitmap",
                                                            "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    jstring configName = env->NewStringUTF("RGB_565");
    jclass bitmapConfigClass = env->FindClass("android/graphics/Bitmap$Config");
    jmethodID valueOfBitmapConfigFunction = env->GetStaticMethodID(bitmapConfigClass,
                                                                   "valueOf",
                                                                   "(Ljava/lang/String;)Landroid/graphics/Bitmap$Config;");

    jobject bitmapConfig = env->CallStaticObjectMethod(bitmapConfigClass,
                                                       valueOfBitmapConfigFunction,
                                                       configName);

    jobject newBitmap = env->CallStaticObjectMethod(bitmapCls,
                                                    createBitmapFunction,
                                                    width, height, bitmapConfig);

    jmethodID set_pix_method = env->GetMethodID(bitmapCls, "setPixel", "(III)V");

    //1080x1920=2073600
    for (int x = 0; x < width; ++x) {
        for (int y = 0; y < height; ++y) {
//            env->CallVoidMethod(newBitmap, set_pix_method, x, y, (int) pixel[x * y]);
        }
    }

    return newBitmap;
}

void saveBitmap(AVFrame *pFrame, int width, int height) {

    const char *out_file = "/storage/emulated/0/get_cover.ppm";
    FILE *file = fopen(out_file, "w+");
    if (!file) {
        logDebug("fopen out file error");
        return;
    }

    fprintf(file, "P6\n%d %d\n255\n", width, height); // header

    for (int y = 0; y < height; y++) {
        fwrite(pFrame->data[0] + y * pFrame->linesize[0], 1, width * 3, file);
    }

    // Close file.
    fclose(file);
}

//http://www.samirchen.com/ffmpeg-tutorial-1/
//https://blog.popkx.com/FFmpeg使用实例详解第一节-读取视频文件-将其逐帧分解为多张图片/
JNIEXPORT jobject JNICALL
Java_demo_simple_example_1ffmpeg_MainActivity_getCover(JNIEnv *env, jclass clazz, jstring path) {
    //初始化
    const char *_path = env->GetStringUTFChars(path, JNI_FALSE);
    int ret = -1;
    int video_stream_index = -1;
    logDebug("视频路径 == %s", _path);

    //代码逻辑：
    // 解封装 ，找到编码器，解码，获取yuv，转rgb，封装bitmap
    AVFormatContext *ifmt_ctx = NULL;
    ret = avformat_open_input(&ifmt_ctx, _path, 0, 0);
    if (ret < 0) {
        logDebug("解封装失败 -- %s", av_err2str(ret));
        return nullptr;
    }
    ret = avformat_find_stream_info(ifmt_ctx, 0);
    if (ret < 0) {
        logDebug("查找流失败");
        return nullptr;
    }

    av_dump_format(ifmt_ctx, 0, _path, 0);
    AVStream *pStream = NULL;
    AVCodecParameters *codecpar = NULL;

    for (int i = 0; i < ifmt_ctx->nb_streams; ++i) {
        pStream = ifmt_ctx->streams[i];
        if (pStream->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
            codecpar = pStream->codecpar;
            video_stream_index = i;
        }
    }
    if (!codecpar) {
        logDebug("codecpar没找到");
        return nullptr;
    }

    logDebug("解码器 == %s", avcodec_get_name(codecpar->codec_id));
    AVCodec *codec = avcodec_find_decoder(codecpar->codec_id);
    if (!codec) {
        logDebug("没找到解码器");
        return nullptr;
    }
    //申请编解码器上下文
    AVCodecContext *codec_ctx = avcodec_alloc_context3(codec);
    if (!codec_ctx) {
        logDebug("codecContext alloc error");
        return nullptr;
    }
    //拷贝codec context
    ret = avcodec_parameters_to_context(codec_ctx, codecpar);
    if (ret < 0) {
        logDebug("avcodec_parameters_to_context  error");
        return nullptr;
    }

    ret = avcodec_open2(codec_ctx, codec, NULL);
    if (ret < 0) {
        logDebug("打开解码器失败 -- %s", av_err2str(ret));
        return nullptr;
    }
    AVFrame *pFrame = av_frame_alloc();
    AVFrame *pFrameRGB = av_frame_alloc();
    if (!pFrame || !pFrameRGB) {
        logDebug("申请Frame失败");
        return nullptr;
    }

    AVPixelFormat pix_fmt = AV_PIX_FMT_RGB24;
    logDebug("codecContext->width == %d", codec_ctx->width);
    logDebug("codecContext->height == %d", codec_ctx->height);
    int num_bytes = av_image_get_buffer_size(pix_fmt, codec_ctx->width, codec_ctx->height, 1);
    logDebug("num_bytes == %d", num_bytes);
    uint8_t *buffer = (uint8_t *) av_malloc(num_bytes * sizeof(uint8_t));
    if (!buffer) {
        logDebug("av_malloc buffer 失败");
        return nullptr;
    }
    ret = av_image_fill_arrays(pFrameRGB->data, pFrameRGB->linesize, buffer, pix_fmt,
                               codec_ctx->width,
                               codec_ctx->height, 1);
    if (ret < 0) {
        logDebug("av_image_fill_arrays 失败");
        return nullptr;
    }


    AVPacket pkg;
    struct SwsContext *sws_ctx = sws_getContext(codec_ctx->width, codec_ctx->height,
                                                codec_ctx->pix_fmt,
                                                codec_ctx->width, codec_ctx->height,
                                                pix_fmt, SWS_BILINEAR,
                                                NULL, NULL, NULL);
    int frameFinished;
    while (av_read_frame(ifmt_ctx, &pkg) >= 0) {
        if (pkg.stream_index != video_stream_index) {
            continue;
        }
        logDebug("av_read_frame");
        //Use avcodec_send_packet() and avcodec_receive_frame().
        ret = avcodec_decode_video2(codec_ctx, pFrame, &frameFinished, &pkg);
        if (ret < 0) {
            logDebug("avcodec_decode_video2 error -- %s", av_err2str(ret));
        }
        if (!frameFinished)
            continue;
        logDebug("开始转换视频帧数据到图像帧数据");
        sws_scale(sws_ctx, pFrame->data, pFrame->linesize,
                  0, codec_ctx->height, pFrameRGB->data,
                  pFrameRGB->linesize);
//        av_packet_unref(&pkg);
    }
    //@deprecated Use av_packet_unref
    //av_free_packet(&pkg);
    av_packet_unref(&pkg);

    logDebug("读取帧完毕");
    int len = sizeof(pFrameRGB->data) / sizeof(pFrameRGB->data[0]);
    logDebug("像素数组 == %d", len);

//    jobject bmp = createBitmap(env, pFrameRGB, codec_ctx->width, codec_ctx->height);
    saveBitmap(pFrameRGB, codec_ctx->width, codec_ctx->height);

    //释放资源
    logDebug("开始释放资源");
    env->ReleaseStringUTFChars(path, _path);

//    int len = sizeof(pFrameRGB->data) / sizeof(pFrameRGB->data[0]);
//    jintArray jArr = env->NewIntArray(len);
//    jint *arr = env->GetIntArrayElements(jArr,NULL);
//    for (int i = 0; i < len; ++i) {
//        arr[i] = pFrameRGB->data[i];
//    }
//    env->INTARR

    return nullptr;
}


}