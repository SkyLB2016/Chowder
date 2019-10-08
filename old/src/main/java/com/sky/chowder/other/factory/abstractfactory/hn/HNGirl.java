package com.sky.chowder.other.factory.abstractfactory.hn;

import com.sky.chowder.other.factory.abstractfactory.api.Girl;
import com.sky.sdk.utils.LogUtils;

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