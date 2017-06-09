package com.glimmer.carrybport.ui.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.glimmer.carrybport.api.presenter.ILoginP;
import com.glimmer.carrybport.base.BasePresenter;
import com.glimmer.carrybport.model.LoginEntity;
import com.glimmer.carrybport.model.requestparams.LoginParams;
import com.glimmer.carrybport.ui.activity.ForgetPwdActivity;
import com.glimmer.carrybport.ui.activity.MainActivity;
import com.glimmer.carrybport.ui.activity.RegisterActivity;
import com.glimmer.carrybport.utils.rox.HttpUtils;
import com.glimmer.carrybport.utils.rox.UseCase;
import com.sky.Common;
import com.sky.ErrorMes;
import com.sky.api.IBaseView;
import com.sky.api.OnRequestCallback;
import com.sky.model.ObjectEntity;
import com.sky.rxbus.DefaultBus;
import com.sky.utils.JumpAct;
import com.sky.utils.LogUtils;
import com.sky.utils.RegexUtils;

import io.reactivex.Observable;

/**
 * Created by sky on 2017/6/7.
 */
public class LoginPresenter extends BasePresenter<IBaseView> implements ILoginP {
    public LoginPresenter(Context mContext) {
        super(mContext);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void login(final String phone, final String pwd) {
        if (TextUtils.isEmpty(phone) || !RegexUtils.isPhone(phone)) {
            mView.showToast("请输入正确的电话号码");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            mView.showToast("请输入正确的密码");
            return;
        }
        mView.showDialog();
        new UseCase<ObjectEntity<LoginEntity>>() {
            @Override
            protected Observable<ObjectEntity<LoginEntity>> buildObservable() {
                return HttpUtils.getInstance().login(new LoginParams(phone, pwd));
            }
        }.subscribe(new OnRequestCallback<ObjectEntity<LoginEntity>>() {
            @Override
            public void onFail(ErrorMes error) {
                mView.dismissDialog();
                mView.showToast(error.getMessage());
            }

            @Override
            public void onSuccess(ObjectEntity<LoginEntity> data) {
                mView.showToast("登录成功");
                mView.dismissDialog();
                setObject(Common.PHONE, phone);
                setObject(Common.PWD, pwd);
                setObject(Common.TOKEN, data.getResult().getToken());
                sendEvent(Common.LOGIN, "aejflk");
                sendEvent(Common.LOGIN);
                JumpAct.jumpActivity(mContext, MainActivity.class);
                mView.finish();
            }
        });

    }

    @Override
    public void receiveEvent(DefaultBus event) {
//        JumpAct.jumpActivity(mContext, LoginActivity.class);
        super.receiveEvent(event);
        LogUtils.i("LoginPresenter"+event.getObject());
    }

    @Override
    public void register() {
        JumpAct.jumpActivity(mContext, RegisterActivity.class);
    }

    @Override
    public void forget() {
        JumpAct.jumpActivity(mContext, ForgetPwdActivity.class);
    }
}
