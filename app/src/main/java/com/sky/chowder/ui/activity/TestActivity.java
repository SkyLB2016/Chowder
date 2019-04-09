package com.sky.chowder.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sky.chowder.R;

/**
 * Created by libin on 2018/9/6 上午10:41.
 */
public class TestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        TextView tv =findViewById(R.id.tv);

    }
}