package com.yc.myegl3demo01.gl;

import android.opengl.GLES20;
import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by ldm on 17/1/22.
 * GL YCProgram Helper
 */

public class YCProgram {

    private int mProgramId = -1;
    private int mVertexShader = -1;
    private int mFragmentShader = -1;

    public YCProgram(String pvs, String fvs) {
        mProgramId = createProgram(pvs, fvs);
    }

    public int getProgramId() {
        return mProgramId;
    }

    /**
     * 创建Program
     *
     * @param pvs 顶点shader
     * @param fvs 片段shader
     * @return programId
     */
    private int createProgram(String pvs, String fvs) {
        int program = GLES20.glCreateProgram();
        YCUtils.checkGLError("glCreateProgram");
        if (program == 0) {
            Log.e(YCProgram.class.getSimpleName(), "Invalid YCProgram ID! Check if the context is binded!");
            return -1;
        }

        mVertexShader = loadShader(GLES20.GL_VERTEX_SHADER, pvs);
        mFragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fvs);

        GLES20.glAttachShader(program, mVertexShader);
        YCUtils.checkGLError("glAttachShader mVertexShader");
        GLES20.glAttachShader(program, mFragmentShader);
        YCUtils.checkGLError("glAttachShader mVertexShader");
        GLES20.glLinkProgram(program);
        YCUtils.checkGLError("glLinkProgram");
        return program;
    }


    /**
     * 加载shader
     *
     * @param type       类型
     * @param shaderCode shader脚本
     * @return shaderId
     */
    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        YCUtils.checkGLError("glCreateShader");
        GLES20.glShaderSource(shader, shaderCode);
        YCUtils.checkGLError("glShaderSource");
        GLES20.glCompileShader(shader);
        YCUtils.checkGLError("glCompileShader");
        int[] compiled = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.e("YCProgram", GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return 0;
        }
        return shader;
    }

    public void bind() {
        GLES20.glUseProgram(mProgramId);
        YCUtils.checkGLError("glUseProgram mProgramId = " + mProgramId);
    }

    public void release() {
        if (mProgramId != -1) {
            GLES20.glDeleteProgram(mProgramId);
        }
        if (mVertexShader != -1) {
            GLES20.glDeleteShader(mVertexShader);
        }
        if (mFragmentShader != -1) {
            GLES20.glDeleteShader(mFragmentShader);
        }
    }
}
