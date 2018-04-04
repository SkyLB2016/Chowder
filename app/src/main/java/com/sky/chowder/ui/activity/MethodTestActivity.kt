package com.sky.chowder.ui.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Animatable
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.BatteryManager
import android.os.Build
import android.provider.ContactsContract
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.*
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.gson.reflect.TypeToken
import com.sky.base.BaseNoPActivity
import com.sky.base.BasePActivity
import com.sky.chowder.R
import com.sky.chowder.model.ActivityModel
import com.sky.chowder.other.factory.abstractfactory.HNFactory
import com.sky.chowder.other.factory.abstractfactory.MCFctory
import com.sky.chowder.other.factory.factory.HairFactory
import com.sky.chowder.other.factory.factory.hair.LeftHair
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
        val method = arrayListOf("字符替换与数组化", "hash相同", "字体颜色背景变换"
                , "系统信息", "获取app信息", "电池电量"
                , "数组排序", "json转换", "list迭代器", "list筛选lambda"
                , "Intent测试"
                , "时间选择"
                , "地址选择"
                , "工厂模式"
                , "SVG与Value"
                , "渐变的文字"
                , "音频处理"
                , "字符串转id"
                , "排序算法"
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
            "字符替换与数组化" -> replaceStr() + "\n" + toArray()
            "hash相同" -> equalHashCode()
            "字体颜色背景变换" -> changeText()
            "系统信息" -> getSystemMessage() + "\n" + getSystemProperty()
            "获取app信息" -> getAppInfo()
            "电池电量" -> "电池电量==$battery"
            "数组排序" -> sortList()
            "json转换" -> changeJson()
            "list迭代器" -> iterator()
            "list筛选lambda" -> lambda()
            "字符串转id" -> changeStrToId()
            "排序算法" -> sort()
            "" -> ""
            else -> ""
        }
        image.visibility = View.GONE
        shader.visibility = View.GONE
        when (v?.tag) {
            "Intent测试" -> intentTest()
            "时间选择" -> selectTime()
            "地址选择" -> selectAddress()
            "工厂模式" -> factoryModel()
            "SVG与Value" -> setSvg()
            "渐变的文字" -> shaderText()
            "音频处理" -> makePlayer()
            "" -> ""
        }
    }

    private fun sort(): CharSequence? {
        val arr = intArrayOf(99, 12, 35, 5, 9, 54, 44)
        var text = "${arr[0]}"
        for (a in 1 until arr.size) text = "$text，${arr[a]}"
        return "原始数据：$text；\n" +
                "冒泡排序：${bubbleSort(arr)}；\n" +
                "冒泡排序：${bubbleSort1(arr)}；\n" +
                "选择排序：${selectSort(arr)}；\n" +
                "插入排序：${insertSort(arr)}。"
    }

    private fun insertSort(arr: IntArray): String {
        var temp: Int
        var text = ""
        for (i in 0 until arr.size - 1) {
            for (j in i + 1 downTo 1) {
                if (arr[j] < arr[j - 1]) {
                    temp = arr[j - 1]
                    arr[j - 1] = arr[j]
                    arr[j] = temp
                } else { //不需要交换
                    break
                }
            }
            text = "${arr[0]}"
            for (a in 1 until arr.size) text = "$text，${arr[a]}"
        }
        return text
    }

    private fun selectSort(arr: IntArray): String {
        var index: Int//临时变量
        var temp: Int//临时变量
        var text = ""
        for (i in 0..arr.size - 2) {
            index = i
            for (j in i + 1 until arr.size) {
                if (arr[j] < arr[index]) index = j
            }
            if (index === i) continue
            temp = arr[i]
            arr[i] = arr[index]
            arr[index] = temp
            text = "${arr[0]}"
            for (a in 1 until arr.size) text = "$text，${arr[a]}"
        }
        return text
    }

    private fun bubbleSort1(arr: IntArray): String {
        var temp: Int//临时变量
        var flag: Boolean
        var text = ""
        for (i in arr.size - 1 downTo 1) { //表示趟数，一共arr.length-1次。
            flag = false
            for (j in 0 until i) {
                if (arr[j] < arr[j + 1]) {
                    temp = arr[j]
                    arr[j] = arr[j + 1]
                    arr[j + 1] = temp
                    flag = true
                }
            }
            text = "${arr[0]}"
            for (a in 1 until arr.size) text = "$text，${arr[a]}"
            if (!flag) break
        }
        return text
    }

    private fun bubbleSort(arr: IntArray): String {
        var temp: Int//临时变量
        var flag: Boolean
        var text = ""
        for (i in 0 until arr.size - 1) { //表示趟数，一共arr.length-1次。
            flag = false
            for (j in arr.size - 1 downTo i + 1) {
                if (arr[j] < arr[j - 1]) {
                    temp = arr[j]
                    arr[j] = arr[j - 1]
                    arr[j - 1] = temp
                    flag = true
                }
            }
            text = "${arr[0]}"
            for (a in 1 until arr.size) text = "$text，${arr[a]}"
            if (!flag) break
        }
        return text
    }

    private fun changeStrToId(): CharSequence? {
        val id = R.string::class.java.getField("loushiming").getInt(R.string())
        val id1 = resources.getIdentifier("xue", "string", packageName)
        val text = StringBuilder()
        text.append(getString(id).replace(" ", ""))
        text.append(getString(id1).replace(" ", ""))
        return text
    }

    private fun makePlayer() {
//        val mMediaPlayer = MediaPlayer.create(this, R.raw.sudi)
//        mMediaPlayer.start()
//        mMediaPlayer.setOnCompletionListener { showToast("播放完成") }
        val soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            SoundPool.Builder()
                    .setMaxStreams(100)   //设置允许同时播放的流的最大值
                    .setAudioAttributes(AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build())   //完全可以设置为null
                    .build()
        else SoundPool(100, AudioManager.STREAM_MUSIC, 0)
//        构建对象
        val soundId = soundPool.load(this, R.raw.sudi, 1)//加载资源，得到soundId
        soundPool.setOnLoadCompleteListener { soundPool, sampleId, status ->
            val streamId = soundPool.play(soundId, 1f, 1f, 1, 0, 1f)//播放，得到StreamId
        }
    }

    private fun shaderText() {
        shader.visibility = View.VISIBLE
//        shader.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        shader.text = "天地玄黄，宇宙洪荒，日月盈仄，辰宿列张。寒来暑往，秋收冬藏。闰余成岁，律吕调阳。云腾致雨，露结为霜。金生丽水，玉出昆冈。"
    }

    private fun setSvg() {
        image.visibility = View.VISIBLE
        val value = ValueAnimator.ofFloat(0f, flow.height.toFloat())
//        value.setTarget(image)
        value.duration = 2000
        value.addUpdateListener { animation ->
            val lp = image.layoutParams
            lp.height = animation!!.animatedValue.toString().toFloat().toInt()
            image.layoutParams = lp
        }
        value.start()
        value.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                (image.drawable as Animatable).start()
            }
        })
    }

    /**
     * 工厂模式应用，待优化
     */
    private fun factoryModel() {
        //工厂模式
        val leftHair = LeftHair()
        leftHair.draw()
        val factory = HairFactory()
        val right = factory.getHair("right")
        right?.draw()
        val left = factory.getHairByClass("com.sky.chowder.other.factory.factory.hair.LeftHair")
        left?.draw()
        val hair = factory.getHairByClassKey("in")
        hair?.draw()

        //抽象工厂模式
        val facoty = MCFctory()
        val girl = facoty.girl
        girl.drawWomen()
        val boyfacoty = HNFactory()
        val boy = boyfacoty.boy
        boy.drawMan()
    }

    private fun getAppInfo() = "当前版本:${AppUtils.getVersionCode(this)};\n" +
            "当前版本号:${AppUtils.getVersionName(this)};\n" +
            "当前通道号:${AppUtils.getChannel(this)}"


    private fun selectAddress() {
        val address = AddressFragment()
        address.show(supportFragmentManager, "address")
        address.onClick = object : AddressFragment.OnClickListener {
            override fun onClick(address: String) {
                tvDisplay.text = address
            }
        }
    }

    private fun selectTime() {
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
        span.setSpan(AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.text_medium)), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(ForegroundColorSpan(Color.RED), 8, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(BackgroundColorSpan(Color.rgb(55, 155, 200)), 17, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        span.setSpan(UnderlineSpan(), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)//下划线
        span.setSpan(StrikethroughSpan(), 4, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)//中划线
        span.setSpan(RelativeSizeSpan(1.2f), 8, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)//字号按比例翻倍
        span.setSpan(SuperscriptSpan(), 10, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)//上标
        span.setSpan(SubscriptSpan(), 12, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)//下标
        span.setSpan(StyleSpan(Typeface.BOLD), 13, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)//字体风格
        span.setSpan(URLSpan("http://www.baidu.com"), 17, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)//超链接
        span.append("\n天地玄黄，宇宙洪荒。\n")
        span.append("日月盈仄，辰宿列张。\n")
        span.append(getText(R.string.ibu))
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
        text.append("原数据：$list\n")
        list.reverse()//逆序
        text.append("逆序：$list\n")
        list.shuffle()//随机
        text.append("随机：$list\n")
        list.sort()//排序
        text.append("sort排序（大写小写文字）：$list\n")
        Collections.sort(list, ascending)
        text.append("Comparable升序(文字小大写)：$list\n")
        Collections.sort(list, descending)
        text.append("Comparable降序(大小写文字)：$list")
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
    private val battery: Int
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
