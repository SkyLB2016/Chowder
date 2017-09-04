package com.sky.demo.other.factory.factory;

import com.sky.demo.utils.LogUtils;

/**
 * 右偏分发型
 *
 * @author Administrator
 */
public class RightHair implements HairInterface {

    @Override
    public void draw() {
        LogUtils.i("-----------------右偏分发型-------------------");
    }

}
