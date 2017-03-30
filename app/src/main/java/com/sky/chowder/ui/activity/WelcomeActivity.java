package com.sky.chowder.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.sky.chowder.R;
import com.sky.utils.JumpAct;
import com.sky.utils.SPUtils;

/**
 * Created by 李彬 on 2017/3/6.
 */

public class WelcomeActivity extends AppCompatActivity {
    public Boolean flag;//是否首次运行，true代表首次
    private int TIME = 1000;//handler延迟时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏,在v7中AppCompatActivity下无用
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        ImageView view = new ImageView(this);
        view.setBackgroundResource(R.mipmap.guide01);
        setContentView(view);
        //SPUtils.put(WelcomeActivity.this, "isfirst", true);
        flag = (Boolean) SPUtils.getInstance().get("isfirst", true);
        //加载动画
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 判断程序与第几次运行，如果是第一次运行引导页面
                        if (flag) {
                            JumpAct.jumpActivity(WelcomeActivity.this, GuideActivity.class);
                            finish();
                        } else {
                            JumpAct.jumpActivity(WelcomeActivity.this, MainActivity.class);
                            finish();
                        }
                    }
                }, TIME);
            }
        });
    }
}
