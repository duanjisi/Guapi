package com.guapi.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.text.DecimalFormat;

/**
 * Created by Johnny on 2017-11-10.
 */
public class BitmapCompare {
    private static final String DIFFERENT_SIZE = "differentSize";
    private static final String RESULT_FORMAT = "00.0%";

    public static String similarity(String url1, String url2) {
        Bitmap bm1 = BitmapFactory.decodeFile(url1);
        Bitmap bm2 = BitmapFactory.decodeFile(url2);
        return similarity(bm1, bm2);
    }

    public static String similarity(Bitmap bm1, Bitmap bm2) {
        final int bm1Width = bm1.getWidth();
        final int bm2Width = bm2.getWidth();

        final int bmHeight = bm1.getHeight();
        final int bm2Height = bm2.getHeight();

        if (bmHeight != bm2.getHeight() || bm1Width != bm2Width)
            return DIFFERENT_SIZE;

        int[] pixels1 = new int[bm1Width];
        int[] pixels2 = new int[bm2Width];

        int[] pixels3 = new int[bmHeight];
        int[] pixels4 = new int[bm2Height];

        reset();
        for (int i = 0; i < bmHeight; i++) {
            bm1.getPixels(pixels1, 0, bm1Width, 0, i, bm1Width, 1);
            bm2.getPixels(pixels2, 0, bm2Width, 0, i, bm2Width, 1);

            comparePixels(pixels1, pixels2, bm1Width);
        }

//        for (int i = 0; i < bm1Width; i++) {
//            bm1.getPixels(pixels3, 0, bmHeight, i, 0, 1, bmHeight);
//            bm2.getPixels(pixels4, 0, bm2Height, i, 0, 1, bm2Height);
//            comparePixels2(pixels3, pixels4, bmHeight);
//        }

        return percent(Count.sT + Count.mT, Count.sF + Count.sT + Count.mF + Count.mT);
//        return percent(Count.sT, Count.sF, Count.mT, Count.mF);
    }

    private static void comparePixels(int[] pixels1, int[] pixels2, int length) {
        for (int i = 0; i < length; i++) {
            int ya1 = rgbToGray(pixels1[i]);
            int ya2 = rgbToGray(pixels2[i]);
            if (Math.abs(ya1 - ya2) <= 20) {
                Count.sT++;
            } else {
                Count.sF++;
            }
//            if (pixels1[i] == pixels2[i]) {
//                Count.sT++;
//            } else {
//                Count.sF++;
//            }
        }
    }

    private static void comparePixels2(int[] pixels1, int[] pixels2, int length) {
        for (int i = 0; i < length; i++) {
            int ya1 = rgbToGray(pixels1[i]);
            int ya2 = rgbToGray(pixels2[i]);
            if (Math.abs(ya1 - ya2) <= 20) {
                Count.mT++;
            } else {
                Count.mF++;
            }
        }
    }

    private static String percent(int divisor, int dividend) {
        final double value = divisor * 1.0 / dividend;
        DecimalFormat df = new DecimalFormat(RESULT_FORMAT);
        return df.format(value);
    }

    private static String percent(int sT, int sF, int mT, int mF) {
        final double value1 = sT * 1.0 / (sT + sF);
        final double value2 = mT * 1.0 / (mT + mF);
        DecimalFormat df = new DecimalFormat(RESULT_FORMAT);
        return df.format(value1 * value2);
    }

    private static void reset() {
        Count.sT = 0;
        Count.sF = 0;

        Count.mT = 0;
        Count.mF = 0;
    }

    private static class Count {
        private static int sT;
        private static int sF;
        private static int mT;
        private static int mF;
    }


    /**
     * 灰度值计算
     *
     * @param pixels 像素
     * @return int 灰度值
     */
    public static int rgbToGray(int pixels) {
        return (int) (0.3 * Color.red(pixels) + 0.59 * Color.green(pixels) + 0.11 * Color.blue(pixels));
    }
}
