package com.sky.api;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by SKY on 2017/5/29.
 * mvp中V的接口基类
 */
public interface IBaseView {
    /**
     * 设置标题
     *
     * @param title
     * @param rightText
     */
    void setToolbarTitle(@NonNull String title, @NonNull String rightText);

    void setToolbarTitle(@NonNull String title);

    /**
     * 提示语
     *
     * @param resId
     */
    void showToast(@StringRes int resId);

    /**
     * 提示语
     *
     * @param text
     */
    void showToast(@NonNull String text);

    void showDialog();

    void dismissDialog();

    void finish();


}
