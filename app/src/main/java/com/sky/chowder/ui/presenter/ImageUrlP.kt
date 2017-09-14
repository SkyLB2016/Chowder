package com.sky.chowder.ui.presenter

import android.content.Context
import com.sky.ErrorMes
import com.sky.api.IRefreshV
import com.sky.api.OnRequestCallback
import com.sky.base.RefreshP
import com.sky.chowder.R
import com.sky.chowder.model.CourseEntity
import com.sky.chowder.utils.http.HttpUtils
import com.sky.chowder.utils.http.UseCase
import com.sky.model.ApiResponse
import io.reactivex.Observable

class ImageUrlP(context: Context) : RefreshP<IRefreshV<CourseEntity>>(context){
    override fun getDataList() {
        mView.showLoading()
        object : UseCase<ApiResponse<CourseEntity>>() {
            override fun buildObservable(): Observable<ApiResponse<CourseEntity>> {
                return HttpUtils.instance.getMuke()
            }
        }.subscribe(object : OnRequestCallback<ApiResponse<CourseEntity>> {
            override fun onFail(error: ErrorMes) {
                mView.showToast(R.string.toast_no_data)
                mView.disLoading()
                mView.setRefreshing(false)
            }

            override fun onSuccess(data: ApiResponse<CourseEntity>) {
                mView.disLoading()
                mView.setRefreshing(false)
//                totalCount = data.totalCount
                val datalist = data.data

                if (datalist.isEmpty()) mView.showToast(R.string.toast_no_data)
                if (page == 1) mView.setAdapterList(datalist)
                else mView.addAdapterList(datalist)
            }
        })
    }
}