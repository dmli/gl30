//
// Created by admin on 2018/5/24.
//

#define AASSDDFFGGG

#ifndef MYEGL3DEMO01_MAETC2TEXTUREMGR_H
#define MYEGL3DEMO01_MAETC2TEXTUREMGR_H

#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>
#include <android/asset_manager.h>

class MaETC2TextureMgr {
public:
    bool IsETC2Supported();

    int loadTextureETC2_KTX(AAssetManager* mgr, const char* _textureFileName);
};


#endif //MYEGL3DEMO01_MAETC2TEXTUREMGR_H
