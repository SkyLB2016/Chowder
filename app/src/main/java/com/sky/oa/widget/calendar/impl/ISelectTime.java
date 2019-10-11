package com.sky.oa.widget.calendar.impl;

import java.util.List;
import java.util.Map;

/**
 * Created by libin on 2018/9/26 下午8:00.
 */
public abstract class ISelectTime<E> {

    public abstract <T> T getSelectMap();

    public abstract void setSelectMap(Map<String, List<E>> selectMap);

    public abstract <T> T getList(int year, int month);

    public abstract <T> T getList(String key);

    public abstract void add(E info);

    protected String getKey(int year, int month) {
        return year + "年" + setDouble(month+1) + "月";
    }

    protected String setDouble(int value) {
        return String.format("%02d", value);
    }

    public abstract void clear();

}
