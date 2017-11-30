package com.sky.chowder.ui.fragment

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
    private var year = 0
    private var monthDay = ""
    private var hour = 0
    private var minHour = 0
    private var minute = "00"


    private val disOnClick: OnDismissListener? = null
    var onClick: OnClickListener? = null

    private var cal: Calendar? = null
    private val interval = 15

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

    override fun onStart() {
        super.onStart()
        //        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.window!!.setLayout(ScreenUtils.getWidthPX(activity) / 8 * 7, dialog.window!!.attributes.height)
    }

    private fun initData() {
        cal = Calendar.getInstance()
        cal?.timeInMillis = System.currentTimeMillis()

        val curYear = cal!!.get(Calendar.YEAR)
        year = curYear
        val curMonth = cal!!.get(Calendar.MONTH) + 1
        val curDay = cal!!.get(Calendar.DAY_OF_MONTH)
        minHour = cal!!.get(Calendar.HOUR_OF_DAY)

        val minutes4 = arrayOf("00", "15", "30", "45")
        val min = cal!!.get(Calendar.MINUTE) / 15 + 1
        minute = minutes4[min % 4]

        if (min >= 4) minHour += 1//当前时间超过45分后，minHour+1
        hour = minHour

        //间隔时间后月份是否有变化
        cal?.timeInMillis = System.currentTimeMillis() + interval * 24 * 3600 * 1000
        val maxYear = cal!!.get(Calendar.YEAR)
        val maxMonth = cal!!.get(Calendar.MONTH) + 1
        val maxDay = cal!!.get(Calendar.DAY_OF_MONTH)

        val maxDayOfMonth = cal!!.getActualMaximum(Calendar.DAY_OF_MONTH)
        val strMD = getStrMD(maxDayOfMonth, curDay, curMonth, maxMonth, maxDay)
        npMonthDay.displayedValues = strMD
        setNpValue(npMonthDay, strMD.size - 1, 0, 0)
        monthDay = "${setTwo(curMonth)}月${setTwo(curDay)}日"
        npMonthDay.setOnValueChangedListener { _, _, newVal ->
            if (newVal === 0) {
                if (minHour > hour) hour = minHour
                setNpValue(npHour, 23, minHour, hour)
                monthDay = "${setTwo(curMonth)}月${setTwo(curDay)}日"
            } else {
                setNpValue(npHour, 23, 0, hour)
                monthDay = strMD[newVal]!!
            }
            year = if (curMonth === 12 && maxMonth === 1 && monthDay.startsWith(setTwo(maxMonth))) maxYear else curYear
        }

        setNpValue(npHour, 23, minHour, minHour)
        npHour.setFormatter(getFormatter())
        npHour.setOnValueChangedListener { _, _, newVal -> hour = newVal }

        npMinute.displayedValues = minutes4
        setNpValue(npMinute, minutes4.size - 1, 0, min % 4)
        npMinute.setOnValueChangedListener { _, _, newVal -> minute = minutes4[newVal] }
    }

    private fun getStrMD(maxDayOfMonth: Int, curDay: Int, curMonth: Int, maxMonth: Int, maxDay: Int): Array<String?> {
        val strMD = arrayOfNulls<String>(interval + 1)
        var position = 0
        for (i in 0..maxDayOfMonth - curDay) {
            if (i !== 0) strMD[i] = "${setTwo(curMonth)}月${setTwo(curDay + i)}日"
            else strMD[i] = "今天"
            position = i
        }
        if (curMonth != maxMonth)
            for (i in 1..maxDay) {
                strMD[i + position] = "${setTwo(maxMonth)}月${setTwo(i)}日"
            }
        return strMD
    }

    private fun setTwo(month: Int) = String.format("%02d", month)

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
                try { //获取属性值
                    formatter = field.get(npHour) as NumberPicker.Formatter?
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
                break
            }
        }
        return formatter
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
