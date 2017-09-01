package com.sky.chowder.ui.activity;

import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sky.base.BasePActivity;
import com.sky.chowder.R;
import com.sky.chowder.api.view.IMainView;
import com.sky.chowder.model.ActivityModel;
import com.sky.chowder.ui.adapter.MainAdapter;
import com.sky.chowder.ui.presenter.MainPresenter;
import com.sky.utils.JumpAct;
import com.sky.widget.MyRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BasePActivity<MainPresenter> implements Toolbar.OnMenuItemClickListener, IMainView {

    @BindView(R.id.recycle)
    MyRecyclerView recycle;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private MainAdapter adapter;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initialize() {
        baseTitle.setLeftButton(-1);

        recycle.setHasFixedSize(true);
        adapter = new MainAdapter(R.layout.adapter_main);
        recycle.setAdapter(adapter);

        adapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                JumpAct.jumpActivity(MainActivity.this, adapter.getDatas().get(position).getComponentName());
            }
        });
        adapter.setOnItemLongClickListener(new MainAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                showToast("长按监听已处理");
                return true;
            }
        });
    }

    @Override
    protected void creatPresenter() {
        presenter = new MainPresenter(this);
    }

    @OnClick(R.id.fab)
    protected void fabOnclick() {
        getMemory();
        getMemory1();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        menu.add(Menu.NONE,Menu.FIRST+1,1000,"dd").setIcon(R.mipmap.back).
//                setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_01://跳转到指定的别的APP的activity
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
            JumpAct.jumpActivity(MainActivity.this, packageName, componentName);
        } catch (ActivityNotFoundException e) {
            showToast("程序未安装");
        }
    }

    private long lastBack;

    @Override
    public void onBackPressed() {
        long nowCurrent = System.currentTimeMillis();
        if (nowCurrent - lastBack > 3000) {
            showToast(getResources().getString(R.string.toast_exit));
            lastBack = nowCurrent;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setData(List<ActivityModel> data) {
        adapter.setDatas(data);
    }

    //获取电量百分比
    public int getBattery() {
        Intent battery = getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int currLevel = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int total = battery.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
        return currLevel * 100 / total;
    }

    private void getMemory() {
        Runtime rt = Runtime.getRuntime();
        long maxMemory = rt.maxMemory();
        long freeMemory = rt.freeMemory();
        long totalMemory = rt.totalMemory();
        showToast("maxMemory:" + Double.toString(maxMemory / (1024 * 1024)));
        showToast("freeMemory:" + Double.toString(freeMemory * 1.0d / (1024 * 1024)));
        showToast("totalMemory:" + Double.toString(totalMemory * 1.0d / (1024 * 1024)));
    }

    private void getMemory1() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        showToast("memory==" + manager.getMemoryClass() + "\n;large==" + manager.getLargeMemoryClass());
    }
}

