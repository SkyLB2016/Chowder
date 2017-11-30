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
import com.sky.chowder.model.AreaEntity
import com.sky.utils.FileUtils
import com.sky.utils.GsonUtils
import com.sky.utils.ScreenUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_time.*

/**
 * Created by SKY on 2017/7/12.
 */
class AddressFragment : DialogFragment() {
    private var province = ""
    private var city = ""
    private var county = ""
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

    var provinceA: Array<AreaEntity>? = null
    var provinceStr: Array<String?>? = null
    var cityA: Array<AreaEntity>? = null
    var cityStr: Array<String?>? = null
    var countyA: Array<AreaEntity>? = null
    var countyStr: Array<String?>? = null
    private fun initData() {
        Observable.just(FileUtils.readAssestToStr(activity, "address.json"))
                .map { s -> GsonUtils.jsonToArray(s, Array<AreaEntity>::class.java) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { areaEntities ->
                    provinceA = areaEntities
                    setAddress()
                }
    }

    private fun setAddress() {
        provinceStr = arrayOfNulls<String>(provinceA!!.size)
        for (i in provinceA!!.indices) {
            provinceStr!![i] = provinceA!![i].name
        }

        npMonthDay.displayedValues = provinceStr
        setNpValue(npMonthDay, provinceStr!!.size - 1, 0, 0)
        province = "${provinceStr!![0]}"
        npMonthDay.setOnValueChangedListener { picker, oldVal, newVal ->
            province = "${provinceStr!![newVal]}"
            cityA = provinceA!![newVal].items?.toTypedArray()
            cityStr = arrayOfNulls<String>(cityA!!.size)
            for (i in cityA!!.indices) {
                cityStr!![i] = cityA!![i].name
            }
            npHour.maxValue = 0
            npHour.displayedValues = cityStr
            setNpValue(npHour, cityStr!!.size - 1, 0, 0)

            countyA = cityA!![0].items?.toTypedArray()
            if (countyA?.size === 0) {
                countyStr = arrayOfNulls<String>(1)
                countyStr!![0] = "无"
            } else {
                countyStr = arrayOfNulls<String>(countyA!!.size)
                for (i in countyA!!.indices) {
                    countyStr!![i] = countyA!![i].name
                }
            }
            npMinute.maxValue = 0
            npMinute.displayedValues = countyStr
            setNpValue(npMinute, countyStr!!.size - 1, 0, 0)
        }

        cityA = provinceA!![0].items?.toTypedArray()
        cityStr = arrayOfNulls<String>(cityA!!.size)
        for (i in cityA!!.indices) {
            cityStr!![i] = cityA!![i].name
        }
        npHour.displayedValues = cityStr
        setNpValue(npHour, cityStr!!.size - 1, 0, 0)
        npHour.setOnValueChangedListener { picker, oldVal, newVal ->
            city = cityStr!![newVal]!!
            countyA = cityA!![newVal].items?.toTypedArray()
            if (countyA?.size === 0) {
                countyStr = arrayOfNulls<String>(1)
                countyStr!![0] = ""
            } else {
                countyStr = arrayOfNulls<String>(countyA!!.size)
                for (i in countyA!!.indices) {
                    countyStr!![i] = countyA!![i].name
                }
            }
            npMinute.maxValue = 0
            npMinute.displayedValues = countyStr
            setNpValue(npMinute, countyStr!!.size - 1, 0, 0)
        }
        countyA = cityA!![0].items?.toTypedArray()
        countyStr = arrayOfNulls<String>(countyA!!.size)
        for (i in countyA!!.indices) {
            countyStr!![i] = countyA!![i].name
        }
        npMinute.displayedValues = countyStr
        setNpValue(npMinute, countyStr!!.size - 1, 0, 0)
        npMinute.setOnValueChangedListener { picker, oldVal, newVal ->
            county = countyStr!![newVal]!!
        }
    }

    private fun setNpValue(np: NumberPicker, max: Int, min: Int, value: Int) {
        np.maxValue = max
        np.minValue = min
        np.value = value
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
                val time = "$"
                onClick?.onClick(time)
                dismiss()
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
