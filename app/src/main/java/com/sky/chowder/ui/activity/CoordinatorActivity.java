package com.sky.chowder.ui.activity;

import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.sky.chowder.R;
import com.sky.utils.LogUtils;

import org.jetbrains.annotations.Nullable;

import common.base.BaseNoPActivity;

/**
 * Created by libin on 2019/0520 19:25 Monday.
 */
public class CoordinatorActivity extends BaseNoPActivity {
    private int expendedtag = 1;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initialize(@Nullable Bundle savedInstanceState) {

        Toolbar toolbarv7 = findViewById(R.id.toolbarv7);
        toolbarv7.setNavigationIcon(R.mipmap.ic_back);
        setSupportActionBar(toolbarv7);
        toolbarv7.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CollapsingToolbarLayout collapsing = findViewById(R.id.collapsing);
        final AppBarLayout appBarLayout = findViewById(R.id.appbar);

        collapsing.setTitle("我的课程");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            //verticalOffset是当前appbarLayout的高度与最开始appbarlayout高度的差，向上滑动的话是负数
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //通过日志得出活动启动是两次，由于之前有setExpanded所以三次
                LogUtils.d("启动活动调用监听次数", "几次");
                if (getSupportActionBar().getHeight() - appBarLayout.getHeight() == verticalOffset) {
                    //折叠监听
                    //Toast.makeText(MainActivity.this,"折叠了",Toast.LENGTH_SHORT).show();
                }
                if (expendedtag == 2 && verticalOffset == 0) {
                    //展开监听
                    Toast.makeText(CoordinatorActivity.this, "展开了", Toast.LENGTH_SHORT).show();
                }
                if (expendedtag != 2 && verticalOffset == 0) {
                    expendedtag++;
                }
                LogUtils.i("差值==" + verticalOffset);
            }
        });
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appBarLayout.setExpanded(false);
                expendedtag = 1;
            }
        });
    }

    @Override
    public void loadData() {

    }
//    override fun loadData() = Unit
//    fun checkin(view: View) = Snackbar.make(view, "checkin success!", Snackbar.LENGTH_SHORT).show()
}

