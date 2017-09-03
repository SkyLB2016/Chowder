package com.sky.demo.ui.activity.custom;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sky.demo.R;
import com.sky.demo.common.Constants;
import com.sky.demo.ui.BaseActivity;
import com.sky.demo.ui.activity.CarouselActivity;
import com.sky.demo.ui.activity.ImageUriActivity;
import com.sky.demo.ui.activity.viewpager.BottomTabBarActivity;
import com.sky.demo.ui.activity.viewpager.TabLayoutActivity;
import com.sky.demo.ui.widget.SolarSystem;
import com.sky.demo.utils.pending.AppUtils;
import com.sky.utils.JumpAct;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by sky on 15/12/9 下午8:54.
 * 卫星菜单栏
 */
@ContentView(R.layout.activity_solar)
public class SolarSystemActivity extends BaseActivity {

    @ViewInject(R.id.solar)
    private SolarSystem solarSystem;
    private AnimationDrawable animationDrawable;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle();
        FloatingActionButton fab = getView(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("action", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showToast("out");
                            }
                        }).show();
            }
        });
        setSolarSystem();
        TextView tv = (TextView) findViewById(R.id.id_tv);
        tv.setText("v" + AppUtils.getVersionName(this)+"\n"+"可以使用ViewStub来执行惰性加载");
    }

    private void setSolarSystem() {
        RelativeLayout relative = (RelativeLayout) findViewById(R.id.relative);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int screenWidth = outMetrics.widthPixels;
        ViewGroup.LayoutParams lp = relative.getLayoutParams();
        lp.width = screenWidth / 3;
        lp.height = screenWidth / 3;
        relative.setLayoutParams(lp);
        relative.setBackgroundResource(R.drawable.solar_rect_list);
        animationDrawable = (AnimationDrawable) relative.getBackground();
        int childCount = solarSystem.getChildCount();
        FrameLayout.LayoutParams childParams = new FrameLayout.LayoutParams(
                screenWidth / 5, screenWidth / 5);
        for (int i = 0; i < childCount - 1; i++) {
            solarSystem.getChildAt(i).setLayoutParams(childParams);
        }
        //solarSystem.setPosition(SolarSystem.Position.CENTER_BOTTOM);
        solarSystem.setRadius(screenWidth / 3);
        solarSystem.setRotaMenu(true);//按钮是否旋转
        solarSystem.setIsRotate(true);//混合还是单次执行
        solarSystem.setRecoverChildView(false);
        solarSystem.setOnMenuState(new SolarSystem.MenuState() {
            @Override
            public void openMenu() {
                animationDrawable.start();
                handler.sendEmptyMessageDelayed(Constants.handler_main, 600);
//                progressDialog();
            }

            @Override
            public void closeMenu() {
                animationDrawable.start();
                handler.sendEmptyMessageDelayed(Constants.handler_main, 600);
            }
        });
        solarSystem.setOnMenuItemClickListener(new SolarSystem.onMenuItemClick() {
            @Override
            public void onItem(View view, int position) {
                String tag = (String) view.getTag();
                if (tag.equals("flow")) {
                    JumpAct.jumpActivity(SolarSystemActivity.this, CarouselActivity.class);
                } else if (tag.equals("list")) {
                    JumpAct.jumpActivity(SolarSystemActivity.this, RefreshListActivity.class);
                } else if (tag.equals("viewpager")) {
                    JumpAct.jumpActivity(SolarSystemActivity.this, TabLayoutActivity.class);
                } else if (tag.equals("recyclerview")) {
                    JumpAct.jumpActivity(SolarSystemActivity.this, ImageUriActivity.class);
                } else if (tag.equals("bitmap")) {
                    JumpAct.jumpActivity(SolarSystemActivity.this, BottomTabBarActivity.class);
                } else if (tag.equals("slidingmenu")) {
                    JumpAct.jumpActivity(SolarSystemActivity.this, SlidingMenuActivity.class);
                }
            }
        });
        solarSystem.toggleMenu(300);
    }

    private void progressDialog() {
        progressDialog = new ProgressDialog(SolarSystemActivity.this);
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
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_solar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_center:
                solarSystem.setPosition(SolarSystem.Position.CENTER);
                break;
            case R.id.action_left_top:
                solarSystem.setPosition(SolarSystem.Position.LEFT_TOP);
                break;
            case R.id.action_left_bottom:
                solarSystem.setPosition(SolarSystem.Position.LEFT_BOTTOM);
                break;
            case R.id.action_right_top:
                solarSystem.setPosition(SolarSystem.Position.RIGHT_TOP);
                break;
            case R.id.action_right_bottom:
                solarSystem.setPosition(SolarSystem.Position.RIGHT_BOTTOM);
                break;
            case R.id.action_center_top:
                solarSystem.setPosition(SolarSystem.Position.CENTER_TOP);
                break;
            case R.id.action_center_bottom:
                solarSystem.setPosition(SolarSystem.Position.CENTER_BOTTOM);
                break;
            case R.id.action_center_left:
                solarSystem.setPosition(SolarSystem.Position.CENTER_LEFT);
                break;
            case R.id.action_center_right:
                solarSystem.setPosition(SolarSystem.Position.CENTER_RIGHT);
                break;
        }
        return false;
    }
    @Override
    protected void handler(Message msg) {
        super.handler(msg);
        if (msg.what == Constants.handler_main)
            animationDrawable.stop();
    }
}
