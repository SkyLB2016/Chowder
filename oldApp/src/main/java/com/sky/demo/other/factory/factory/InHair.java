package com.sky.demo.other.factory.factory;

import com.sky.demo.utils.LogUtils;

/**
 * 中分发型
 *
 * @author Administrator
 */
public class InHair implements HairInterface {

    @Override
    public void draw() {
        LogUtils.i("-----------------中分发型-------------------");
    }

}
