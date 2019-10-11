package com.sky.oa.presenter

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.sky.design.app.BasePresenter
import com.sky.oa.R
import com.sky.oa.api.presenter.IMainPresenter
import com.sky.oa.api.view.IMainView
import com.sky.oa.model.ActivityModel
import com.sky.sdk.utils.LogUtils
import java.text.Collator
import java.util.*

/**
 * Created by SKY on 2017/5/29.
 */
class MainP(context: Context) : com.sky.design.app.BasePresenter<IMainView>(context) {
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

//            Collections.sort(activityInfos);//使用activityModel中的compareTo进行排序

//            var iterator = activityInfos.iterator()
//            while (iterator.hasNext()) {
//                LogUtils.i("==${iterator.next().className}")
//            }
            return activityInfos
        }
    public val acti: List<ActivityModel>
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
            var iterator = activityInfos.iterator()
            while (iterator.hasNext()) {
                val cla = iterator.next()
                var name = cla.className!!
                val index = name.indexOfFirst { char -> char == '.' }
//                LogUtils.i("==${name}")
                name = name.substring(index + 1, name.length)
//                LogUtils.i("==${name}")
                cla.className = name
            }
            Collections.sort(activityInfos, comparator)

            iterator = activityInfos.iterator()
            while (iterator.hasNext()) {
                val name = iterator.next().className!!
                LogUtils.i("==${name.toCharArray()[0]}")
                LogUtils.i("==${name.toCharArray()[0].toInt()}")
            }
            activityInfos.sort();//使用activityModel中的compareTo进行排序
            iterator = activityInfos.iterator()
            while (iterator.hasNext()) {
                val name = iterator.next().className!!
                LogUtils.i("==${name.toCharArray()[0]}")
                LogUtils.i("==${name.toCharArray()[0].toInt()}")
            }
            return activityInfos
        }
    private val comparator = Comparator<ActivityModel> { first, second ->
        Collator.getInstance().compare(first.className, second.className)
    }
    private val sort =
        Comparator<Double> { first, second -> if (first > second) 1 else if (first < second) -1 else 0 }
}
