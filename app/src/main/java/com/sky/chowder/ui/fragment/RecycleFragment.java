package com.sky.chowder.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sky.base.BaseFragment;
import com.sky.chowder.R;
import com.sky.chowder.ui.adapter.CollapsingAdapter;

import butterknife.BindView;

/**
 * Created by SKY on 2015/11/28 14:52.
 */
public class RecycleFragment extends BaseFragment {
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutResId() {
        return R.layout.include_recycler;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initialize() {

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(new CollapsingAdapter(R.layout.item_recycle_card_main));
    }
}
