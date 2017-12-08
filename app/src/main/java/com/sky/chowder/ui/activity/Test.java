package com.sky.chowder.ui.activity;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sky.chowder.MyApplication;
import com.sky.chowder.R;
import com.sky.chowder.model.ActivityModel;
import com.sky.chowder.model.AreaEntity;
import com.sky.model.ApiResponse;
import com.sky.utils.FileUtils;
import com.sky.utils.GsonUtils;
import com.sky.utils.LogUtils;

import java.lang.reflect.Type;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by SKY on 2017/9/28 9:44.
 */
public class Test {
    private AreaEntity[] area;

    public void ddqd(Context context) {
        area= new AreaEntity[4];
    }
    public void ddd(Context context) {
        GsonUtils.jsonToList("",ActivityModel[].class);
    }
    public void dd(Context context) {
        toObj(context.getString(R.string.jsonlist), new TypeToken<ApiResponse<List<ActivityModel>>>() {
        }.getType());
        ApiResponse<List<ActivityModel>> model = GsonUtils.jsonToEntity(context.getString(R.string.jsonlist),
                 new TypeToken<ApiResponse<List<ActivityModel>>>() { }.getType());
        LogUtils.i(model.getObjList().get(3).getClassName());
    }

    private void toObj(String json, Type type) {
        ApiResponse<List<ActivityModel>> model = new Gson().fromJson(json, type);
        LogUtils.i(model.getObjList().get(4).getClassName());
        Observable.just(FileUtils.readAssestToStr(MyApplication.getInstance(), "address.json"))
                .map(new Function<String, List<AreaEntity>>() {
                    @Override
                    public List<AreaEntity> apply(String s) throws Exception {
                        return GsonUtils.jsonToList(s, AreaEntity[].class);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AreaEntity>>() {
                    @Override
                    public void accept(List<AreaEntity> areaEntities) throws Exception {

                    }
                });
    }
//    MemTotal:        2758932 kB
//    MemFree:           74616 kB
//    MemAvailable:     649880 kB
//    Buffers:           30004 kB
//    Cached:           733408 kB
//    SwapCached:         8924 kB
//    Active:           856568 kB
//    Inactive:         866112 kB
//    Active(anon):     553096 kB
//    Inactive(anon):   562812 kB
//    Active(file):     303472 kB
//    Inactive(file):   303300 kB
//    Unevictable:      144132 kB
//    Mlocked:          144132 kB
//    SwapTotal:       1048572 kB
//    SwapFree:         677776 kB
//    Dirty:                 4 kB
//    Writeback:             0 kB
//    AnonPages:       1098260 kB
//    Mapped:           430520 kB
//    Shmem:             12764 kB
//    Slab:             210228 kB
//    SReclaimable:      63388 kB
//    SUnreclaim:       146840 kB
//    KernelStack:       46896 kB
//    PageTables:        48272 kB
//    NFS_Unstable:          0 kB
//    Bounce:                0 kB
//    WritebackTmp:          0 kB
//    CommitLimit:     2428036 kB
//    Committed_AS:   89864440 kB
//    VmallocTotal:   258998208 kB
//    VmallocUsed:      246476 kB
//    VmallocChunk:   258613732 kB
}