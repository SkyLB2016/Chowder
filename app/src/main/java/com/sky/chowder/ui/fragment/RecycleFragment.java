package com.sky.chowder.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sky.base.BaseFragment;
import com.sky.chowder.R;
import com.sky.chowder.ui.adapter.RecyclerViewAdapter;

import butterknife.BindView;

/**
 * @author sky QQ:1136096189
 * @Description:
 * @date 15/11/28 下午2:00
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
        mRecyclerView.setAdapter(new RecyclerViewAdapter(R.layout.item_recycle_card_main));
    }
}
