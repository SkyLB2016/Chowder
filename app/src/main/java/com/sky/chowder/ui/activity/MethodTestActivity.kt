package com.sky.chowder.ui.activity

import android.annotation.TargetApi
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.widget.TextView
import com.sky.base.BaseNoPActivity
import com.sky.base.BasePActivity
import com.sky.chowder.R
import com.sky.utils.AppUtils
import kotlinx.android.synthetic.main.activity_method.*
import java.lang.StringBuilder

/**
 * Created by SKY on 2018/3/6 16:43.
 */
class MethodTestActivity : BaseNoPActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_method
    override fun initialize() {
        super.initialize()
        val method = arrayListOf("字符替换"
                , "hash相同"
                , "字符串数组化"
                , "系统信息"
                , "获取app信息"
                , "Intent测试"
                , "电池电量"
        )
        for (i in method) {
            val tvText = LayoutInflater.from(this).inflate(R.layout.tv, flow, false) as TextView
            tvText.text = i
            tvText.tag = i
            flow.addView(tvText)
            tvText.setOnClickListener { v ->
                when (v.tag) {
                    "字符替换" -> tvDisplay.text = replaceStr()
                    "hash相同" -> tvDisplay.text = equalHashCode()
                    "字符串数组化" -> tvDisplay.text = toArray()
                    "系统信息" -> tvDisplay.text = getSystemMessage() + "\n" + getSystemProperty()
                    "获取app信息" -> tvDisplay.text = "当前版本:${AppUtils.getVersionCode(this)};\n" +
                            "当前版本号:${AppUtils.getVersionName(this)};\n" +
                            "当前通道号:${AppUtils.getChannel(this)}"
                    "Intent测试" -> intentTest()
                    "电池电量" -> tvDisplay.text = "电池电量==$battery"
                    "" -> ""
                }
            }
        }
        replaceStr()
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
    private fun intentTest(): Unit {
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
                val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                val number = cursor.getString(numberIndex);
                showToast(number)
                tvDisplay.text = number
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
    fun getSystemMessage(): String {
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
    fun getSystemProperty(): String {
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
