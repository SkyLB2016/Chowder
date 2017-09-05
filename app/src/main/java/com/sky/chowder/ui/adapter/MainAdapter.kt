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
import com.sky.chowder.model.ActivityModel

/**
 * @author sky QQ:1136096189
 * @Description:
 * @date 15/12/9 下午8:52
 */
class MainAdapter(layoutId: Int) : RecyclerAdapter<ActivityModel>(layoutId) {

    override fun onAchieveHolder(holder: RecyclerHolder, position: Int) {
        holder.setText(R.id.tv_name, (position + 1).toString() + "." + datas[position].className)
        holder.setText(R.id.tv_describe, datas[position].describe)
        holder.setImage(R.id.img_describe, datas[position].img)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            //使用ripple
            //((CardView) holder.getView(R.id.cardView)).setRadius(new Random().nextInt(50));
            //((CardView) holder.getView(R.id.cardView)).setCardElevation(new Random().nextInt(50));
            holder.getView<View>(R.id.cardView).background = context.getDrawable(R.drawable.ripple)
            //点击效果，阴影效果
            //((CardView) holder.getView(R.id.cardView)).setCardElevation(new Random().nextInt(100));
            holder.getView<View>(R.id.cardView).stateListAnimator = AnimatorInflater.loadStateListAnimator(context, R.drawable.state_list_animator)
            //视图裁剪
            holder.getView<View>(R.id.img_describe).clipToOutline = true
            holder.getView<View>(R.id.img_describe).outlineProvider = object : ViewOutlineProvider() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                override fun getOutline(view: View, outline: Outline) {
                    outline.setRoundRect(view.left, view.top,
                            view.right, view.bottom, 60f)
                }
            }
        } else {
            holder.getView<View>(R.id.cardView).background = context.resources.getDrawable(R.drawable.bg_card)
        }
    }

}