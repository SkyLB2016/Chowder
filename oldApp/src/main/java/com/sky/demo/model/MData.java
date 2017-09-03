package com.sky.demo.model;

import java.io.Serializable;
/**
 * Created by sky on 15/12/9 下午8:54.
 * 数据交互模型
 */
public class MData<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    public String id;
    public String type;
    public T dataList;//多种类型数据，一般是List集合，比如获取所有员工列表
}
