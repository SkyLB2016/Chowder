package com.sky.chowder.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.os.AsyncTaskCompat;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.sky.utils.BitmapUtils;

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
    private Set<ImageAsyncTask> tasks;

    private void addBitmapToCache(String url, Bitmap bitmap) {
        if (getBitmapFromCache(url) == null)
            lruCache.put(url, bitmap);
    }

    private Bitmap getBitmapFromCache(String url) {
        return lruCache.get(url);
    }

    public ImageLoaderAsync() {
        lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 4)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        tasks = new HashSet<>();
//        diskCache = new DiskLruCache();
    }


    public void showAsyncImage(ImageView view, String path) {
        view.setTag(path);

        //先从内存中查找是否含有图片
        if (getBitmapFromCache(path) == null) {
            //开启异步任务加载图片
            ImageAsyncTask task = new ImageAsyncTask(view);
            tasks.add(task);
            //task.execute(path);//多任务串行运行方式。
            AsyncTaskCompat.executeParallel(task, path);//多任务并行方式
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
            Bitmap bitmap = BitmapUtils.getBitmapFromUrl(params[0]);
            addBitmapToCache(url, bitmap);
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