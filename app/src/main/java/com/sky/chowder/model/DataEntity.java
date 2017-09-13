package com.sky.chowder.model;

import com.sky.model.BaseEntity;

import java.util.List;

/**
 * Created by SKY on 2017/5/29.
 * 返回的result为单个类
 */
public class DataEntity<E> extends BaseEntity{
    private List<E> data;

    public List<E> getData() {
        return data;
    }
}
