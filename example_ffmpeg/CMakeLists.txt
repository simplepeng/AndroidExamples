# 设置构建本机库文件所需的 CMake的最小版本
cmake_minimum_required(VERSION 3.4.1)

# 添加自己写的 C/C++源文件
add_library(utils #so名称
        SHARED #动态库
        src/main/cpp/FFmpegUtils.cpp
        )

#添加头文件的搜索路径
#include_directories(src/main/cpp/include)

#
#set(LINK_DIR ${PROJECT_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI})
#link_directories(${LINK_DIR})

#第一种方式引入
#set(LIBS_DIR libs)
#add_library(avcodec SHARED IMPORTED)
#set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${LIBS_DIR}/${ANDROID_ABI}/libavcodec.so)
#add_library(avutils SHARED IMPORTED)
#set_target_properties(avutils PROPERTIES IMPORTED_LOCATION ${LIBS_DIR}/${ANDROID_ABI}/libavutil.so)



#  依赖 NDK中的库
find_library(log-lib log)

#  将目标库与 NDK中的库进行连接
target_link_libraries(
        utils
#        avcodec
#        avutils
#        -landroid
        ${log-lib})