package com.sky.chowder.ui.widget;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.sky.chowder.R;
import com.sky.chowder.ui.adapter.PopPagerAdapter;
import com.sky.widget.BasePop;

/**
 * Created by SKY on 2016/1/11 13:14.
 */
public class URIPop extends BasePop<String> {

    private String parentPath;
    private ViewPager viewPager;
    private PopPagerAdapter adapter;

    public URIPop(View contentView) {
        super(contentView);
    }

    @Override
    protected void initEvent() {
        viewPager = (ViewPager) view.findViewById(R.id.id_viewpager);
        adapter = new PopPagerAdapter();
        viewPager.setAdapter(adapter);
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    @Override
    protected void initDatas() {
        adapter.setParentPath(parentPath);
        adapter.setStrings(popDatas);
        adapter.notifyDataSetChanged();

    }

    public void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
    }
}
