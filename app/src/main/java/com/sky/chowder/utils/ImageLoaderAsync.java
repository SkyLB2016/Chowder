package com.sky.chowder.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.sky.chowder.MyApplication;
import com.sky.utils.BitmapUtils;
import com.sky.utils.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by SKY on 2015/12/16 13:45.
 * 图片预加载缓存类，async的实现
 */
public class ImageLoaderAsync {
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageHolder holder = (ImageHolder) msg.obj;
            if (holder.view.getTag().equals(holder.path))
                if (holder.view instanceof ImageView) {
                    Bitmap bitmap = holder.bitmap;
                    ImageView view = (ImageView) holder.view;
//                    ViewGroup.LayoutParams params = view.getLayoutParams();
//                    //params.width = bitmap.getWidth();
//                    params.height = bitmap.getHeight();
//                    view.setLayoutParams(params);
                    view.setImageBitmap(bitmap);
                } else {
                    holder.view.setBackground(BitmapUtils.getDrawableFromBitmap(holder.view.getContext(), holder.bitmap));
                }
        }
    };
    private LruCache<String, Bitmap> lruCache;
    private DiskLruCache diskLruCache;
    private Set<ImageAsyncTask> tasks;

    private void addBitmapToCache(String url, Bitmap bitmap) {
        if (getBitmapFromCache(url) == null)
            lruCache.put(url, bitmap);
    }

    private Bitmap getBitmapFromCache(String url) {
//        Bitmap bitmap = lruCache.get(url);
//        String key;
//        if (bitmap == null) {
//            try {
//                MessageDigest md5 = MessageDigest.getInstance("MD5");
//                md5.update(url.getBytes());
//                key = changeKey(md5.digest());
//            } catch (NoSuchAlgorithmException e) {
//                key = String.valueOf(url.hashCode());
//            }
//            try {
//                DiskLruCache.Editor editor = diskLruCache.edit(key);
//                OutputStream os = editor.newOutputStream(0);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                DiskLruCache.Snapshot snap = diskLruCache.get(key);
//                FileInputStream is = (FileInputStream) snap.getInputStream(0);
//                FileDescriptor fd = is.getFD();
//            bitmap= BitmapFactory.decodeFileDescriptor(fd);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        return lruCache.get(url);
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

    public ImageLoaderAsync() {
        lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 4)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        tasks = new HashSet<>();
        try {
            diskLruCache = DiskLruCache.open(new File(MyApplication.getInstance().getPicCacheDir()), 1, 1, Runtime.getRuntime().maxMemory() / 4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showAsyncImage(ImageView view, String path) {
        view.setTag(path);

        //先从内存中查找是否含有图片
        if (getBitmapFromCache(path) == null) {
            //开启异步任务加载图片
            ImageAsyncTask task = new ImageAsyncTask(view);
            tasks.add(task);
            task.execute(path);//多任务串行运行方式。
//            AsyncTaskCompat.executeParallel(task, path);//多任务并行方式
        } else {
            //发送message消息，view，path，bitmap
            sendMessage(view, path, getBitmapFromCache(path));
        }
    }

    public void cancelAlltasks() {
        if (tasks == null || tasks.isEmpty())
            return;
        for (ImageAsyncTask task : tasks) {
            //标记为cancel状态，并没有取消，如要取消，在doInBackground中调用isCancelled（）
            task.cancel(true);
        }
    }

    private class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        String url;
        View view;

        public ImageAsyncTask(View view) {
            super();
            this.view = view;
        }

        /**
         * 执行后台耗时操作前被调用，完成一些初始化操作
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * 异步任务，并且asyncTask中只有他是异步任务，其他都不是；必须重写
         *
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            if (isCancelled())
                return null;
            url = params[0];
//            publishProgress();
            Bitmap bitmap = BitmapUtils.getBitmapUP(params[0]);
            addBitmapToCache(url, bitmap);
            BitmapUtils.saveBitmapToFile(bitmap, MyApplication.getInstance().getPicCacheDir() + url.substring(url.lastIndexOf("/")));
            return bitmap;
        }

        /**
         * 可在此设置进度条更新，在doInBackground()中调用publishProgress()
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        /**
         * 完成后，系统自动调用，在此更新UI
         *
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            //发送message消息，view，path，bitmap
            sendMessage(view, url, bitmap);
            tasks.remove(this);
        }
    }

    /**
     * 取得图片后通知UIHandler更新
     *
     * @param view
     * @param path
     * @param bitmap
     */
    private void sendMessage(View view, String path, Bitmap bitmap) {
        //发送message消息，view，path，bitmap
        ImageHolder holder = new ImageHolder();
        holder.bitmap = bitmap;
        holder.view = view;
        holder.path = path;
        Message msg = Message.obtain();
        msg.obj = holder;
        //msg.sendToTarget();
        handler.sendMessage(msg);
    }

    private class ImageHolder {
        View view;
        Bitmap bitmap;
        String path;
    }
}