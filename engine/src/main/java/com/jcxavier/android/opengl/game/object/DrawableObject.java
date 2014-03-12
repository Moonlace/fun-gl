package com.jcxavier.android.opengl.game.object;

import com.jcxavier.android.opengl.engine.shader.ColorShaderProgram;
import com.jcxavier.android.opengl.engine.shader.ShaderCache;
import com.jcxavier.android.opengl.math.Matrix4;
import com.jcxavier.android.opengl.math.Vector2;
import com.jcxavier.android.opengl.math.Vector3;
import com.jcxavier.android.opengl.math.Vector4;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;

/**
 * Created on 11/03/2014.
 *
 * @author João Xavier <jcxavier@jcxavier.com>
 */
public class DrawableObject extends GameObject {

    private final Matrix4 mMvpMatrix;
    private final ColorShaderProgram mShader;
    private int mVertexBufferHandle;
    private FloatBuffer mVertexBuffer;

    private final Vector4 mColor;

    public DrawableObject() {
        mColor = new Vector4();
        mMvpMatrix = new Matrix4();

        mShader = (ColorShaderProgram) ShaderCache.getSharedShaderCache().getShaderProgram(ColorShaderProgram.class);

        generateBuffers();
        populateBuffers(true);
    }

    protected void generateBuffers() {
        int[] buffers = { 0 };
        glGenBuffers(1, buffers, 0);
        mVertexBufferHandle = buffers[0];
    }

    protected void populateBuffers(final boolean fromScratch) {
        if (fromScratch) {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 4 * 4);
            byteBuffer.order(ByteOrder.nativeOrder());
            byteBuffer.position(0);
            mVertexBuffer = byteBuffer.asFloatBuffer();
        }

        updateBufferInformation();

        glBindBuffer(GL_ARRAY_BUFFER, mVertexBufferHandle);

        mVertexBuffer.position(0);

        if (fromScratch) {
            glBufferData(GL_ARRAY_BUFFER, mVertexBuffer.capacity() * 4, mVertexBuffer, GL_DYNAMIC_DRAW);
        } else {
            glBufferSubData(GL_ARRAY_BUFFER, 0, mVertexBuffer.capacity() * 4, mVertexBuffer);
        }
    }

    private void updateBufferInformation() {
        mVertexBuffer.position(0);

        // 0 ---- 3
        //
        // |  \   |
        // |   \  |
        //
        // 1 ---- 2

        final float halfWidth = mSize.x / 2;
        final float halfHeight = mSize.y / 2;

        for (int i = 0; i < 4; i++) {
            if (i == 0 || i == 1) {
                mVertexBuffer.put(-halfWidth);
            } else {
                mVertexBuffer.put(halfWidth);
            }

            if (i == 0 || i == 3) {
                mVertexBuffer.put(-halfHeight);
            } else {
                mVertexBuffer.put(halfHeight);
            }

            mVertexBuffer.put(0.0f);
            mVertexBuffer.put(1.0f);
        }
    }

    @Override
    public void update(Matrix4 projectionMatrix) {
        mMvpMatrix.set(projectionMatrix);
        mMvpMatrix.multiply(mModelMatrix);

        mColor.w = mAlpha;
    }

    @Override
    public void draw() {
        glUseProgram(mShader.getProgram());

        // additive alpha blending
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBlendEquation(GL_FUNC_ADD);

        glBindBuffer(GL_ARRAY_BUFFER, mVertexBufferHandle);

        mShader.setAttributePointers();
        mShader.setMVPMatrixUniform(mMvpMatrix.m);
        mShader.setColorUniform(mColor);

        glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
    }

    public void setColor(final Vector3 color) {
        mColor.x = color.x;
        mColor.y = color.y;
        mColor.z = color.z;
    }

    @Override
    public void setSize(final Vector2 size) {
        super.setSize(size);
        populateBuffers(false);
    }
}