package com.sky.chowder.ui.presenter

import android.content.Context
import com.sky.ErrorMes
import com.sky.chowder.R
import com.sky.chowder.api.OnRequestCallback
import com.sky.chowder.model.CourseEntity
import com.sky.chowder.utils.http.HttpUtils
import com.sky.chowder.utils.http.UseCase
import com.sky.design.api.IRefreshV
import com.sky.design.app.RefreshP
import com.sky.sdk.net.http.ApiResponse
import io.reactivex.Observable

class ImageUrlP(context: Context) : com.sky.design.app.RefreshP<com.sky.design.api.IRefreshV<CourseEntity>>(context) {
    override fun getDataList() {
        mView.showLoading()
        object : UseCase<com.sky.sdk.net.http.ApiResponse<CourseEntity>>() {
            override fun buildObservable(): Observable<com.sky.sdk.net.http.ApiResponse<CourseEntity>> {
                return HttpUtils.instance.getMuke()
            }
        }.subscribe(object : OnRequestCallback<com.sky.sdk.net.http.ApiResponse<CourseEntity>> {
            override fun onFail(error: ErrorMes) {
                mView.showToast(R.string.toast_no_data)
                mView.disLoading()
                mView.setRefreshing(false)
            }

            override fun onSuccess(data: com.sky.sdk.net.http.ApiResponse<CourseEntity>) {
                mView.disLoading()
                mView.setRefreshing(false)
//                totalCount = data.totalCount
                val datalist = data.data

                if (datalist != null && datalist.isEmpty()) return mView.showToast(R.string.toast_no_data)
                if (page == 1) mView.setAdapterList(datalist)
                else mView.addAdapterList(datalist)
            }
        })
    }
}