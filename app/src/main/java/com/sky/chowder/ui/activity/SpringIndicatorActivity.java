package com.sky.chowder.ui.activity;

import com.google.common.collect.Lists;
import com.sky.chowder.R;
import com.sky.chowder.ui.BaseActivity;
import com.sky.chowder.ui.fragment.IndicatorFragment;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;

/**
 * Created by 李彬 on 2017/3/6.
 */

public class SpringIndicatorActivity extends BaseActivity {


    @BindView(R.id.viewpager)
    ScrollerViewPager viewpager;
    @BindView(R.id.indicator)
    SpringIndicator indicator;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_indicator;
    }
    @Override
    public void initialize() {
        PagerModelManager manager = new PagerModelManager();
        manager.addCommonFragment(IndicatorFragment.class, getRes(), getList());
        ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(),manager);
        viewpager.setAdapter(adapter);
        viewpager.fixScrollSpeed();
        indicator.setViewPager(viewpager);
    }

    private List<? extends Serializable> getRes() {

        return Lists.newArrayList(R.mipmap.bg1,R.mipmap.bg2,R.mipmap.bg3,R.mipmap.bg4,R.mipmap.bg4);
    }

    private List<String> getList() {

        return Lists.newArrayList("1", "2", "3", "4", "5");

    }
}
