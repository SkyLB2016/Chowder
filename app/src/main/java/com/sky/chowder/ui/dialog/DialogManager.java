package com.sky.chowder.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sky.chowder.R;

/**
 * @author LiBin
 * @ClassName: DialogManager
 * @Description: dialog管理类
 * @date 2015年3月26日 下午5:51:37
 */
public class DialogManager {
    private static Dialog loading;

    public static Dialog createDialog(Context context) {
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);// 得到加载view
        ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        imageView.setBackgroundResource(R.drawable.dialog_rotate);
        AnimationDrawable drawable = (AnimationDrawable) imageView.getBackground();
        drawable.start();
        loading = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog

//        loading.setCancelable(false);// 不可以用“返回键”取消
        loading.setContentView(imageView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        loading.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                loading = null;
            }
        });
        return loading;
    }

    public static void showDialog(Context context) {
        if (loading == null) createDialog(context);
        loading.show();
    }

    public static void disDialog() {
        if (loading == null) return;
        loading.dismiss();
    }
}
