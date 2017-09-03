package com.sky.chowder.ui.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.sky.Common;
import com.sky.ErrorMes;
import com.sky.api.IBaseView;
import com.sky.api.OnRequestCallback;
import com.sky.base.BasePresenter;
import com.sky.chowder.api.presenter.ILoginP;
import com.sky.chowder.model.LoginEntity;
import com.sky.chowder.model.params.LoginParams;
import com.sky.chowder.ui.activity.MainActivity;
import com.sky.chowder.utils.http.HttpUtils;
import com.sky.http.UseCase;
import com.sky.model.ObjectEntity;
import com.sky.utils.JumpAct;
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
        if (!RegexUtils.isPhone(phone)){
            mView.showToast("请输入正确的电话号码");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            mView.showToast("请输入正确的密码");
            return;
        }
        mView.showLoading();
        new UseCase<ObjectEntity<LoginEntity>>() {
            @Override
            protected Observable<ObjectEntity<LoginEntity>> buildObservable() {
                return HttpUtils.getInstance().login(new LoginParams(phone, pwd));
            }
        }.subscribe(new OnRequestCallback<ObjectEntity<LoginEntity>>() {
            @Override
            public void onFail(ErrorMes error) {
                mView.disLoading();
                mView.showToast(error.getMessage());
            }

            @Override
            public void onSuccess(ObjectEntity<LoginEntity> data) {
                mView.showToast("登录成功");
                mView.disLoading();
                setObject(Common.PHONE, phone);
                setObject(Common.PWD, pwd);
                setObject(Common.TOKEN, data.getResult().getToken());
                JumpAct.jumpActivity(context, MainActivity.class);
                mView.finish();
            }
        });

    }


    @Override
    public void register() {
    }

    @Override
    public void forget() {
    }
}
