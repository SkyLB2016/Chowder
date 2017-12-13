package com.sky.chowder.ui.activity

import android.widget.NumberPicker
import com.sky.chowder.R.id.npHour

/**
 * Created by SKY on 2017/12/13 10:09.
 */
class TestKotlin {
    private fun getFormatter(): NumberPicker.Formatter? {
        //获取全部私有属性
        val pickerFields = NumberPicker::class.java.declaredFields
        var formatter: NumberPicker.Formatter? = null
        for (field in pickerFields) {
            field.isAccessible = true
            //遍历找到我们需要获取值的那个属性
            if (field.name == "sTwoDigitFormatter") {
                try { //获取属性值
                    formatter = field.get(npHour) as NumberPicker.Formatter?
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                break
            }
        }
        return formatter
    }
}
