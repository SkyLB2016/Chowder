package com.sky.api;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by SKY on 2017/5/29.
 * mvp中V的接口基类
 */
public interface IBaseView {
    /**
     * @param title     中间标题
     * @param rightText 右侧标题
     */
    void setToolbarTitle(@NonNull String title, @NonNull String rightText);

    /**
     * @param title 中间标题
     */
    void setToolbarTitle(@NonNull String title);

    /**
     * @param rightText 右侧标题
     */
    void setToolbarRightTitle(@NonNull String rightText);

    /**
     * @param resId 提示语id
     */
    void showToast(@StringRes int resId);

    /**
     * @param text 提示语
     */
    void showToast(@NonNull String text);

    /**
     * 加载弹窗
     */
    void showLoading();

    /**
     * 取消弹窗
     */
    void disLoading();

    /**
     * 关闭页面
     */
    void finish();
}