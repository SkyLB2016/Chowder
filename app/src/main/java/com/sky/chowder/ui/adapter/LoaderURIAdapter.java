package com.sky.chowder.ui.adapter;

import android.animation.AnimatorInflater;
import android.graphics.Outline;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.sky.adapter.RecyclerAdapter;
import com.sky.adapter.RecyclerHolder;
import com.sky.chowder.R;
import com.sky.chowder.utils.ImageLoaderExecutors;

/**
 * @author sky QQ:1136096189
 * @Description:
 * @date 15/12/9 下午8:52
 */
public class LoaderURIAdapter extends RecyclerAdapter<String> {
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
    protected void onAchieveHolder(RecyclerHolder holder, int position) {

        CardView cardView = holder.getView(R.id.cardView);
        ImageView img = holder.getView(R.id.img_describe);
        img.setTag(parentPath + "/" + datas.get(position));
        img.setBackgroundResource(R.mipmap.ic_launcher);
        img.setMaxWidth(300);
        img.setMinimumWidth(300);
        imageLoader.loadImage(img, parentPath + "/" + datas.get(position));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            //使用ripple
            cardView.setBackground(context.getDrawable(R.drawable.ripple));
            //点击效果，阴影效果
            cardView.setStateListAnimator(
                    AnimatorInflater.loadStateListAnimator(context, R.drawable.state_list_animator));
            //视图裁剪
            img.setClipToOutline(true);
            img.setOutlineProvider(new ViewOutlineProvider() {
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
}