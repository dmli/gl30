package com.yc.myegl3demo01;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.TextureView;

import com.yc.myegl3demo01.egl.YCEngineEglWrap;
import com.yc.myegl3demo01.gl.YCUtils;
import com.yc.myegl3demo01.gl3.YCV3Node_Blend;
import com.yc.myegl3demo01.gl3.YCV3Node_Etc2;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

//测试融合
public class Activity_Blend extends Activity {

    private String SCENE_NAME = "scene1";
    private Handler mHandler = new Handler();
    private YCEngineEglWrap mEngineEglWrap = null;
    private YCV3Node_Blend mBlend = null;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        if(mBlend != null){
            mBlend.uninit();
            mBlend = null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        init();
    }

    private void init() {
        TextureView mTextureView1 = findViewById(R.id.mTextureView1);
        mTextureView1.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mEngineEglWrap = new YCEngineEglWrap(null);
                mEngineEglWrap.createScene(SCENE_NAME, surface);
                if(mEngineEglWrap.activeScene(SCENE_NAME)) {
                    mBlend = new YCV3Node_Blend();
                    mBlend.init(width, height);

                    Bitmap greenPngBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.green_png);
                    Bitmap redPngBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.red_png);
                    int greenPngBitmapId = YCUtils.loadTexture(greenPngBitmap);
                    int redPngBitmapId = YCUtils.loadTexture(redPngBitmap);
                    mBlend.setTextureId1(greenPngBitmapId);
                    mBlend.setTextureId2(redPngBitmapId);

                    mEngineEglWrap.swapScene(SCENE_NAME);
                }
                update();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        });
    }

    private void update() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mEngineEglWrap.activeScene(SCENE_NAME)){
                    mBlend.draw();

                    mEngineEglWrap.swapScene(SCENE_NAME);
                }
                update();
            }
        }, 33);
    }
}
