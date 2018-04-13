package com.sky.chowder.model

import com.sky.utils.GsonUtils
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
    companion object {
        private const val serialVersionUID = -6504989616188082278L//保证增加属性后，依然可以读取之前的属性
    }

    val objList: List<ActivityModel>? = null;

    /**
     *  排序
     */
    override fun compareTo(model: ActivityModel): Int = className!!.compareTo(model.className!!)

    override fun toString(): String {
//        return "{\"className\":\"$className\",\"describe\":\"$describe\",\"img\":$img,\"componentName\":\"$componentName\"}"
        return GsonUtils.toJson(this)
    }
}
