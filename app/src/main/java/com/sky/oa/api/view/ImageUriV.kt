package com.sky.oa.api.view

import com.sky.oa.model.ImageFloder
import com.sky.design.api.IRefreshV
import java.io.File

interface ImageUriV<T> : IRefreshV<T> {
    fun showFloderPop(floders: List<ImageFloder>)
    fun setAdapterData(parent: File?)
}
