package com.sky.demo.other.factory.factory;

import com.sky.demo.utils.LogUtils;

/**
 * 左偏分发型
 *
 * @author Administrator
 */
public class LeftHair implements HairInterface {

    @Override
    public void draw() {
        LogUtils.i("-----------------左偏分发型-------------------");
    }

}
