package com.sky.oa.other.factory.abstractfactory.mc;

import com.sky.oa.other.factory.abstractfactory.api.Boy;
import com.sky.sdk.utils.LogUtils;

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