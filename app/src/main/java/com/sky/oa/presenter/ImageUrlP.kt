package com.sky.oa.presenter

import android.content.Context
import com.sky.oa.model.CourseEntity
import com.sky.design.api.IRefreshV
import com.sky.design.app.RefreshP
import com.sky.oa.R
import com.sky.oa.api.OnRequestCallback
import com.sky.oa.utils.http.HttpUtils
import com.sky.oa.utils.http.UseCase
import com.sky.sdk.net.http.ApiResponse
import io.reactivex.Observable

class ImageUrlP(context: Context) : RefreshP<IRefreshV<CourseEntity>>(context) {
    override fun getDataList() {
        mView.showLoading()
        object : UseCase<ApiResponse<CourseEntity>>() {
            override fun buildObservable(): Observable<ApiResponse<CourseEntity>> {
                return HttpUtils.instance.getMuke()
            }
        }.subscribe(object : OnRequestCallback<ApiResponse<CourseEntity>> {
            override fun onFail(code: Int, message: String?) {
                mView.showToast(R.string.toast_no_data)
                mView.disLoading()
                mView.setRefreshing(false)
            }

            override fun onSuccess(data: ApiResponse<CourseEntity>) {
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