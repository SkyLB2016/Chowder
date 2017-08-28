package com.sky.model;

/**
 * Created by SKY on 2017/5/29.
 * 返回的result为单个类
 */
public class ObjectEntity<E> extends BaseEntity{
    private E result;

    public E getResult() {
        return result;
    }
}
