package com.glimmer.carrybport.ui.activity;

import android.view.View;

import com.glimmer.carrybport.R;
import com.glimmer.carrybport.base.BasePActivity;
import com.glimmer.carrybport.ui.presenter.LoginPresenter;
import com.sky.Common;
import com.sky.api.IBaseView;
import com.sky.utils.TextUtil;
import com.sky.widget.EditTextDel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sky on 2017/6/2.
 * 登录界面
 */
public class LoginActivity extends BasePActivity<LoginPresenter> implements IBaseView {
    @BindView(R.id.et_phone)
    EditTextDel etPhone;
    @BindView(R.id.et_pwd)
    EditTextDel etPwd;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initialize() {
        etPhone.setText(presenter.getObject(Common.PHONE, ""));
        etPwd.setText(presenter.getObject(Common.PWD, ""));

    }

    @Override
    protected void creatPresenter() {
        presenter = new LoginPresenter(this);
    }

    @OnClick({R.id.tv_login, R.id.tv_register, R.id.tv_forget})
    void onLoginClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                presenter.login(TextUtil.getText(etPhone), TextUtil.getText(etPwd));
                break;
            case R.id.tv_register:
                presenter.register();
                break;
            case R.id.tv_forget:
                presenter.forget();
                break;

        }
    }
}
