package com.sky.widget;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
/**
 * Created by SKY on 16/5/10 下午3:50.
 */
public class MyRecyclerView extends RecyclerView {

    public MyRecyclerView(Context context) {
        this(context, null);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //设置布局管理
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        //分割线效果
//        addItemDecoration(new DividerGridItemDecoration(context));
        //addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        // 添加删除时的动画效果
        setItemAnimator(new DefaultItemAnimator());
    }
}