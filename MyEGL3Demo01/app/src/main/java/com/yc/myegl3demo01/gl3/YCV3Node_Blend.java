package com.yc.myegl3demo01.gl3;

import android.opengl.GLES20;

import com.yc.myegl3demo01.gl.YCProgram;
import com.yc.myegl3demo01.gl.YCUtils;

public class YCV3Node_Blend {

    private YCProgram mProgram = null;

    private final static String VS = "#version 300 es \n" +
            "layout(location = 0) in vec2 aPosition;\n" +
            "layout(location = 1) in vec2 aTextureCoordinate0;\n" +
            "out vec2 vTextureCoordinate0;\n" +
            "out vec4 sColor;\n" +
            "void main(){" +
            "    gl_Position = vec4(aPosition, 0.0, 1.0);\n" +
            "    vTextureCoordinate0 = aTextureCoordinate0;\n" +
            "}";
    private final static String FS = "#version 300 es \n" +
            "precision mediump float;\n" +
            "uniform sampler2D aTexture0;\n" +
            "uniform sampler2D aTexture1;\n" +
            "in vec2 vTextureCoordinate0;\n" +
            "layout(location = 0) out vec4 fragColor;\n" +
            "void main(){\n" +
            "    vec4 t1 = texture(aTexture0, vTextureCoordinate0);\n"+
            "    vec4 t2 = texture(aTexture1, vTextureCoordinate0);\n"+
            "    fragColor = t1 + t2; \n" +
            "}";

    private final float[] mVertexArray = new float[]{
            -1.0f, -1.0f,
            0.5f, -1.0f,
            0.5f, 0.5f,
            -1.0f, 0.5f
    };

    private final float[] mTextureArray = new float[]{
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f
    };

    private int mVertexBufferId = 0;
    private int mTextureBufferId = 0;
    private int mWidth, mHeight;
    private int mTextureId1 = -1;
    private int mTextureId2 = -1;

    public void init(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        mProgram = new YCProgram(VS, FS);
        mProgram.bind();

        mVertexBufferId = YCUtils.glGenBuffers(GLES20.GL_ARRAY_BUFFER, mVertexArray);
        mTextureBufferId = YCUtils.glGenBuffers(GLES20.GL_ARRAY_BUFFER, mTextureArray);

        GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram.getProgramId(), "aTexture0"), 0);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram.getProgramId(), "aTexture1"), 1);
    }

    public void setTextureId1(int textureId1) {
        this.mTextureId1 = textureId1;
    }

    public void setTextureId2(int textureId2) {
        this.mTextureId2 = textureId2;
    }

    public void uninit() {
        if (mVertexBufferId >= 0) {
            GLES20.glDeleteBuffers(1, new int[]{mVertexBufferId}, 0);
        }
        if (mTextureBufferId >= 0) {
            GLES20.glDeleteBuffers(1, new int[]{mTextureBufferId}, 0);
        }
        if (mTextureId1 >= 0) {
            YCUtils.deleteTexture(mTextureId1);
        }
        if (mTextureId2 >= 0) {
            YCUtils.deleteTexture(mTextureId2);
        }
        if (mProgram != null) {
            mProgram.release();
            mProgram = null;
        }
    }

    public void draw() {
        mProgram.bind();
        GLES20.glViewport(0, 0, mWidth, mHeight);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVertexBufferId);
        GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mTextureBufferId);
        GLES20.glVertexAttribPointer(1, 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(1);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId1);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId2);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);

        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glDisableVertexAttribArray(0);
        GLES20.glDisableVertexAttribArray(1);
        GLES20.glDisableVertexAttribArray(2);
    }
}
