package com.sky.demo.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

/**
 * @author LiBin
 * @Description: TODO Notification管理类
 * @date 2015/9/18 16:19
 */
public class NotficationManager {
    /**
     * 发出Notification
     *
     * @param context
     * @param iconId       图片id
     * @param title        标题
     * @param contentText  内容
     * @param activity     跳转到的activity
     * @param message      传输的内容
     */
    public static void PushNotification(Context context, int iconId, String title,
                                        String contentText, Class<?> activity, String message) {
//        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))//获取NotificationManager
                //显示Notification
                .notify(0, new NotificationCompat.Builder(context)
                        //设置大图
                        .setLargeIcon(ImageUtils.getBitmapFromDrawable(context.getResources().getDrawable(iconId)))
                        .setSmallIcon(iconId)//设置小图标
                        .setContentTitle(title)//设置标题
                        .setContentText(contentText)//设置内容
                        .setTicker("superapp")//手机状态栏的提示；
                        .setContentInfo(contentText + "superapp")
                        .setNumber(100)
                        .setAutoCancel(true)//可取消
//                        .setWhen(System.currentTimeMillis())//设置时间,默认已设置
//                        .setDefaults(Notification.DEFAULT_SOUND)//设置提示声音
//                        .setDefaults(Notification.DEFAULT_LIGHTS)//设置指示灯
//                        .setDefaults(Notification.DEFAULT_VIBRATE)//设置震动
                        .setDefaults(Notification.DEFAULT_ALL)//设置提示声音，指示灯，震动
                        //点击后的意图
                        // .setFullScreenIntent(PendingIntent.getActivity(context, 0, new Intent(context, activity).putExtra("message", message), 0), false)//在title处预先显示
                        .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, activity).putExtra("message", message), 0))//直接显示在通知栏中
                        // .setDeleteIntent(PendingIntent.getActivity(context, 0, new Intent(context, activity).putExtra("message", message), 0))//删除notification后执行
                        .build());
    }
}