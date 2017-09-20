package com.sky.chowder.ui.presenter

import android.content.Context
import android.content.Intent
import com.sky.base.BasePresenter
import com.sky.chowder.R
import com.sky.chowder.api.presenter.IMainPresenter
import com.sky.chowder.api.view.IMainView
import com.sky.chowder.model.ActivityModel
import com.sky.utils.LogUtils
import java.text.Collator
import java.util.*

/**
 * Created by SKY on 2017/5/29.
 */
class MainP(context: Context) : BasePresenter<IMainView>(context), IMainPresenter {
    override fun showToast(toast: String) {
        mView.showToast(toast)
    }

    init {
        LogUtils.i("mainpresenter")
    }

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
            val manager = context.packageManager
            val resolveInfos = manager.queryIntentActivities(mainIntent, 0) ?: return activityInfos

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
            }
            //排序
//            Collections.sort(activityInfos, sDisplayNameComparator)
//            Collections.sort(activityInfos);//使用activityModel中的compareTo进行排序
            return activityInfos
        }

    companion object {
        /**
         * 为筛选出的act进行排序
         */
        private val sDisplayNameComparator = object : Comparator<ActivityModel> {
            private val collator = Collator.getInstance()

            override fun compare(lhs: ActivityModel, rhs: ActivityModel): Int {
                return collator.compare(lhs.className, rhs.className)
            }
        }
    }
}
