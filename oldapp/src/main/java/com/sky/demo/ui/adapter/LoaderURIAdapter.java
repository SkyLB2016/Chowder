package com.sky.demo.ui.adapter;

import android.animation.AnimatorInflater;
import android.graphics.Outline;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.sky.adapter.RecyclerAdapter;
import com.sky.demo.R;
import com.sky.demo.utils.ImageLoaderExecutors;

/**
 * @author sky QQ:1136096189
 * @Description:
 * @date 15/12/9 下午8:52
 */
public class LoaderURIAdapter extends RecyclerAdapter<String, LoaderURIAdapter.MyHolder> {
    private ImageLoaderExecutors imageLoader;
    public String parentPath;

    public LoaderURIAdapter(int layoutId, String parentPath) {
        super(layoutId);
        this.parentPath = parentPath;
        imageLoader = new ImageLoaderExecutors();
    }

    public LoaderURIAdapter(int layoutId) {
        super(layoutId);
        imageLoader = new ImageLoaderExecutors();
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    @Override
    protected MyHolder onCreateBodyHolder(View view) {
        return new MyHolder(view);//19139763,1215232
    }

    @Override
    protected void onAchieveHolder(MyHolder holder, int position) {
        holder.img.setTag(parentPath + "/" + datas.get(position));
        holder.img.setBackgroundResource(R.mipmap.logo);
        holder.img.setMaxWidth(300);
        holder.img.setMinimumWidth(300);
        imageLoader.loadImage(holder.img, parentPath + "/" + datas.get(position));
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

    /**
     * 空闲时在加载image
     *
     * @param start
     * @param last
     * @param viewGroup
     */
    public void setImageLoader(int start, int last, RecyclerView viewGroup) {
        for (int i = start; i <= last; i++) {
//            imageLoader.loadImage(viewGroup.findViewWithTag(parentPath + "/" + datas.get(i)), parentPath + "/" + datas.get(i));
        }
    }

    public void interruptExecutors() {
        imageLoader.closeExecutors();
    }

    public void getBitmap(int position) {

    }

    class MyHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView img;

        public MyHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            img = (ImageView) itemView.findViewById(R.id.img_describe);
        }
    }
}