package com.sky.chowder.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sky.chowder.R;
import com.sky.chowder.ui.widget.WorkProgress;

/**
 * Created by libin on 2018/9/6 上午10:41.
 */
public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        WorkProgress progress = findViewById(R.id.progressBar);

        progress.setCurrent(70);
//        progress.setBeyond(200);
    }
}
