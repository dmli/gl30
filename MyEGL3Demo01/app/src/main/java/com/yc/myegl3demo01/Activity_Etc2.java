package com.yc.myegl3demo01;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.opengl.ETC1Util;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.TextureView;
import android.widget.ImageView;

import com.yc.myegl3demo01.egl.YCEngineEglWrap;
import com.yc.myegl3demo01.gl.YCUtils;
import com.yc.myegl3demo01.gl3.YCV3Node_Etc2;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class Activity_Etc2 extends Activity {

    private String SCENE_NAME = "scene1";
    private TextureView mTextureView1 = null;
    private Handler mHandler = new Handler();
    private YCEngineEglWrap mEngineEglWrap = null;
    private YCV3Node_Etc2 mEtc2 = null;
    private ImageView mImage0;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        if(mEtc2 != null){
            mEtc2.uninit();
            mEtc2 = null;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        init();
    }

    private void init() {
        mImage0 = findViewById(R.id.mImage0);
        mTextureView1 = findViewById(R.id.mTextureView1);
        mTextureView1.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mEngineEglWrap = new YCEngineEglWrap(null);
                mEngineEglWrap.createScene(SCENE_NAME, surface);
                if(mEngineEglWrap.activeScene(SCENE_NAME)) {
                    mEtc2 = new YCV3Node_Etc2();
                    mEtc2.assetManager = getAssets();
                    InputStream is = getResources().openRawResource(R.raw.image2_mipmap0_combined);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    ByteBuffer mBuf = ByteBuffer.allocateDirect(bitmap.getWidth() * bitmap.getHeight() * 4);
                    mBuf.order(ByteOrder.nativeOrder());
                    mBuf.rewind();
                    mBuf.position(0);
                    bitmap.copyPixelsToBuffer(mBuf);

                    mEtc2.init(width, height);
                    mBuf.position(0);
                    mEtc2.setTextureData(mBuf);
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
                    mEtc2.draw();

                    mEngineEglWrap.swapScene(SCENE_NAME);
                }
                update();
            }
        }, 33);
    }

}
