package com.yc.myegl3demo01;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;

import com.yc.myegl3demo01.egl.YCEngineEglWrap;
import com.yc.myegl3demo01.gl3.YCV3Node_Line;

public class Activity_Line extends Activity implements View.OnClickListener {

    private String SCENE_NAME = "scene1";

    private YCEngineEglWrap mEngineEglWrap = null;
    private YCV3Node_Line mV3Node = null;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mV3Node != null) {
            mV3Node.uninit();
            mV3Node = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        findViewById(R.id.mClearBtn).setOnClickListener(this);
        ImageView mImageView1 = findViewById(R.id.mImageView1);
        mImageView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mV3Node.setStartPoint(event.getX(), event.getY());
                        update();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        mV3Node.addPoint(event.getX(), event.getY());
                        update();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:

                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        TextureView mTextureView1 = findViewById(R.id.mTextureView1);
        mTextureView1.setOpaque(false);
        mTextureView1.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                mEngineEglWrap = new YCEngineEglWrap(null);
                mEngineEglWrap.createScene(SCENE_NAME, surface);
                if (mEngineEglWrap.activeScene(SCENE_NAME)) {
                    mV3Node = new YCV3Node_Line();
                    mV3Node.init(width, height);
                    mEngineEglWrap.swapScene(SCENE_NAME);
                }
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
        sendRenderCMD(new Runnable() {
            @Override
            public void run() {
                mV3Node.draw();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mClearBtn:
                sendRenderCMD(new Runnable() {
                    @Override
                    public void run() {
                        mV3Node.clear();
                    }
                });
                break;
            default:
                break;
        }
    }

    public void sendRenderCMD(Runnable runnable){
        if(runnable == null){
            return;
        }
        if (mEngineEglWrap.activeScene(SCENE_NAME)) {
            runnable.run();
            mEngineEglWrap.swapScene(SCENE_NAME);
        }
    }
}
