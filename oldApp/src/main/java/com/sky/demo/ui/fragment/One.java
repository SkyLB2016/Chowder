package com.sky.demo.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.sky.demo.R;
import com.sky.demo.ui.BaseFragment;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.fragment_first)
public class One extends BaseFragment {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            String mTitle = getArguments().getString("title");
        }
    }
}
