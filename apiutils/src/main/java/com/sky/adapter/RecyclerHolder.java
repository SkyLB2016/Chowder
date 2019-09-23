package com.sky.adapter;

import android.graphics.Bitmap;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by SKY on 16/5/10 下午3:50.
 */
public class RecyclerHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;

    public RecyclerHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    public <T extends View> T getView(int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            mViews.put(id, view);
        }
        return (T) view;
    }

    public void setText(int id, CharSequence text) {
        ((TextView) getView(id)).setText(text);
    }

    public void setImage(int id, int imgId) {
        getView(id).setBackgroundResource(imgId);
    }

    public void setImageBitmap(int id, Bitmap bm) {
        ((ImageView) getView(id)).setImageBitmap(bm);
    }
}
