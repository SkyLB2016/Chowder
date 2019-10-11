package com.sky.oa.adapter

import android.graphics.Typeface
import android.view.ViewGroup
import android.view.animation.LayoutAnimationController
import android.view.animation.ScaleAnimation
import com.sky.design.adapter.RecyclerAdapter
import com.sky.design.adapter.RecyclerHolder
import com.sky.oa.R
import com.sky.oa.model.ActivityModel
import kotlinx.android.synthetic.main.adapter_main.view.*

/**
 * Created by SKY on 2015/12/6.
 * 主页
 */
class MainAdapter(layoutId: Int) : RecyclerAdapter<ActivityModel>(layoutId) {
    private val fontIcon = intArrayOf(
        R.string.font,
        R.string.font01,
        R.string.font02,
        R.string.font03,
        R.string.font04,
        R.string.font05,
        R.string.font06,
        R.string.font07,
        R.string.font08
    )

    override fun onAchieveHolder(holder: RecyclerHolder, position: Int) {
        //应写成异步调用
        val scale = ScaleAnimation(0f, 1f, 0f, 1f)
        scale.duration = 100
        var face = Typeface.createFromAsset(context.assets, "font/icomoon.ttf")//字体，icomoon对应fonticon
        if (position % 2 == 1) {
            val controller = LayoutAnimationController(scale, 0.5f)
            controller.order = LayoutAnimationController.ORDER_RANDOM
            (holder?.itemView as ViewGroup).layoutAnimation = controller
//            face = Typeface.createFromAsset(context.assets, "font/Lobster-Regular.ttf")//不对应fonticon
        } else holder?.itemView.startAnimation(scale)
        with(holder!!.itemView) {
            tvName.text =
                "${datas[position].className}" + resources.getString(fontIcon[position % 9])
            tvDescribe.text =
                resources.getString(fontIcon[8 - position % 9]) + datas[position].describe
            tvImage.text = resources.getString(fontIcon[position % 9])

            tvName.typeface = face
            tvDescribe.typeface = face
            tvImage.typeface = face
            tvImage.textSize = 50f

            //对应的是adapter_main01与adapter_main02
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                //使用ripple
//                //((CardView) holder.getView(R.id.cardView)).setRadius(new Random().nextInt(50));
//                //((CardView) holder.getView(R.id.cardView)).setCardElevation(new Random().nextInt(50));
//                cardView.background = context.getDrawable(R.drawable.ripple)
//                //点击效果，阴影效果
//                //((CardView) holder.getView(R.id.cardView)).setCardElevation(new Random().nextInt(100));
//                cardView.stateListAnimator =
//                    AnimatorInflater.loadStateListAnimator(context, R.drawable.state_list_animator)
//                //视图裁剪
//                image.clipToOutline = true
//                image.outlineProvider = object : ViewOutlineProvider() {
//                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//                    override fun getOutline(view: View, outline: Outline) {
//                        outline.setRoundRect(view.left, view.top, view.right, view.bottom, 60f)
//                    }
//                }
//            } else{
//                cardView.background = context.resources.getDrawable(R.drawable.bg_card)}
//            setOnClickListener { v -> LogUtils.i("lkjdflkajdkf") }

        }
    }
}