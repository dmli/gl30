package com.yc.myegl3demo01;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;

import com.yc.myegl3demo01.egl.YCEngineEglWrap;
import com.yc.myegl3demo01.gl3.YCV3Node_Layout;
import com.yc.myegl3demo01.gl3.YCV3Node_Line;

public class Activity_Layout extends Activity {

    private String SCENE_NAME = "scene1";

    private TextureView mTextureView1 = null;
    private Handler mHandler = new Handler();
    private YCEngineEglWrap mEngineEglWrap = null;
    private YCV3Node_Layout mV3Node = null;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        if(mV3Node != null){
            mV3Node.uninit();
            mV3Node = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        init();
    }

    private void init() {
        mTextureView1 = findViewById(R.id.mTextureView1);
        mTextureView1.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mEngineEglWrap = new YCEngineEglWrap(null);
                mEngineEglWrap.createScene(SCENE_NAME, surface);
                if(mEngineEglWrap.activeScene(SCENE_NAME)){
                    mV3Node = new YCV3Node_Layout();
                    mV3Node.init(width, height);
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

                    mV3Node.draw();

                    mEngineEglWrap.swapScene(SCENE_NAME);
                }
                update();
            }
        }, 33);
    }

}
