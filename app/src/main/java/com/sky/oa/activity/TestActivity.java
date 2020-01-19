package com.sky.oa.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sky.oa.R;
import com.sky.sdk.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by libin on 2018/9/6 上午10:41.
 */
public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i(getClass().getSimpleName() + "==" + Thread.currentThread().getStackTrace()[2].getMethodName());
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        findViewById(R.id.touchC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("==执行了点击事件");
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.i(getClass().getSimpleName() + "==" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.i(getClass().getSimpleName() + "==" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.i(getClass().getSimpleName() + "==" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.i(getClass().getSimpleName() + "==" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }


    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.i(getClass().getSimpleName() + "==" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i(getClass().getSimpleName() + "==" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.i(getClass().getSimpleName() + "==" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.i(getClass().getSimpleName() + "==" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i(getClass().getSimpleName() + "==" + Thread.currentThread().getStackTrace()[2].getMethodName());
    }

}