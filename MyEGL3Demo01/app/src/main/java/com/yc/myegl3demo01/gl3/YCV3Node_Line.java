package com.yc.myegl3demo01.gl3;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import com.yc.myegl3demo01.gl.YCFbo;
import com.yc.myegl3demo01.gl.YCMSAAFbo;
import com.yc.myegl3demo01.gl.YCProgram;
import com.yc.myegl3demo01.gl.YCTextureNode;
import com.yc.myegl3demo01.gl.YCUtils;
import com.yc.myegl3demo01.utils.YCPointMgr;


public class YCV3Node_Line {
    private static final String __TAG = "YCV3Node_Line";
    private final static String VS = "#version 300 es \n" +
            "layout(location = 0) in vec2 aPosition;\n" +
            "uniform InColors {\n" +
            "    vec4 aColor1; \n" +
            "    vec4 aColor2; \n" +
            "}; \n" +
            "out vec4 sColor;\n" +
            "void main(){" +
            "    gl_Position = vec4(aPosition.xy, 0.0, 1.0);\n" +
            "    sColor = aColor1 + aColor2;\n" +
            "}";
    private final static String FS = "#version 300 es \n" +
            "precision mediump float;\n" +
            "in vec4 sColor;" +
            "out vec4 fragColor;\n" +
            "void main(){\n" +
            "    fragColor = sColor;\n" +
            "}";

    private YCProgram mProgram = null;
    private int mWidth, mHeight;
    private int mVertexBufferId = -1;
    private YCPointMgr mPointMgr = null;
    private YCMSAAFbo mFbo = null;
    private YCTextureNode mTextureNode = null;
    private boolean mClearTask = false;

    public void init(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        mProgram = new YCProgram(VS, FS);
        mProgram.bind();

        mPointMgr = new YCPointMgr();
        GLES30.glLineWidth(10);

        int tInColorsBuffer = YCUtils.glGenBuffers(GLES30.GL_UNIFORM_BUFFER, new float[]{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f});
        int tInColorsUBlockIndex = GLES30.glGetUniformBlockIndex(mProgram.getProgramId(), "InColors");
        int[] tInColorsUBlockSize = new int[1];
        GLES30.glGetActiveUniformBlockiv(mProgram.getProgramId(), tInColorsUBlockIndex, GLES30.GL_UNIFORM_BLOCK_DATA_SIZE, tInColorsUBlockSize, 0);
        GLES30.glBindBufferBase(GLES30.GL_UNIFORM_BUFFER, tInColorsUBlockIndex, tInColorsBuffer);

        mTextureNode = YCTextureNode.create(width, height);
        if(mTextureNode == null){
            Log.e(__TAG, "create YCTextureNode fail!");
            return;
        }

        mFbo = YCMSAAFbo.create(width, height, 0);
        if(mFbo == null){
            Log.e(__TAG, "create fbo fail!");
            return;
        }
        mFbo.setLineWidth(10);
        mTextureNode.setTextureId(mFbo.getTextureId());
        mFbo.unbind();
    }

    public void addPoint(float px, float py) {
        mPointMgr.put(getPointX(px), getPointY(py));
    }

    private float getPointY(float py) {
        return (mHeight - py) * 2.0f / mHeight - 1.0f;
    }

    private float getPointX(float px) {
        return px * 2.0f / mWidth - 1.0f;
    }

    public void setStartPoint(float px, float py) {
        mPointMgr.putStartPoint(getPointX(px), getPointY(py));
    }

    public void uninit() {
        if (mProgram != null) {
            mProgram.release();
            mProgram = null;
        }
    }

    public void clear(){
        mClearTask = true;
        draw();
    }



    public void draw() {
        if (mVertexBufferId == -1) {
            mVertexBufferId = YCUtils.glGenBuffers(GLES30.GL_ARRAY_BUFFER, mPointMgr.getData());
            return;
        }

        if(mClearTask){
            mClearTask = false;
            _clear();
        } else {
            YCUtils.glBufferSubData(GLES30.GL_ARRAY_BUFFER, mVertexBufferId, mPointMgr.getData());
            boolean isReset = mPointMgr.isMax();
            if (isReset) {
                _reset();
            }
        }

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        mProgram.bind();
        GLES30.glViewport(0, 0, mWidth, mHeight);
        GLES30.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        mTextureNode.draw();

        mProgram.bind();
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);
        GLES30.glVertexAttribPointer(0, 2, GLES30.GL_FLOAT, false, 0, 0);
        GLES30.glEnableVertexAttribArray(0);

        GLES30.glDrawArrays(GLES30.GL_LINES, 0, mPointMgr.getLength() / 2);
    }

    private void _reset() {
        mFbo.bind();
        GLES30.glViewport(0, 0, mWidth, mHeight);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);
        GLES30.glVertexAttribPointer(0, 2, GLES30.GL_FLOAT, false, 0, 0);
        GLES30.glEnableVertexAttribArray(0);

        GLES30.glDrawArrays(GLES30.GL_LINES, 0, mPointMgr.getLength() / 2);

        mFbo.blit();
        mFbo.unbind();
        mPointMgr.reset();
    }

    private void _clear() {
        mFbo.bind();
        GLES30.glViewport(0, 0, mWidth, mHeight);
        GLES30.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        mFbo.blit();
        mFbo.unbind();
        mPointMgr.reset();
    }

}
