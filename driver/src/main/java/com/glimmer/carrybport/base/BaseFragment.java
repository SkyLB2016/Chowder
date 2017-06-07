package com.glimmer.carrybport.base;

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
 * Created by ASUS on 2017/6/7.
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IBaseView {
    protected View mContentView;
    protected P mPresenter;
    protected Context mContext;
    protected Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(getLayoutResId(), container, false);
        mContext = getActivity();
        unbinder = ButterKnife.bind(this, mContentView);
        if (mPresenter == null) initPresenter();
        checkPresenterIsNull();
        initialize();
        mPresenter.onCreateView(savedInstanceState);
        return mContentView;
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
        if (mPresenter == null) {
            throw new IllegalStateException("please init mPresenter at initPresenter() method...");
        }
    }

    @Override
    public void setToolbarTitle(@NonNull String title, @NonNull String rightText) {
        ((BasePActivity) getActivity()).setToolbarTitle(title, rightText);

    }

    @Override
    public void setToolbarTitle(@NonNull String title) {
        ((BasePActivity) getActivity()).setToolbarTitle(title);
    }

    @Override
    public void showToast(@StringRes int resId) {
        ((BasePActivity) getActivity()).showToast(resId);
    }

    /**
     * 显示信息提示条
     *
     * @param text 提示信息字符串
     */
    @Override
    public void showToast(@NonNull String text) {
        ((BasePActivity) getActivity()).showToast(text);
    }

    @Override
    public void showDialog() {
        ((BasePActivity) getActivity()).showDialog();
//        DialogManager.showDialog(mContext);
    }

    @Override
    public void dismissDialog() {
        ((BasePActivity) getActivity()).dismissDialog();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mPresenter != null)
            mPresenter.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mPresenter.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.onDetach();
    }

    @Override
    public void finish() {
        if (getActivity() != null)
            getActivity().finish();
    }
}
