package com.sky.chowder.ui.adapter

import android.animation.AnimatorInflater
import android.graphics.Outline
import android.graphics.Typeface
import android.os.Build
import androidx.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import com.sky.adapter.RecyclerAdapter
import com.sky.adapter.RecyclerHolder
import com.sky.chowder.R
import com.sky.chowder.model.CourseEntity
import kotlinx.android.synthetic.main.adapter_main_01.view.*
import java.util.*

/**
 * Created by SKY on 2015/12/9 20:52.
 */
class LoaderURLAdapter(layoutIds: List<Int>) : RecyclerAdapter<CourseEntity>(0) {
    private var layoutIds = ArrayList<Int>()//主体布局

    init {
        this.layoutIds = layoutIds as ArrayList<Int>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        context = parent.context
        return RecyclerHolder(LayoutInflater.from(context).inflate(layoutIds[viewType % layoutIds.size], parent, false))
    }

    override fun onAchieveHolder(holder: RecyclerHolder, position: Int) {
        val face = Typeface.createFromAsset(context.assets, "font/Lobster-Regular.ttf")
        with(holder.itemView) {
            tvName.typeface = face
            tvDescribe.typeface = face
            val viewType = getItemViewType(position)
            when (viewType % layoutIds.size) {
                0 -> setView(position)
                1 -> setView1(position)
                2 -> setView2(position)
                3 -> setView3(position)
            }
        }
    }

    private fun View.setView(position: Int) {
        tvName.text = "${position + 1}.${datas[position].name}   第一种布局"
        tvDescribe.text = datas[position].description
        image.tag = datas[position].picBig
        clipView()
    }

    private fun View.setView1(position: Int) {
        tvName.text = "${position + 1}.${datas[position].name}   第二种布局"
        tvDescribe.text = datas[position].description
        image.tag = datas[position].picBig
    }

    private fun View.setView2(position: Int) {
        tvName.text = "${position + 1}.${datas[position].name}   第三种布局"
        tvDescribe.text = datas[position].description
        image.tag = datas[position].picBig
    }

    private fun View.setView3(position: Int) {
        tvName.text = "${position + 1}.${datas[position].name}   第四种布局"
        tvDescribe.text = datas[position].description
        image.tag = datas[position].picBig
        clipView()
    }

    private fun View.clipView() {
        image.setBackgroundResource(R.mipmap.ic_launcher)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
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
                    outline.setRoundRect(view.left, view.top, view.right, view.bottom, 30f)
                }
            }
        } else cardView.background = context.resources.getDrawable(R.drawable.bg_card)
    }
}