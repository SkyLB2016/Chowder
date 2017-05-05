package com.sky.chowder.ui;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.sky.chowder.R;

/**
 * Created by 李彬 on 2016/9/21.
 */

public class BaseTitle {
    public AppCompatActivity activity;
    //定义toolbar
    private Toolbar toolbar;
    private TextView centerTitle;

    public BaseTitle(AppCompatActivity activity) {
        this.activity = activity;
    }

    /**
     * 配合XML文件，设置toolbar在每个需要标题的XML中引用
     * <include layout="@layout/activity_title"/>
     * 引用manifest里的label
     */
    public void setToolbar() {
        try {
            ActivityInfo info = activity.getPackageManager().getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
            //如果未设置返回程序的名称,即application里的label,
            setToolbar((String) info.loadLabel(activity.getPackageManager()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 配合XML文件，设置toolbar
     * 在每个需要标题的XML中引用
     * <include layout="@layout/activity_title"/>
     */
    public void setToolbar(String title) {
        toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        if (toolbar == null) return;
        toolbar.setTitle("");//默认为居左,所以隐藏
        centerTitle = (TextView) toolbar.findViewById(R.id.center_title);
        centerTitle.setText(title);//居中的标题
        activity.setSupportActionBar(toolbar);
        //toolbar.setBackground(R.);
//        toolbar.setLogo(R.drawable.div_line_v);
        toolbar.setNavigationIcon(R.mipmap.ic_back);
        if (activity instanceof Toolbar.OnMenuItemClickListener)
            toolbar.setOnMenuItemClickListener((Toolbar.OnMenuItemClickListener) activity);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leftOnClick(v);
            }
        });
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setCenterTitle(String title) {
        toolbar.setTitle("");
        centerTitle.setText(title);
    }

    public void setLeftTitle(String title) {
        toolbar.setTitle(title);
        centerTitle.setText("");
    }

    /**
     * 更换左侧图标
     *
     * @param left 为－1时，隐藏图标
     */
    public void setLeftButton(int left) {
        if (left == -1) {
            toolbar.setNavigationIcon(null);
        } else {
            toolbar.setNavigationIcon(left);
        }
    }


    public interface OnLeftClick {
        void OnLeftClick(View v);
    }

    public OnLeftClick onLeftClick = null;

    public void setOnLeftClick(OnLeftClick onLeftClick) {
        this.onLeftClick = onLeftClick;
    }
    /**
     * 左侧按钮的点击事件，默认关闭，如需重写，把继承的super删掉
     */
    public void leftOnClick(View v) {
        if (onLeftClick != null) {
            onLeftClick.OnLeftClick(v);
        } else
            activity.finish();
    }
}
