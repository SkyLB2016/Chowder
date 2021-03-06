package com.sky.oa.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.sky.oa.model.AreaEntity
import com.sky.oa.R
import com.sky.oa.gson.GsonUtils
import com.sky.sdk.utils.FileUtils
import com.sky.sdk.utils.ScreenUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_address.*

/**
 * Created by SKY on 2017/7/12.
 */
class AddressFragment : DialogFragment() {
    private var province = ""//省
    private var city = ""//市
    private var county = ""//区
    private var disOnClick: OnDismissListener? = null

    lateinit var onClick: OnClickListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater!!.inflate(R.layout.fragment_address, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view!!, savedInstanceState)
        initData()
    }

    private lateinit var provinceA: Array<AreaEntity>
    private lateinit var provinceStr: Array<String?>
    private lateinit var cityA: Array<AreaEntity>
    private lateinit var cityStr: Array<String?>
    private lateinit var countyA: Array<AreaEntity>
    private lateinit var countyStr: Array<String?>
    private fun initData() {
//        Observable.just(FileUtils.readAssestToChar(activity, "address.json"))
        Observable.just(FileUtils.readInput(activity?.assets?.open("address.json")))
            .map { s -> GsonUtils.fromJson(s, Array<AreaEntity>::class.java) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { areaEntities ->
                provinceA = areaEntities
                setAddress()
            }
        tvLeft.setOnClickListener { dismiss() }
        tvRight.setOnClickListener {
            onClick?.onClick("$province-$city-$county")
            dismiss()
        }

    }

    private fun setAddress() {
        provinceStr = arrayOfNulls<String>(provinceA.size)
        for (i in provinceA.indices) {
            provinceStr[i] = provinceA[i].name
        }

        npProvince.displayedValues = provinceStr
        setNpValue(npProvince, provinceStr.size - 1, 0, 0)
        province = "${provinceStr[0]}"
        npProvince.setOnValueChangedListener { _, _, newVal ->
            province = "${provinceStr[newVal]}"
            cityA = provinceA[newVal].items!!.toTypedArray()
            cityStr = arrayOfNulls<String>(cityA.size)
            for (i in cityA.indices) {
                cityStr[i] = cityA[i].name
            }
            npCity.maxValue = 0
            npCity.displayedValues = cityStr
            setNpValue(npCity, cityStr.size - 1, 0, 0)
            city = "${cityStr[0]}"

            countyA = cityA[0].items!!.toTypedArray()
            if (countyA.size === 0) {
                countyStr = arrayOfNulls<String>(1)
                countyStr[0] = "无"
            } else {
                countyStr = arrayOfNulls<String>(countyA.size)
                for (i in countyA.indices) {
                    countyStr[i] = countyA[i].name
                }
            }
            npCounty.maxValue = 0
            npCounty.displayedValues = countyStr
            setNpValue(npCounty, countyStr.size - 1, 0, 0)
            county = countyStr[0]!!
        }

        cityA = provinceA[0].items!!.toTypedArray()
        cityStr = arrayOfNulls<String>(cityA.size)
        for (i in cityA.indices) {
            cityStr[i] = cityA[i].name
        }
        npCity.displayedValues = cityStr
        setNpValue(npCity, cityStr.size - 1, 0, 0)
        city = "${cityStr[0]}"
        npCity.setOnValueChangedListener { _, _, newVal ->
            city = "${cityStr[newVal]}"
            countyA = cityA[newVal].items!!.toTypedArray()
            if (countyA?.size === 0) {
                countyStr = arrayOfNulls<String>(1)
                countyStr[0] = ""
            } else {
                countyStr = arrayOfNulls<String>(countyA.size)
                for (i in countyA.indices) {
                    countyStr[i] = countyA[i].name
                }
            }
            npCounty.maxValue = 0
            npCounty.displayedValues = countyStr
            setNpValue(npCounty, countyStr.size - 1, 0, 0)
            county = countyStr[0]!!
        }


        countyA = cityA[0].items!!.toTypedArray()
        countyStr = arrayOfNulls<String>(countyA.size)
        for (i in countyA.indices) {
            countyStr[i] = countyA[i].name
        }
        npCounty.displayedValues = countyStr
        setNpValue(npCounty, countyStr.size - 1, 0, 0)
        county = countyStr[0]!!
        npCounty.setOnValueChangedListener { _, _, newVal -> county = countyStr[newVal]!! }
    }

    private fun setNpValue(np: NumberPicker, max: Int, min: Int, value: Int) {
        np.maxValue = max
        np.minValue = min
        np.value = value
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ScreenUtils.getWidthPX(activity) / 8 * 7, dialog?.window?.attributes!!.height)
    }

    override fun onDismiss(dialog: DialogInterface) {
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
