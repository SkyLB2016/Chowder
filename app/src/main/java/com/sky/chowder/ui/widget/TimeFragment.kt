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
    var month = 0
    var day = 0
    var hour = 0
    var minute = "00"
    private val disOnClick: OnDismissListener? = null


    var onClick: OnClickListener? = null

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
        val interval = 15
        val newCalendar = Calendar.getInstance()
        year = newCalendar.get(Calendar.YEAR)
        month = newCalendar.get(Calendar.MONTH) + 1
        day = newCalendar.get(Calendar.DAY_OF_MONTH)
        hour = newCalendar.get(Calendar.HOUR_OF_DAY)

        val str = arrayOf("00", "15", "30", "45")
        val min = newCalendar.get(Calendar.MINUTE) / 15
        minute = str[min]

        newCalendar.timeInMillis = System.currentTimeMillis() + 15 * 24 * 3600 * 1000
        var maxMonth = newCalendar.get(Calendar.MONTH) + 1
        var maxDay = newCalendar.get(Calendar.DAY_OF_MONTH)
        var listMonth = arrayListOf<String>()
        if (month == maxMonth) listMonth.add(month.toString())
        else {
            listMonth.add(month.toString())
            listMonth.add(maxMonth.toString())
        }

//        for (i in listMonth)var listMonth = arrayListOf<String>()

        npMonth.displayedValues = listMonth.toArray() as Array<out String>?
        npMonth.maxValue = listMonth.size - 1
        npMonth.minValue = 0
        npMonth.value = 0
        npHour.setFormatter(formatter)
        npMonth.setOnValueChangedListener { picker, oldVal, newVal -> month = newVal }


        npDate.maxValue = 31
        npDate.minValue = 1
        npDate.value = day
        npHour.setFormatter(formatter)
        npDate.setOnValueChangedListener { picker, oldVal, newVal -> day = newVal }

        npHour.maxValue = 23
        npHour.minValue = 0
        npHour.value = hour
        npHour.setFormatter(formatter)
        npHour.setOnValueChangedListener { picker, oldVal, newVal -> hour = newVal }


        npMinute.displayedValues = str
        npMinute.maxValue = str.size - 1
        npMinute.minValue = 0
        npMinute.value = min
        npMinute.setOnValueChangedListener { picker, oldVal, newVal -> minute = str[newVal] }
    }

    override fun onStart() {
        super.onStart()
        //        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.window!!.setLayout(ScreenUtils.getWidthPX(activity) / 8 * 7, dialog.window!!.attributes.height)
    }

    @OnClick(R.id.tvLeft, R.id.tvRight)
    fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvLeft -> ""
            R.id.tvRight -> {
                val time = "$year-$month-$day $hour:$minute"
                if (DateUtil.dateToStamp(time, DateUtil.YMDHM) < System.currentTimeMillis())
                    ToastUtils.showShort(activity, "下单时间时间不能小于当前时间")
                else onClick?.onClick(time)
            }
        }
        dismiss()
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
