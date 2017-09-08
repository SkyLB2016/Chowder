package com.sky.chowder.ui.activity;

import android.os.Bundle;

import com.sky.base.BaseNoPActivity;
import com.sky.chowder.R;

public class PullDownActivity extends BaseNoPActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulldown);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pulldown;
    }

    @Override
    protected void initialize() {
        super.initialize();
        showToast(getIntent().getStringExtra("message"));
    }
}
