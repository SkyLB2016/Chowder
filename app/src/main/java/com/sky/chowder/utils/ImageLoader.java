package com.sky.chowder.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import com.sky.chowder.MyApplication;
import com.sky.utils.DiskLruCache;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by SKY on 2018/5/22 10:18.
 */
public class ImageLoader {
    private LruCache<String, Bitmap> lruCache;
    private DiskLruCache diskLruCache;

    private void addBitmapToCache(String url, Bitmap bitmap) {
        if (getBitmapFromCache(url) == null)
            lruCache.put(url, bitmap);
    }

    public ImageLoader() {
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 4);
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        try {
            diskLruCache = DiskLruCache.open(new File(MyApplication.getInstance().getPicCacheDir()), 1, 1, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromCache(String url) {
        return lruCache.get(url);
    }

    private Bitmap getBitmapFromDisk(String url) {
        Bitmap bitmap=null;
        String key;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(url.getBytes());
            key = changeKey(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            key = String.valueOf(url.hashCode());
        }
        try {
            DiskLruCache.Snapshot snap = diskLruCache.get(key);
            FileInputStream is = (FileInputStream) snap.getInputStream(0);
            FileDescriptor fd = is.getFD();
            bitmap = BitmapFactory.decodeFileDescriptor(fd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void addBitmapFromDisk(String url, Bitmap bitmap) throws IOException {
        String key;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(url.getBytes());
            key = changeKey(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            key = String.valueOf(url.hashCode());
        }
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            OutputStream os = editor.newOutputStream(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        diskLruCache.flush();
    }

    private String changeKey(byte[] digest) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0xFF & digest[i]);
            if (hex.length() == 1) {
                builder.append("0");
            }
            builder.append(hex);
        }
        return builder.toString();
    }
}
