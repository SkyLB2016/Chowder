package com.sky.oa.model;

/**
 * Created by libin on 2020/04/15 4:19 PM Wednesday.
 */
public class Generic<T> {
    private T  data;

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
