package com.sky.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by SKY on 2017/6/6.
 * bitmap工具类
 */
public class ImageUtils {

    /**
     * 从资源中获取Bitmap
     *
     * @param context
     * @param id
     * @return
     */
    public static Bitmap getBitmapFromId(Context context, int id) {
        return BitmapFactory.decodeResource(context.getResources(), id);
    }

    /**
     * 获取最小资源的bitmap，参考一下就行，一般用不着
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap getBitmapFromRes(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
        return bitmap;
    }

    /**
     * 从文件路径获取bitmap,BitmapFactory.Options
     *
     * @param path
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap getBitmapFromPath(String path, int newWidth, int newHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        //默认就是ARGB_8888
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.decodeFile(path, opts);
        // 调用上面定义的方法计算inSampleSize值
        opts.inSampleSize = getInSampleSize(opts, newWidth, newHeight);
        opts.inJustDecodeBounds = false;
        //节省内存
        opts.inPurgeable = true;
        opts.inInputShareable = true;
//        opts.inTempStorage = new byte[5 * 1024 * 1024];

        LogUtils.i("应用空闲的内存==" + Runtime.getRuntime().freeMemory() / 1024 + "KB");
        LogUtils.i("应用可用最大内存==" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB");
        LogUtils.i("应用当前总内存==" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "MB");

        try {
            return BitmapFactory.decodeFile(path, opts);
        } catch (OutOfMemoryError error) {
            System.gc();
            LogUtils.i("OutOfMemoryError");
        }
        return null;
    }


    /**
     * 计算InSampleSize的值==old/new
     *
     * @param newWidth
     * @param newHeight
     * @param opts
     * @return
     */
    private static int getInSampleSize(BitmapFactory.Options opts, int newWidth, int newHeight) {
        //oldWidth/oldHeight==newWidth/newHeight
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
//            newHeight = newWidth*height/width;
        if (width > newWidth || height > newHeight) {
            // 缩放
            scaleWidth = ((float) width) / newWidth;
            scaleHeight = ((float) height) / newHeight;
        }
        return (int) Math.ceil(Math.max(scaleWidth, scaleHeight));
    }

    /**
     * matrix 缩放/裁剪图片
     *
     * @param bm        图片
     * @param newWidth  新的宽度
     * @param newHeight 新的高度
     * @return 裁剪后的图片
     */
    public static Bitmap scaleBitmap(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例；缩放率X*width =newWidth；
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    /**
     * 获取bitmap的大小
     *
     * @param bitmap
     * @return
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//API 19
            return bitmap.getAllocationByteCount();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();//earlier version
    }

    /**
     * 保存bitmap到file
     *
     * @param bmp
     * @param filename
     * @return
     */
    public static boolean saveBitmap2file(Context context, Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(context.getExternalCacheDir().getAbsolutePath() + "/" + filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmp.compress(format, quality, stream);
    }

    /**
     * bitmap转换为drawable
     *
     * @param context
     * @param toBitmap
     * @return 转换后的drawable
     */
    public static Drawable getDrawableFromBitmap(Context context, Bitmap toBitmap) {
        return new BitmapDrawable(context.getResources(), toBitmap);
    }

    /**
     * 从drawable中获取bitmap图片
     *
     * @param drawable
     * @return
     */
    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) return null;

        if (drawable instanceof BitmapDrawable) return ((BitmapDrawable) drawable).getBitmap();

        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888);
                //drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    /**
     * 获取网络图片
     *
     * @param urlStr
     * @return
     */
    public static Bitmap getBitmapFromUrl(String urlStr) {
        Bitmap bitmap;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(connection.getInputStream());
            bitmap = BitmapFactory.decodeStream(inputStream);
            connection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 把base64转换成bitmap
     *
     * @param base64 base64 的字符串
     * @return
     */
    public static Bitmap getBitmapFromBase64(String base64) {
        if (base64 == null || base64.isEmpty()) return null;
        try {
            byte[] bitmapArray = Base64.decode(base64, Base64.DEFAULT);
            return getBitmapFromBytes(bitmapArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把bitmap转换成base64
     *
     * @param bitmap
     * @param quality 压缩百分比1-100,100代表不压缩
     * @return
     */
    public static String getBase64FromBitmap(Bitmap bitmap, int quality) {
        return Base64.encodeToString(getBytesFromBitmap(bitmap, quality), Base64.DEFAULT);
    }

    /**
     * 把bitmap转换成bytes
     *
     * @param bitmap
     * @param quality 压缩百分比1-100,100代表不压缩
     * @return
     */
    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        //定义数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //把压缩后的bitmap数据写入到bStream中
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
        return baos.toByteArray();
    }

    /**
     * byte[]→Bitmap
     *
     * @param bytes
     * @return
     */
    private static Bitmap getBitmapFromBytes(byte[] bytes) {
        if (bytes.length != 0) return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return null;
    }

    //获得圆角图片的方法
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        Bitmap cornerBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(cornerBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff424242);

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return cornerBitmap;
    }

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
    public static byte[] decodeBitmap(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeFile(path, opts);
        opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        opts.inTempStorage = new byte[16 * 1024];
        FileInputStream is = null;
        Bitmap bmp = null;
        ByteArrayOutputStream baos = null;
        try {
            is = new FileInputStream(path);
            bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
            double scale = getScaling(opts.outWidth * opts.outHeight,
                    1024 * 600);
            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,
                    (int) (opts.outWidth * scale),
                    (int) (opts.outHeight * scale), true);
            bmp.recycle();
            baos = new ByteArrayOutputStream();
            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            bmp2.recycle();
            LogUtils.i("应用空闲的内存==" + Runtime.getRuntime().freeMemory() / 1024 + "KB");
            LogUtils.i("应用可用最大内存==" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB");
            LogUtils.i("应用当前总内存==" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "MB");

            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }
        return baos.toByteArray();
    }

    private static double getScaling(int src, int des) {
        /**
         * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49
         */
        double scale = Math.sqrt((double) des / (double) src);
        return scale;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
