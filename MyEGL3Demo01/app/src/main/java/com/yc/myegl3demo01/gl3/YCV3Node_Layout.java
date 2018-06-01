package com.yc.myegl3demo01.gl3;

import android.opengl.GLES30;
import android.opengl.GLES30;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.yc.myegl3demo01.gl.YCProgram;
import com.yc.myegl3demo01.gl.YCUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static android.opengl.GLES30.GL_UNIFORM_BLOCK_DATA_SIZE;


/**
 * 测试统一变量
 */
public class YCV3Node_Layout {

    private YCProgram mProgram = null;

    private final static String VS = "#version 300 es \n" +
            "layout(location = 0) in vec2 aPosition;\n" +
            "layout(location = 1) in vec2 aOffXY;\n" +
            "uniform InColors {\n" +
            "  vec4 aColor1; \n" +
            "  vec4 aColor2; \n" +
            "}; \n" +
            "uniform InWeight { \n" +
            "  vec2 weight;" +
            "};\n" +
            "out vec4 sColor;\n" +
            "void main(){" +
            "   gl_Position = vec4(aPosition.x + aOffXY.x, aPosition.y + aOffXY.y, 0.0, 1.0);\n" +
            "   sColor = (aColor1 + aColor2) * vec4(weight.xy, 1.0, 1.0);\n" +
            "}";
    private final static String FS = "#version 300 es \n" +
            "precision mediump float;\n" +
            "in vec4 sColor;" +
            "out vec4 fragColor;\n"+
            "void main(){\n" +
            "   fragColor = sColor;\n" +
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

    private int mShaderPositionId = 0;
    private int mVertexBufferId = 0;
    private int mOffXYBufferId = 0;
    private int mWidth, mHeight;

    public void init(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        mProgram = new YCProgram(VS, FS);
        mProgram.bind();
        //如果aPosition已经绑定，返回绑定的索引 如果没有绑定返回-1 绑定可以使用layout(location = 0)或GLES30.glBindAttribLocation(...);
//        int aPosition = GLES30.glGetAttribLocation(mProgram.getProgramId(), "aPosition");
//        int aOffXY = GLES30.glGetAttribLocation(mProgram.getProgramId(), "aOffXY");
//        int aOffXY1 = GLES30.glGetAttribLocation(mProgram.getProgramId(), "aOffXY1");
//        int aOffXY2 = GLES30.glGetAttribLocation(mProgram.getProgramId(), "aOffXY2");
//        Log.e("cccccccccccccc", "aPosition = "+aPosition);
//        Log.e("cccccccccccccc", "aOffXY = "+aOffXY);
//        Log.e("cccccccccccccc", "aOffXY1 = "+aOffXY1);
//        Log.e("cccccccccccccc", "aOffXY2 = "+aOffXY2);
        mVertexBufferId = YCUtils.glGenBuffers(GLES30.GL_ARRAY_BUFFER, mVertexArray);
        mOffXYBufferId = YCUtils.glGenBuffers(GLES30.GL_ARRAY_BUFFER, mNormalArray);
        //---------color----------
        int tInColorsBlockIndex = GLES30.glGetUniformBlockIndex(mProgram.getProgramId(), "InColors");
        int[] tInColorsBlockSize = new int[1];
        GLES30.glGetActiveUniformBlockiv(mProgram.getProgramId(), tInColorsBlockIndex, GL_UNIFORM_BLOCK_DATA_SIZE, tInColorsBlockSize, 0);

        int tInColorsBuffer = YCUtils.glGenBuffers(GLES30.GL_UNIFORM_BUFFER, new float[]{1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f});
        GLES30.glUniformBlockBinding(mProgram.getProgramId(), tInColorsBlockIndex, 0);
//        GLES30.glBindBufferRange(GLES30.GL_UNIFORM_BUFFER, 0, tInColorsBuffer[0], 0, tInColorsBlockSize[0]);
        GLES30.glBindBufferBase(GLES30.GL_UNIFORM_BUFFER, tInColorsBlockIndex, tInColorsBuffer);

        //---------weight----------
        int tInWeightBlockIndex = GLES30.glGetUniformBlockIndex(mProgram.getProgramId(), "InWeight");
        int[] tInWeightBlockSize = new int[1];
        GLES30.glGetActiveUniformBlockiv(mProgram.getProgramId(), tInWeightBlockIndex, GL_UNIFORM_BLOCK_DATA_SIZE, tInWeightBlockSize, 0);

        int tInWeightBuffer = YCUtils.glGenBuffers(GLES30.GL_UNIFORM_BUFFER, new float[]{0.5f, 0.5f});
        GLES30.glUniformBlockBinding(mProgram.getProgramId(), tInWeightBlockIndex, 0);
//        GLES30.glBindBufferRange(GLES30.GL_UNIFORM_BUFFER, 0, tInColorsBuffer[0], 0, tInWeightBlockSize[0]);
        GLES30.glBindBufferBase(GLES30.GL_UNIFORM_BUFFER, tInWeightBlockIndex, tInWeightBuffer);

    }

    public void uninit() {
        if (mProgram != null) {
            mProgram.release();
            mProgram = null;
        }
        if(mVertexBufferId >= 0){
            GLES30.glDeleteBuffers(1, new int[]{mVertexBufferId}, 0);
        }
        if(mOffXYBufferId >= 0){
            GLES30.glDeleteBuffers(1, new int[]{mOffXYBufferId}, 0);
        }
    }

    public void draw() {
        mProgram.bind();
        GLES30.glViewport(0, 0, mWidth, mHeight);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        GLES30.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

//        GLES30.glVertexAttrib4fv(0, );

        //2.0填充数据
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);
        GLES30.glVertexAttribPointer(0, 2, GLES30.GL_FLOAT, false, 0, 0);
        GLES30.glEnableVertexAttribArray(0);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mOffXYBufferId);
        GLES30.glVertexAttribPointer(1, 2, GLES30.GL_FLOAT, false, 0, 0);
        GLES30.glEnableVertexAttribArray(1);

        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_FAN, 0, 4);
        GLES30.glDisableVertexAttribArray(0);
        GLES30.glDisableVertexAttribArray(1);
    }
}
