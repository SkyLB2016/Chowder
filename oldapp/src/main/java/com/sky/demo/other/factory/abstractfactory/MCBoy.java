package com.sky.demo.other.factory.abstractfactory;

import com.sky.demo.utils.LogUtils;

/**
 * 圣诞系列的男孩子
 *
 * @author Administrator
 */
public class MCBoy implements Boy {

    @Override
    public void drawMan() {
        LogUtils.i("-----------------圣诞系列的男孩子--------------------");
    }
}