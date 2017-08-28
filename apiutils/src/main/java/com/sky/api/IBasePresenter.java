package com.sky.api;

import android.support.annotation.ArrayRes;
import android.support.annotation.StringRes;

import com.sky.rxbus.DefaultBus;

import java.io.Serializable;

/**
 * Created by SKY on 2017/5/29.
 * mvp中P的接口基类
 */
public interface IBasePresenter {

    void loadData();

    /**
     * @return 获取传递的数据
     */
    Serializable getExtras();

    /**
     * @param code 发送空事件
     */
    void sendEvent(int code);

    /**
     * @param code  标识码
     * @param event 对应的数据
     * @param <T>   类型
     */
    <T> void sendEvent(int code, T event);

    /**
     * @param event 接收数据
     */
    void onReceiveEvent(DefaultBus event);

    /**
     * @param key
     * @param value 获取不到数据时默认的value
     * @param <T>   对应的类型
     * @return 获取对应的字段数据
     */
    <T extends Object> T getObject(String key, T value);

    /**
     * @param key
     * @param value 需要写入数据
     * @param <T>   对应的何种类型
     */
    <T extends Object> void setObject(String key, T value);

    /**
     * @return 是否在线
     */
    boolean getUsertOnline();

    /**
     * @return 获取用户token
     */
    String getToken();

    /**
     * @return 判断是否有网络连接, 没有返回false
     */
    boolean hasInternetConnected();

    /**
     * @param array    数组
     * @param position 位置
     * @return 获取数组字符串
     */
    String getStringArray(@ArrayRes int array, int position);

    String getString(@StringRes int resId);
}
