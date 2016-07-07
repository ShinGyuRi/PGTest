package com.moyusoft.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

/**
 * Created by wonderland on 16. 2. 12..
 */
public class ImageFilterUtil {

    public static Bitmap updateBrightness(Bitmap src, int value) {
        // original image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // increase/decrease each channel
                R += value;
                if (R > 255) {
                    R = 255;
                } else if (R < 0) {
                    R = 0;
                }

                G += value;
                if (G > 255) {
                    G = 255;
                } else if (G < 0) {
                    G = 0;
                }

                B += value;
                if (B > 255) {
                    B = 255;
                } else if (B < 0) {
                    B = 0;
                }

                // apply new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }

    public static Bitmap updateContrast(Bitmap src, double value) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap

        // create a mutable empty bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

        // create a canvas so that we can draw the bmOut Bitmap from source bitmap
        Canvas c = new Canvas();
        c.setBitmap(bmOut);

        // draw bitmap to bmOut from src bitmap so we can modify it
        c.drawBitmap(src, 0, 0, new Paint(Color.BLACK));


        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int) (((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (R < 0) {
                    R = 0;
                } else if (R > 255) {
                    R = 255;
                }

                G = Color.green(pixel);
                G = (int) (((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (G < 0) {
                    G = 0;
                } else if (G > 255) {
                    G = 255;
                }

                B = Color.blue(pixel);
                B = (int) (((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if (B < 0) {
                    B = 0;
                } else if (B > 255) {
                    B = 255;
                }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
        return bmOut;
    }

    public static Bitmap updateSaturation(Bitmap src, float settingSat) {

        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap bitmapResult =
                Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvasResult = new Canvas(bitmapResult);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(settingSat);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(filter);
        canvasResult.drawBitmap(src, 0, 0, paint);
        return bitmapResult;
    }

    public static Bitmap updateCMY(Bitmap src) {

        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap bitmapResult =
                Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvasResult = new Canvas(bitmapResult);
        Paint paint = new Paint();
        ColorFilter yellow = new LightingColorFilter(Color.YELLOW, 40);
        paint.setColorFilter(yellow);
        canvasResult.drawBitmap(src, 0, 0, paint);
//        ColorFilter cyan = new LightingColorFilter(Color.CYAN, 40);
//        paint.setColorFilter(cyan);
//        canvasResult.drawBitmap(src, 0, 0, paint);
//        ColorFilter mageta = new LightingColorFilter(Color.MAGENTA, 40);
//        paint.setColorFilter(mageta);

        return bitmapResult;
    }

    public static Bitmap updateDRGV(Bitmap src, int depth, double red, double green, double blue) {

        int width = src.getWidth();
        int height = src.getHeight();

        Bitmap finalBitmap = Bitmap.createBitmap(width, height, src.getConfig());

        final double grayScale_Red = 0.3;
        final double grayScale_Green = 0.59;
        final double grayScale_Blue = 0.11;

        int channel_aplha, channel_red, channel_green, channel_blue;
        int pixel;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {

                pixel = src.getPixel(x, y);
                channel_aplha = Color.alpha(pixel);
                channel_red = Color.red(pixel);
                channel_green = Color.green(pixel);
                channel_blue = Color.blue(pixel);

                channel_blue = channel_green = channel_red = (int) (grayScale_Red * channel_red + grayScale_Green * channel_green + grayScale_Blue * channel_blue);
                channel_red += (depth * red);
                if (channel_red > 255) {
                    channel_red = 255;
                }
                channel_green += (depth * green);
                if (channel_green > 255) {
                    channel_green = 255;
                }
                channel_blue += (depth * blue);
                if (channel_blue > 255) {
                    channel_blue = 255;
                }

                finalBitmap.setPixel(x, y, Color.argb(channel_aplha, channel_red, channel_green, channel_blue));
            }
        }
        return finalBitmap;
    }

    public static Bitmap updateExposure(Bitmap src, float value) {

        int w = src.getWidth();
        int h = src.getHeight();

        Bitmap bitmapResult =
                Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvasResult = new Canvas(bitmapResult);
        Paint paint = new Paint();
        float pow = (float) Math.pow(2, value);
        ColorMatrix exposureMatrix = new ColorMatrix(new float[]
                {
                        pow, 0, 0, 0, 0,
                        0, pow, 0, 0, 0,
                        0, 0, pow, 0, 0,
                        0, 0, 0, 1, 0
                });
        ColorFilter exposureColorFilter = new ColorMatrixColorFilter(exposureMatrix);
        paint.setColorFilter(exposureColorFilter);
        canvasResult.drawBitmap(src, 0, 0, paint);

        return bitmapResult;
    }
}
