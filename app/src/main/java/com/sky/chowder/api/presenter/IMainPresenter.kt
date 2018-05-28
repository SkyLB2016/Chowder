package com.sky.chowder.api.presenter

import common.api.IBasePresenter

/**
 * Created by SKY on 2017/5/31.
 */
interface IMainPresenter : IBasePresenter {
    fun showToast(toast: String)
}