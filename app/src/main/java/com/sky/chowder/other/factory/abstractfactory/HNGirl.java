package com.sky.chowder.other.factory.abstractfactory;

import com.sky.utils.LogUtils;

/**
 * 新年系列的女孩子
 *
 * @author Administrator
 */
public class HNGirl implements Girl {

    @Override
    public void drawWomen() {
        LogUtils.i("-----------------新年系列的女孩子--------------------");
    }
}