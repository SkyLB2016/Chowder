package com.sky.demo.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.sky.demo.R;
import com.sky.demo.ui.animation.DepthPageTransformer;

import java.util.ArrayList;
import java.util.List;

public class GuideContoler {
    private Context mContext;
    private ViewPager mViewPager;
    //ViewPager要显示的视图集合
    private List<View> mViews;

    //点的集合
    private LinearLayout pointGroup;
    private View[] points;

    //默认点的宽高
    private static final int POINT_W = 25;
    private static final int POINT_H = 25;
    //点的宽高
    private int pointW;
    private int pointH;

    private int pointSelect;
    private int unPointSelect;

    public GuideContoler(Context context) {
        super();
        this.mContext = context;
    }

    /***
     * 设置数据,适用于前面页面是图片，最后一个页面是一个layout布局
     * @param imgIds 图片的id数组
     * @param view
     */
    public void init(int[] imgIds, View view) {
        mViews = new ArrayList<>();
        for (int i = 0; i < imgIds.length; i++) {
            ImageView iv = new ImageView(mContext);
            iv.setBackgroundResource(imgIds[i]);
            iv.setScaleType(ScaleType.FIT_XY);
            mViews.add(iv);
        }
        mViews.add(view);
        setViewPager();
        setPoints();
    }

    /**
     * 设置ViewPager
     **/
    private void setViewPager() {
        mViewPager = (ViewPager) ((Activity) mContext).findViewById(R.id.viewpager);
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
        mViewPager.setAdapter(new GuideViewPagerAdapter(mViews));
        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < points.length; i++) {
                    if (i == position) {
                        points[i].setBackgroundResource(pointSelect);
                    } else {
                        points[i].setBackgroundResource(unPointSelect);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置指示器
     **/
    private void setPoints() {
        setPointWH();
        pointGroup = (LinearLayout) ((Activity) mContext).findViewById(R.id.layout_group);
        points = new View[mViews.size()];
        LayoutParams params = new LayoutParams(pointW, pointH);
        params.setMargins(0, 0, 15, 0);
        for (int i = 0; i < points.length; i++) {
            points[i] = new View(mContext);
            if (i == 0) {
                points[i].setBackgroundResource(pointSelect);
            } else {
                points[i].setBackgroundResource(unPointSelect);
            }
            points[i].setLayoutParams(params);
            pointGroup.addView(points[i]);
        }
    }

    private void setPointWH() {
        pointW = pointW == 0 ? POINT_W : pointW;
        pointH = pointH == 0 ? POINT_H : pointH;
        pointSelect = R.drawable.shape_selected_oval;
        unPointSelect = R.drawable.shape_unselected_oval;
    }


    public void setPointW(int pointW) {
        this.pointW = pointW;
    }

    public void setPointH(int pointH) {
        this.pointH = pointH;
    }
}
