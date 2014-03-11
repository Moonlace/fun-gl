package com.jcxavier.android.opengl.engine;

import android.graphics.Bitmap;

/**
 * @author jxav
 */
final class BitmapConfigHelper {

    private BitmapConfigHelper() {
        // can't be instantiated
    }

    static int getRedBytes(final Bitmap.Config config) {
        switch (config) {
            case RGB_565:
                return 5;

            case ARGB_4444:
                return 4;

            default:
                return 8;
        }
    }

    static int getGreenBytes(final Bitmap.Config config) {
        switch (config) {
            case RGB_565:
                return 6;

            case ARGB_4444:
                return 4;

            default:
                return 8;
        }
    }

    static int getBlueBytes(final Bitmap.Config config) {
        switch (config) {
            case RGB_565:
                return 5;

            case ARGB_4444:
                return 4;

            default:
                return 8;
        }
    }

    static int getAlphaBytes(final Bitmap.Config config) {
        switch (config) {
            case RGB_565:
                return 0;

            case ARGB_4444:
                return 4;

            default:
                return 8;
        }
    }
}
