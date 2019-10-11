package com.sky.oa.other.factory.factory.hair;

import com.sky.oa.other.factory.factory.api.HairInterface;
import com.sky.sdk.utils.LogUtils;

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
