package com.sky.chowder.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.view.View;

import com.sky.adapter.RecyclerAdapter;
import com.sky.adapter.RecyclerHolder;

/**
 * @author sky QQ:1136096189
 * @Description:
 * @date 15/11/28 下午2:07
 */
public class RecyclerViewAdapter extends RecyclerAdapter<Void> {
    public RecyclerViewAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void onAchieveHolder(RecyclerHolder holder, int position) {
        final View view = holder.itemView;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationZ", 20, 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        context.startActivity(new Intent().setAction("com.sky.action.detail"));
                    }
                });
                animator.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
