package com.sky.chowder.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by sky on 16/5/10 下午3:50.
 * RecyclerView的万能适配器
 */
public abstract class RecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    //viewItem的点击事件监听
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener itemLongClickListener;
    protected Context context;
    protected List<T> datas;
    //主体与底部布局判断
    protected static final int TYPE_ITEM = 0;
    protected static final int TYPE_FOOTER = 1;

    private int layoutId;//主体布局
    private int layoutFootViewId;//底部view布局，不推荐使用，建议用snackbar替代

    protected static final int LASTITEM = -1;

    public RecyclerAdapter(int layoutId) {
        this.layoutId = layoutId;
    }

    public RecyclerAdapter(int layoutId, int layoutFootViewId) {
        this.layoutId = layoutId;
        this.layoutFootViewId = layoutFootViewId;
    }

    /**
     * 子view的按钮监听
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 长按监听
     *
     * @param itemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

//    public int getLayoutId() {
//        return layoutId;
//    }
//
//    public void setLayoutId(int layoutId) {
//        this.layoutId = layoutId;
//    }
//
//    public int getLayoutFootViewId() {
//        return layoutFootViewId;
//    }
//
//    public void setLayoutFootViewId(int layoutFootViewId) {
//        this.layoutFootViewId = layoutFootViewId;
//    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void addDatas(List<T> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public void clearDatas() {
        this.datas.clear();
        notifyDataSetChanged();
    }

    /**
     * 添加item,默认从最后添加
     *
     * @param obj
     */
    public void addItem(T obj) {
        addItem(obj, LASTITEM);
    }

    /**
     * 添加item
     *
     * @param obj
     * @param position -1时最后一个添加
     */
    public void addItem(T obj, int position) {
        position = position == LASTITEM ? getItemCount() : position;
        datas.add(position, obj);
        notifyItemInserted(position);
    }

    /**
     * 删除item
     *
     * @param position,为-1时，删除最后一个
     */
    public void deleteItem(int position) {
        if (position == LASTITEM && getItemCount() > 0)
            position = getItemCount() - 1;
        if (position > LASTITEM && position < getItemCount()) {
            datas.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        //是否是最后一个，是的话加载底部view，不推荐使用，建议用snackbar替代
        if (layoutFootViewId != 0 && viewType == TYPE_FOOTER) {
            return onCreateFootHolder(LayoutInflater.from(context).
                    inflate(layoutFootViewId, parent, false));
        } else
            //加载主体view
            return onCreateBodyHolder(LayoutInflater.from(context).
                    inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        onAchieveHolder(holder, position);//
        if (itemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return itemLongClickListener.onItemLongClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
    }

    /**
     * 主体view
     *
     * @param view
     * @return
     */
    protected abstract VH onCreateBodyHolder(View view);

    /**
     * 底部view
     *
     * @param footView
     * @return
     */
    protected VH onCreateFootHolder(View footView) {
        return null;
    }

    /**
     * 加载数据内容于view上
     *
     * @param holder
     * @param position
     */
    protected abstract void onAchieveHolder(VH holder, int position);

    //viewItem的点击事件监听接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

}