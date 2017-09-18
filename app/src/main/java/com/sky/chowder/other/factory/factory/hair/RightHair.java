package com.sky.chowder.other.factory.factory.hair;

import com.sky.chowder.other.factory.factory.api.HairInterface;
import com.sky.utils.LogUtils;

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
