//
// Created by admin on 2018/5/24.
//

#include "MaETC2TextureMgr.h"
#include "utils/file.h"
#include "libktx/ktx.h"
#include <android/log.h>
#include <malloc.h>

#define  LOG_TAG  "etc2"
#define  MAAAAA_LOG_ERROR(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

bool MaETC2TextureMgr::IsETC2Supported() {
#if KTX_OPENGL_ES5
    int aaa = 10;
#endif
    return true;
}

int MaETC2TextureMgr::loadTextureETC2_KTX(AAssetManager* mgr, const char *_textureFileName) {
    // Read/Load Texture File
    char *pData = NULL;
    unsigned int fileSize = 0;

    SetAssetManager(mgr);
    ReadFile(_textureFileName, &pData, &fileSize);

    // Generate handle & Load Texture
    GLuint handle = 0;
    GLenum target;
    GLboolean mipmapped;

    KTX_error_code result = ktxLoadTextureM(pData, fileSize, &handle, &target, NULL, &mipmapped, NULL, NULL, NULL);
    if (result != KTX_SUCCESS) {
        MAAAAA_LOG_ERROR("KTXLib couldn't load texture %s. Error: %d", _textureFileName, result);
        return 0;
    }

    glBindTexture(target, handle);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    if (mipmapped) {
        // Use mipmaps with bilinear filtering
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
    }

    // clean up
    free(pData);

    // Return handle
    return handle;
}


