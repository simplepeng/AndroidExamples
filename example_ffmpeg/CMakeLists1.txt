# 设置构建本机库文件所需的 CMake的最小版本
cmake_minimum_required(VERSION 3.4.1)

#添加头文件的搜索路径
include_directories(src/main/cpp/include/)

# 添加自己写的 C/C++源文件
add_library(utils #so名称
        SHARED #动态库
        src/main/cpp/ffmpeg_utils.cpp
        )

# 添加外部的库(可以是动态库或静态库)
set(LIBS_DIR ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI})
add_library(
        avutil
        SHARED
        IMPORTED)
set_target_properties(
        avutil
        PROPERTIES IMPORTED_LOCATION
        ${LIBS_DIR}/libavutil.so)

#  依赖 NDK中自带的log库
find_library(log-lib log)

#  链接库
target_link_libraries(
        utils
        avutil
        ${log-lib})