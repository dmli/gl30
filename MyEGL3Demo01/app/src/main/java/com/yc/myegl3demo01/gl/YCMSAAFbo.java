package com.yc.myegl3demo01.gl;

import android.opengl.GLES20;
import android.opengl.GLES30;

/**
 * Created by lidm on 17/8/7.
 * glFrameBuffer
 */

public class YCMSAAFbo {

    private int mMultiSampleFboId = 0;
    private int mNormalFboId = 0;
    private int mRenderId = 0;
    private int mTextureId = 0;
    private int mDepthId = 0;
    private final int[] mFboSize = new int[2];
    private int mAngle = 0;
    private int mLineWidth = 0;
    private int mDefFboId = 0;


    private YCMSAAFbo() {
    }

    public void setDefFboId(int defFboId) {
        this.mDefFboId = defFboId;
    }

    public static YCMSAAFbo create(int width, int height, int angle) {
        YCMSAAFbo fbo = new YCMSAAFbo();
        if (fbo.init(width, height, angle)) {
            return fbo;
        }
        return null;
    }

    private boolean init(int width, int height, int angle) {
        mAngle = angle;
        if (angle == 90 || angle == 270) {
            mFboSize[0] = height;
            mFboSize[1] = width;
        } else {
            mFboSize[0] = width;
            mFboSize[1] = height;
        }

        //fbo id
        int tFboIds[] = new int[2];
        GLES30.glGenFramebuffers(2, tFboIds, 0);
        mMultiSampleFboId = tFboIds[0];
        mNormalFboId = tFboIds[1];
        //render buffer id
        int[] tRenderIds = new int[1];
        GLES30.glGenRenderbuffers(1, tRenderIds, 0);
        mRenderId = tRenderIds[0];

        if(mLineWidth != 0){
            GLES30.glLineWidth(mLineWidth);
        }

        //普通FBO
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mNormalFboId);
        mTextureId = YCUtils.createBlankTexture2DId(mFboSize[0], mFboSize[1], GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mTextureId, 0);

        //多采样FBO
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mMultiSampleFboId);
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, mRenderId);
        GLES30.glRenderbufferStorageMultisample(GLES30.GL_RENDERBUFFER, 4, GLES30.GL_RGBA8, getWidth(), getHeight());
        GLES30.glFramebufferRenderbuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_RENDERBUFFER, mRenderId);
        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, 0);
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mDefFboId);

        if (GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER) != GLES30.GL_FRAMEBUFFER_COMPLETE) {
            destroy();
            return false;
        }
        return true;
    }

    public void setLineWidth(int lineWidth) {
        this.mLineWidth = lineWidth;
        GLES30.glLineWidth(lineWidth);
    }

    public int getTextureId() {
        return mTextureId;
    }

    public int getWidth() {
        return mFboSize[0];
    }

    public int getHeight() {
        return mFboSize[1];
    }

    public int getAngle() {
        return mAngle;
    }

    public void blit(){
        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, mMultiSampleFboId);
        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, mNormalFboId);

        GLES30.glBlitFramebuffer(0, 0, getWidth(), getHeight(), 0, 0, getWidth(), getHeight(), GLES30.GL_COLOR_BUFFER_BIT, GLES30.GL_NEAREST);

        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, 0);
        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, 0);
    }

    //绑定当前的FBO
    public void bind() {
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mMultiSampleFboId);
    }

    //释放绑定的FBO
    public void unbind() {
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mDefFboId);
    }

    //销毁
    public void destroy() {
        if (mMultiSampleFboId != 0) {
            GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mMultiSampleFboId);
            int[] tColorIds = new int[1];
            tColorIds[0] = mRenderId;
            GLES30.glDeleteRenderbuffers(1, tColorIds, 0);
            GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER, GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, 0, 0);
            YCUtils.deleteFrameBuffers(mMultiSampleFboId);
        }
        if(mTextureId != 0){
            YCUtils.deleteTexture(mTextureId);
        }
        mTextureId = 0;
        mRenderId = 0;
        mMultiSampleFboId = 0;
    }
}
