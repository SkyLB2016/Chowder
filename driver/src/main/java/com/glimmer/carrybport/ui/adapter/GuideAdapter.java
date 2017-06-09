package com.glimmer.carrybport.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glimmer.carrybport.R;
import com.glimmer.carrybport.ui.activity.LoginActivity;
import com.sky.Common;
import com.sky.utils.JumpAct;
import com.sky.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页
 * Created by keweiquan on 17/01/11.
 */
@SuppressWarnings("unused")
public class GuideAdapter extends PagerAdapter {
    private Context mContext;
    private List<Integer> lists;

    public GuideAdapter(Context context) {
        this.mContext = context;
        this.lists = new ArrayList<>(4);
        lists.add(R.mipmap.guide_1);
        lists.add(R.mipmap.guide_2);
        lists.add(R.mipmap.guide_3);
        lists.add(R.mipmap.guide_4);
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_guide, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.img_guide);
        // 图片路径
        img.setImageResource( lists.get(position));
        // 立即体验
        TextView tv = (TextView) view.findViewById(R.id.tvExperienceRightNow);
        if (position == lists.size() - 1) {
            tv.setVisibility(View.VISIBLE);
        } else {
            tv.setVisibility(View.GONE);
        }
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == lists.size() - 1) {
                    SPUtils.getInstance().put(Common.ISFIRST, false);
                    JumpAct.jumpActivity(mContext, LoginActivity.class);
                    ((AppCompatActivity) mContext).finish();
                }
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

}
