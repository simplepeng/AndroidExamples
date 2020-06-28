# 设置构建本机库文件所需的 CMake的最小版本
cmake_minimum_required(VERSION 3.4.1)

# 添加自己写的 C/C++源文件
add_library(utils #so名称
        SHARED #动态库
        src/main/cpp/FFmpegUtils.cpp
        )

#第二种方式引入
#set(LINK_DIR ${PROJECT_SOURCE_DIR}/example_ffmpeg/src/main/jniLibs/${ANDROID_ABI})
##link_directories(${LINK_DIR})

# 配置 预编译库 如ffmpeg库 路径
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_GLAGS} -L${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}")

#第一种方式引入
#message("simple ---- ${ANDROID_ABI}")
#message("simple ---- ${CMAKE_ANDROID_ARCH_ABI}")
set(LIBS_DIR ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI})
#message("simple ---- ${LIBS_DIR}")
#add_library(
#        avcodec
#        SHARED
#        IMPORTED)
#set_target_properties(
#        avcodec
#        PROPERTIES IMPORTED_LOCATION
#        ${LIBS_DIR}/libavcodec.so)

add_library(
        avutil
        SHARED
        IMPORTED)
set_target_properties(
        avutil
        PROPERTIES IMPORTED_LOCATION
        ${LIBS_DIR}/libavutil.so)

#添加头文件的搜索路径
include_directories(src/main/cpp/include)

#  依赖 NDK中的库
find_library(log-lib log)

#  将目标库与 NDK中的库进行连接
target_link_libraries(
        utils
        avutil
        ${log-lib})