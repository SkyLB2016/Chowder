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
import com.sky.utils.LogUtils
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

    val minCal = Calendar.getInstance()
    val maxCal = Calendar.getInstance()
    val currentCal = Calendar.getInstance()

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
        minCal.timeInMillis = System.currentTimeMillis()-10000
        currentCal.timeInMillis = System.currentTimeMillis()
        maxCal.timeInMillis = System.currentTimeMillis() + 15 * 24 * 3600 * 1000
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

        year = currentCal.get(Calendar.YEAR)
        month = currentCal.get(Calendar.MONTH) + 1
        day = currentCal.get(Calendar.DAY_OF_MONTH)
        hour = currentCal.get(Calendar.HOUR_OF_DAY)

        val str = arrayOf("00", "15", "30", "45")
        val min = currentCal.get(Calendar.MINUTE) / 15
        minute = str[min]


        var maxMonth = maxCal.get(Calendar.MONTH) + 1
        var maxDay = maxCal.get(Calendar.DAY_OF_MONTH)

        npMonth.maxValue = 12
        npMonth.minValue = 1
        npMonth.value = month
        npMonth.setFormatter(formatter)
        npMonth.setOnValueChangedListener(onChangeListener)

        npDate.maxValue = 31
        npDate.minValue = 1
        npDate.value = day
        npDate.setFormatter(formatter)
        npDate.setOnValueChangedListener(onChangeListener)

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

    private val onChangeListener = NumberPicker.OnValueChangeListener { picker, oldVal, newVal ->
        val mTempDate = Calendar.getInstance()
        mTempDate.timeInMillis = currentCal.timeInMillis
        // take care of wrapping of days and months to update greater fields
        if (picker === npDate) {
            val maxDayOfMonth = mTempDate.getActualMaximum(Calendar.DAY_OF_MONTH)
            if (oldVal == maxDayOfMonth && newVal == 1) {
                mTempDate.add(Calendar.DAY_OF_MONTH, 1)
            } else if (oldVal == 1 && newVal == maxDayOfMonth) {
                mTempDate.add(Calendar.DAY_OF_MONTH, -1)
            } else {
                mTempDate.add(Calendar.DAY_OF_MONTH, newVal - oldVal)
            }
        } else if (picker === npMonth) {
            if (oldVal == 11 && newVal == 0) {
                mTempDate.add(Calendar.MONTH, 1)
            } else if (oldVal == 0 && newVal == 11) {
                mTempDate.add(Calendar.MONTH, -1)
            } else {
                mTempDate.add(Calendar.MONTH, newVal - oldVal)
            }
//        } else if (picker === mYearSpinner) {
//            mTempDate.set(Calendar.YEAR, newVal)
        } else {
            throw IllegalArgumentException()
        }
        LogUtils.i("min==${currentCal.timeInMillis}")
        LogUtils.i("current==${minCal.timeInMillis}")
        LogUtils.i("max==${maxCal.timeInMillis}")
        if (mTempDate.before(minCal)) {
            mTempDate.timeInMillis = minCal.timeInMillis
        } else if (mTempDate.after(maxCal)) {
            mTempDate.timeInMillis = maxCal.timeInMillis
        }
        npMonth.value = currentCal.get(Calendar.MONTH) + 1
        npDate.value = currentCal.get(Calendar.DAY_OF_MONTH)
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
