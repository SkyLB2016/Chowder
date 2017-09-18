package com.sky.chowder.ui.activity

import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import com.sky.chowder.other.factory.abstractfactory.HNFactory
import com.sky.chowder.other.factory.abstractfactory.MCFctory
import com.sky.chowder.other.factory.factory.HairFactory
import com.sky.chowder.other.factory.factory.hair.LeftHair

/**
 * Created by SKY on 16/5/11 下午8:26.
 * 工厂模式应用
 */
class FactoryActivity : BaseNoPActivity() {
    override fun getLayoutResId(): Int = R.layout.tv

    init {
        factoryModel()
    }

    /**
     * 工厂模式应用，待优化
     */
    private fun factoryModel() {
        //工厂模式
        val leftHair = LeftHair()
        leftHair.draw()
        val factory = HairFactory()
        val right = factory.getHair("right")
        right?.draw()
        val left = factory.getHairByClass("com.sky.chowder.other.factory.factory.hair.LeftHair")
        left?.draw()
        val hair = factory.getHairByClassKey("in")
        hair?.draw()

        //抽象工厂模式
        val facoty = MCFctory()
        val girl = facoty.girl
        girl.drawWomen()
        val boyfacoty = HNFactory()
        val boy = boyfacoty.boy
        boy.drawMan()
    }
}