package com.sky.oa.other.factory.abstractfactory.hn;

import com.sky.oa.other.factory.abstractfactory.api.Boy;
import com.sky.sdk.utils.LogUtils;

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
