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

import com.sky.api.IBaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by SKY on 2017/6/7.
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IBaseView {
    protected View view;
    protected P presenter;
    protected Context context;
    protected Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutResId(), container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        if (presenter == null) initPresenter();
        checkPresenterIsNull();
        initialize();
        presenter.onCreateView(savedInstanceState);
        return view;
    }

    /**
     * 获取布局的resId
     */
    @CheckResult
    protected abstract int getLayoutResId();

    /**
     * 初始化presenter
     */
    protected abstract void initPresenter();

    /**
     * 初始化
     */
    protected abstract void initialize();

    /**
     * 检查mPresenter是否为空
     */
    protected void checkPresenterIsNull() {
        if (presenter == null) {
            throw new IllegalStateException("please init presenter at initPresenter() method...");
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
//        DialogManager.showLoading(context);
    }

    @Override
    public void disLoading() {
        ((SkyActivity) getActivity()).disLoading();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (presenter != null)
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
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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
        unbinder.unbind();
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
