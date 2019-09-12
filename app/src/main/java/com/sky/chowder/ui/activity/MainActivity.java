package com.sky.chowder.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    //易筋经、五禽戏、六字诀和八段锦
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

        builder.append("天地玄黄");
        builder.append("\n\n");
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DATE, 1);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);

                LogUtils.i("时间==" + calendar.getTimeInMillis());
                calendar.set(year, month, 1, 0, 0, 0);
                LogUtils.i("时间==" + calendar.getTimeInMillis());
                calendar.set(year, month + 1, 1, 0, 0, 0);
                LogUtils.i("时间==" + calendar.getTimeInMillis());
                LogUtils.i("时间==" + calendar.get(Calendar.MONTH));
                LogUtils.i("时间==" + calendar.get(Calendar.DATE));

                calendar.setTimeInMillis(calendar.getTimeInMillis() - 1000);

                LogUtils.i("时间==" + calendar.get(Calendar.MONTH));
                LogUtils.i("时间==" + calendar.get(Calendar.DATE));

            }
        });
    }

    SpannableStringBuilder builder = new SpannableStringBuilder();

    private static final String EMAIL = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

    private void gsonBuilder() {
//        pattern("1136096189@qq.com",EMAIL);
//        pattern("n.(/","n\\.\\(\\/");
//        presenter.getActi();
//        video();
        long start = System.currentTimeMillis();
        LogUtils.i("值为==" + getNumber(40));
        long end = System.currentTimeMillis();
        LogUtils.i("所耗时间==" + (end - start));

        start = System.currentTimeMillis();
        LogUtils.i("40值为==" + getNumber1(40));
        LogUtils.i("50值为==" + getNumber1(50));
        end = System.currentTimeMillis();
        LogUtils.i("所耗时间==" + (end - start));

    }

    public int getNumber(int num) {
        if (num == 1 || num == 2) {
            return 1;
        } else {
            return getNumber(num - 1) + getNumber(num - 2);
        }
    }

    public int getNumber1(int num) {
        if (num == 1 || num == 2) {
            return 1;
        }
        int temp = 0;
        int n_1 = 1;//前一个
        int n_2 = 1;//前二个
        for (int i = 3; i <= num; i++) {
            temp = n_1 + n_2;
            n_2 = n_1;
            n_1 = temp;
        }
        return temp;
    }

//    String text = "aaa";
//        LogUtils.i("aaa==" + isNumerEX("aaa"));
//        LogUtils.i("-a=" + isNumerEX("-a"));
//        LogUtils.i("12a==" + isNumerEX("12a"));
//        LogUtils.i("a12==" + isNumerEX("a12"));
//        LogUtils.i("111==" + isNumerEX("111"));
//        LogUtils.i("1.11==" + isNumerEX("1.11"));
//        LogUtils.i("1.11342==" + isNumerEX("1.11342"));
//        LogUtils.i("-2==" + isNumerEX("-2"));
//        LogUtils.i("-1==" + isNumerEX("-"));
//        LogUtils.i("-1.22222==" + isNumerEX("-1.22222"));

    /**
     * 判断字符串是否为数字
     * 包括负数
     *
     * @param str
     * @return
     */
    public static boolean isNumerEX(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Pattern pattern1 = Pattern.compile("-?[0-9]*");
        return pattern.matcher(str).matches() || pattern1.matcher(str).matches();
    }

    private void pattern(String text, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        if (m.find()) {
//            int size = m.groupCount();
//            LogUtils.i("正则==" + m.group(0));
            LogUtils.i("正则==" + m.matches());
//            LogUtils.i("正则==" + size);
//            for (int i = 0; i < size; i++) {
//                LogUtils.i("正则==" + m.group(i));
//            }
        }
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

    public int[] getLocation(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        LogUtils.i(location[0] + "==xy==" + location[1]);

        Rect rect = new Rect();
        view.getLocalVisibleRect(rect);
        LogUtils.i("getLocalVisibleRect==");
        LogUtils.i("left==" + rect.left);
        LogUtils.i("top==" + rect.top);
        LogUtils.i("right==" + rect.right);
        LogUtils.i("bottom==" + rect.bottom);
        LogUtils.i("width==" + rect.width());
        LogUtils.i("height==" + rect.height());
        view.getGlobalVisibleRect(rect);

        LogUtils.i("getGlobalVisibleRect==");
        LogUtils.i("left==" + rect.left);
        LogUtils.i("top==" + rect.top);
        LogUtils.i("right==" + rect.right);
        LogUtils.i("bottom==" + rect.bottom);
        LogUtils.i("width==" + rect.width());
        LogUtils.i("height==" + rect.height());

        view.getLocationInWindow(location);
        LogUtils.i(location[0] + "==xy==" + location[1]);
        return location;
    }
}