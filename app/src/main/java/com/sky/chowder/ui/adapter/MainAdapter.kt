package com.sky.chowder.ui.adapter

import android.animation.AnimatorInflater
import android.graphics.Outline
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.animation.LayoutAnimationController
import android.view.animation.ScaleAnimation
import com.sky.adapter.RecyclerAdapter
import com.sky.adapter.RecyclerHolder
import com.sky.chowder.R
import com.sky.chowder.model.ActivityModel
import com.sky.utils.LogUtils
import kotlinx.android.synthetic.main.adapter_main.view.*

/**
 * Created by SKY on 2015/12/6.
 * 主页
 */
class MainAdapter(layoutId: Int) : RecyclerAdapter<ActivityModel>(layoutId) {

    override fun onAchieveHolder(holder: RecyclerHolder, position: Int) {
        val scale = ScaleAnimation(0f, 1f, 0f, 1f)
        scale.duration = 2000
        if (position % 2 == 1) {
            val controller = LayoutAnimationController(scale, 0.5f)
            controller.order = LayoutAnimationController.ORDER_RANDOM
            (holder?.itemView as ViewGroup).layoutAnimation = controller
        } else holder?.itemView.startAnimation(scale)
        with(holder!!.itemView) {
            tv_name.text = "${position + 1}.${datas[position].className}"
            tv_describe.text = datas[position].describe
            image.background = resources.getDrawable(datas[position].img)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //使用ripple
                //((CardView) holder.getView(R.id.cardView)).setRadius(new Random().nextInt(50));
                //((CardView) holder.getView(R.id.cardView)).setCardElevation(new Random().nextInt(50));
                cardView.background = context.getDrawable(R.drawable.ripple)
                //点击效果，阴影效果
                //((CardView) holder.getView(R.id.cardView)).setCardElevation(new Random().nextInt(100));
                cardView.stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.drawable.state_list_animator)
                //视图裁剪
                image.clipToOutline = true
                image.outlineProvider = object : ViewOutlineProvider() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    override fun getOutline(view: View, outline: Outline) {
                        outline.setRoundRect(view.left, view.top, view.right, view.bottom, 60f)
                    }
                }
            } else
                cardView.background = context.resources.getDrawable(R.drawable.bg_card)
            setOnClickListener { v -> LogUtils.i("lkjdflkajdkf") }
        }
    }
}