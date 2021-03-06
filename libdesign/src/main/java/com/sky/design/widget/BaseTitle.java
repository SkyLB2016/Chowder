package com.sky.design.widget;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.sky.R;
import com.sky.sdk.utils.LogUtils;

/**
 * Created by SKY on 2016/9/21.
 */
public class BaseTitle {
    public AppCompatActivity activity;
    //定义toolbar
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private TextView tvCenter;
    private TextView tvRight;

    public BaseTitle(AppCompatActivity activity) {
        this.activity = activity;
        setToolbar();
    }

    /**
     * 配合XML文件，设置toolbar在每个需要标题的XML中引用
     * 引用manifest里的label
     */
    public void setToolbar() {
        try {
            ActivityInfo info = activity.getPackageManager().getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
            //如果未设置返回程序的名称,即application里的label,
            setToolbar((String) info.loadLabel(activity.getPackageManager()));
        } catch (PackageManager.NameNotFoundException e) {
            LogUtils.d(e.toString());
        }
    }

    /**
     * 配合XML文件，设置toolbar
     * 在每个需要标题的XML中引用
     */
    public void setToolbar(String title) {
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        if (toolbar == null) return;
        toolbar.setTitle("");//默认为居左,所以隐藏
        tvCenter = (TextView) toolbar.findViewById(R.id.tvCenter);
        tvRight = (TextView) toolbar.findViewById(R.id.tvRight);
        tvCenter.setText(title);//居中的标题
        activity.setSupportActionBar(toolbar);
        //toolbar.setBackground(R.);
//        toolbar.setLogo(R.drawable.div_line_v);

        //必须在设置了toolbar之后获取actionbar才有效
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示默认的icon
        }
//        toolbar.setNavigationIcon(R.mipmap.ic_close);//左侧图标
//        toolbar.setNavigationIcon(null);//左侧图标

        //可以用tool自带的menu，需要注册；也可以用activity默认的menu，不需要注册。
        if (activity instanceof Toolbar.OnMenuItemClickListener)//右侧menu的点击事件
            toolbar.setOnMenuItemClickListener((Toolbar.OnMenuItemClickListener) activity);

        //返回图标的点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftOnClick(v);
            }
        });
    }

    public void setVisibility(int vis) {
        toolbar.setVisibility(vis);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public TextView getTvCenter() {
        return tvCenter;
    }

    public TextView getTvRight() {
        return tvRight;
    }

    /**
     * 左侧标题，自带的标题就是左侧的，
     *
     * @param title
     */
    public void setLeftTitle(String title) {
        toolbar.setTitle(title);
        tvCenter.setText("");
    }

    /**
     * 设置居中标题
     *
     * @param title
     */
    public void setCenterTitle(String title) {
        tvCenter.setText(title);
    }

    public void setRightText(String text) {
        tvRight.setText(text);
    }

    public void setRightDrawableImgId(int imgId) {
        tvRight.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, imgId, 0);
    }

    /**
     * 为空时，隐藏左侧图标
     *
     * @param icon
     */
    public void setNavigationIcon(@Nullable Drawable icon) {
        toolbar.setNavigationIcon(null);
    }

    /**
     * 为0时隐藏图标
     *
     * @param resId
     */
    public void setNavigationIcon(@DrawableRes int resId) {
        if (resId == 0) {
            toolbar.setNavigationIcon(null);
        } else {
            toolbar.setNavigationIcon(resId);
        }
    }

    public interface OnClickListener {
        void OnClick(View v);
    }

    public OnClickListener onLeftImgClick = null;

    public void setLeftImgOnClick(OnClickListener onClick) {
        this.onLeftImgClick = onClick;
    }

    /**
     * 左侧按钮的点击事件，默认关闭，如需重写，把继承的super删掉
     */
    public void leftOnClick(View v) {
        if (onLeftImgClick != null) {
            onLeftImgClick.OnClick(v);
        } else
            activity.finish();
    }
}
