package com.sky.chowder.other.factory.factory;

import com.sky.chowder.other.factory.factory.api.HairInterface;
import com.sky.chowder.other.factory.factory.hair.LeftHair;
import com.sky.chowder.other.factory.factory.hair.RightHair;

import java.util.Map;

/**
 * 发型工厂
 *
 * @author Administrator
 */
public class HairFactory {

    /**
     * 根据类型来创建对象
     *
     * @param key
     * @return
     */
    public HairInterface getHair(String key) {
        if ("left".equals(key)) return new LeftHair();
        else if ("right".equals(key)) return new RightHair();
        return null;
    }

    /**
     * 根据类的名称来生产对象
     *
     * @param className
     * @return
     */
    public HairInterface getHairByClass(String className) {

        try {
            return (HairInterface) Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据类的名称来生产对象
     *
     * @param key
     * @return
     */
    public HairInterface getHairByClassKey(String key) {

        try {
            Map<String, String> map = new PropertiesReader().getProperties();

            HairInterface hair = (HairInterface) Class.forName(map.get(key)).newInstance();
            return hair;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}