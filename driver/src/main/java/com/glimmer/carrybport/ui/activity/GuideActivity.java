package com.glimmer.carrybport.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.glimmer.carrybport.ui.adapter.GuideAdapter;

/**
 * Created by sky on 2017/6/7.
 * 引导页
 */
public class GuideActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        ViewPager viewPager = new ViewPager(this);
        setContentView(viewPager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(new GuideAdapter(this));

    }

}
