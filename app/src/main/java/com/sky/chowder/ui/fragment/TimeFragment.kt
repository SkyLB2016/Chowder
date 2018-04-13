package com.sky.chowder.ui.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.NumberPicker
import com.sky.chowder.R
import com.sky.utils.DateUtil
import com.sky.utils.ScreenUtils
import com.sky.utils.ToastUtils
import kotlinx.android.synthetic.main.fragment_time.*
import java.util.*

/**
 * Created by SKY on 2017/7/12.
 */
class TimeFragment : DialogFragment() {
    lateinit var onClick: OnClickListener
    private val interval = 15//最长间隔
    private val minutesInterval = 10//分钟间隔
    private val minutesSize = 60 / minutesInterval//分钟间隔
    var time = 0L//最长间隔
    private val cal = Calendar.getInstance()!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater!!.inflate(R.layout.fragment_time, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view!!, savedInstanceState)
        initData()
    }

    override fun onStart() {
        super.onStart()
        dialog.window!!.setLayout(ScreenUtils.getWidthPX(activity) / 8 * 7, dialog.window!!.attributes.height)
    }

    private fun initData() {
        cal?.timeInMillis = System.currentTimeMillis()
        var minHour = cal.get(Calendar.HOUR_OF_DAY)
        val min = cal.get(Calendar.MINUTE) / minutesInterval + 1
        cal.set(Calendar.MINUTE, (min % minutesSize) * minutesInterval)//设置当前分钟
        //当前时间超过45分后，minHour+1
        if (min >= minutesSize) {
            minHour += 1
            cal.set(Calendar.HOUR_OF_DAY, minHour)
        }
        val strMD = getStrMD()
        npMonthDay.displayedValues = strMD
        setNpValue(npMonthDay, strMD.size - 1, 0, 0)
        npMonthDay.setOnValueChangedListener { _, oldVal, newVal ->
            var hour = cal.get(Calendar.HOUR_OF_DAY)
            if (newVal === 0) {
                if (hour <= minHour) {
                    hour = minHour
                    cal.set(Calendar.HOUR_OF_DAY, minHour)
                    setNpValue(npHour, 23, minHour, hour)
                    cal.set(Calendar.MINUTE, min * minutesInterval)
                    setMinutes(min, 0)

                } else {
                    setNpValue(npHour, 23, minHour, hour)
                    setMinutes(0, cal.get(Calendar.MINUTE) / minutesInterval)
                }
            } else {
                setNpValue(npHour, 23, 0, hour)
                setMinutes(0, cal.get(Calendar.MINUTE) / minutesInterval)
            }
            cal.add(Calendar.DAY_OF_MONTH, newVal - oldVal)
        }

        setNpValue(npHour, 23, minHour, minHour)
        npHour.setFormatter { value -> String.format("%02d", value) }
//        npHour.setFormatter(getFormatter())
        npHour.setOnValueChangedListener { _, oldVal, newVal ->
            cal.add(Calendar.HOUR_OF_DAY, newVal - oldVal)
            if ("${DateUtil.timeStampToDate(cal.timeInMillis, "dd")}-$newVal" == DateUtil.timeStampToDate(System.currentTimeMillis(), "dd-HH")) {
                cal.set(Calendar.MINUTE, min * minutesInterval)
                setMinutes(min, 0)
            } else setMinutes(0, cal.get(Calendar.MINUTE) / minutesInterval)

        }

        setMinutes(min, 0)
        npMinute.setOnValueChangedListener { _, oldVal, newVal -> cal.add(Calendar.MINUTE, (newVal - oldVal) * minutesInterval) }
        if (time !== 0L && time > System.currentTimeMillis()) setSelectTime()//如已有设置好的时间，则载入
        tvLeft.setOnClickListener { dismiss() }
        tvRight.setOnClickListener {
            if (cal.timeInMillis < System.currentTimeMillis()) ToastUtils.showShort(activity, "下单时间时间不能小于当前时间")
            else {
                onClick?.onClick(DateUtil.timeStampToDate(cal.timeInMillis))
                dismiss()
            }
        }
    }

    private fun setMinutes(min: Int, value: Int) {
        val minutes = if (min === 6)
            (0 until minutesSize).map { "${String.format("%02d", minutesInterval * it)}" }.toTypedArray()
        else (min until minutesSize).map { "${String.format("%02d", minutesInterval * it)}" }.toTypedArray()

        npMinute.maxValue = 0
        npMinute.displayedValues = minutes
        setNpValue(npMinute, minutes.size - 1, 0, value)
    }

    private fun setSelectTime() {
        //把当前时间戳转换成日期格式，在转换成00:00时的时间戳
        val curr = DateUtil.dateToTimeStamp(DateUtil.timeStampToDate(System.currentTimeMillis(), "yyyy-MM-dd"), "yyyy-MM-dd")
        val po = (time - curr) / (24 * 3600 * 1000)//选中的时间为第几天
        cal.timeInMillis = time
        if (po.toInt() !== 0) setNpValue(npHour, 23, 0, cal.get(Calendar.HOUR_OF_DAY))
        npMonthDay.value = po.toInt()
        npHour.value = cal.get(Calendar.HOUR_OF_DAY)
        npMinute.value = cal.get(Calendar.MINUTE) / minutesInterval
    }


    private fun getStrMD(): Array<String?> {
        val strMD = arrayOfNulls<String>(interval)
        val cal = Calendar.getInstance()
        cal?.timeInMillis = System.currentTimeMillis()
        for (i in 0 until interval) {
            if (i !== 0) strMD[i] = DateUtil.timeStampToDate(cal.timeInMillis, "MM月dd日") else strMD[i] = "今天"
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return strMD
    }

    private fun setNpValue(np: NumberPicker, max: Int, min: Int, value: Int) {
        np.maxValue = max
        np.minValue = min
        np.value = value
        np.wrapSelectorWheel = false
    }

    interface OnClickListener {
        fun onClick(time: String)
    }

    private fun getFormatter(): NumberPicker.Formatter? {
        //获取全部私有属性
        val pickerFields = NumberPicker::class.java.declaredFields
        var formatter: NumberPicker.Formatter? = null
        for (field in pickerFields) {
            field.isAccessible = true
            //遍历找到我们需要获取值的那个属性
            if (field.name == "sTwoDigitFormatter") {
                try { //获取属性值
                    formatter = field.get(R.id.npHour) as NumberPicker.Formatter?
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                break
            }
        }
        return formatter
    }
}
