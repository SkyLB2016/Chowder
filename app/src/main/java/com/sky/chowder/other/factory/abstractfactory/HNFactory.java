package com.sky.chowder.other.factory.abstractfactory;

import com.sky.chowder.other.factory.abstractfactory.api.Boy;
import com.sky.chowder.other.factory.abstractfactory.api.Girl;
import com.sky.chowder.other.factory.abstractfactory.api.PersonFactory;
import com.sky.chowder.other.factory.abstractfactory.hn.HNBoy;
import com.sky.chowder.other.factory.abstractfactory.hn.HNGirl;

/**
 * 新年系列加工厂
 *
 * @author Administrator
 */
public class HNFactory implements PersonFactory {

    @Override
    public Boy getBoy() {
        return new HNBoy();
    }

    @Override
    public Girl getGirl() {
        return new HNGirl();
    }

}

