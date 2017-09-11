package com.sky.chowder.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.sky.base.BaseFragment;
import com.sky.chowder.R;

/**
 * Created by SKY on 2015/12/9 20:52.
 */
public class One extends BaseFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            String mTitle = getArguments().getString("title");
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_first;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initialize() {

    }
}
