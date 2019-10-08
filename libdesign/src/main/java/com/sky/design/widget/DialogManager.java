package com.sky.design.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.sky.R;

/**
 * Created by SKY on 16/5/10 下午3:50.
 * dialog管理类
 */
public class DialogManager {

    public DialogManager(Context context) {
        createDialog(context);
    }

    private Dialog loading;

    public Dialog createDialog(Context context) {
        ImageView imageView = new ImageView(context);
        imageView.setAlpha((float) 0.8);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setBackgroundResource(R.drawable.dialog_rotate);
        AnimationDrawable drawable = (AnimationDrawable) imageView.getBackground();
        drawable.start();
        loading = new AlertDialog.Builder(context, R.style.loading_dialog).setView(imageView).create();// 创建自定义样式dialog
        return loading;
    }

    public void showDialog(Context context) {
        if (loading == null) createDialog(context);
        loading.show();
    }

    public void disDialog() {
        if (loading == null) return;
        loading.dismiss();
    }

    public void setCancelable() {
        if (loading == null) return;
        loading.setCancelable(false);//不可以用“返回键”取消
    }

    public void setCanceledOnTouchOutside() {
        if (loading == null) return;
        loading.setCanceledOnTouchOutside(false);//点击外部不消失
    }
}
