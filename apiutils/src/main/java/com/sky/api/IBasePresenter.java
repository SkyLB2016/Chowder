package com.sky.api;

import java.io.Serializable;

/**
 * Created by SKY on 2017/5/29.
 * mvp中P的接口基类
 */
public interface IBasePresenter {

    void loadData();

    /**
     * 获取传递的数据
     *
     * @return
     */
    Serializable getExtras();

    /**
     * 发送事件
     *
     * @param event
     */
    void sendEvent(Object event);

    /**
     * 处理事件
     *
     * @param event
     */
    void receiveEvent(Object event);

    /**
     * 获取对应的字段数据
     *
     * @param text  key
     * @param value 默认的value，同时也是对应类型
     * @param <T>   对应的何种类型
     * @return
     */
    <T extends Object> T getObject(String text, T value);

    /**
     * 写入数据
     *
     * @param text
     * @param value
     * @param <T>
     */
    <T extends Object> void setObject(String text, T value);

    //是否在线
    boolean getUsertOnline();

    //获取手机号
    String getPhone();

    //获取用户token
    String getToken();

    //判断是否有网络连接,没有返回false
    boolean hasInternetConnected();
}
