package com.sky.chowder.ui.activity

import android.annotation.TargetApi
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Build
import android.provider.ContactsContract
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.gson.reflect.TypeToken
import com.sky.base.BaseNoPActivity
import com.sky.base.BasePActivity
import com.sky.chowder.R
import com.sky.chowder.model.ActivityModel
import com.sky.chowder.ui.fragment.AddressFragment
import com.sky.chowder.ui.fragment.TimeFragment
import com.sky.model.ApiResponse
import com.sky.utils.AppUtils
import com.sky.utils.DateUtil
import com.sky.utils.GsonUtils
import kotlinx.android.synthetic.main.activity_method.*
import java.lang.StringBuilder
import java.text.Collator
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.experimental.buildSequence

/**
 * Created by SKY on 2018/3/6 16:43.
 */
class MethodTestActivity : BaseNoPActivity(), View.OnClickListener {
    override fun getLayoutResId(): Int = R.layout.activity_method
    override fun initialize() {
        super.initialize()
        val method = arrayListOf("字符替换", "hash相同", "字符串数组化", "字体颜色背景变换"
                , "系统信息", "获取app信息", "电池电量"
                , "数组排序", "json转换", "list迭代器", "list筛选lambda"
                , "Intent测试"
                , "时间选择"
                , "地址选择"
        )
        for (i in method) {
            val tvText = LayoutInflater.from(this).inflate(R.layout.tv, flow, false) as TextView
            tvText.text = i
            tvText.tag = i
            flow.addView(tvText)
            tvText.setOnClickListener(this)
        }
        tvDisplay.text = replaceStr()
    }

    override fun onClick(v: View?) {
        tvDisplay.text = when (v?.tag) {
            "字符替换" -> replaceStr()
            "hash相同" -> equalHashCode()
            "字符串数组化" -> toArray()
            "字体颜色背景变换" -> changeText()
            "系统信息" -> getSystemMessage() + "\n" + getSystemProperty()
            "获取app信息" -> getAppInfo()
            "电池电量" -> "电池电量==$battery"
            "数组排序" -> sortList()
            "json转换" -> changeJson()
            "list迭代器" -> iterator()
            "list筛选lambda" -> lambda()
            "" -> ""
            else -> ""
        }
        when (v?.tag) {
            "Intent测试" -> intentTest()
            "时间选择" -> selectTime()
            "地址选择" -> selectAddress()
            "" -> ""
        }
    }

    private fun getAppInfo() = "当前版本:${AppUtils.getVersionCode(this)};\n" +
            "当前版本号:${AppUtils.getVersionName(this)};\n" +
            "当前通道号:${AppUtils.getChannel(this)}"


    fun selectAddress() {
        val address = AddressFragment()
        address.show(supportFragmentManager, "address")
        address.onClick = object : AddressFragment.OnClickListener {
            override fun onClick(address: String) {
                tvDisplay.text = address
            }
        }
    }

    fun selectTime() {
        val time = TimeFragment()
        time.show(supportFragmentManager, "time")
        time.time = DateUtil.dateToTimeStamp(tvDisplay.text.toString().trim())
        time.onClick = object : TimeFragment.OnClickListener {
            override fun onClick(time: String) {
                tvDisplay.text = time
            }
        }
    }

    private fun changeText(): SpannableStringBuilder {
        val text = "天行健，君子以自强不息；地势坤，君子以厚德载物。"
//        val span = SpannableString(text)
        val span = SpannableStringBuilder(text)
        span.setSpan(AbsoluteSizeSpan(resources.getDimension(R.dimen.text_medium).toInt()), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(ForegroundColorSpan(Color.RED), 8, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(BackgroundColorSpan(Color.rgb(55, 155, 200)), 17, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.append("\n天地玄黄，宇宙洪荒。\n")
        span.append("日月盈仄，辰宿列张。\n")
        span.append(getText(R.string.test))
        return span
    }

    private fun lambda(): String {
        val text = StringBuilder()
        val seq = buildSequence {
            // 产生一个 i 的平方
            for (i in 1..5) yield(i * i)
            // 产生一个区间
            yieldAll(26..28)
        }
        text.append(seq.toList().toString() + "\n")
        var fruits = listOf("banana", "avocado", "apple", "kiwi")
        fruits.filter { it.startsWith("a") }
                .sortedBy { it }
                .map { it.toUpperCase() }
                .forEach { text.append(it + "\n") }
        val array = GsonUtils.jsonToArray(getString(R.string.jsonarray), Array<ActivityModel>::class.java)
        array.sortedByDescending { it }
                .forEach { text.append(it.className + "\n") }
        return text.toString()
    }

    private fun iterator(): String {
        val list = GsonUtils.jsonToList(getString(R.string.jsonarray), Array<ActivityModel>::class.java)
        val iter = list.iterator()
        val text = StringBuilder()
        for (i in iter) text.append(i.toString())
//        while (iter.hasNext()) {
//            val obj = iter.next()
//            text.append(obj.toString())
//        }
        return text.toString()
    }

    private fun changeJson(): String {
        val model = GsonUtils.jsonToEntity(getString(R.string.jsonobj), ActivityModel::class.java)
        val entity = GsonUtils.jsonToEntity<ApiResponse<List<ActivityModel>>>(getString(R.string.jsonlist), object : TypeToken<ApiResponse<List<ActivityModel>>>() {}.type)
        val list = GsonUtils.jsonToList(getString(R.string.jsonarray), Array<ActivityModel>::class.java)
        val array = GsonUtils.jsonToArray(getString(R.string.jsonarray), Array<ActivityModel>::class.java)
        val text = StringBuilder()
        text.append(model.toString() + "\n")
        text.append(entity.objList.toString() + "\n")
        text.append(list.toString() + "\n")
        for (i in array)
            text.append(i.toString() + "\n")
        return text.toString()
    }

    private fun sortList(): String {
        val list = ArrayList<SortModel>()
        ('g' downTo 'a').mapTo(list) { SortModel("$it") }
        (22222 downTo 22217).mapTo(list) { SortModel("${it.toChar()}") }
        ('G' downTo 'A').mapTo(list) { SortModel("$it") }
        val text = StringBuilder()
        text.append("原数据：${list.toString()}\n")
        Collections.reverse(list)//逆序
        text.append("逆序：${list.toString()}\n")
        Collections.shuffle(list)//随机
        text.append("随机：${list.toString()}\n")
        Collections.sort(list)//排序
        text.append("sort排序（大写小写文字）：${list.toString()}\n")
        Collections.sort(list, ascending)
        text.append("Comparable升序(文字小大写)：${list.toString()}\n")
        Collections.sort(list, descending)
        text.append("Comparable降序(大小写文字)：${list.toString()}")
        return text.toString()
    }

    class SortModel(var className: String?) : Comparable<SortModel> {
        override fun compareTo(another: SortModel): Int = className!!.compareTo(another.className!!)
        override fun toString(): String = "$className"
    }

    companion object {
        private val collator = Collator.getInstance()
        //升序
        private val ascending = Comparator<SortModel> { first, second -> collator.compare(first.className, second.className) }
        //降序
        private val descending = Comparator<SortModel> { first, second -> collator.compare(second.className, first.className) }
    }

    //获取电量百分比
    val battery: Int
        get() {
            val battery = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val currLevel = battery!!.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            val total = battery.getIntExtra(BatteryManager.EXTRA_SCALE, 0)
            return currLevel * 100 / total
        }

    /**
     * intent测试
     */
    private fun intentTest() {
        //        IntentTest.startIntent(this, Extra<String>(),"com.sky.action")
//        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
//        intent.putExtra(AlarmClock.EXTRA_HOUR, 15)
//        intent.putExtra(AlarmClock.EXTRA_MINUTES, 24)
//        intent.putExtra(AlarmClock.EXTRA_MESSAGE, "测试")
        //        val intent = Intent(AlarmClock.ACTION_SET_TIMER)
        //                .putExtra(AlarmClock.EXTRA_MESSAGE, "测试")
        //                .p`utExtra(AlarmClock.EXTRA_LENGTH, 10)
        //                .putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        //        val intent = Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA)
        //
        //        if (intent.resolveActivity(packageManager) != null) {
        //            startActivity(intent);
        //        }
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == BasePActivity.RESULT_OK) {
            val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val cursor = contentResolver.query(data?.data, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                val name = cursor.getString(nameIndex);
                val number = cursor.getString(numberIndex);
                showToast(number)
                tvDisplay.text = "姓名:$name;\n电话:$number"
            }
            cursor.close()
        }
    }

    private fun equalHashCode(): String = "Aa的hashCode:${"Aa".hashCode()}==BB的hashCode:${"BB".hashCode()};\n" +
            "Bb的hashCode:${"Bb".hashCode()}==CC的hashCode:${"CC".hashCode()};\n" +
            "Cc的hashCode:${"Cc".hashCode()}==DD的hashCode:${"DD".hashCode()};\n"


    private fun toArray(): String {
        val chars = "字符串数组化".toCharArray()
        val text = StringBuilder()
        for (i in 0 until chars.size - 1) text.append("${chars[i]},")
        text.append("${chars.last()}")
        return text.toString()
    }

    private fun replaceStr() = String.format(getString(R.string.format00), 99, 44) +
            String.format(getString(R.string.format00), 99, 44) +
            String.format(getString(R.string.format01), 99, 44) +
            String.format(getString(R.string.format02), "abc", 77) +
            String.format(getString(R.string.format03), 29) +
            String.format(getString(R.string.format04), 9982999, 9998.876)

    //获取系统信息
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getSystemMessage(): String {
        val text = StringBuilder()
        val list = Build.SUPPORTED_ABIS
        for (i in list)
            text.append("CPU指令集==$i\n")
        text.append("主板型号==${Build.BOARD}\n" +
                "系统定制商==${Build.BRAND}\n" +
                "设备参数==${Build.DEVICE}\n" +
                "显示屏参数==${Build.DISPLAY}\n" +
                "唯一编号==${Build.FINGERPRINT}\n" +
                "硬件序列号==${Build.SERIAL}\n" +
                "修订版本列表==${Build.ID}\n" +
                "硬件制造商==${Build.MANUFACTURER}\n" +
                "版本==${Build.MODEL}\n" +
                "硬件名==${Build.HARDWARE}\n" +
                "手机产品名==${Build.PRODUCT}\n" +
                "描述Build的标签==${Build.TAGS}\n" +
                "Builder的类型==${Build.TYPE}\n" +
                "当前开发代号==${Build.VERSION.CODENAME}\n" +
                "源码控制版本号==${Build.VERSION.INCREMENTAL}\n" +
                "系统版本==${Build.VERSION.RELEASE}\n" +
                "系统版本号==${Build.VERSION.SDK_INT}\n" +
                "Host值==${Build.HOST}\n" +
                "User名==${Build.USER}\n" +
                "编译时间==${Build.TIME}\n")
        return text.toString()
    }

    //获取系统信息
    private fun getSystemProperty(): String {
        return "OS版本==${System.getProperty("os.version")}\n" +
                "OS名称==${System.getProperty("os.name")}\n" +
                "OS架构==${System.getProperty("os.arch")}\n" +
                "Home属性==${System.getProperty("user.home")}\n" +
                "Name属性==${System.getProperty("user.name")}\n" +
                "Dir属性==${System.getProperty("user.dir")}\n" +
                "时区==${System.getProperty("user.timezone")}\n" +
                "路径分隔符==${System.getProperty("path.separator")}\n" +
                "行分隔符==${System.getProperty("line.separator")}\n" +
                "文件分隔符==${System.getProperty("file.separator")}\n" +
                "Java vendor URL 属性==${System.getProperty("java.vendor.url")}\n" +
                "Java class 路径==${System.getProperty("java.class.path")}\n" +
                "Java class 版本==${System.getProperty("java.class.version")}\n" +
                "Java Vendor 属性==${System.getProperty("java.vendor")}\n" +
                "Java 版本==${System.getProperty("java.version")}\n" +
                "Java Home 属性==${System.getProperty("java.home")}\n"
    }
}
