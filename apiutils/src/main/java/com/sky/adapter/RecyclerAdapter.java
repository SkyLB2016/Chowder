package com.sky.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by SKY on 16/5/10 下午3:50.
 * RecyclerView的万能适配器
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> {
    //viewItem的点击事件监听
    protected OnItemClickListener onItemClickListener;
    protected OnItemLongClickListener itemLongClickListener;
    protected Context context;
    protected List<T> datas;
    private int layoutId;//主体布局
    protected static final int LASTITEM = -1;

    public RecyclerAdapter(int layoutId) {
        this.layoutId = layoutId;
    }

    //子view的按钮监听
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //长按监听
    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }

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
        if (datas == null) return;
        this.datas = null;
        notifyDataSetChanged();
    }

    //添加item,默认从最后添加
    public void addItem(T obj) {
        addItem(obj, LASTITEM);
    }

    /**
     * @param obj      添加item
     * @param position -1时最后一个添加
     */
    public void addItem(T obj, int position) {
        position = position == LASTITEM ? getItemCount() : position;
        datas.add(position, obj);
        notifyItemInserted(position);
    }

    /**
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
        return position;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new RecyclerHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, int position) {
        onAchieveHolder(holder, position);
        if (itemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return itemLongClickListener.onItemLongClick(
                            holder.itemView, holder.getLayoutPosition());
                }
            });
        }
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(
                            holder.itemView, holder.getLayoutPosition());
                }
            });
        }
    }

    /**
     * 加载数据内容于view上
     *
     * @param holder   holder
     * @param position 位置
     */
    protected abstract void onAchieveHolder(RecyclerHolder holder, int position);

    //viewItem的点击事件监听接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

}