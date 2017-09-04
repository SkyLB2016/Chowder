package com.sky.demo.ui.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.text.format.Formatter;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sky.adapter.RecyclerAdapter;
import com.sky.demo.R;
import com.sky.demo.model.ProcessEntity;
import com.sky.demo.ui.BaseActivity;
import com.sky.demo.ui.adapter.ProcessAdapter;
import com.sky.demo.ui.widget.pending.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MemoryActivity extends BaseActivity {

    private MyRecyclerView recycle;
    private TextView tv;
    private ActivityManager manager;
    private ProcessAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);
        setTitle();
        tv = getView(R.id.tv);
        recycle = getView(R.id.recycle);
        registerForContextMenu(recycle);

        //获得ActivityManager服务的对象
        manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        getRunningAppProcessInfo();
        adapter = new ProcessAdapter(R.layout.adapter_memory);
        adapter.setDatas(processEntities);
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                new AlertDialog.Builder(MemoryActivity.this).setMessage("是否杀死该进程")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //杀死该进程，释放进程占用的空间
                                manager.killBackgroundProcesses(processEntities.get(position).getProcessName());
                                //刷新界面
                                getRunningAppProcessInfo();
                                adapter.setDatas(processEntities);
                                tv.setText("当前系统进程共有：" + processEntities.size());

                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
            }
        });
        recycle.setAdapter(adapter);
        tv.setText("系统可用内存为" + getMemory() + ";当前系统进程共有：" + processEntities.size());

    }

    List<ProcessEntity> processEntities;

    public void getRunningAppProcessInfo() {
        processEntities = new ArrayList<>();
        List<ActivityManager.RunningAppProcessInfo> appRunProList = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo pro : appRunProList) {
            // 进程ID号
            int pid = pro.pid;
            // 用户ID 类似于Linux的权限不同，ID也就不同 比如 root等
            int uid = pro.uid;
            // 进程名，默认是包名或者由属性android：process=""指定
            String processName = pro.processName;
            // 获得该进程占用的内存
            int[] myMempid = new int[]{pid};
            // 此MemoryInfo位于android.os.Debug.MemoryInfo包中，用来统计进程的内存信息
            Debug.MemoryInfo[] memoryInfo = manager.getProcessMemoryInfo(myMempid);
            // 获取进程占内存用信息 kb单位
            int memSize = memoryInfo[0].dalvikPrivateDirty;

            String appName = "";
            try {
                ApplicationInfo appinfo = getPackageManager().getApplicationInfo(processName, 0);
                appName = (String) appinfo.loadLabel(getPackageManager());
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            // 构造一个ProcessInfo对象
            ProcessEntity processInfo = new ProcessEntity();
            processInfo.setPid(pid);
            processInfo.setUid(uid);
            processInfo.setMemSize(memSize);
            processInfo.setProcessName(processName);
            processInfo.setAppName(appName);
            processEntities.add(processInfo);

            // 获得每个进程里运行的应用程序(包),即每个应用程序的包名
//            String[] packageList = pro.pkgList;
//            LogUtils.i("process id is " + pid + "has " + packageList.length);
//            for (String pkg : packageList) {
//                LogUtils.i("packageName " + pkg + " in process id is -->" + pid);
//            }
        }
    }

    public String getMemory() {
        //获得MemoryInfo对象,获得系统可用内存，保存在MemoryInfo对象上
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(memoryInfo);
        //调用系统函数，字符串转换 long -String KB/MB
        return Formatter.formatFileSize(this, memoryInfo.availMem);
    }

    private static final int KILL_PORCESS = 1;
    private static final int SEARCH_RUNNING_APP = 2;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 0, KILL_PORCESS, "杀死该进程");
        menu.add(0, 0, SEARCH_RUNNING_APP, "运行在该进程的应用程序");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case KILL_PORCESS: // 杀死该进程 ， 重新加载界面
                new AlertDialog.Builder(this).setMessage("是否杀死该进程")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();
                break;
            case SEARCH_RUNNING_APP: // 查看运行在该进程的应用程序信息
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }
}
