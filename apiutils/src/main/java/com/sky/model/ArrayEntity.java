package com.sky.model;

import java.util.List;

/**
 * Created by SKY on 2017/5/29.
 * 返回的result为数组类
 */
public class ArrayEntity<E> extends BaseEntity {
    private int totalCount;
    private List<E> result;

    public int getTotalCount() {
        return totalCount;
    }

    public List<E> getResult() {
        return result;
    }
}
