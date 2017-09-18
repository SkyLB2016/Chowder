package com.sky.chowder.other.factory.abstractfactory;

import com.sky.chowder.other.factory.abstractfactory.api.Boy;
import com.sky.chowder.other.factory.abstractfactory.api.Girl;
import com.sky.chowder.other.factory.abstractfactory.api.PersonFactory;
import com.sky.chowder.other.factory.abstractfactory.mc.MCBoy;
import com.sky.chowder.other.factory.abstractfactory.mc.MCGirl;

/**
 * 圣诞系列加工厂
 *
 * @author Administrator
 */
public class MCFctory implements PersonFactory {

    @Override
    public Boy getBoy() {
        return new MCBoy();
    }

    @Override
    public Girl getGirl() {
        return new MCGirl();
    }

}
