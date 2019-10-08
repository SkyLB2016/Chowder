package com.sky.chowder.api.view

import com.sky.chowder.model.ImageFloder
import com.sky.design.api.IRefreshV
import java.io.File

interface ImageUriV<T> : com.sky.design.api.IRefreshV<T> {
    fun showFloderPop(floders: List<ImageFloder>)
    fun setAdapterData(parent: File?)
}
