package com.sky.chowder.ui;

import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sky.Common;
import com.sky.api.IBase;
import com.sky.chowder.common.Constants;
import com.sky.chowder.ui.dialog.DialogManager;
import com.sky.utils.NetworkJudgment;
import com.sky.utils.SPUtils;
import com.sky.utils.ToastUtils;
import com.sky.utils.UIHandler;

import butterknife.ButterKnife;

/**
 * Created by 李彬 on 2017/3/3.
 */

public  class BaseActivity extends AppCompatActivity implements IBase {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hasInternetConnected();//判断有无网络
        setHandler();
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
        if (msg.what == Constants.handler_0x001)
            showToast(msg.getData().getString("data") + "地势坤，君子以厚德载物。");
    }
    //handler 完

    @Override
    public void initView() {
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {

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
    public boolean hasInternetConnected() {
        return NetworkJudgment.isConnected(this);
    }

    public <T extends Object> T getObject(String text, T a) {
        return (T) SPUtils.get(this, text, a);
    }

    public <T extends Object> void setObject(String text, T a) {
        SPUtils.put(this, text, a);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        showToast("level="+level);
    }
}
