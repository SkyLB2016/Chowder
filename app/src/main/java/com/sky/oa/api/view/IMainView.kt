package com.sky.oa.api.view

import com.sky.design.api.IBaseView
import com.sky.oa.model.ActivityModel

/**
 * Created by SKY on 2017/5/29.
 */
interface IMainView : IBaseView {
    fun setData(data: List<ActivityModel>)
}