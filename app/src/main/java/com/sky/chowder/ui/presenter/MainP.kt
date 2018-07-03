package com.sky.chowder.ui.presenter

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.sky.chowder.R
import com.sky.chowder.api.presenter.IMainPresenter
import com.sky.chowder.api.view.IMainView
import com.sky.chowder.model.ActivityModel
import common.base.BasePresenter
import java.text.Collator
import java.util.*

/**
 * Created by SKY on 2017/5/29.
 */
class MainP(context: Context) : BasePresenter<IMainView>(context), IMainPresenter {
    override fun loadData() {
        mView.setData(activities)
    }

    /**
     * @return 从manifest中获取activity的信息
     */
    private val activities: List<ActivityModel>
        get() {
            val activityInfos = ArrayList<ActivityModel>()
            //Intent mainIntent = new Intent(Intent.ACTION_MAIN);//获取action为ACTION_MAIN的activity
            //mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);//筛选category为sample code的act
            //mainIntent.setPackage(getPackageName());//只选出自己应用的act
            val mainIntent = Intent("com.sky.coustom") //自定义的action
//            mainIntent.action="com.sky.coustomd"
//            mainIntent.addCategory(Intent.CATEGORY_CAR_MODE)
//            mainIntent.addCategory(Intent.CATEGORY_APP_MUSIC)
//            mainIntent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
//            mainIntent.flags=Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            val manager = context.packageManager
            val resolveInfos = manager?.queryIntentActivities(mainIntent, PackageManager.MATCH_ALL)
                    ?: return activityInfos
//            val resolveInfos1 = manager?.resolveActivity(mainIntent, 0) ?: return activityInfos

            for (i in resolveInfos.indices) {
                val info = resolveInfos[i]
                //获取label,activity中未设置的话返回程序名称
                val labelSeq = info.loadLabel(manager)
                val label = labelSeq?.toString() ?: info.activityInfo.name
                //获取说明
                val descriptionRes = info.activityInfo.descriptionRes
                val describe = if (descriptionRes == 0) "未添加" else getString(descriptionRes)
                //获取icon  Drawable icon = info.loadIcon(pm);
                val iconRes = info.activityInfo.icon
                val icon = if (iconRes == 0) R.mipmap.ic_launcher else iconRes
                activityInfos.add(ActivityModel(label, describe, icon, info.activityInfo.name))
//                LogUtils.i("${info.activityInfo.name}")
            }
            //排序
            Collections.sort(activityInfos, comparator)
//            Collections.comparator(activityInfos);//使用activityModel中的compareTo进行排序

//            val iterator = activityInfos.iterator()
//            while (iterator.hasNext()) {
//                LogUtils.i("==${iterator.next().className}")
//            }
            return activityInfos
        }
    private val comparator = Comparator<ActivityModel> { first, second -> Collator.getInstance().compare(first.className, second.className) }
    private val sort = Comparator<Double> { first, second -> if (first > second) 1 else if (first < second) -1 else 0 }
}
