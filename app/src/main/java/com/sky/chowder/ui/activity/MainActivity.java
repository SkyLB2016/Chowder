package com.sky.chowder.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
}
//大地之毁灭 	地 	地14空12 	敌方·全体 	攻击 	160 	90 	　 	20 	20 	7

//冰狱冥嚎 	水 	水25风12空5 	敌方·全体 	攻击 	150 	60 	20%几率冻结（400AT） 	20 	20 	1

//弧形日珥 	火 	地4火15风8空8 	敌方·全体 	攻击 	180 	130 	　 	20 	20 	1

//天龙卷 	风 	地3风20空5 	敌方·全体 	攻击 	140 	65 	　 	20 	20 	1
//审判之雷 	风 	风15空8 	敌方·地点指定直线2 	攻击 	80 	65 	20%几率封技（400AT） 	15 	20 	1

//死亡咆哮 	时 	时15空10幻10 	敌方·全体 	攻击 	120 	25 	20%几率战斗不能 	20 	20 	1
//堕落深渊 	时 	风6时18空6幻6 	敌方·目标指定圆5 	攻击 	90 	80 	20%几率气绝（300AT） 	25 	20 	1

//暗物质 	空 	空4 	敌方·单体 	攻击 	40 	90 	　 	15 	25 	1
//破碎虚空 	空 	地3火3风5空12 	敌方·地点指定圆4 	攻击 	100 	120 	吸引 	20 	25 	1

//银色荆棘 	幻 	时4空12幻20 	敌方·地点指定圆3 	攻击 	90 	0 	90%几率混乱（200AT） 	13 	30 	1

//大地之墙 	地 	地8空4 	我方·目标指定圆2 	辅助 	75 	- 	完全防御1次 	20 	20 	-
//盖亚之盾 	地 	地16空15 	我方·全体 	辅助 	220 	- 	完全防御1次 	28 	30 	-

//中回复术·复 	水 	水8风2空2 	我方·目标指定圆4 	回复 	55 	- 	HP回复3000 	7 	20 	-
//大回复术·复 	水 	水16风8空4 	我方·目标指定圆6 	回复 	200 	- 	HP回复6500 	10 	20 	-
//全回复术·复 	水 	水22风12空6 	我方·全体 	回复 	300 	- 	HP回复至满 	10 	20 	-
//治愈术·复 	水 	水8空2幻4 	我方·目标指定圆5 	回复 	30 	- 	解除异常状态 	10 	20 	-
//圣灵术 	水 	地8水18幻4 	我方·单体 	回复 	180 	- 	解除战斗不能、 	10 	20 	-
