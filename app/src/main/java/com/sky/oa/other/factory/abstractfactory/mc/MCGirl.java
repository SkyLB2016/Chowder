package com.sky.oa.other.factory.abstractfactory.mc;

import com.sky.oa.other.factory.abstractfactory.api.Girl;
import com.sky.sdk.utils.LogUtils;

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
