package com.sky.chowder.model

import java.io.Serializable

/**
 * Created by SKY on 15/12/9 下午8:54.
 * activity信息类
 */
class TestModel(var className: String?/*activity的名称*/) : Serializable, Comparable<TestModel> {
    /**
     * Collections.sort(student)   排序
     */
    override fun compareTo(another: TestModel): Int = className!!.compareTo(another.className!!)

    //    override fun toString(): String {
//        return "\n{\"className\":\"$className\"}"
////        return GsonUtils.toJson(this)
//    }
    override fun toString(): String {
        return "$className"
//        return GsonUtils.toJson(this)
    }
}
