package com.sky.chowder.ui.adapter

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import com.sky.adapter.RecyclerAdapter
import com.sky.adapter.RecyclerHolder

/**
 * Created by SKY on 2015/11/28 14:07.
 */
class CollapsingAdapter(layoutId: Int) : RecyclerAdapter<Void>(layoutId) {

    override fun onAchieveHolder(holder: RecyclerHolder, position: Int) {
        val view = holder.itemView
        view.setOnClickListener {
            val animator = ObjectAnimator.ofFloat(view, "translationZ", 20f, 80f)
            animator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    context.startActivity(Intent().setAction("com.sky.action.detail"))
                }
            })
            animator.start()
        }
    }
    override fun getItemCount(): Int = 10
}
