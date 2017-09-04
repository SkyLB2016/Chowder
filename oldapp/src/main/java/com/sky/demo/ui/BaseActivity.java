package com.sky.demo.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.sky.Common;
import com.sky.api.IBase;
import com.sky.api.IService;
import com.sky.demo.api.IDataCallback;
import com.sky.demo.R;
import com.sky.demo.common.Constants;
import com.sky.demo.model.Employee;
import com.sky.demo.model.MData;
import com.sky.demo.model.Person;
import com.sky.demo.ui.bsc.MyService;
import com.sky.demo.ui.dialog.DialogManager;
import com.sky.demo.utils.LogUtils;
import com.sky.utils.JumpAct;
import com.sky.utils.NetworkJudgment;
import com.sky.utils.SPUtils;
import com.sky.utils.ToastUtils;
import com.sky.utils.UIHandler;

import org.xutils.x;

/**
 * Created by sky on 2015年3月26日 下午4:01:00.
 * activity基类
 */
public class BaseActivity extends AppCompatActivity implements IBase, IService, Toolbar.OnMenuItemClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
//        hasInternetConnected();//判断有无网络
//        getBattery();//获取电量
//        send();//发送广播
//        startService();//启动服务
//        bindService();//启动bind绑定服务
        setHandler();
//        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 动态注册reciver
        IntentFilter filter = new IntentFilter();
        filter.setPriority(1000);//动态注册顺序高于静态注册
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);//网络变化监听
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);//电量变化监听
        filter.addAction(Intent.ACTION_BATTERY_LOW);//电量过低监听
        filter.addAction(Constants.ACTION_PUSH_DATA);//自定义的action
        filter.addAction(Constants.ACTION_NEW_VERSION);//自定义的action
        filter.addAction(Constants.ACTION_MY);//自定义的action
        registerReceiver(receiver, filter);
        //还可能发送统计数据，比如第三方的SDK 做统计需求
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);//解注册
        //还可能发送统计数据，比如第三方的SDK 做统计需求
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }

    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    public void showToast(String text) {
        ToastUtils.showShort(this, text);
    }//初始化toast提示

    public void setTitle() {
        BaseTitle title = new BaseTitle(this);
        title.setToolbar();
    }

    public void setTitle(int left) {
        BaseTitle title = new BaseTitle(this);
        title.setToolbar();
        title.setLeftButton(left);
    }

    /**
     * 右侧menu的点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
    //toolbar部分完

    public void jumpActivity(String packageName, String componentName) {
        Intent intent = new Intent();
        intent.setClassName(packageName, componentName);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    //handler部分
    protected UIHandler handler = new UIHandler(Looper.getMainLooper());

    //设置handler监听
    private void setHandler() {
        handler.setHandler(new UIHandler.IHandler() {
            public void handleMessage(Message msg) {
                handler(msg);//有消息就提交给子类实现的方法
            }
        });
    }

    //让子类处理消息
    protected void handler(Message msg) {
        if (msg.what == Constants.handler_base)
            showToast(msg.getData().getString("data") + "地势坤，君子以厚德载物。");
    }
//handler 完

    // 数据回调接口，都传递Person(Domine主)的子类实体
    protected IDataCallback<MData<? extends Person>> dataCallback;

    public void setDataCallback(IDataCallback<MData<? extends Person>> dataCallback) {
        this.dataCallback = dataCallback;
    }


    //获取电量百分比
    public int getBattery() {
        Intent battery = getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int currLevel = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int total = battery.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
        return currLevel * 100 / total;
    }

    //broadcast广播
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) { // 网络发生变化
                if (!NetworkJudgment.isConnected(context))
                    ToastUtils.showShort(context, "网络已断开");
            } else if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int currLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);    //当前电量
                int total = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);        //总电量
                if (currLevel * 100 / total < 10) {
                    ToastUtils.showShort(context, "电量过低");
                }
            } else if (Constants.ACTION_PUSH_DATA.equals(action)) { // 可能有新数据
                Bundle b = intent.getExtras();
                Employee employee = (Employee) b.get("data");
                MData<Employee> mdata = new MData<>();
                mdata.dataList = employee;
                if (dataCallback != null) { // 数据通知
                    dataCallback.onNewData(mdata);
                }
            } else if (Constants.ACTION_NEW_VERSION.equals(action)) { // 可能发现新版本
//                 VersionDialog 可能是版本提示是否需要下载的对话框
            } else if (Constants.ACTION_MY.equals(action)) {
                showToast("actionmy");
                abortBroadcast();//终止传递
            }
//            String msg = intent.getStringExtra("msg");
//            Bundle bundle = new Bundle();
//            bundle.putString("msg", msg + "@FirstReceiver");
//            setResultExtras(bundle);//传递给下一个广播
        }
    };

    public void send() {
        Intent broad = new Intent(Constants.ACTION_MY);
        broad.putExtra("msg", "baseactivity");
//        sendBroadcast(broad);
        sendOrderedBroadcast(broad, "sky.permission.ACTION_MY");
    }
    //广播完

    //bind 与 普通service 设定
    private boolean normalService = false;//监听service是否启动
    private boolean bindService = false;//监听bindservice是否启动
    ServiceConnection Connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.i("onServiceConnected");
            Message msg = new Message();
            msg.what = Constants.handler_base;
            Bundle bundle = new Bundle();
            bundle.putString("data", "天行健，君子以自强不息；");
            msg.setData(bundle);
            handler.sendMessage(msg);
            MyService.MyBinder binder = (MyService.MyBinder) service;
            binder.greet("天行健，君子以自强不息；");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.i("onServiceDisconnected");
        }
    };

    /**
     * 在这里开启所有需要开启的服务
     */
    @Override
    public void startService() {
        normalService = true;
        startService(new Intent(this, MyService.class));
    }

    /**
     * 在这里开启所有需要开启的服务
     */
    @Override
    public void bindService() {
        bindService = true;
        Intent service = new Intent(this, MyService.class);
        bindService(service, Connection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 在这里关闭所有需要开启的服务
     */
    @Override
    public void stopService() {
        if (bindService) {
            unbindService(Connection);
            bindService = false;
        }
        if (normalService) {
            stopService(new Intent(this, MyService.class));
            normalService = false;
        }
    }

    //    @Override
//    public void startActivity(Intent intent) {
//        super.startActivity(intent);
//        overridePendingTransition(com.sky.R.anim.in_from_right, com.sky.R.anim.out_to_left);
//    }
    public void isExit() {
        isExit(0);
    }

    public void isExit(int id) {
        new AlertDialog.Builder(this, id)
                .setTitle(getResources().getString(R.string.dialog_exit))
                .setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyApplication.getInstance().exit();
                    }
                })
                .setNegativeButton(getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    /**
     * 打开网络设置对话框
     */
    public void openWirelessSet() {
        new AlertDialog.Builder(this)
                .setTitle("网络设置")
                //.setMessage("检查网络")
                .setPositiveButton("网络设置",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                                JumpAct.jumpActivity(BaseActivity.this, new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                })
                .show();
    }


    @Override
    public String getUserName() {
        return getObject(Common.USERNAME, "");
    }

    @Override
    public String getUserId() {
        return getObject(Common.USERID, "");
    }

    @Override
    public String getPhone() {
        return getObject(Common.PHONE, "");
    }

    @Override
    public String getToken() {
        return getObject(Common.TOKEN, "");
    }

    @Override
    public void showLoading() {
        DialogManager.showDialog(this);
    }

    @Override
    public void hideLoading() {
        DialogManager.disDialog();
    }

    @Override
    public boolean getUserOnlineState() {
        return getObject(Common.ISONLINE, false);
    }

    @Override
    public void setUserOnlineState(boolean isOnline) {
        setObject(Common.ISONLINE, true);
    }

    @Override
    public void initialize() {

    }

    @Override
    public boolean hasInternetConnected() {
        return NetworkJudgment.isConnected(this);
    }

    /**
     * 检查是否登录,未登录跳转到登录页
     *
     * @return
     */
    public boolean checkLogin() {
        if (!getUserOnlineState()) {
            isLogin(4);
            return true;
        }
        return false;
    }

    public void isLogin(int id) {
    }


    public <T extends Object> T getObject(String text, T a) {
        return (T) SPUtils.getInstance().get(text, a);
    }

    public <T extends Object> void setObject(String text, T a) {
        SPUtils.getInstance().put(text, a);
    }

}