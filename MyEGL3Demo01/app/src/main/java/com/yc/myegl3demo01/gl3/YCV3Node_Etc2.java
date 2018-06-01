package com.yc.myegl3demo01.gl3;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLES20;
import android.util.Log;

import com.yc.myegl3demo01.gl.YCFbo;
import com.yc.myegl3demo01.gl.YCProgram;
import com.yc.myegl3demo01.gl.YCUtils;

import java.io.File;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import static android.opengl.GLES30.GL_UNIFORM_BLOCK_DATA_SIZE;

public class YCV3Node_Etc2 {

    static {
        System.loadLibrary("native-lib");
    }

    private YCProgram mProgram = null;

    private final static String VS = "#version 300 es \n" +
            "layout(location = 0) in vec2 aPosition;\n" +
            "layout(location = 3) in vec2 aOffXY;\n" +
            "layout(location = 5) in vec2 aTextureCoordinate0;\n" +
            "out vec2 vTextureCoordinate0;\n" +
            "out vec4 sColor;\n" +
            "void main(){" +
            "    gl_Position = vec4(aPosition.x + aOffXY.x, aPosition.y + aOffXY.y, 0.0, 1.0);\n" +
            "    vTextureCoordinate0 = aTextureCoordinate0;\n" +
            "}";
    private final static String FS = "#version 300 es \n" +
            "precision mediump float;\n" +
            "uniform sampler2D aTexture0;\n" +
            "in vec2 vTextureCoordinate0;\n" +
            "layout(location = 0) out vec4 fragColor;\n" +
            "void main(){\n" +
            "    fragColor = texture(aTexture0, vTextureCoordinate0);\n" +
//            "    fragColor = vec4(0.0, 1.0, 1.0, 1.0);\n"+
            "}";

    private final float[] mVertexArray = new float[]{
            -1.0f, -1.0f,
            0.5f, -1.0f,
            0.5f, 0.5f,
            -1.0f, 0.5f
    };

    private final float[] mNormalArray = new float[]{
            0.1f, 0.1f,
            0.1f, 0.1f,
            0.1f, 0.1f,
            0.1f, 0.1f
    };

    private final float[] mTextureArray = new float[]{
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f
    };


    private int mVertexBufferId = 0;
    private int mOffXYBufferId = 0;
    private int mTextureBufferId = 0;
    private int mWidth, mHeight;
    private int mTextureId1 = -1;
    private int mTextureId2 = -1;
    private int mTextureWidth = 720;
    private int mTextureHeight = 1280;

    public void init(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        mProgram = new YCProgram(VS, FS);
        mProgram.bind();

        mVertexBufferId = YCUtils.glGenBuffers(GLES20.GL_ARRAY_BUFFER, mVertexArray);
        mOffXYBufferId = YCUtils.glGenBuffers(GLES20.GL_ARRAY_BUFFER, mNormalArray);
        mTextureBufferId = YCUtils.glGenBuffers(GLES20.GL_ARRAY_BUFFER, mTextureArray);

        GLES20.glUniform1i(GLES20.glGetUniformLocation(mProgram.getProgramId(), "aTexture0"), 0);

    }

    public AssetManager assetManager;

    public void setTextureData(Buffer textureData) {
        int[] textures = new int[1];
        if (mTextureId1 == -1) {
            GLES20.glGenTextures(1, textures, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0]);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, mTextureWidth, mTextureHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, textureData);
        } else {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId1);
            GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D, 0, 0, 0, mTextureWidth, mTextureHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, textureData);
        }
        mTextureId1 = textures[0];


        mTextureId2 = loadTextureETC2_KTX(assetManager, "weinisiheceng.ktx");
    }

    public void uninit() {
        if (mVertexBufferId >= 0) {
            GLES20.glDeleteBuffers(1, new int[]{mVertexBufferId}, 0);
        }
        if (mOffXYBufferId >= 0) {
            GLES20.glDeleteBuffers(1, new int[]{mOffXYBufferId}, 0);
        }
        if (mTextureBufferId >= 0) {
            GLES20.glDeleteBuffers(1, new int[]{mTextureBufferId}, 0);
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
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        int texId;
        texId = mTextureId2;
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVertexBufferId);
        GLES20.glVertexAttribPointer(0, 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mOffXYBufferId);
        GLES20.glVertexAttribPointer(3, 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(3);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mTextureBufferId);
        GLES20.glVertexAttribPointer(5, 2, GLES20.GL_FLOAT, false, 0, 0);
        GLES20.glEnableVertexAttribArray(5);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);

        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glDisableVertexAttribArray(0);
        GLES20.glDisableVertexAttribArray(1);
        GLES20.glDisableVertexAttribArray(2);

    }


    public native int loadTextureETC2_KTX(AssetManager assetManager, String fileName);

}
