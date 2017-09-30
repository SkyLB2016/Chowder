package com.sky.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
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
public class BitmapUtils {

    /**
     * 从资源中获取Bitmap
     */
    public static Bitmap getBitmapFromId(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    /**
     * bitmap转换为drawable
     */
    public static Drawable getDrawableFromBitmap(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * 获取bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        return bitmap.getAllocationByteCount();
    }

    /**
     * 获取网络图片
     */
    public static Bitmap getBitmapFromUrl(String urlStr) {
        InputStream input = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            input = new BufferedInputStream(connection.getInputStream());
            return BitmapFactory.decodeStream(input);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) input.close();
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 从文件路径中获取bitmap,并进行裁剪
     */
    public static Bitmap getBitmapFromPath(String path, int newWidth, int newHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;//设置为ture只获取图片大小
        BitmapFactory.decodeFile(path, opts);

        opts.inSampleSize = getInSampleSize(opts, newWidth, newHeight);//计算缩放率，缩放图片
        opts.inJustDecodeBounds = false;//至为false
        return BitmapFactory.decodeFile(path, opts);
    }

    public static Bitmap getBitmapFromPath(String path) {
        return BitmapFactory.decodeFile(path);
    }

    /**
     * 计算InSampleSize大于1的整数时是缩小原图
     */
    private static int getInSampleSize(BitmapFactory.Options opts, int newW, int newH) {
        int outWidth = opts.outWidth;
        int outHeight = opts.outHeight;
        if (outWidth > newW || outHeight > newH) {
            return (int) Math.ceil(Math.max(outWidth * 1d / newW, outHeight * 1d / newH));
        }
        return 1;
    }

    /**
     * matrix 缩放/裁剪图片
     *
     * @return 裁剪后的图片
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int newW, int newH) {
        int bW = bitmap.getWidth();
        int bH = bitmap.getHeight();
        // 计算缩放比例；缩放率X*width =newWidth；
        float scaleW = ((float) newW) / bW;
        float scaleH = ((float) newH) / bH;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleW, scaleH);
        return Bitmap.createBitmap(bitmap, 0, 0, bW, bH, matrix, true);
    }

    /**
     * base64->bitmap
     *
     * @param base64 base64 的字符串
     * @return
     */
    public static Bitmap getBitmapFromBase64(String base64) {
        if (base64 == null || base64.isEmpty()) return null;
        try {
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
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
     * bitmap->bytes
     *
     * @param bitmap
     * @param quality 压缩百分比1-100,100代表不压缩
     * @return
     */
    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream byteArrayout = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, byteArrayout);
        return byteArrayout.toByteArray();
    }

    /**
     * bitmap -> file
     *
     * @param bitmap
     * @param absoluteName 绝对路径
     * @return
     */
    public static boolean saveBitmapToFile(Bitmap bitmap, String absoluteName) {
        int quality = 100;
        OutputStream out = null;
        try {
            out = new FileOutputStream(absoluteName);
            return bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 获得圆角图片的方法
     */
    public static Bitmap getRoundCornerBitmap(Bitmap bitmap, float round) {
        Bitmap corner = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(corner);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff424242);

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, round, round, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return corner;
    }

}
