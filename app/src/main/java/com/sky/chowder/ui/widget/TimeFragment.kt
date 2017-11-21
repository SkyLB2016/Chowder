package com.sky.chowder.ui.widget

import android.content.DialogInterface
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
    var year = 0
    var monthDay = ""
    var hour = 0
    var minute = "00"
    private val disOnClick: OnDismissListener? = null

    var onClick: OnClickListener? = null

    var minCal: Calendar? = null
    var maxCal: Calendar? = null
    var currentCal: Calendar? = null
    val interval = 15

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //去掉默认的标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false
        val view = inflater!!.inflate(R.layout.fragment_time, container)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private fun initData() {
        minCal = Calendar.getInstance()
        maxCal = Calendar.getInstance()
        currentCal = Calendar.getInstance()
        minCal?.timeInMillis = System.currentTimeMillis() - 10000
        currentCal?.timeInMillis = System.currentTimeMillis()
        maxCal?.timeInMillis = System.currentTimeMillis() + interval * 24 * 3600 * 1000

        year = currentCal!!.get(Calendar.YEAR)
        val month = currentCal!!.get(Calendar.MONTH) + 1
        val day = currentCal!!.get(Calendar.DAY_OF_MONTH)
        hour = currentCal!!.get(Calendar.HOUR_OF_DAY)

        val minutes4 = arrayOf("00", "15", "30", "45")
        val min = currentCal!!.get(Calendar.MINUTE) / 15 + 1
        if (min >= 4) hour += 1
        minute = minutes4[min % 4]

        var maxMonth = maxCal!!.get(Calendar.MONTH) + 1
        var maxDay = maxCal!!.get(Calendar.DAY_OF_MONTH)

        val strMD = arrayOfNulls<String>(interval + 1)
        var position = 0
        val maxDayOfMonth = currentCal!!.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (i in 0..maxDayOfMonth - day) {
            if (i !== 0)
                strMD[i] = "${month}月${day + i}日"
            else strMD[i] = "今天"
            position = i
        }
        if (month != maxMonth)
            for (i in 1..maxDay) {
                strMD[i + position] = "${maxMonth}月${i}日"
            }
        npMonthDay.displayedValues = strMD
        setNpValue(npMonthDay, strMD.size - 1, 0, 0)
        monthDay = "${month}月${day}日"
        npMonthDay.setOnValueChangedListener { picker, oldVal, newVal ->
            if (newVal == 0) {
                if (currentCal!!.get(Calendar.HOUR_OF_DAY) > hour) hour = currentCal!!.get(Calendar.HOUR_OF_DAY)
                setNpValue(npHour, 23, hour, hour)
                monthDay = "${month}月${day}日"
            } else {
                setNpValue(npHour, 23, 0, hour)
                monthDay = strMD[newVal]!!
            }
        }

        setNpValue(npHour, 23, hour, hour)
        npHour.setFormatter(getFormatter())
        npHour.setOnValueChangedListener { picker, oldVal, newVal -> hour = newVal }

        npMinute.displayedValues = minutes4
        setNpValue(npMinute, minutes4.size - 1, 0, min % 4)
        npMinute.setOnValueChangedListener { picker, oldVal, newVal -> minute = minutes4[newVal] }

    }

    private fun setNpValue(np: NumberPicker, max: Int, min: Int, value: Int) {
        np.maxValue = max
        np.minValue = min
        np.value = value
        np.wrapSelectorWheel = false
    }

    private fun getFormatter(): NumberPicker.Formatter? {
        //获取全部私有属性
        val pickerFields = NumberPicker::class.java.declaredFields
        var formatter: NumberPicker.Formatter? = null
        for (field in pickerFields) {
            field.isAccessible = true
            //遍历找到我们需要获取值的那个属性
            if (field.name == "sTwoDigitFormatter") {
                try {
                    //获取属性值
                    formatter = field.get(npHour) as NumberPicker.Formatter?
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                break
            }
        }
        return formatter
    }

    override fun onStart() {
        super.onStart()
        //        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.window!!.setLayout(ScreenUtils.getWidthPX(activity) / 8 * 7, dialog.window!!.attributes.height)
    }

    @OnClick(R.id.tvLeft, R.id.tvRight)
    fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvLeft -> dismiss()
            R.id.tvRight -> {
                val time = "${year}年$monthDay $hour:$minute"
                val timeL = DateUtil.dateToStamp(time, "yyyy年MM月dd日 HH:mm")
//                val timeL = DateUtil.dateToStamp(time, DateUtil.CYMHM)
                if (timeL < System.currentTimeMillis())
                    ToastUtils.showShort(activity, "下单时间时间不能小于当前时间")
                else {
                    onClick?.onClick(DateUtil.stampToTime(timeL))
                    dismiss()
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        disOnClick?.onDismiss()
    }

    interface OnClickListener {
        fun onClick(time: String)
    }

    interface OnDismissListener {
        fun onDismiss()
    }
}
