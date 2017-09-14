package com.sky.chowder.ui.adapter;

import android.animation.AnimatorInflater;
import android.graphics.Outline;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.sky.adapter.RecyclerAdapter;
import com.sky.adapter.RecyclerHolder;
import com.sky.chowder.R;
import com.sky.chowder.model.CourseEntity;

/**
 * Created by SKY on 2015/12/9 20:52.
 */
public class LoaderURLAdapter extends RecyclerAdapter<CourseEntity> {
    public LoaderURLAdapter(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void onAchieveHolder(RecyclerHolder holder, int position) {

        holder.setText(R.id.tv_name, position + 1 + "." + datas.get(position).getName());
        holder.setText(R.id.tv_describe, datas.get(position).getDescription());

        ImageView img = holder.getView(R.id.img_describe);
        CardView cardView = holder.getView(R.id.cardView);

        img.setTag(datas.get(position).getPicBig());
//        imageLoader.showAsyncImage(holder.img, datas.get(position).getPicBig());
        img.setBackgroundResource(R.mipmap.ic_launcher);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            //使用ripple
            cardView.setBackground(context.getDrawable(R.drawable.ripple));
            //点击效果，阴影效果
            cardView.setStateListAnimator(
                    AnimatorInflater.loadStateListAnimator(context, R.drawable.state_list_animator));
            //视图裁剪
            img.setClipToOutline(true);
            img.setOutlineProvider(new ViewOutlineProvider() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(view.getLeft(), view.getTop(),
                            view.getRight(), view.getBottom(), 30);
                }
            });
        }
    }
}