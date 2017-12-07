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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //去掉默认的标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false
        val view = inflater!!.inflate(R.layout.fragment_address, container)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    private lateinit var provinceA: Array<AreaEntity>
    private lateinit var provinceStr: Array<String?>
    private lateinit var cityA: Array<AreaEntity>
    private lateinit var cityStr: Array<String?>
    private lateinit var countyA: Array<AreaEntity>
    private lateinit var countyStr: Array<String?>
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
        //        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.window.setLayout(ScreenUtils.getWidthPX(activity) / 8 * 7, dialog.window.attributes.height)
    }

    @OnClick(R.id.tvLeft, R.id.tvRight)
    fun onClick(view: View?) {
        when (view?.id) {
//            R.id.tvLeft -> dismiss()
            R.id.tvRight -> onClick?.onClick("$province-$city-$county")
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
