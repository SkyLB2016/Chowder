package com.sky.oa.presenter

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.sky.oa.R
import com.sky.oa.api.view.IMainView
import com.sky.oa.model.ActivityModel
import com.sky.oa.model.PoetryEntity
import com.sky.sdk.utils.FileUtils
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
//            mainIntent.action="com.sky.coustom"
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
//            Collections.sort(activityInfos);//使用activityModel中的compareTo进行排序，java中使用的
//            activityInfos.sort();//使用activityModel中的compareTo进行排序，kotlin中使用的

            //去掉前边的数字。
//            var iterator = activityInfos.iterator()
//            while (iterator.hasNext()) {
//                val activityInfo = iterator.next()
//                var name = activityInfo.activityName!!
//                val index = name.indexOfFirst { char -> char == '.' }
//                name = name.substring(index + 1, name.length)
//                activityInfo.activityName = name
//            }
            return activityInfos
        }
    private val comparator = Comparator<ActivityModel> { first, second ->
        Collator.getInstance().compare(first.activityName, second.activityName)
    }
    private val sort =
        Comparator<Double> { first, second -> if (first > second) 1 else if (first < second) -1 else 0 }

    /**
     * 计算文本数据的数量
     */
    fun calculationTextLength() {
        val poetries = ArrayList<PoetryEntity>()
        var dir = "Documents"//assets初始路径
        var array: Array<String>? = null//assets取出的目录名称
        val link = LinkedList<String>()
        link.add(dir)
//            val stack = Stack<String>();
//            stack.add(dir)
        var item = PoetryEntity()
        //取出Documents下的所有文本文件
        while (link.isNotEmpty()) {
            dir = link.removeFirst()
            array = context.assets.list(dir)
            for (i in array!!) {
                if (i.endsWith(".txt")) {
                    item = item.clone() as PoetryEntity
//                    item = PoetryEntity()
                    item.name = i.substring(0, i.length - 4)
                    item.path = "$dir/$i"
                    poetries.add(item)
                } else {
                    link.add("$dir/$i")
                }
            }
        }
        val collator = Collator.getInstance(Locale.CHINA)
        poetries.sortWith(Comparator { o1, o2 -> collator.compare(o1.path, o2.path) })

        var text = ""
        for (poetry in poetries) {
//            text = FileUtils.readAssestToByte(context, poetry.path)//字节流也可以但是不建议。
            text = FileUtils.readAssestToChar(context, poetry.path)
                .replace("　".toRegex(), "")
                .replace("\n".toRegex(), "")
            LogUtils.i("${poetry.name}==${text.length}")
        }
        //        String poetry = FileUtils.readAssestToChar(this, "Documents/文学/道家/道德经.txt")
    }

}
