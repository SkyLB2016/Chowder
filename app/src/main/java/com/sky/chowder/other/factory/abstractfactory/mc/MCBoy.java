package com.sky.chowder.other.factory.abstractfactory.mc;

import com.sky.chowder.other.factory.abstractfactory.api.Boy;
import com.sky.utils.LogUtils;

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