package com.sky.chowder.ui.adapter;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sky.chowder.R;
import com.sky.chowder.ui.widget.ZoomImageView;
import com.sky.chowder.utils.ImageLoaderExecutors;

import java.util.List;

/**
 * Created by SKY on 2016/1/13 14:09.
 */
public class PopPagerAdapter extends PagerAdapter {
    private List<String> strings;
    private String parentPath;
    private ImageView[] views;
    private ImageLoaderExecutors loader;

    public PopPagerAdapter() {
        loader = new ImageLoaderExecutors();
    }

    public List<String> getStrings() {
        return strings;
    }

    public void setStrings(List<String> strings) {
        this.strings = strings;
        if (views != null)
            views = null;
        views = new ImageView[strings.size()];
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ZoomImageView imageView = new ZoomImageView(container.getContext());
        imageView.setBackgroundResource(R.color.white);
        imageView.setColorFilter(Color.parseColor("#77000000"));
        loader.loadImage(imageView, parentPath + "/" + strings.get(position));

        container.addView(imageView);
        views[position] = imageView;
        if (Runtime.getRuntime().totalMemory() > Runtime.getRuntime().maxMemory() * 5 / 6) {
        }
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position > views.length)
            container.removeAllViews();
        else {
            container.removeView(views[position]);
            views[position] = null;
        }
    }

    @Override
    public int getCount() {
        return strings == null ? 0 : strings.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


}
