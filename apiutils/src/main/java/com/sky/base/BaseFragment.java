package com.sky.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;
import com.sky.api.IBaseView;

/**
 * Created by SKY on 2017/6/7.
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IBaseView {
    protected P presenter;
    protected Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getActivity();
        if (presenter == null) presenter = creatPresenter();
        checkPresenterIsNull();
        presenter.onCreateView(savedInstanceState);
        return inflater.inflate(getLayoutResId(), null, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
        presenter.onViewCreate(savedInstanceState);
    }

    /**
     * 获取布局的resId
     */
    @CheckResult
    protected abstract int getLayoutResId();

    /**
     * 初始化presenter
     */
    protected abstract P creatPresenter();

    /**
     * 初始化
     */
    protected abstract void initialize();

    /**
     * 检查mPresenter是否为空
     */
    protected void checkPresenterIsNull() {
        if (presenter == null) {
            throw new IllegalStateException("please init presenter at creatPresenter() method...");
        }
    }

    @Override
    public void setToolbarTitle(@NonNull String title, @NonNull String rightText) {
        ((SkyActivity) getActivity()).setToolbarTitle(title, rightText);
    }

    @Override
    public void setToolbarTitle(@NonNull String title) {
        ((SkyActivity) getActivity()).setToolbarTitle(title);
    }

    @Override
    public void setToolbarRightTitle(@NonNull String rightText) {
        ((SkyActivity) getActivity()).setToolbarRightTitle(rightText);
    }

    @Override
    public void showToast(@StringRes int resId) {
        ((SkyActivity) getActivity()).showToast(resId);
    }

    /**
     * 显示信息提示条
     *
     * @param text 提示信息字符串
     */
    @Override
    public void showToast(@NonNull String text) {
        ((SkyActivity) getActivity()).showToast(text);
    }

    @Override
    public void showLoading() {
        ((SkyActivity) getActivity()).showLoading();
    }

    @Override
    public void disLoading() {
        ((SkyActivity) getActivity()).disLoading();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 页面埋点
        StatService.onPageStart(getActivity(), getClass().getName());
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 页面埋点
        StatService.onPageEnd(getActivity(), getClass().getName());
        presenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.onDetach();
    }

    @Override
    public void finish() {
        if (getActivity() != null)
            getActivity().finish();
    }
}
