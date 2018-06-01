

#include <jni.h>
#include <android/asset_manager_jni.h>

#ifndef _Included_com_yc_myegl3demo01_gl3_YCV3Node_Etc2
#define _Included_com_yc_myegl3demo01_gl3_YCV3Node_Etc2

#include "MaETC2TextureMgr.h"


extern "C"
JNIEXPORT jint JNICALL
Java_com_yc_myegl3demo01_gl3_YCV3Node_1Etc2_loadTextureETC2_1KTX(JNIEnv *env, jobject instance, jobject assetManager, jstring fileName_) {
    const char *fileName = env->GetStringUTFChars(fileName_, 0);

    AAssetManager* mgr = AAssetManager_fromJava( env, assetManager );

    MaETC2TextureMgr *t_Etc2Mgr = new MaETC2TextureMgr();
    int textureId = t_Etc2Mgr->loadTextureETC2_KTX(mgr, fileName);
    delete t_Etc2Mgr;

    env->ReleaseStringUTFChars(fileName_, fileName);
    return textureId;
}

#endif