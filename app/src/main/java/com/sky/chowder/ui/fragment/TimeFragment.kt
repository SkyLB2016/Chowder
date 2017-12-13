package com.sky.chowder.ui.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.NumberPicker
import butterknife.ButterKnife
import butterknife.OnClick
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
    private val interval = 30//最长间隔
    var time = 0L//最长间隔
    private val cal = Calendar.getInstance()!!

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = inflater!!.inflate(R.layout.fragment_time, container)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    override fun onStart() {
        super.onStart()
        dialog.window!!.setLayout(ScreenUtils.getWidthPX(activity) / 8 * 7, dialog.window!!.attributes.height)
    }

    private fun initData() {
        cal?.timeInMillis = System.currentTimeMillis()
        var minHour = cal.get(Calendar.HOUR_OF_DAY)
        val min = cal.get(Calendar.MINUTE) / 15 + 1
        cal.set(Calendar.MINUTE, (min % 4) * 15)//设置当前分钟
        //当前时间超过45分后，minHour+1
        if (min >= 4) {
            minHour += 1
            cal.set(Calendar.HOUR_OF_DAY, minHour)
        }
        val strMD = getStrMD()
        npMonthDay.displayedValues = strMD
        setNpValue(npMonthDay, strMD.size - 1, 0, 0)
        npMonthDay.setOnValueChangedListener { _, oldVal, newVal ->
            var hour = cal.get(Calendar.HOUR_OF_DAY)
            if (newVal === 0) {
                if (hour < minHour) {
                    hour = minHour
                    cal.set(Calendar.HOUR_OF_DAY, minHour)
                }
                setNpValue(npHour, 23, minHour, hour)
            } else setNpValue(npHour, 23, 0, hour)
            cal.add(Calendar.DAY_OF_MONTH, newVal - oldVal)
        }

        setNpValue(npHour, 23, minHour, minHour)
        npHour.setFormatter { value -> String.format("%02d", value) }
        npHour.setOnValueChangedListener { _, oldVal, newVal -> cal.add(Calendar.HOUR_OF_DAY, newVal - oldVal) }

        val minutes4 = arrayOf("00", "15", "30", "45") //分钟分为四个时刻
        npMinute.displayedValues = minutes4
        setNpValue(npMinute, minutes4.size - 1, 0, min % 4)
        npMinute.setOnValueChangedListener { _, oldVal, newVal -> cal.add(Calendar.MINUTE, (newVal - oldVal) * 15) }

        if (time !== 0L) setSelectTime()//如已有设置好的时间，则载入
    }

    private fun setSelectTime() {
        //把当前时间戳转换成日期格式，在转换成00:00时的时间戳
        val curr = DateUtil.dateToStamp(DateUtil.stampToTime(System.currentTimeMillis(), "yyyy-MM-dd"), "yyyy-MM-dd")
        val po = (time - curr) / (24 * 3600 * 1000)//选中的时间为第几天
        cal.timeInMillis = time
        if (po.toInt() !== 0) setNpValue(npHour, 23, 0, cal.get(Calendar.HOUR_OF_DAY))
        npMonthDay.value = po.toInt()
        npHour.value = cal.get(Calendar.HOUR_OF_DAY)
        npMinute.value = cal.get(Calendar.MINUTE) / 15
    }


    private fun getStrMD(): Array<String?> {
        val strMD = arrayOfNulls<String>(interval + 1)
        val cal = Calendar.getInstance()
        cal?.timeInMillis = System.currentTimeMillis()
        for (i in 0..interval) {
            if (i !== 0) strMD[i] = DateUtil.stampToTime(cal.timeInMillis, "MM月dd日") else strMD[i] = "今天"
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

    @OnClick(R.id.tvLeft, R.id.tvRight)
    fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvLeft -> dismiss()
            R.id.tvRight -> {
                if (cal.timeInMillis < System.currentTimeMillis()) ToastUtils.showShort(activity, "下单时间时间不能小于当前时间")
                else {
                    onClick?.onClick(DateUtil.stampToTime(cal.timeInMillis))
                    dismiss()
                }
            }
        }
    }

    interface OnClickListener {
        fun onClick(time: String)
    }
}
