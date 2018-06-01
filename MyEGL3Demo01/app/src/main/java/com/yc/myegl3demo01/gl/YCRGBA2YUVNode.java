package com.yc.myegl3demo01.gl;

import android.opengl.GLES20;

/**
 * Created by lidm on 17/5/23.
 * 绘制RGBA纹理
 */

public class YCRGBA2YUVNode extends YCNode {


    public static final String TEXTURE_VERTEX_SHADER = "attribute vec2 aPosition;" +
            "attribute vec2 aTextureCoordinate;" +
            "varying vec2 vTextureCoordinate;" +
            "uniform mat2 uRotation;" +
            "uniform vec2 uFlipScale;" +
            "void main() {" +
            "gl_Position = vec4(aPosition.x, aPosition.y, 0.0, 1.0);" +
            "vTextureCoordinate = aTextureCoordinate;" +
            "}";


    private static final String FRAGMENT_SHADER =
            "            precision highp float;\n" +
            "            varying vec2 vTextureCoordinate;\n" +
            "            uniform sampler2D inputImageTexture;\n" +
            "            uniform float width;\n" +
            "            uniform float height;\n" +
            "            void main() {\n" +
            "                vec3 offset = vec3(0.0625, 0.5, 0.5);\n" +
            "                vec3 ycoeff = vec3(0.256816, 0.504154, 0.0979137);\n" +
            "                vec3 ucoeff = vec3(-0.148246, -0.29102, 0.439266);\n" +
            "                vec3 vcoeff = vec3(0.439271, -0.367833, -0.071438);\n" +
            "                vec2 nowTxtPos = vTextureCoordinate;\n" +
            "                vec2 size = vec2(width, height);\n" +
            "                vec2 yScale = vec2(4, 4);\n" +
            "                vec2 uvScale = vec2(4, 4);\n" +
            "                if (nowTxtPos.y < 0.25) {\n" +
            "                    vec2 basePos = nowTxtPos * yScale * size;\n" +
            "                    float addY = float(int((basePos.x) / width));\n" +
            "                    basePos.x -= addY * width;\n" +
            "                    basePos.y += addY;\n" +
            "                    float y1, y2, y3, y4;\n" +
            "                    vec2 samplingPos = basePos / size;\n" +
            "                    vec4 texel = texture2D(inputImageTexture, samplingPos);\n" +
            "                    y1 = dot(texel.rgb, ycoeff);\n" +
            "                    y1 += offset.x;\n" +
            "                    basePos.x += 1.0;\n" +
            "                    samplingPos = basePos / size;\n" +
            "                    texel = texture2D(inputImageTexture, samplingPos);\n" +
            "                    y2 = dot(texel.rgb, ycoeff);\n" +
            "                    y2 += offset.x;\n" +
            "                    basePos.x += 1.0;\n" +
            "                    samplingPos = basePos / size;\n" +
            "                    texel = texture2D(inputImageTexture, samplingPos);\n" +
            "                    y3 = dot(texel.rgb, ycoeff);\n" +
            "                    y3 += offset.x;\n" +
            "                    basePos.x += 1.0;\n" +
            "                    samplingPos = basePos / size;\n" +
            "                    texel = texture2D(inputImageTexture, samplingPos);\n" +
            "                    y4 = dot(texel.rgb, ycoeff);\n" +
            "                    y4 += offset.x;\n" +
            "                    gl_FragColor = vec4(y1, y2, y3, y4);\n" +
            "                }\n" +
            "                else if (nowTxtPos.y <=0.375) {\n" +
            "                    nowTxtPos.y -= 0.25;\n" +
            "                    vec2 basePos = nowTxtPos * uvScale * size;\n" +
            "                    float addY = float(int((basePos.x) / width));\n" +
            "                    basePos.x -= addY * width;\n" +
            "                    basePos.y += addY;\n" +
            "                    basePos.y *= 2.0;\n" +
            "                    basePos -= clamp(uvScale * 0.5 , vec2(0.0), uvScale);\n" +
            "                    basePos.x -= 2.0;\n" +
            "                    vec4 sample = texture2D(inputImageTexture, basePos / size).rgba;\n" +
            "                    float u1 = dot(sample.rgb, vcoeff);\n" +
            "                    u1 += offset.y;\n" +
            "                    sample = texture2D(inputImageTexture, basePos / size).rgba;\n" +
            "                    float u2 = dot(sample.rgb, ucoeff);\n" +
            "                    u2 += offset.y;\n" +
            "                    basePos.x += 2.0;\n" +
            "                    sample = texture2D(inputImageTexture, basePos / size).rgba;\n" +
            "                    float u3 = dot(sample.rgb, vcoeff);\n" +
            "                    u3 += offset.y;\n" +
            "                    sample = texture2D(inputImageTexture, basePos / size).rgba;\n" +
            "                    float u4 = dot(sample.rgb, ucoeff);\n" +
            "                    u4 += offset.y;\n" +
            "                    gl_FragColor = vec4(u1, u2, u3, u4);\n" +
            "                }\n" +
            "            }";

    private int mVertexBufferId;
    private int mTextureBufferId;
    private int mWidth;
    private int mHeight;
    private int mTextureId;

    private YCRGBA2YUVNode() {

    }

    public static YCRGBA2YUVNode create() {
        return create(0, 0);
    }

    public static YCRGBA2YUVNode create(int width, int height) {
        YCRGBA2YUVNode node = new YCRGBA2YUVNode();
        node.mWidth = width;
        node.mHeight = height;
        if (node.init(TEXTURE_VERTEX_SHADER, FRAGMENT_SHADER)) {
            return node;
        }
        node.destroy();
        return null;
    }

    @Override
    protected void onBindShader() {
        GLES20.glUniform1i(GLES20.glGetUniformLocation(getProgramId(), "inputImageTexture"), 0);

        int tWidth = GLES20.glGetUniformLocation(getProgramId(), "width");
        int tHeight = GLES20.glGetUniformLocation(getProgramId(), "height");
        GLES20.glUniform1f(tWidth, 720);
        GLES20.glUniform1f(tHeight, 1280);

        mVertexBufferId = createDefaultVertexBuffer();
        mTextureBufferId = createDefaultTextureBuffer();

        setFlipScale(1, 1);
    }


    public void setTextureId(int textureId) {
        this.mTextureId = textureId;
    }

    @Override
    public void draw() {
        if (mTextureId > 0) {
            bindProgram();
            if (mWidth != 0) {
                GLES20.glViewport(0, 0, mWidth, mHeight);
            }
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);

            YCUtils.subVertexAttribPointer(getShaderPositionId(), 2, mVertexBufferId);
            YCUtils.subVertexAttribPointer(getShaderTextureCoordinateId(), 2, mTextureBufferId);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
        }
    }
}
