package com.sky.demo.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sky.demo.R;
import com.sky.demo.ui.BaseFragment;
import com.sky.demo.ui.adapter.RecyclerViewAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


/**
 * @author sky QQ:1136096189
 * @Description:
 * @date 15/11/28 下午2:00
 */
@ContentView(R.layout.fragment_recycle)
public class RecycleFragment extends BaseFragment {
    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(new RecyclerViewAdapter(R.layout.item_recycle_card_main));
    }
}
