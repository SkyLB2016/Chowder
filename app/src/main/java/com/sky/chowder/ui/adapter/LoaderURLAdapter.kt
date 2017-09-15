package com.sky.chowder.ui.adapter

import android.animation.AnimatorInflater
import android.graphics.Outline
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import android.view.ViewOutlineProvider
import com.sky.adapter.RecyclerAdapter
import com.sky.adapter.RecyclerHolder
import com.sky.chowder.R
import com.sky.chowder.model.CourseEntity
import kotlinx.android.synthetic.main.adapter_main.view.*

/**
 * Created by SKY on 2015/12/9 20:52.
 */
class LoaderURLAdapter(layoutId: Int) : RecyclerAdapter<CourseEntity>(layoutId) {
    override fun onAchieveHolder(holder: RecyclerHolder, position: Int) {
        with(holder.itemView) {
            tv_name.text = "${position + 1}.${datas[position].name}"
            tv_describe.text = datas[position].description
            image.tag = datas[position].picBig
            image.setBackgroundResource(R.mipmap.ic_launcher)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                //使用ripple
                cardView.background = context.getDrawable(R.drawable.ripple)
                //点击效果，阴影效果
                cardView.stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.drawable.state_list_animator)
                //视图裁剪
                image.clipToOutline = true
                image.outlineProvider = object : ViewOutlineProvider() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    override fun getOutline(view: View, outline: Outline) {
                        outline.setRoundRect(view.left, view.top, view.right, view.bottom, 30f)
                    }
                }
            }
        }
    }
}