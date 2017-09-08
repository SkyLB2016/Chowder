package com.sky.chowder.ui.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sky.adapter.RecyclerAdapter;
import com.sky.adapter.RecyclerHolder;
import com.sky.chowder.R;
import com.sky.chowder.model.ImageFloder;
import com.sky.utils.ImageUtils;
import com.sky.widget.BasePop;

/**
 * @author sky QQ:1136096189
 * @Description: TODO
 * @date 16/1/11 下午1:14
 */
public class FloderPop extends BasePop<ImageFloder> {
    //看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
//看看pageradapter，FragmentStatePagerAdapter等三个的源码
    private RecyclerView recycle;

    private RecyclerAdapter<ImageFloder> adapter;

    public FloderPop(View view, int width, int height) {
        super(view, width, height);
    }

    @Override
    protected void initView() {
        super.initView();
        recycle = (RecyclerView) view.findViewById(R.id.recycler);
        adapter = new RecyclerAdapter<ImageFloder>(R.layout.pop_uri_item) {
            @Override
            protected void onAchieveHolder(RecyclerHolder holder, int position) {
                holder.setImageBitmap(R.id.image, ImageUtils.getBitmapFromPath(datas.get(position).getFirstImagePath(), 100, 100));
                holder.setText(R.id.tv_name, datas.get(position).getName());
                holder.setText(R.id.tv_count, datas.get(position).getCount() + "个");
            }
        };
        recycle.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
        adapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (itemClickListener != null)
                    itemClickListener.onItemClick(view,position);
            }
        });
    }

    @Override
    protected void initDatas() {
        adapter.setDatas(popDatas);
    }
}
