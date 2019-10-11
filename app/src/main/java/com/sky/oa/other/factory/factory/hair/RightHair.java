package com.sky.oa.other.factory.factory.hair;

import com.sky.oa.other.factory.factory.api.HairInterface;
import com.sky.sdk.utils.LogUtils;

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
