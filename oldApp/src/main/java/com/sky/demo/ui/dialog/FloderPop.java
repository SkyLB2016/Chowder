package com.sky.demo.ui.dialog;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sky.adapter.RecyclerAdapter;
import com.sky.adapter.RecyclerHolder;
import com.sky.demo.R;
import com.sky.demo.model.ImageFloder;
import com.sky.demo.utils.ImageUtils;

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

    private RecyclerAdapter<ImageFloder, RecyclerHolder> adapter;

    public FloderPop(View view, int width, int height) {
        super(view, width, height);
    }

    @Override
    protected void initView() {
        super.initView();
        recycle = (RecyclerView) view.findViewById(R.id.recycle);
        adapter = new RecyclerAdapter<ImageFloder, RecyclerHolder>(R.layout.pop_uri_item) {
            @Override
            protected RecyclerHolder onCreateBodyHolder(View view) {
                return new RecyclerHolder(view);
            }

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
                    itemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    protected void initDatas() {
        adapter.setDatas(popDatas);
    }
}
