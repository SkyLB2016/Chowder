package com.sky.chowder.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toolbar;

import com.sky.adapter.RecyclerAdapter;
import com.sky.chowder.R;
import com.sky.chowder.api.view.IMainView;
import com.sky.chowder.model.ActivityModel;
import com.sky.chowder.ui.adapter.MainAdapter;
import com.sky.chowder.ui.presenter.MainP;
import com.sky.utils.AppUtils;
import com.sky.utils.JumpAct;
import com.sky.utils.LogUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import common.base.BasePActivity;

/**
 * Created by libin on 2018/11/13 4:26 PM.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends BasePActivity<MainP> implements Toolbar.OnMenuItemClickListener, IMainView {
    //需要替换的图片
    //drawable-hdpi notification_default_icon.png
    //drawable-xhdpi notification_default_icon.png,landing_center
    //drawable-xhdpi ic_splash_indicator_selected
    //lib.account drawable ic_splash_indicator_selected

    private MainAdapter adapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected MainP creatPresenter() {
        return new MainP(this);
    }

    @Override
    protected void initialize(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        baseTitle.setLeftButton(-1);
        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        adapter = new MainAdapter(R.layout.adapter_main_delete);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                JumpAct.jumpActivity(MainActivity.this, adapter.getDatas().get(position).getComponentName());
            }
        });
        AppUtils.isPermissions(this,
                new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE},
                new int[]{0, 0, 0}
        );
        //Rw2 B2 U2 Lw U2 Rw' U2 Rw U2 F2 Rw F2 Lw' B2 Rw2
        final double dou = 82973.908;
        //LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gsonBuilder();
            }
        });
    }

    private void gsonBuilder() {
        double a = 1.10000000000000001;
        double b = 1.10000000000000002;
        double c = 1.1000000000000001;
        double d = 1.1000000000000002;
        LogUtils.i("a>b="+(a>b));
        LogUtils.i("a<b="+(a<b));
        LogUtils.i("c<d="+(c<d));
        LogUtils.i("c>d="+(c>d));
    }

    @Override
    public void setData(@NotNull List<ActivityModel> data) {
        adapter.setDatas(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_01:
                startOther("com.cx.architecture", "com.cx.architecture.ui.activity.LoginActivity");
                break;
            case R.id.action_02:
                startOther("com.gc.materialdesigndemo", "com.gc.materialdesigndemo.ui.MainActivity");
                break;
        }
        return false;
    }


    private void startOther(String packageName, String componentName) {
        try {
            JumpAct.jumpActivity(this, packageName, componentName);
        } catch (ActivityNotFoundException e) {
            showToast("程序未安装");
        }
    }

    private long lastBack = 0;

    @Override
    public void onBackPressed() {
        long now = System.currentTimeMillis();
        if (now - lastBack > 3000) {
            showToast(getString(R.string.toast_exit));
            lastBack = now;
        } else
            super.onBackPressed();
    }
}