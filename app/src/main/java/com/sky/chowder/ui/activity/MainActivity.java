package com.sky.chowder.ui.activity;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sky.chowder.R;
import com.sky.chowder.model.ActivityModel;
import com.sky.chowder.ui.BaseActivity;
import com.sky.chowder.ui.BaseTitle;
import com.sky.chowder.ui.adapter.MainAdapter;
import com.sky.chowder.utils.FileSizeUtil;
import com.sky.utils.JumpAct;
import com.sky.widget.MyRecyclerView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @BindView(R.id.recycle)
    MyRecyclerView recycle;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle();
        initView();
        initData();
    }

    @Override
    public void initView() {
        super.initView();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMemory();
//                getMemory1();
                showLoading();
            }
        });
        recycle.setHasFixedSize(true);
        adapter = new MainAdapter(R.layout.adapter_main);
        recycle.setAdapter(adapter);
        adapter.setOnItemClickListener(new MainAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                JumpAct.jumpActivity(MainActivity.this,
                        adapter.getDatas().get(position).getComponentName());
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

    public void setTitle() {
        BaseTitle title = new BaseTitle(this);
        title.setToolbar();
        title.setLeftButton(-1);
    }

    @Override
    public void initData() {
        adapter.setDatas(getData());
    }

    /**
     * 从manifest中获取activity的信息
     *
     * @return
     */
    protected List<ActivityModel> getData() {
        List<ActivityModel> activityInfos = new ArrayList<>();

        //Intent mainIntent = new Intent(Intent.ACTION_MAIN);//获取action为ACTION_MAIN的activity
        //mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);//筛选category为sample code的act
        //mainIntent.setPackage(getPackageName());//只选出自己应用的act
        Intent mainIntent = new Intent("com.sky.coustom");//自定义的action
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);

        if (null == resolveInfos)
            return activityInfos;

        int len = resolveInfos.size();
        for (int i = 0; i < len; i++) {
            ResolveInfo info = resolveInfos.get(i);
            //获取label,activity中未设置的话返回程序名称
            CharSequence labelSeq = info.loadLabel(pm);
            String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;
            //获取说明
            int descriptionRes = info.activityInfo.descriptionRes;
            String describe = descriptionRes == 0 ? "未添加" : getResources().getString(descriptionRes);
            //获取icon  Drawable icon = info.loadIcon(pm);
            int iconRes = info.activityInfo.icon;
            int icon = iconRes == 0 ? R.mipmap.ic_launcher : iconRes;
            activityInfos.add(new ActivityModel(label, describe, icon, info.activityInfo.name));
        }
        //排序
        Collections.sort(activityInfos, sDisplayNameComparator);
//        Collections.sort(activityInfos);//使用activityModel中的compareTo进行排序
        return activityInfos;
    }

    /**
     * 为筛选出的act进行排序
     */
    private final static Comparator<ActivityModel> sDisplayNameComparator =
            new Comparator<ActivityModel>() {
                private final Collator collator = Collator.getInstance();

                @Override
                public int compare(ActivityModel lhs, ActivityModel rhs) {
                    return collator.compare(lhs.getClassName(), rhs.getClassName());
                }
            };

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

    private void progressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("main");
        progressDialog.setMessage("main");
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMax(100);
        progressDialog.incrementProgressBy(50);
        progressDialog.setIndeterminate(false);
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showLoading();
            }
        });
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showLoading();
            }
        });
        progressDialog.setCancelable(true);
        progressDialog.show();
    }


    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
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
        showToast("freeMemory:" + Double.toString(freeMemory *1.0d/ (1024 * 1024)));
        showToast("totalMemory:" + Double.toString(totalMemory*1.0d / (1024 * 1024)));
    }

    private void getMemory1() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        showToast("memory==" + manager.getMemoryClass()+"\n;large=="+manager.getLargeMemoryClass());
    }

    public void getFileSize() {
        long sdTotal = FileSizeUtil.getTotalExternalMemorySize();
        String sdTotal1 = FileSizeUtil.formatFileSize(sdTotal, false);

        long sdshengyu = FileSizeUtil.getAvailableExternalMemorySize();
        String sdshengyu1 = FileSizeUtil.formatFileSize(sdshengyu, false);

        boolean sdFlag = FileSizeUtil.externalMemoryAvailable();
        long total = FileSizeUtil.getTotalInternalMemorySize();
        String total1 = FileSizeUtil.formatFileSize(total, false);

        long neibushengyu = FileSizeUtil.getAvailableInternalMemorySize();
        String neibushengyu1 = FileSizeUtil.formatFileSize(neibushengyu, false);

        long neicun = FileSizeUtil.getTotalMemorySize(this);
        String neicun1 = FileSizeUtil.formatFileSize(neicun, false);

        long neic = FileSizeUtil.getAvailableMemory(this);
        String neic1 = FileSizeUtil.formatFileSize(neic, false);


        FileSizeUtil.formatFileSize(9, false);

//        tv.append("sd卡总容量=" + sdTotal1 + "\n" +
//                "sd卡剩余容量=" + sdshengyu1 + "\n" +
//                "内部存储总容量=" + total1 + "\n" +
//                "内部存储剩余容量=" + neibushengyu1 + "\n" +
//                "内存总容量=" + neicun1 + "\n" +
//                "内存2剩余容量=" + neic1
//        );
    }
}

