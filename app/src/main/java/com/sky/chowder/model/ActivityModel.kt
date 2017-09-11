package com.sky.chowder.model

import java.io.Serializable

/**
 * Created by SKY on 15/12/9 下午8:54.
 * activity信息类
 */
class ActivityModel(var className: String?//activity的名称
                    , var describe: String?//activity的描述
                    , var img: Int//代表图片
                    , var componentName: String?//跳转所需
) : Serializable, Comparable<ActivityModel> {

    /**
     * Collections.sort(student)  排序
     *
     * @param another
     * @return
     */
    override fun compareTo(another: ActivityModel): Int = className!!.compareTo(another.className!!)

}
