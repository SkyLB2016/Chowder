package com.sky.demo.ui.adapter;

import android.animation.AnimatorInflater;
import android.graphics.Outline;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.sky.adapter.RecyclerAdapter;
import com.sky.demo.R;
import com.sky.demo.model.CourseEntity;
import com.sky.demo.utils.ImageLoaderAsync;

import java.util.List;

/**
 * @author sky QQ:1136096189
 * @Description:
 * @date 15/12/9 下午8:52
 */
public class LoaderURLAdapter extends RecyclerAdapter<CourseEntity, LoaderURLAdapter.MyHolder> {
    private ImageLoaderAsync imageLoader;

    public LoaderURLAdapter(int layoutId) {
        super(layoutId);
        imageLoader = new ImageLoaderAsync();
    }

    @Override
    public void setDatas(List<CourseEntity> datas) {
        super.setDatas(datas);

    }

    @Override
    protected MyHolder onCreateBodyHolder(View view) {
        return new MyHolder(view);//19139763,1215232
    }

    @Override
    protected void onAchieveHolder(MyHolder holder, int position) {
        holder.className.setText(position + 1 + "." + datas.get(position).getName());
        holder.describe.setText(datas.get(position).getDescription());


        holder.img.setTag(datas.get(position).getPicBig());
//        imageLoader.showAsyncImage(holder.img, datas.get(position).getPicBig());
        holder.img.setBackgroundResource(R.mipmap.pictures_no);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            //使用ripple
            holder.cardView.setBackground(context.getDrawable(R.drawable.ripple));
            //点击效果，阴影效果
            holder.cardView.setStateListAnimator(
                    AnimatorInflater.loadStateListAnimator(context, R.drawable.state_list_animator));
            //视图裁剪
            holder.img.setClipToOutline(true);
            holder.img.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(view.getLeft(), view.getTop(),
                            view.getRight(), view.getBottom(), 30);
                }
            });
        }
    }

    public void setImageLoader(int start, int last, RecyclerView recycle) {

        for (int i = start; i <= last; i++) {
            imageLoader.showAsyncImage((ImageView) recycle.findViewWithTag(datas.get(i).getPicBig()), datas.get(i).getPicBig());
        }
    }

    public void cancelAlltasks() {
        imageLoader.cancelAlltasks();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView className;
        TextView describe;
        ImageView img;

        public MyHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            className = (TextView) itemView.findViewById(R.id.tv_name);
            describe = (TextView) itemView.findViewById(R.id.tv_describe);
            img = (ImageView) itemView.findViewById(R.id.img_describe);
        }
    }
}