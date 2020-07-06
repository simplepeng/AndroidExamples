#include <jni.h>
#include <libyuv.h>
#include <libyuv/convert_from.h>

extern "C" {
#include <libavutil/avutil.h>
#include "libavcodec/avcodec.h"
#include "android/log.h"
#include "libavformat/avformat.h"
#include "libavutil/error.h"
#include <libavutil/imgutils.h>
#include <libswscale/swscale.h>
#include "android/bitmap.h"
#include "libyuv.h"
#include "libyuv/convert_argb.h"

#define logDebug(...) __android_log_print(ANDROID_LOG_DEBUG,"MainActivity",__VA_ARGS__)


JNIEXPORT jstring JNICALL
Java_demo_simple_example_1ffmpeg_MainActivity_getVersion(JNIEnv *env, jclass clazz) {
    const char *version = av_version_info();
    return env->NewStringUTF(version);
}

//定义的静态方法，将某帧AVFrame在Android的Bitmap中绘制
static void fill_bitmap(AndroidBitmapInfo *info, void *pixels, AVFrame *pFrame) {
    uint8_t *frameLine;

    int yy;
    for (yy = 0; yy < info->height; yy++) {
        uint8_t *line = (uint8_t *) pixels;
        frameLine = (uint8_t *) pFrame->data[0] + (yy * pFrame->linesize[0]);

        int xx;
        for (xx = 0; xx < info->width; xx++) {
            int out_offset = xx * 4;
            int in_offset = xx * 3;

            line[out_offset] = frameLine[in_offset];
            line[out_offset + 1] = frameLine[in_offset + 1];
            line[out_offset + 2] = frameLine[in_offset + 2];
            line[out_offset + 3] = 0;
        }
        pixels = (char *) pixels + info->stride;
    }
}

jobject createBitmap(JNIEnv *env,
                     int width, int height) {

    jclass bitmapCls = env->FindClass("android/graphics/Bitmap");
    jmethodID createBitmapFunction = env->GetStaticMethodID(bitmapCls,
                                                            "createBitmap",
                                                            "(IILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;");
    jstring configName = env->NewStringUTF("ARGB_8888");
    jclass bitmapConfigClass = env->FindClass("android/graphics/Bitmap$Config");
    jmethodID valueOfBitmapConfigFunction = env->GetStaticMethodID(bitmapConfigClass,
                                                                   "valueOf",
                                                                   "(Ljava/lang/String;)Landroid/graphics/Bitmap$Config;");

    jobject bitmapConfig = env->CallStaticObjectMethod(bitmapConfigClass,
                                                       valueOfBitmapConfigFunction,
                                                       configName);

    jobject newBitmap = env->CallStaticObjectMethod(bitmapCls,
                                                    createBitmapFunction,
                                                    width, height,
                                                    bitmapConfig);

//    jmethodID set_pix_method = env->GetMethodID(bitmapCls, "setPixel", "(III)V");

    return newBitmap;
}

void saveBitmap(AVFrame *pFrame, int width, int height) {

//    const char *out_file = "/storage/emulated/0/get_cover.ppm";
    const char *out_file = "/storage/emulated/0/get_cover.png";
    FILE *file = fopen(out_file, "w+");
    if (!file) {
        logDebug("fopen out file error");
        return;
    }

    fprintf(file, "P6\n%d %d\n255\n", width, height); // header

    for (int y = 0; y < height; y++) {
        fwrite(pFrame->data[0] + y * pFrame->linesize[0], 1, width * 4, file);
    }

    // Close file.
    fclose(file);
}

//http://www.samirchen.com/ffmpeg-tutorial-1/
//https://blog.popkx.com/FFmpeg使用实例详解第一节-读取视频文件-将其逐帧分解为多张图片/
//RGB565 与RGB888的区别-https://blog.csdn.net/ctthuangcheng/article/details/8551559
//https://segmentfault.com/a/1190000016674715
//https://github.com/churnlabs/android-ffmpeg-sample/blob/master/jni/native.c
JNIEXPORT jobject JNICALL
Java_demo_simple_example_1ffmpeg_MainActivity_getCover(JNIEnv *env, jclass clazz, jstring path) {
    //初始化
    const char *_path = env->GetStringUTFChars(path, JNI_FALSE);
    int ret = -1;
    int video_stream_index = -1;
    logDebug("视频路径 == %s", _path);

    //代码逻辑：
    // 解封装 ，找到编解码器，解码，获取yuv，转rgb，封装bitmap返回
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
    //找出视频流
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
    if (codec_ctx->pix_fmt == AV_PIX_FMT_YUV420P) {
        logDebug("视频帧像素格式 -- YUV420P");
    }

    //打开编解码器
    ret = avcodec_open2(codec_ctx, codec, NULL);
    if (ret < 0) {
        logDebug("打开解码器失败 -- %s", av_err2str(ret));
        return nullptr;
    }
    AVFrame *pFrame = av_frame_alloc();
//    AVFrame *pFrameRGB = av_frame_alloc();
//    if (!pFrame || !pFrameRGB) {
//        logDebug("申请Frame失败");
//        return nullptr;
//    }

    logDebug("codecContext->width == %d", codec_ctx->width);
    logDebug("codecContext->height == %d", codec_ctx->height);

    //定义像素格式
    AVPixelFormat pix_fmt = AV_PIX_FMT_ARGB;
    //avpicture_get_size() @deprecated use av_image_get_buffer_size() instead.
    //占用内存=宽x高x一个像素占几个字节，但是要考虑align对齐的方式
    //ARGB=1080*1920*4=8294400
    //RGB=1080*1920*3=6220800
    //RGB565=1080*1920*2=
    int num_bytes = av_image_get_buffer_size(pix_fmt, codec_ctx->width, codec_ctx->height, 1);
    logDebug("num_bytes == %d", num_bytes);
    uint8_t *buffer = (uint8_t *) av_malloc(num_bytes * sizeof(uint8_t));
    if (!buffer) {
        logDebug("av_malloc buffer 失败");
        return nullptr;
    }
//    ret = av_image_fill_arrays(pFrameRGB->data, pFrameRGB->linesize, buffer, pix_fmt,
//                               codec_ctx->width, codec_ctx->height, 1);
//    if (ret < 0) {
//        logDebug("av_image_fill_arrays 失败");
//        return nullptr;
//    }

    AVPacket pkg;
    struct SwsContext *sws_ctx = sws_getContext(codec_ctx->width, codec_ctx->height,
                                                codec_ctx->pix_fmt,
                                                codec_ctx->width, codec_ctx->height,
                                                pix_fmt, SWS_BILINEAR,
                                                NULL, NULL, NULL);

    jobject bmp;
    bmp = createBitmap(env, codec_ctx->width, codec_ctx->height);
    if (!bmp) {
        logDebug("bmp == null");
    }
    void *addr_pixels;
    ret = AndroidBitmap_lockPixels(env, bmp, &addr_pixels);
    if (ret < 0) {
        logDebug("lockPixel error");
        return nullptr;
    }
    AndroidBitmapInfo info;
    ret = AndroidBitmap_getInfo(env, bmp, &info);
    if (ret < 0) {
        logDebug("getInfo error");
        return nullptr;
    }

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
//        sws_scale(sws_ctx, pFrame->data, pFrame->linesize,
//                  0,
//                  codec_ctx->height, pFrameRGB->data, pFrameRGB->linesize);
    }

//    fill_bitmap(&info, addr_pixels, pFrameRGB);
    int linesize = pFrame->width * 4;
    libyuv::I420ToABGR(pFrame->data[0], pFrame->linesize[0], // Y
                       pFrame->data[1], pFrame->linesize[1], // U
                       pFrame->data[2], pFrame->linesize[2], // V
                       (uint8_t *) addr_pixels, linesize,  // RGBA
                       pFrame->width, pFrame->height);

    //av_free_packet(&pkg) @deprecated Use av_packet_unref
    av_packet_unref(&pkg);


    logDebug("读取首帧完毕");

//    saveBitmap(pFrameRGB, codec_ctx->width, codec_ctx->height);

    //释放资源
    logDebug("开始释放资源");
    AndroidBitmap_unlockPixels(env, bmp);
    av_free(pFrame);
//    av_free(pFrameRGB);
    avcodec_close(codec_ctx);
    avformat_close_input(&ifmt_ctx);
    env->ReleaseStringUTFChars(path, _path);

    return bmp;
}


}