package com.glimmer.carrybport.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.glimmer.carrybport.MyApplication;

import cn.jpush.android.api.JPushInterface;


/**
 * 自定义接收器
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!MyApplication.getInstance().getUsertOnline()) return;
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//            ToastUtils.showShort(CarryApp.getInstance(),"注册");
            //JPush用户注册成功
//            actionRegistrationId(bundle);

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            //接受到推送下来的自定义消息
//            ToastUtils.showShort(CarryApp.getInstance(),"自定义");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            ToastUtils.showShort(CarryApp.getInstance(),"系统的通知");
            //接受到推送下来的通知
//            receivingNotification(context, bundle);
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//            ToastUtils.showShort(CarryApp.getInstance(),"打开");
            //用户点击打开了通知
//            openNotification(context, bundle);
        } else {
//            ToastUtils.showShort(CarryApp.getInstance(),"其他推送");
//            LogUtils.d("Unhandled intent - " + intent.getAction());
        }
    }

//    private void actionRegistrationId(Bundle bundle) {
//        String token = (String) SPUtils.getInstance().get(Constants.TOKEN, "");
//        String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
//        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(regId)) return;
//        // 更新设备号
//        updateJPushDevice(regId);
//    }
//
//    private void receivingNotification(Context context, Bundle bundle) {
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        if (TextUtils.isEmpty(extras)) return;
//        JPushMessage message = JsonTool.json2Obj(extras, JPushMessage.class);
//        if (message == null) return;
//
//        int type = message.getType();
//        if (type == 2) {
//            jumpToOrderDetail(context, message);
//        } else if (type == 5 || type == 6) {
//            jumpToOrderPlay(context, message);
////            soundMedia = R.raw.c5;
////            soundMedia = R.raw.c6;
//        }
//        NotificationManager manger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification();
//        notification.when = System.currentTimeMillis();
//
//        int soundMedia = Notification.DEFAULT_SOUND;
//        notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + soundMedia);
//        manger.notify(1, notification);
//    }
//
//    private void jumpToOrderDetail(final Context context, final JPushMessage message) {
//        if (ActivityLifecycle.getInstance().getCurrentActivity() instanceof NavigationActivity) {
//            NavigationActivity navi = (NavigationActivity) ActivityLifecycle.getInstance().getCurrentActivity();
//            navi.refresh();
//        } else {
//            AlertDialogFragment.getInstance("订单有更新，是否进入")
//                    .setOnSimpleDialogClickListener(new AlertDialogFragment.OnSimpleDialogClickListener() {
//                        @Override
//                        public void onPositiveButtonClickListener() {
//                            Intent intent = new Intent(context, NavigationActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.putExtra(Constants.EXTRA, message.getOrderNo());
//                            context.startActivity(intent);
//                        }
//                    })
//                    .show(((AppCompatActivity) ActivityLifecycle.getInstance().getCurrentActivity()).getSupportFragmentManager(), "cancel");
//        }
//    }
//
//    private void jumpToOrderPlay(Context context, JPushMessage message) {
//        if (ActivityLifecycle.getInstance().getCurrentActivity() instanceof OrderPlayActivity) {
//            OrderPlayActivity order = (OrderPlayActivity) ActivityLifecycle.getInstance().getCurrentActivity();
//            order.loopMessage(message);
//        } else {
//            Intent intent = new Intent(context, OrderPlayActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra(Constants.EXTRA, message);
//            context.startActivity(intent);
//        }
//    }
//
//    private void openNotification(Context context, Bundle bundle) {
//        // 接收到的数据 {"orderNo":"2017010510022566868","type":"2"}
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        if (!TextUtils.isEmpty(extras)) {
//            JPushMessage jPushMessage = JsonTool.json2Obj(extras, JPushMessage.class);
//            //【1.系统，2.订单，3.优惠券，4.提现，5.注册】
//            if (jPushMessage != null) {
////                    if (ActivityStackUtils.getTopClass() == null) {
//                if (ActivityLifecycle.getInstance().getCurrentActivity() == null) {
//                    Intent[] intents;
//                    if (jPushMessage.getType() == 1) {
//                        intents = new Intent[1];
//                    } else {
//                        intents = new Intent[2];
//                    }
//
//                    // 主页
//                    Intent mainIntent = new Intent(context, MainActivity.class);
//                    mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intents[0] = mainIntent;
//
//                    if (jPushMessage.getType() == 2) {
//                        // 订单详情
//                        Intent orderDetailIntent = new Intent(context, NavigationActivity.class);
//                        orderDetailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        Bundle activityExtras = new Bundle();
//                        activityExtras.putSerializable(
//                                Constants.EXTRA,
//                                jPushMessage.getOrderNo()
//                        );
//                        orderDetailIntent.putExtras(activityExtras);
//                        intents[1] = orderDetailIntent;
//                    } else if (jPushMessage.getType() == 4) {
//                        // 提现消息
//                        Intent withdrawalIntent = new Intent(context, TextContentActivity.class);
//                        withdrawalIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        Bundle activityExtras = new Bundle();
//                        activityExtras.putSerializable(
//                                Constants.EXTRA,
//                                new TextContentData(
//                                        "提现通知",
//                                        jPushMessage.getContent()
//                                )
//                        );
//                        withdrawalIntent.putExtras(activityExtras);
//                        intents[1] = withdrawalIntent;
//                    } else if (jPushMessage.getType() == 5 || jPushMessage.getType() == 6) {
//                        Bundle mainExtras = new Bundle();
//                        MainActivityData mainActivityData = new MainActivityData();
//                        mainActivityData.setMessageType(jPushMessage.getType());
//                        mainExtras.putSerializable(Constants.EXTRA, mainActivityData);
//                        mainIntent.putExtras(mainExtras);
//                    } else {
//                        Bundle activityExtras = new Bundle();
//                        activityExtras.putSerializable(
//                                Constants.EXTRA,
//                                MainActivity.VIEW_TYPE_MESSAGE
//                        );
//                        mainIntent.putExtras(activityExtras);
//                    }
//
//                    context.startActivities(intents);
//                } else {
//
//                    if (jPushMessage.getType() == 1) {
//                        // 首页
//                        Intent mainIntent = new Intent(context, MainActivity.class);
//                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        context.startActivity(mainIntent);
//                    } else if (jPushMessage.getType() == 2) {
//                        // 订单详情
//                        Intent orderDetailIntent = new Intent(context, NavigationActivity.class);
//                        orderDetailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        Bundle activityExtras = new Bundle();
//                        activityExtras.putSerializable(
//                                Constants.EXTRA,
//                                jPushMessage.getOrderNo()
//                        );
//                        orderDetailIntent.putExtras(activityExtras);
//                        context.startActivity(orderDetailIntent);
//                    } else if (jPushMessage.getType() == 4) {
//                        // 提现消息
//                        Intent withdrawalIntent = new Intent(context, TextContentActivity.class);
//                        withdrawalIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        Bundle activityExtras = new Bundle();
//                        activityExtras.putSerializable(
//                                Constants.EXTRA,
//                                new TextContentData(
//                                        "提现通知",
//                                        jPushMessage.getContent()
//                                )
//                        );
//                        withdrawalIntent.putExtras(activityExtras);
//                        context.startActivity(withdrawalIntent);
//                    } else if (jPushMessage.getType() == 5 || jPushMessage.getType() == 6) {
//                        Intent mainIntent = new Intent(context, MainActivity.class);
//                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        context.startActivity(mainIntent);
//                        // 切换列表
//                        CarryApp.getInstance()
//                                .getRxBus()
//                                .sendNormalEvent(new ESwitchRobOrderView(jPushMessage.getType()));
//                    } else {
//                        Intent mainIntent = new Intent(context, MainActivity.class);
//                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        // 切换到消息列表
//                        CarryApp.getInstance()
//                                .getRxBus()
//                                .sendNormalEvent(new ESwitchToMessageView());
//                        context.startActivity(mainIntent);
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 更新设备号
//     *
//     * @param regId 设备号
//     */
//    private void updateJPushDevice(String regId) {
//        new SetMobileIdUseCase(new MobileIdParams(regId)).execute(new OnRequestCallback<ObjEntity>() {
//            @Override
//            public void onFail(ErrBundle errBundle) {
//            }
//
//            @Override
//            public void onSuccess(ObjEntity objEntity) {
//            }
//        });
//    }

}
