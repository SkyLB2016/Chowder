package com.sky.chowder.api.view

import com.sky.api.IBaseView
import com.sky.chowder.model.ActivityModel

/**
 * Created by SKY on 2017/5/29.
 */
interface IMainView : IBaseView {
    fun setData(data: List<ActivityModel>)
}