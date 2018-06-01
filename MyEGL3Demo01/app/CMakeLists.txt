cmake_minimum_required(VERSION 3.4.1)

file(GLOB LIB_KTX_INCLUDE "src/main/cpp/libktx/*.h")
file(GLOB LIB_KTX_SRC "src/main/cpp/libktx/*.c")
file(GLOB LIB_UTILS_INCLUDE "src/main/cpp/utils/*.h")
file(GLOB LIB_UTILS_SRC "src/main/cpp/utils/*.c")


add_library(
             native-lib

             SHARED

             ${LIB_KTX_INCLUDE}
             ${LIB_KTX_SRC}
             ${LIB_UTILS_INCLUDE}
             ${LIB_UTILS_SRC}

             src/main/cpp/native-lib.cpp
             src/main/cpp/MaETC2TextureMgr.cpp

             )

find_library(
              log-lib

              log )

target_link_libraries(
                       native-lib
                        android
                        GLESv3
                        EGL
                        OpenSLES
                        jnigraphics
                       ${log-lib} )