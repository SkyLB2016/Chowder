package com.sky.chowder.ui.adapter

import android.animation.AnimatorInflater
import android.graphics.Outline
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewOutlineProvider
import com.sky.adapter.RecyclerAdapter
import com.sky.adapter.RecyclerHolder
import com.sky.chowder.R
import com.sky.chowder.utils.imageloader.ImageLoaderExecutors
import kotlinx.android.synthetic.main.adapter_uri.view.*

/**
 * Created by SKY on 2015/12/9.
 */
class LoaderUriAdapter(layoutId: Int) : RecyclerAdapter<String>(layoutId) {
    private val imageLoader: ImageLoaderExecutors = ImageLoaderExecutors()
    var parentPath: String? = null

    override fun onAchieveHolder(holder: RecyclerHolder, position: Int) {
        with(holder!!.itemView) {
            image.tag = parentPath + "/" + datas[position]
            image.setBackgroundResource(R.mipmap.ic_launcher)
            image.maxWidth = 300
            image.minimumWidth = 300
//            imageLoader.loadImage(image, "$parentPath/${datas[position]}")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                //使用ripple
                cardView.background = context.getDrawable(R.drawable.ripple)
                //点击效果，阴影效果
                cardView.stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.drawable.state_list_animator)
                //视图裁剪
                image.clipToOutline = true
                image.outlineProvider = object : ViewOutlineProvider() {
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun getOutline(view: View, outline: Outline) {
                        outline.setRoundRect(view.left, view.top, view.right, view.bottom, 30f)
                    }
                }
            }
        }
    }

    /**
     * 空闲时在加载image
     *
     * @param start
     * @param last
     * @param viewGroup
     */
    fun setImageLoader(start: Int, last: Int, viewGroup: RecyclerView) {
        for (i in start..last) {
            imageLoader.loadImage(viewGroup.findViewWithTag("$parentPath/${datas[i]}"), "$parentPath/${datas[i]}");
        }
    }

    fun interruptExecutors() {
        imageLoader.closeExecutors()
    }
}