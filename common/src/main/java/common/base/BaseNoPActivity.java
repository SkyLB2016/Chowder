package common.base;

import android.os.Bundle;
import android.text.TextUtils;

import com.sky.Common;
import com.sky.R;
import com.sky.base.SkyActivity;
import com.sky.rxbus.DefaultBus;
import com.sky.utils.NetworkUtils;
import com.sky.utils.SPUtils;

import org.jetbrains.annotations.Nullable;

import common.api.IBasePresenter;
import io.reactivex.functions.Consumer;

/**
 * activity 的基类
 * Created by SKY on 2017/5/27.
 */
public abstract class BaseNoPActivity extends SkyActivity implements IBasePresenter {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Common.getRxBus().add(this,
                Common.getRxBus().register(DefaultBus.class)
                        .subscribe(new Consumer<DefaultBus>() {
                            @Override
                            public void accept(DefaultBus o) throws Exception {
                                onReceiveEvent(o);
                            }
                        }));
        if (!hasInternetConnected()) showToast(getString(R.string.toast_isinternet));
        initialize(savedInstanceState);
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Common.getRxBus().unregister(this);
    }

    protected abstract void initialize(@Nullable Bundle savedInstanceState);
    public abstract void loadData();

    @Override
    public Bundle getExtras() {
        return  getIntent().getExtras();
    }

    @Override
    public void sendEvent(int code) {
        Common.getRxBus().send(code);
    }

    @Override
    public <T> void sendEvent(int code, T event) {
        Common.getRxBus().send(code, event);
    }

    @Override
    public void onReceiveEvent(DefaultBus event) {
    }

    @Override
    public <T> T getObject(String text, T value) {
        return (T) SPUtils.getInstance().get(text, value);
    }

    @Override
    public <T> void setObject(String text, T value) {
        SPUtils.getInstance().put(text, value);
    }

    @Override
    public boolean getUsertOnline() {
        return !TextUtils.isEmpty(getToken());
    }


    @Override
    public String getToken() {
        return getObject(Common.TOKEN, "");
    }

    @Override
    public boolean hasInternetConnected() {
        return NetworkUtils.isConnected(this);
    }

    @Override
    public String getStringArray(int array, int position) {
        return getResources().getStringArray(array)[position];
    }
}