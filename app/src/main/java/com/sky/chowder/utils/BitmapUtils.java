package com.sky.chowder.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;

/**
 * Created by SKY on 2017/9/30 15:17.
 */
public class BitmapUtils {
    /**
     * 获得带倒影的图片方法
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
        return bitmapWithReflection;
    }

    /**
     * 创建新图片
     *
     * @param bitmap     图片
     * @param huergb     色相
     * @param lum        亮度
     * @param saturation 饱和度
     * @return bmp
     */
    public static Bitmap creatNewBitmap(Bitmap bitmap, float huergb, float saturation, float lum) {
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //* <code>axis=0</code> correspond to a rotation around the RED color
        //* <code>axis=1</code> correspond to a rotation around the GREEN color
        //* <code>axis=2</code> correspond to a rotation around the BLUE color
        //设置色相
        ColorMatrix hueMatrix = new ColorMatrix();
        hueMatrix.setRotate(0, huergb);
        hueMatrix.setRotate(1, huergb);
        hueMatrix.setRotate(2, huergb);
        //设置亮度
        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(saturation);
        //设置饱和度
        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(lum, lum, lum, 1);
        //综合在一起
        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(hueMatrix);
        imageMatrix.postConcat(saturationMatrix);
        imageMatrix.postConcat(lumMatrix);
        //设置画笔
        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(bmp, 0, 0, paint);//画出bitmap
        return bmp;
    }

    /**
     * 创建底片效果的图片
     *
     * @param bitmap 图片
     * @return bmp
     */
    public static Bitmap createImageNegative(Bitmap bitmap) {
        //获取宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //获取图片的像素值
        int[] oldPx = new int[width * height];
        bitmap.getPixels(oldPx, 0, width, 0, 0, width, height);
        //取出像素中的ARGB
        int[] newPx = new int[width * height];
        //避免在for循环中频繁创建销毁对象，达到对象的重用
        int r, g, b, a, color;
        //计算公式 new.r=255-old.r,同理gb一样，不算a
        for (int i = 0; i < width * height; i++) {
            //获取原来的rgb
            color = oldPx[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);

            //获取新的rgb，并判断是否超出0-255的界限,写入newPx中
            newPx[i] = Color.argb(a, check(255 - r), check(255 - g), check(255 - b));
        }
        //创建新图片
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //为新图片赋值
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;
    }

    /**
     * 创建怀旧效果的图片
     *
     * @param bitmap 图片
     * @return bmp
     */
    public static Bitmap createImageOldPhoto(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] oldPx = new int[width * height];
        bitmap.getPixels(oldPx, 0, width, 0, 0, width, height);
        int[] newPx = new int[width * height];
        //避免在for循环中频繁创建销毁对象，达到对象的重用
        int r, g, b, a, color;
        for (int i = 0; i < width * height; i++) {
            color = oldPx[i];
            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);
            a = Color.alpha(color);
            //计算并检查，公式：（int）（0.393 * oldr + 0.769 * oldg + 0.189 * oldb）
            newPx[i] = Color.argb(a,
                    check((int) (0.393 * r + 0.769 * g + 0.189 * b)),
                    check((int) (0.349 * r + 0.686 * g + 0.168 * b)),
                    check((int) (0.272 * r + 0.534 * g + 0.131 * b)));
        }
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        return bmp;
    }

    /**
     * 创建浮雕效果的图片
     *
     * @param bitmap 图片
     * @return bmp
     */
    public static Bitmap createImagePixelsrelier(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] oldPx = new int[width * height];
        bitmap.getPixels(oldPx, 0, width, 0, 0, width, height);
        int[] newPx = new int[width * height];
        //避免在for循环中频繁创建销毁对象，达到对象的重用
        int r, g, b, a;
        int r1, g1, b1;
        int color, colorBefore;
        for (int i = 1; i < width * height; i++) {
            colorBefore = oldPx[i - 1];
            r = Color.red(colorBefore);
            g = Color.green(colorBefore);
            b = Color.blue(colorBefore);
            a = Color.alpha(colorBefore);

            color = oldPx[i];
            r1 = Color.red(color);
            g1 = Color.green(color);
            b1 = Color.blue(color);

            newPx[i] = Color.argb(a, check(r - r1 + 127), check(g - g1 + 127), check(b - b1 + 127));
        }
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        bmp.setPixels(newPx, 0, width, 0, 0, width, height);
        //不清楚是否需要释放，待学习
        recyleBitmap(bitmap);
        return bmp;
    }

    /**
     * 判断是否在1-255的范围之内
     *
     * @param argb
     * @return
     */
    private static int check(int argb) {
        if (argb > 255) {
            argb = 255;
        } else if (argb < 0) {
            argb = 0;
        }
        return argb;
    }

    /**
     * 释放bitmap
     *
     * @param bitmap
     */
    public static void recyleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
    }

}
