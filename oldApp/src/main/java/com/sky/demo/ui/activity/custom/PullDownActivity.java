package com.sky.demo.ui.activity.custom;

import android.os.Bundle;

import com.sky.demo.R;
import com.sky.demo.ui.BaseActivity;

public class PullDownActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pulldown);
        showToast(getIntent().getStringExtra("message"));
    }
}
