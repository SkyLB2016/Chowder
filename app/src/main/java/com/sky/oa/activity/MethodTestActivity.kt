package com.sky.oa.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.AlertDialog
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
import android.os.Bundle
import android.provider.ContactsContract
import android.speech.tts.TextToSpeech
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sky.design.app.BaseActivity
import com.sky.design.app.BasePActivity
import com.sky.oa.R
import com.sky.oa.fragment.AddressFragment
import com.sky.oa.fragment.TimeFragment
import com.sky.oa.gson.GsonUtils
import com.sky.oa.model.ActivityModel
import com.sky.oa.other.factory.abstractfactory.HNFactory
import com.sky.oa.other.factory.abstractfactory.MCFctory
import com.sky.oa.other.factory.factory.HairFactory
import com.sky.oa.other.factory.factory.hair.LeftHair
import com.sky.oa.utils.EmulatorDetector
import com.sky.oa.utils.http.HttpUrl
import com.sky.sdk.net.http.ApiResponse
import com.sky.sdk.utils.AppUtils
import com.sky.sdk.utils.DateUtil
import com.sky.sdk.utils.LogUtils
import com.sky.sdk.utils.MD5Utils
import kotlinx.android.synthetic.main.activity_method.*
import java.io.File
import java.net.URL
import java.text.Collator
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by SKY on 2018/3/6 16:43.
 */
class MethodTestActivity : BaseActivity(), View.OnClickListener, Observer {
    override fun getLayoutResId(): Int = R.layout.activity_method

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val method = arrayListOf(
            "字符替换与数组化", "hash相同", "字体颜色背景变换"
            , "系统信息", "获取app信息", "电池电量"
            , "数组排序", "json转换", "list迭代器", "list筛选lambda"
            , "Intent测试", "时间选择", "地址选择", "工厂模式"
            , "SVG与Value", "渐变的文字", "音频处理", "字符串转id"
            , "排序算法", "LinkedList使用"
            , "MD5加密", "科学计数法", "虚拟机鉴定", "获取当前方法的名称"
            , "输出View的位置信息", "URL的结构"
        )
        for (i in method) {
            val tvText =
                LayoutInflater.from(this).inflate(R.layout.item_tv, flow, false) as TextView
            tvText.setPadding(20, 6, 20, 6)
            tvText.text = i
            tvText.tag = i
            flow.addView(tvText)
            tvText.setOnClickListener(this)
        }
        tvDisplay.text = replaceStr()
        val controller = AnimationUtils.loadLayoutAnimation(this, R.anim.anim_layout)

//        val controller = LayoutAnimationController(animation)
        controller.delay = 0.5f
        controller.order = LayoutAnimationController.ORDER_NORMAL
        flow.layoutAnimation = controller
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
            "LinkedList使用" -> getNum()
            "MD5加密" -> MD5Utils.encryption("http://img.mukewang.com/55237dcc0001128c06000338.jpg")
            "科学计数法" -> format("0")
            "输出View的位置信息" -> outPutViewParameter(v)
            "URL的结构" -> getURL()
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
            "虚拟机鉴定" -> detectEmulatorSimple()
            "获取当前方法的名称" -> getMethodName()
        }
    }

    /**
     * 输出view的left、top、right、bottom、x、y、translationX、translationY。
     */
    private fun outPutViewParameter(v: View): String {

        val builder = StringBuilder()
        builder.append("left == ${v.left}\n")
        builder.append("top == ${v.top}\n")
        builder.append("right == ${v.right}\n")
        builder.append("bottom == ${v.bottom}\n")
        builder.append("x == ${v.x}\n")
        builder.append("y == ${v.y}\n")
        builder.append("translationX == ${v.translationX}\n")
        builder.append("translationY == ${v.translationY}\n")
        builder.append("touchSlop == ${ViewConfiguration.get(this).scaledTouchSlop}\n")
        val layoutParams: ViewGroup.LayoutParams = v.layoutParams
        builder.append("width == ${layoutParams.width}\n")
        builder.append("height == ${layoutParams.height}\n")

        builder.append("width == ${v.width}\n")
        builder.append("height == ${v.height}\n")


        val params: ViewGroup.MarginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
        builder.append("width == ${params.width}\n")
        builder.append("height == ${params.height}\n")
        builder.append("layoutDirection == ${params.layoutDirection}\n")
        builder.append("leftMargin == ${params.leftMargin}\n")
        builder.append("topMargin == ${params.topMargin}\n")
        builder.append("rightMargin == ${params.rightMargin}\n")
        builder.append("bottomMargin == ${params.bottomMargin}\n")
        builder.append("marginEnd == ${params.marginEnd}\n")
        builder.append("marginStart == ${params.marginStart}\n")
        return builder.toString()
    }

    private fun getMethodName() {
        //获取当前方法的名称的两种方法。
        //1）StackTraceElement[] traceElement = Thread.currentThread().getStackTrace();
        //此方法取得的StackTraceElement栈：
        //第零条数据是VmStack的getThreadStackTrace；
        //第一条数据是Thread的getStackTrace
        //第二条数据是当前方法的所在位置。

        var traceElement = Thread.currentThread().stackTrace
        val builder = StringBuilder()
        builder.append("第一种")
        builder.append("\n")
        for (i in traceElement.indices) {
            val stack = traceElement[i]
            var className = stack.className
            className = className.substring(className.lastIndexOf(".") + 1)
//            builder.setLength(0)//清空字符串
            builder.append("位置：")
            builder.append(i)
            builder.append("；")
            builder.append(className)
            builder.append(".")
            builder.append(stack.methodName)
            builder.append("\n")
//            Log.i("logutils", "第" + i + "个" + builder)
        }
        traceElement = Throwable().stackTrace
        builder.append("\n")
        builder.append("第二种")
        builder.append("\n")
        for (i in traceElement.indices) {
            val stack = traceElement[i]
            var className = stack.className
            className = className.substring(className.lastIndexOf(".") + 1)
//            builder.setLength(0)
            builder.append("位置：")
            builder.append(i)
            builder.append("；")
            builder.append(className)
            builder.append(".")
            builder.append(stack.methodName)
            builder.append("\n")
//            Log.i("logutils", "第" + i + "个" + builder)
        }
        tvDisplay.text = builder.toString()
    }


    private fun detectEmulatorSimple() {
        EmulatorDetector.with(this)
            .detectSimple { isEmulator ->
                runOnUiThread {
                    tvDisplay.text = "This is not a emulator !"
                    if (isEmulator) {
                        tvDisplay.text = "This is a emulator !"
                        AlertDialog.Builder(this)
                            .setTitle("虚拟机不能执行此功能")
                            .setCancelable(false)
                            .setPositiveButton("确定") { dialog, which ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                    }
                }
            }
    }

    /**
     * @param num 科学技术： #,###.00"
     * 0 表示如果位数不足则以 0 填充，# 表示只要有可能就把数字拉上这个位置
     * @return
     */
    fun format(num: Double): String {
        return DecimalFormat("#,##0.00").format(num)
    }

    fun format(num: String): String {
        return format(num.toDouble())
    }

    //观察者模式
    private fun setObserver(@LayoutRes item: Int?) {
        //观察者
        val observer =
            Observer { o, arg -> LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}==$arg") }

        //被观察者
        val observable = object : Observable() {
            fun send(content: Any) {
                setChanged()
                //通知观察者
                notifyObservers(content)
            }
        }
        //把观察者注册到被观察者中
        observable.addObserver(observer)
        observable.addObserver(this)

        //被观察者发送消息
        observable.send("观察者模式")
    }

    override fun update(o: Observable?, arg: Any?) {
        LogUtils.i("${javaClass.simpleName}==${Throwable().stackTrace[0].methodName}==$arg")
    }


    //一组数据从1开始数，到13时移除此数，之后继续从1开始数
    private fun getNum(): String {
        val nums = (1..14).mapTo(LinkedList<String>()) { it.toString() + "" }
        val builder = StringBuilder()
        var a = 1
        while (!nums.isEmpty()) {
            val temp = nums.first
            nums.removeFirst()
            if (a != 13) {
                nums.addLast(temp)
                a++
            } else {
                builder.append("$temp,")
                a = 1
            }
        }
        return builder.toString()
    }

    private fun sort(): CharSequence? {
        val arr = intArrayOf(99, 12, 35, 5, 9, 54, 44)
        return "原始数据：${getArrayString(arr)}；\n" +
                "冒泡排序：${bubbleSort(arr.clone())}；\n" +
                "冒泡排序：${bubbleSort1(arr.clone())}；\n" +
                "选择排序：${selectSort(arr.clone())}；\n" +
                "插入排序：${insertSort(arr.clone())}。"
    }

    /**
     * 插入排序
     */
    private fun insertSort(arr: IntArray): String {
        LogUtils.i("数据==${getArrayString(arr)}")
        var temp: Int
        for (i in 0 until arr.size - 1) {
            for (j in i + 1 downTo 1) {
//                LogUtils.i("j==$j")
                if (arr[j] < arr[j - 1]) {
                    temp = arr[j - 1]
                    arr[j - 1] = arr[j]
                    arr[j] = temp
                } else { //不需要交换
                    break
                }
            }
        }
        return getArrayString(arr);
    }

    private fun getArrayString(arr: IntArray): String {
        var builder = StringBuilder()
        for (element in arr) builder.append("，$element")
//        LogUtils.i("text==$builder")
        builder.replace(0, 1, "")//移除开始的逗号
//        LogUtils.i("text==$builder")
        return builder.toString()
    }

    /**
     * 选择排序
     */
    private fun selectSort(arr: IntArray): String {
        LogUtils.i("数据==${getArrayString(arr)}")
        var index: Int//临时变量
        var temp: Int//临时变量
        for (i in 0..arr.size - 2) {
            index = i
            for (j in i + 1 until arr.size) {
                if (arr[j] < arr[index]) index = j
            }
            if (index === i) continue
            temp = arr[i]
            arr[i] = arr[index]
            arr[index] = temp
        }
        return getArrayString(arr);
    }

    private fun bubbleSort1(arr: IntArray): String {
        LogUtils.i("数据==${getArrayString(arr)}")
        if (arr == null) throw IllegalArgumentException("arr 不能为空")
        if (arr.size < 2) return "";
        var temp: Int//临时变量
        var flag: Boolean
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
            if (!flag) break
        }
        return getArrayString(arr);
    }

    /**
     * 冒泡排序
     */
    private fun bubbleSort(arr: IntArray): String {
        LogUtils.i("数据==${getArrayString(arr)}")
        var temp: Int//临时变量
        var flag: Boolean
        for (i in 0 until arr.size - 1) { //表示趟数，一共arr.length-1次。
            flag = false
            for (j in arr.size - 1 downTo i + 1) {//从后往前跑
                if (arr[j] < arr[j - 1]) {
                    temp = arr[j]
                    arr[j] = arr[j - 1]
                    arr[j - 1] = temp
                    flag = true
                }
            }
            if (!flag) break
        }
        return getArrayString(arr);
    }

    private fun changeStrToId(): CharSequence? {
        val id = R.string::class.java.getField("jsonobj").getInt(R.string())
        val id1 = resources.getIdentifier("jsonobj", "string", packageName)
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
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )   //完全可以设置为null
                .build()
        else SoundPool(100, AudioManager.STREAM_MUSIC, 0)
//        构建对象
        val soundId = soundPool.load(this, R.raw.bgyxc, 1)//加载资源，得到soundId
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
        val animator = ValueAnimator.ofFloat(0f, flow.height.toFloat())
//        value.setTarget(image)
        animator.duration = 2000
        animator.addUpdateListener { animation ->
            //            animation!!.animatedFraction//当前百分比
            val lp = image.layoutParams
            lp.height = animation!!.animatedValue.toString().toFloat().toInt()
            image.layoutParams = lp
        }
        animator.start()
        animator.addListener(object : AnimatorListenerAdapter() {
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
        val left = factory.getHairByClass("com.sky.oa.other.factory.factory.hair.LeftHair")
        left?.draw()
        val hair = factory.getHairByClassKey("in")
        hair?.draw()
        val ddddd = factory.create(LeftHair::class.java)
        ddddd.draw()

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
        span.setSpan(
            AbsoluteSizeSpan(resources.getDimensionPixelSize(R.dimen.text_18)),
            0,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        span.setSpan(
            ForegroundColorSpan(Color.RED),
            8,
            17,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )//字体颜色
        span.setSpan(
            BackgroundColorSpan(Color.rgb(55, 155, 200)),
            17,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        span.setSpan(UnderlineSpan(), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)//下划线
        span.setSpan(StrikethroughSpan(), 4, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)//中划线
        span.setSpan(RelativeSizeSpan(1.2f), 8, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)//字号按比例翻倍
        span.setSpan(SuperscriptSpan(), 10, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)//上标
        span.setSpan(SubscriptSpan(), 12, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)//下标
        span.setSpan(StyleSpan(Typeface.BOLD), 13, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)//字体风格
        span.setSpan(
            URLSpan("http://www.baidu.com"),
            17,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )//超链接
        span.append("\n天地玄黄，宇宙洪荒。\n")
        span.append("日月盈仄，辰宿列张。\n")
        span.append(getText(R.string.ibu))
        return span
    }

    private fun lambda(): String {
        val text = StringBuilder()
        val seq = sequence {
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
        val array =
            GsonUtils.fromJson(getString(R.string.jsonarray), Array<ActivityModel>::class.java)
        array.sortedByDescending { it }
            .forEach { text.append(it.activityName + "\n") }
        return text.toString()
    }

    private fun iterator(): String {
        val list =
            GsonUtils.fromJson(getString(R.string.jsonarray), Array<ActivityModel>::class.java)
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
        val model = GsonUtils.fromJson(getString(R.string.jsonobj), ActivityModel::class.java)
        val entity = GsonUtils.fromJson<ApiResponse<List<ActivityModel>>>(
            getString(R.string.jsonlist),
            object : TypeToken<ApiResponse<List<ActivityModel>>>() {}.type
        )
        val list = GsonUtils.fromJson<List<ActivityModel>>(
            getString(R.string.jsonarray),
            object : TypeToken<ArrayList<ActivityModel>>() {}.type
        )

        val array = Gson().fromJson(getString(R.string.jsonarray), Array<ActivityModel>::class.java)

        val builder = StringBuilder()

        builder.append("单个类：")
        builder.append("\n")
        builder.append(model.toString())
        builder.append("\n")
        builder.append("\n")

        builder.append("类中套列表：")
        builder.append("\n")
        builder.append(entity.objList.toString())
        builder.append("\n")
        builder.append("\n")

        builder.append("列表：")
        builder.append("\n")
        builder.append(list.toString())
        builder.append("\n")
        builder.append("\n")

        builder.append("数组：")
        builder.append("\n")
        builder.append("[")
        for (i in array) builder.append(i.toString())
        builder.append("]")
        builder.append("\n")
        return builder.toString()
    }

    private fun sortList(): String {
        val list = ArrayList<SortModel>()
        ('g' downTo 'a').mapTo(list) { SortModel("$it") }
        (22222 downTo 22217).mapTo(list) { SortModel("${it.toChar()}") }
        ('G' downTo 'A').mapTo(list) { SortModel("$it") }
        val builder = StringBuilder()
        builder.append("原数据：$list\n")
        list.reverse()//逆序
        builder.append("逆序：$list\n")
        list.shuffle()//随机
        builder.append("随机：$list\n")
        list.sort()//排序
        builder.append("sort排序（大写小写文字）：$list\n")
        Collections.sort(list, ascending)
        builder.append("Comparable升序(文字小大写)：$list\n")
        Collections.sort(list, descending)
        builder.append("Comparable降序(大小写文字)：$list")
        return builder.toString()
    }

    class SortModel(var className: String?) : Comparable<SortModel> {
        override fun compareTo(another: SortModel): Int = className!!.compareTo(another.className!!)
        override fun toString(): String = "$className"
    }

    companion object {
        private val collator = Collator.getInstance()
        //升序
        private val ascending =
            Comparator<SortModel> { first, second ->
                collator.compare(
                    first.className,
                    second.className
                )
            }
        private val ascending1 =
            Comparator<SortModel> { first, second -> first.className!!.compareTo(second.className!!) }
        //降序
        private val descending =
            Comparator<SortModel> { first, second ->
                collator.compare(
                    second.className,
                    first.className
                )
            }
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
            val projection = arrayOf(
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )
            val cursor = contentResolver.query(data?.data!!, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                val nameIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                val numberIndex =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                val name = cursor.getString(nameIndex);
                val number = cursor.getString(numberIndex);
                showToast(number)
                tvDisplay.text = "姓名:$name;\n电话:$number"
            }
            cursor?.close()
        }
    }

    private fun equalHashCode(): String =
        "Aa的hashCode:${"Aa".hashCode()}==BB的hashCode:${"BB".hashCode()};\n" +
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
    private fun getSystemMessage(): String {
        val text = StringBuilder()
        val cpus = Build.SUPPORTED_ABIS
        for (i in cpus)
            text.append("CPU指令集==$i\n")
        val abis32 = Build.SUPPORTED_32_BIT_ABIS
        for (i in abis32)
            text.append("CPU32指令集==$i\n")
        val abis64 = Build.SUPPORTED_64_BIT_ABIS
        for (i in abis64)
            text.append("CPU64指令集==$i\n")
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            text.append("CPU指令集==${Build.CPU_ABI}\n")
//            text.append("CPU指令集==${Build.CPU_ABI2}\n")
//            text.append("硬件序列号==${Build.SERIAL}\n")
//        }
        text.append("主板型号==${Build.BOARD}\n")
        text.append("系统定制商==${Build.BRAND}\n")
        text.append("设备参数==${Build.DEVICE}\n")
        text.append("显示屏参数==${Build.DISPLAY}\n")
        text.append("唯一编号==${Build.FINGERPRINT}\n")
        text.append("修订版本列表==${Build.ID}\n")
        text.append("硬件制造商==${Build.MANUFACTURER}\n")
        text.append("版本==${Build.MODEL}\n")
        text.append("硬件名==${Build.HARDWARE}\n")
        text.append("手机产品名==${Build.PRODUCT}\n")
        text.append("描述Build的标签==${Build.TAGS}\n")
        text.append("Builder的类型==${Build.TYPE}\n")
        text.append("当前开发代号==${Build.VERSION.CODENAME}\n")
        text.append("源码控制版本号==${Build.VERSION.INCREMENTAL}\n")
        text.append("系统版本==${Build.VERSION.RELEASE}\n")
        text.append("系统版本号==${Build.VERSION.SDK_INT}\n")
        text.append("Host值==${Build.HOST}\n")
        text.append("User名==${Build.USER}\n")
        text.append("编译时间==${Build.TIME}\n")
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

    /**
     * URL的结构
     */
    private fun getURL(): String {
        val builder = StringBuilder()
        val url = URL(HttpUrl.URL_MUKE + HttpUrl.URL_MUKE1)
        builder.append("资源名 == ${url.file}\n")
        builder.append("主机名 == ${url.host}\n")
        builder.append("路径 == ${url.path}\n")
        builder.append("端口 == ${url.port}\n")
        builder.append("协议名称 == ${url.protocol}\n")
        builder.append("查询字符串 == ${url.query}\n")
        return builder.toString()
    }

    private fun ttsTest() {
        tts = TextToSpeech(this, TextToSpeech.OnInitListener { status -> checkTTS(status) })
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tts?.speak("gesture", TextToSpeech.QUEUE_ADD, null, "speech")
            //                tts?.synthesizeToFile("gesture",null,File(""),"record")
        }
    }

    var tts: TextToSpeech? = null
    private fun checkTTS(status: Int) {

    }

    /**
     * 文件属性
     */
    private fun fileTest() {
        val f = File("pass.txt")
        LogUtils.i(f.getParent())//返回此抽象路径名父目录的路径名字符串；如果此路径名没有指定父目录，则返回 null
        LogUtils.i(f.getName())//返回由此抽象路径名表示的文件或目录的名称
        LogUtils.i("${f.exists()}")//测试此抽象路径名表示的文件或目录是否存在
        LogUtils.i("${f.getAbsoluteFile()}")// 返回此抽象路径名的绝对路径名形式
        LogUtils.i(f.getAbsolutePath())//返回此抽象路径名的规范路径名字符串
        LogUtils.i(f.getPath())//将此抽象路径名转换为一个路径名字符串
        LogUtils.i("${f.hashCode()}")//计算此抽象路径名的哈希码
        LogUtils.i("${f.length()}")//返回由此抽象路径名表示的文件的长度
        LogUtils.i("${f.list()}")// 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中的文件和目录
        LogUtils.i("${f.mkdir()}")//创建此抽象路径名指定的目录
    }

    override fun onDestroy() {
        super.onDestroy()
        tts?.shutdown()
    }
}
