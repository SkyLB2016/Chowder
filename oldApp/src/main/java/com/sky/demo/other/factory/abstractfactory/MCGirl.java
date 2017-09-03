package com.sky.demo.other.factory.abstractfactory;

import com.sky.demo.utils.LogUtils;

/**
 * 圣诞系列的女孩
 *
 * @author Administrator
 */
public class MCGirl implements Girl {
    @Override
    public void drawWomen() {
        LogUtils.i("-----------------圣诞系列的女孩子--------------------");
    }
}
