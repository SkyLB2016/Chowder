package com.sky.rxbus;

/**
 * RxBus 默认的封装事件
 * Created by SKY on 2017/6/4.
 */
public class DefaultBus<T> {
    private int code;
    private T object;

    public DefaultBus() {
    }

    public DefaultBus(int code, T object) {
        this.code = code;
        this.object = object;
    }

    public int getCode() {
        return code;
    }

    public T getObject() {
        return object;
    }
}
