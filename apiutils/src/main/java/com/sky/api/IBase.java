package com.sky.api;

/**
 * Created by sky on 16/5/10 下午3:50.
 */
public interface IBase {

    //载入View
    void initView();
    //载入数据
    void initData();

    //判断是否有网络连接,没有返回false
    boolean hasInternetConnected();

    //获取用户名
    String getUserName();

    //获取用户id
    String getUserId();

    //获取手机号
    String getPhone();

    //获取用户token
    String getToken();

    //显示dialog
    void showLoading();

    //隐藏 dialog
    void hideLoading();

    //用户是否在线（当前网络是否重连成功）
    boolean getUserOnlineState();

    //设置用户在线状态 true 在线 false 不在线
    void setUserOnlineState(boolean isOnline);
}
