package com.jcxavier.android.opengl.math;

import android.opengl.Matrix;

/**
 * Created on 11/03/2014.
 *
 * @author João Xavier <jcxavier@jcxavier.com>
 */
public final class Matrix4 {

    public final float[] m;

    public Matrix4() {
        m = new float[16];
        Matrix.setIdentityM(m, 0);
    }

    public void setIdentity() {
        Matrix.setIdentityM(m, 0);
    }

    public void translate(final Vector3 pos) {
        Matrix.translateM(m, 0, pos.x, pos.y, pos.z);
    }

    public void scale(final Vector3 scale) {
        Matrix.scaleM(m, 0, scale.x, scale.y, scale.z);
    }
}
