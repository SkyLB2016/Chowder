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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.sky.adapter.RecyclerAdapter;
import com.sky.chowder.R;
import com.sky.chowder.api.view.IMainView;
import com.sky.chowder.model.ActivityEntity;
import com.sky.chowder.model.ActivityModel;
import com.sky.chowder.ui.adapter.MainAdapter;
import com.sky.chowder.ui.presenter.MainP;
import com.sky.chowder.utils.NullStringToEmptyAdapterFactory;
import com.sky.chowder.utils.StringNullAdapter;
import com.sky.utils.AppUtils;
import com.sky.utils.JumpAct;
import com.sky.utils.LogUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import common.base.BasePActivity;
import common.model.ApiResponse;

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

        //        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}")
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ApiResponse<List<ActivityEntity>> entity = GsonUtils.jsonToEntity(getString(R.string.jsonlist), new TypeToken<ApiResponse<List<ActivityEntity>>>() {
//                }.getType());
//                ApiResponse<List<ActivityEntity>> entity1 = GsonUtils.jsonToEntity(getString(R.string.jsonlistnull), new TypeToken<ApiResponse<List<com.sky.chowder.model.ActivityEntity>>>() {
//                }.getType());
//                LogUtils.i(entity.getObjList().toString());
//                LogUtils.i(entity1.getObjList().toString());
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()//序列化
                        .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<String>())
//                        .registerTypeAdapterFactory(NullStringToEmptyAdapterFactory.getInstance())
                        .registerTypeAdapterFactory(TypeAdapters.newFactory(String.class, new StringNullAdapter()))
//                        .registerTypeAdapter(String.class, new StringNullAdapter())
                        .serializeNulls()
                        .create();
                ApiResponse<List<ActivityEntity>> entity2 = new GsonBuilder().create().fromJson(getString(R.string.jsonlistnull), new TypeToken<ApiResponse<List<ActivityEntity>>>() {
                }.getType());
                ApiResponse<List<ActivityEntity>> entity3 = gson.fromJson(getString(R.string.jsonlistnull), new TypeToken<ApiResponse<List<ActivityEntity>>>() {
                }.getType());
                LogUtils.i(entity2.getObjList().toString());
                LogUtils.i(entity3.getObjList().toString());
                LogUtils.i(gson.toJson(entity3.getObjList()));
            }
        });
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
