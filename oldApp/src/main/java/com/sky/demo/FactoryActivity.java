package com.sky.demo;

import android.os.Bundle;

import com.sky.demo.other.factory.abstractfactory.Boy;
import com.sky.demo.other.factory.abstractfactory.Girl;
import com.sky.demo.other.factory.abstractfactory.HNFactory;
import com.sky.demo.other.factory.abstractfactory.MCFctory;
import com.sky.demo.other.factory.abstractfactory.PersonFactory;
import com.sky.demo.other.factory.factory.HairFactory;
import com.sky.demo.other.factory.factory.HairInterface;
import com.sky.demo.other.factory.factory.LeftHair;
import com.sky.demo.ui.BaseActivity;

/**
 * Created by IF on 16/5/11 下午8:26.
 */
public class FactoryActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        factoryModel();
    }

    /**
     * 工厂模式应用，待优化
     */
    private void factoryModel() {
        //工厂模式
        HairInterface leftHair = new LeftHair();
        leftHair.draw();
        HairFactory factory = new HairFactory();
        HairInterface right = factory.getHair("right");
        right.draw();
        HairInterface left = factory.getHairByClass("com.sky.demo.other.factory.factory.LeftHair");
        left.draw();
        HairInterface hair = factory.getHairByClassKey("in");
        hair.draw();

        //抽象工厂模式
        PersonFactory facoty = new MCFctory();
        Girl girl = facoty.getGirl();
        girl.drawWomen();
        PersonFactory boyfacoty = new HNFactory();
        Boy boy = boyfacoty.getBoy();
        boy.drawMan();
    }
}
