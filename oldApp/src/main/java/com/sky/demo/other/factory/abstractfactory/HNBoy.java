package com.sky.demo.other.factory.abstractfactory;

import com.sky.demo.utils.LogUtils;

/**
 * 新年系列的男孩子
 *
 * @author Administrator
 */
public class HNBoy implements Boy {

    @Override
    public void drawMan() {
        LogUtils.i("-----------------新年系列的男孩子--------------------");
    }
}
