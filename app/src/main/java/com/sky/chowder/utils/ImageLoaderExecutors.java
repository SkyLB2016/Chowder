package com.sky.chowder.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sky.chowder.R;
import com.sky.chowder.ui.widget.ZoomImageView;
import com.sky.utils.ImageUtils;
import com.sky.utils.LogUtils;
import com.sky.utils.SDCardUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by SKY on 2015/1/6 17:21.
 * 线程池的使用
 */
public class ImageLoaderExecutors {

    private LruCache<String, Bitmap> lruCache;//内存缓存

    private FileType type = FileType.LIFO;//任务执行方式
    private static final int MAXCOUNT = 5;//最多执行多少个线程
    private LinkedList<Runnable> threads;
    private static final int UIH_CODE = 0X01;//UI线程用
    private static final int THREADH_CODE = 0X02;//异步线程用
    //UI主线程handler
    private Handler UIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0X01:
                    ImageHolder holder = (ImageHolder) msg.obj;
                    if (holder.view.getTag().equals(holder.path))
                        if (holder.view instanceof ImageView) {
                            Bitmap bitmap = holder.bitmap;
                            ImageView view = (ImageView) holder.view;

//                            ViewGroup.LayoutParams params = view.getLayoutParams();
//                            //params.width = bitmap.getWidth();
//                            params.height = bitmap.getHeight();
//                            view.setLayoutParams(params);
                            view.setImageBitmap(bitmap);
//                            holder.bitmap=bitmap=null;
                        } else {
                            holder.view.setBackground(ImageUtils.getDrawableFromBitmap(holder.view.getContext(), holder.bitmap));
//                            holder.bitmap=null;
                        }
                    break;
            }
        }
    };
    //异步线程handler，检查runnable使用
    private Handler threadHandler;
    //线程池
    private ExecutorService executors;
    //异步线程信号量,主要用于FileType.LIFO模式
    private Semaphore threadSemaphore;
    //因为异步线程与主线程是并行的，所以为防止threadHandler未创建，用信号量做一个限制
    private Semaphore semaphore = new Semaphore(0);

    public ImageLoaderExecutors() {
        this(FileType.LIFO, 3);
    }

    public ImageLoaderExecutors(FileType type, int count) {
        this.type = type;
        if (count > MAXCOUNT)
            count = MAXCOUNT;
        threads = new LinkedList<>();
        lruCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / 4)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return ImageUtils.getBitmapSize(value);
            }
        };
        executors = Executors.newFixedThreadPool(count);
        threadSemaphore = new Semaphore(count);
        new Thread() {
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                threadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case THREADH_CODE:
                                //获取一个任务，并执行
                                executors.execute(getThread());
                                try {
                                    //同时请求一个信号量
                                    threadSemaphore.acquire();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                break;
                        }
                    }
                };
                //threadHandler已创建，释放semaphore
                semaphore.release();
                Looper.loop();//开始轮询接收消息
            }
        }.start();
    }

    /**
     * 获取一个Runnable任务
     *
     * @return
     */
    private Runnable getThread() {
        if (type == FileType.FIFO)
            return threads.removeFirst();
        else if (type == FileType.LIFO)
            return threads.removeLast();
        throw new NullPointerException("getThread()获取任务失败");
    }


    /**
     * 外部调用方法，为view添加图片
     *
     * @param view
     * @param path
     */
    public void loadImage(final View view, final String path) {
        view.setTag(path);
        //先从内存中查找是否含有图片
        if (getBitmapFromLruCache(path) == null) {
            if (view instanceof ImageView) {
                if (!(view instanceof ZoomImageView))
                    ((ImageView) view).setImageBitmap(ImageUtils.getBitmapFromId(view.getContext(), R.mipmap.ic_launcher));
            } else
                view.setBackgroundResource(R.mipmap.ic_launcher);
            //开启异步任务加载图片
            createRunnable(new Runnable() {
                @Override
                public void run() {
                    //从网络或本地获取图片
                    ImageSize size = getViewSize(view);

                    Bitmap bitmap = getBitmap(path, size);

                    //添加到缓存中
                    addBitmapToLruCache(path, bitmap);
                    //发送message消息，view，path，bitmap
                    sendMessage(view, path, bitmap);
                    //释放异步线程中使用的信号量
                    threadSemaphore.release();
                    LogUtils.i(threadSemaphore.availablePermits() + "个");
                }
            });
        } else {
            Bitmap bitmap = getBitmapFromLruCache(path);
            //发送message消息，view，path，bitmap
            sendMessage(view, path, bitmap);
            //不为空直接可以加载图片，但传递给UIhandler来加载为好，统一规则
            //if (view instanceof ImageView) ((ImageView) view).setImageBitmap(bitmap);
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
        ImageHolder holder = new ImageHolder();
        holder.bitmap = bitmap;
        holder.view = view;
        holder.path = path;
        Message msg = UIHandler.obtainMessage();
//        Message msg = Message.obtain();
//        Message msg = Message.obtain(UIHandler);
        msg.obj = holder;
        msg.what = UIH_CODE;
        msg.sendToTarget();
//        UIHandler.sendMessage(msg);
    }

    /**
     * 获取图片
     *
     * @param path
     * @param size
     * @return
     */
    private Bitmap getBitmap(String path, ImageSize size) {
        if (path.startsWith("http"))
            return ImageUtils.scaleBitmap(ImageUtils.getBitmapFromUrl(path), size.width, size.height);
        else if (path.startsWith(SDCardUtils.getSDCardPath())) {
            return ImageUtils.getBitmapFromPath(path, size.width, size.height);
        }
        throw new NullPointerException("图片的路径应该是全路径");
    }

    private void createRunnable(Runnable runnable) {
        try {
            //检查threadHandler是否为空，在thread创建完成后释放
            if (threadHandler == null)
                semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threads.add(runnable);
        //添加完成后发送handler，轮询添加到线程池中
        threadHandler.sendEmptyMessage(THREADH_CODE);
    }

    /**
     * 添加bitmap到lrucache中
     *
     * @param path
     * @param bitmap
     * @return 是否成功
     */
    private Boolean addBitmapToLruCache(String path, Bitmap bitmap) {
        if (getBitmapFromLruCache(path) == null) {
            if (bitmap != null) {
                lruCache.put(path, bitmap);
                return true;
            }
        }
        return false;
    }

    /**
     * 从缓存中获取bitmap
     *
     * @param path
     * @return
     */
    private Bitmap getBitmapFromLruCache(String path) {
        return lruCache.get(path);
    }

    /**
     * 根据View获得适当的压缩的宽和高
     *
     * @param view
     * @return
     */
    private ImageSize getViewSize(View view) {
        ImageSize imageSize = new ImageSize();
        final DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        final ViewGroup.LayoutParams params = view.getLayoutParams();
        int width = 0;
        if (params != null)
            width = params.width == ViewGroup.LayoutParams.WRAP_CONTENT
                    ? 0
                    : view.getWidth(); // Get actual image width
        if (width <= 0 && params != null)
            width = params.width; // Get layout width parameter
        // maxWidth
        if (width <= 0)
            width = getViewFieldValue(view, "mMaxWidth"); // Check
        if (width <= 0)
            width = view.getMinimumWidth(); // Check// parameter
        if (width <= 0)
            width = displayMetrics.widthPixels;
        int height = 0;
        if (params != null)
            height = params.height == ViewGroup.LayoutParams.WRAP_CONTENT
                    ? 0
                    : view.getHeight(); // Get actual image height
        if (height <= 0 && params != null)
            height = params.height; // Get layout height parameter
        // maxHeight
        if (height <= 0)
            height = getViewFieldValue(view, "mMaxHeight"); // Check
        // minHeight
        if (height <= 0)
            height = view.getMinimumHeight(); // Check
        // parameter
        if (height <= 0)
            height = displayMetrics.heightPixels;
        imageSize.width = width;
        imageSize.height = height;
        LogUtils.i("width==" + width);
        LogUtils.i("height==" + height);
        return imageSize;

    }

    /**
     * 反射获得View设置的最大宽度和高度
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static int getViewFieldValue(Object object, String fieldName) {
        int value = 0;
        try {
            if (object instanceof ImageView) {
                Field field = ImageView.class.getDeclaredField(fieldName);
                field.setAccessible(true);
                int fieldValue = (Integer) field.get(object);
                if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                    value = fieldValue;
                    LogUtils.i(value + "");
                }
            }
        } catch (Exception e) {
        }
        return value;
    }

    public void closeExecutors() {
        executors.shutdown();//结束空闲的线程interrupt
//        executors.shutdownNow();//中断部分未在执行的线程
        executors=null;
    }

    private class ImageHolder {
        Bitmap bitmap;
        View view;
        String path;
    }

    private class ImageSize {
        int width;
        int height;
    }

    public enum FileType {
        FIFO,//==LILO,先进先出，后进后出
        LIFO//==FILO，后进先出，先进后出
    }
}
