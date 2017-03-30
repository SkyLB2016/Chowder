package com.sky.chowder.ui.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.sky.chowder.R;
import com.sky.chowder.common.Constants;
import com.sky.chowder.ui.BaseActivity;
import com.sky.chowder.ui.widget.SolarSystem;
import com.sky.utils.ScreenUtils;

import butterknife.BindView;

/**
 * Created by sky on 15/12/9 下午8:54.
 * 卫星菜单栏
 */
public class SolarSystemActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @BindView(R.id.solar)
    SolarSystem solarSystem;
    private AnimationDrawable layoutDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solar);
        setTitle();
        initView();
    }

    @Override
    public void initView() {
        super.initView();
        RelativeLayout relative = getView(R.id.relative);
        int width = ScreenUtils.getWH(this)[0];
        ViewGroup.LayoutParams lp = relative.getLayoutParams();
        lp.width = lp.height = width / 3;
        relative.setLayoutParams(lp);
        relative.setBackgroundResource(R.drawable.solar_rect_list);
        layoutDraw = (AnimationDrawable) relative.getBackground();

        int childCount = solarSystem.getChildCount();
        FrameLayout.LayoutParams childParams = new FrameLayout.LayoutParams(width / 5, width / 5);
        for (int i = 0; i < childCount - 1; i++) {
            solarSystem.getChildAt(i).setLayoutParams(childParams);
        }
        //solarSystem.setPosition(SolarSystem.CENTER_BOTTOM);
        solarSystem.setRadius(width / 3);
        solarSystem.setRotaMenu(true);//按钮是否旋转
        solarSystem.setIsRotate(true);//混合还是单次执行
        solarSystem.setRecoverChildView(false);
        solarSystem.setOnMenuState(new SolarSystem.MenuState() {
            @Override
            public void openMenu() {
                layoutDraw.start();
                handler.sendEmptyMessageDelayed(Constants.handler_0x002, 600);
            }

            @Override
            public void closeMenu() {
                layoutDraw.start();
                handler.sendEmptyMessageDelayed(Constants.handler_0x002, 600);
            }
        });
        solarSystem.setOnMenuItemClickListener(new SolarSystem.onMenuItemClickListener() {
            @Override
            public void onMenuItemClick(View view, int position) {
                String tag = (String) view.getTag();
                //可以把所需要跳转的activity的全称写在tag里
//                JumpAct.jumpActivity(SolarSystemActivity.this, tag);
                switch (tag) {
                    case "flow":
                        //JumpAct.jumpActivity(SolarSystemActivity.this, CarouselActivity.class);
                        break;
                    case "list":
//                    JumpAct.jumpActivity(SolarSystemActivity.this, RefreshListActivity.class);
                        break;
                    case "viewpager":
//                    JumpAct.jumpActivity(SolarSystemActivity.this, TabLayoutActivity.class);
                        break;
                    case "recyclerview":
//                    JumpAct.jumpActivity(SolarSystemActivity.this, ImageUriActivity.class);
                        break;
                    case "bitmap":
//                    JumpAct.jumpActivity(SolarSystemActivity.this, BottomTabBarActivity.class);
                        break;
                    case "slidingmenu":
//                    JumpAct.jumpActivity(SolarSystemActivity.this, SlidingMenuActivity.class);
                        break;
                }
            }
        });
        solarSystem.toggleMenu(300);
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
                solarSystem.setPosition(SolarSystem.CENTER);
                break;
            case R.id.action_left_top:
                solarSystem.setPosition(SolarSystem.LEFT_TOP);
                break;
            case R.id.action_left_bottom:
                solarSystem.setPosition(SolarSystem.LEFT_BOTTOM);
                break;
            case R.id.action_right_top:
                solarSystem.setPosition(SolarSystem.RIGHT_TOP);
                break;
            case R.id.action_right_bottom:
                solarSystem.setPosition(SolarSystem.RIGHT_BOTTOM);
                break;
            case R.id.action_center_top:
                solarSystem.setPosition(SolarSystem.CENTER_TOP);
                break;
            case R.id.action_center_bottom:
                solarSystem.setPosition(SolarSystem.CENTER_BOTTOM);
                break;
            case R.id.action_center_left:
                solarSystem.setPosition(SolarSystem.CENTER_LEFT);
                break;
            case R.id.action_center_right:
                solarSystem.setPosition(SolarSystem.CENTER_RIGHT);
                break;
        }
        return false;
    }

    @Override
    protected void handler(Message msg) {
        super.handler(msg);
        if (msg.what == Constants.handler_0x002)
            layoutDraw.stop();
    }
}