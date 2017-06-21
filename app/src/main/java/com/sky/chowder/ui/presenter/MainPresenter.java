package com.sky.chowder.ui.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.sky.chowder.R;
import com.sky.chowder.api.presenter.IMainPresenter;
import com.sky.chowder.api.view.IMainView;
import com.sky.chowder.model.ActivityModel;
import com.sky.chowder.ui.BasePresenter;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by SKY on 2017/5/29.
 */
public class MainPresenter extends BasePresenter<IMainView> implements IMainPresenter {

    public MainPresenter(Context mContext) {
        super(mContext);
    }

    @Override
    public void loadData() {
        mView.setData(getData());
    }

    /**
     * 从manifest中获取activity的信息
     *
     * @return
     */
    protected List<ActivityModel> getData() {
        List<ActivityModel> activityInfos = new ArrayList<>();

        //Intent mainIntent = new Intent(Intent.ACTION_MAIN);//获取action为ACTION_MAIN的activity
        //mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);//筛选category为sample code的act
        //mainIntent.setPackage(getPackageName());//只选出自己应用的act
        Intent mainIntent = new Intent("com.sky.coustom");//自定义的action
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);

        if (null == resolveInfos)
            return activityInfos;

        int len = resolveInfos.size();
        for (int i = 0; i < len; i++) {
            ResolveInfo info = resolveInfos.get(i);
            //获取label,activity中未设置的话返回程序名称
            CharSequence labelSeq = info.loadLabel(pm);
            String label = labelSeq != null ? labelSeq.toString() : info.activityInfo.name;
            //获取说明
            int descriptionRes = info.activityInfo.descriptionRes;
            String describe = descriptionRes == 0 ? "未添加" : context.getResources().getString(descriptionRes);
            //获取icon  Drawable icon = info.loadIcon(pm);
            int iconRes = info.activityInfo.icon;
            int icon = iconRes == 0 ? R.mipmap.ic_launcher : iconRes;
            activityInfos.add(new ActivityModel(label, describe, icon, info.activityInfo.name));
        }
        //排序
        Collections.sort(activityInfos, sDisplayNameComparator);
//        Collections.sort(activityInfos);//使用activityModel中的compareTo进行排序
        return activityInfos;
    }

    /**
     * 为筛选出的act进行排序
     */
    private final static Comparator<ActivityModel> sDisplayNameComparator =
            new Comparator<ActivityModel>() {
                private final Collator collator = Collator.getInstance();

                @Override
                public int compare(ActivityModel lhs, ActivityModel rhs) {
                    return collator.compare(lhs.getClassName(), rhs.getClassName());
                }
            };
}
