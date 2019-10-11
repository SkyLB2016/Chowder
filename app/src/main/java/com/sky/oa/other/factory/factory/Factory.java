package com.sky.oa.other.factory.factory;

import com.sky.oa.other.factory.factory.api.HairInterface;

/**
 * Created by SKY on 2018/6/27 16:42.
 */
public abstract class Factory {

    public abstract <T extends HairInterface> T create(Class<T> cla);
}
