package com.sky.chowder.other.factory.abstractfactory;

import com.sky.utils.LogUtils;

/**
 * 新年系列的男孩子
 *
 */
public class HNBoy implements Boy {

    @Override
    public void drawMan() {
        LogUtils.i("-----------------新年系列的男孩子--------------------");
    }
}
