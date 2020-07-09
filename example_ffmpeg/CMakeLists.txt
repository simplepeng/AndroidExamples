# 设置构建本机库文件所需的 CMake的最小版本
cmake_minimum_required(VERSION 3.4.1)

#添加头文件的搜索路径
include_directories(src/main/cpp/include)
#include_directories(src/main/cpp/ffmpeg/)

#设置查找动态库位置
set(LINK_DIR ${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI})
link_directories(${LINK_DIR})
#找到所有的so库，存放在全局变量SO_DIR中
file(GLOB SO_DIR ${LINK_DIR}/*.so)

#找到所有的源文件，存放在全局变量中
#file(GLOB FFMPEG_DIR src/main/cpp/ffmpeg/*.c)
#message("FFMPEG_DIR == ${FFMPEG_DIR}")

file(GLOB FFMPEG_DIR src/main/cpp/include/*.c)
file(GLOB CPP_DIR src/main/cpp/*.cpp)

# 添加自己写的 C/C++源文件
add_library(utils #so名称
        SHARED #动态库
        ${FFMPEG_DIR}
        ${CPP_DIR}
        )

#  依赖 NDK中自带的log库
find_library(log-lib log)

#  链接库
target_link_libraries(
        utils
        ${SO_DIR}
        jnigraphics
        ${log-lib})