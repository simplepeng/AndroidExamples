# 设置构建本机库文件所需的 CMake的最小版本
cmake_minimum_required(VERSION 3.4.1)

#添加头文件的搜索路径
include_directories(src/main/jni/)

# 添加自己写的 C/C++源文件
add_library(hello_jni #so名称
        SHARED #动态库
        src/main/jni/android_log.h
        src/main/jni/hello_jni.c
        )

#  依赖 NDK中的库
find_library(log-lib
        log)

#  将目标库与 NDK中的库进行连接
target_link_libraries(
        hello_jni
        ${log-lib})