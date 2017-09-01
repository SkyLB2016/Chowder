package com.sky.chowder.ui.adapter;

import android.animation.AnimatorInflater;
import android.graphics.Outline;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.sky.adapter.RecyclerAdapter;
import com.sky.adapter.RecyclerHolder;
import com.sky.chowder.R;
import com.sky.chowder.model.ActivityModel;

/**
 * @author sky QQ:1136096189
 * @Description:
 * @date 15/12/9 下午8:52
 */
public class MainAdapter extends RecyclerAdapter<ActivityModel> {

    public MainAdapter(int layoutId) {
        super(layoutId);
    }

    protected void onAchieveHolder(RecyclerHolder holder, int position) {
        holder.setText(R.id.tv_name, position + 1 + "." + datas.get(position).getClassName());
        holder.setText(R.id.tv_describe, datas.get(position).getDescribe());
        holder.setImage(R.id.img_describe, datas.get(position).getImg());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            //使用ripple
            //((CardView) holder.getView(R.id.cardView)).setRadius(new Random().nextInt(50));
            //((CardView) holder.getView(R.id.cardView)).setCardElevation(new Random().nextInt(50));
            holder.getView(R.id.cardView).setBackground(context.getDrawable(R.drawable.ripple));
            //点击效果，阴影效果
            //((CardView) holder.getView(R.id.cardView)).setCardElevation(new Random().nextInt(100));
            holder.getView(R.id.cardView).setStateListAnimator(
                    AnimatorInflater.loadStateListAnimator(context, R.drawable.state_list_animator)
            );
            //视图裁剪
            holder.getView(R.id.img_describe).setClipToOutline(true);
            holder.getView(R.id.img_describe).setOutlineProvider(new ViewOutlineProvider() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(view.getLeft(), view.getTop(),
                            view.getRight(), view.getBottom(), 60);
                }
            });
        } else{
            holder.getView(R.id.cardView).setBackground(context.getResources().getDrawable(R.drawable.bg_card));
        }
    }

}