package com.sky.chowder.api.view

import com.sky.chowder.model.ImageFloder
import common.api.IRefreshV
import java.io.File

interface ImageUriV<T> : IRefreshV<T> {
    fun showFloderPop(floders: List<ImageFloder>)
    fun setAdapterData(parent: File?)
}
