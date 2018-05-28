package com.sky.chowder.ui.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.sky.api.TextWatcherAdapter
import com.sky.chowder.R
import com.sky.utils.TextUtil
import common.base.BaseNoPActivity
import kotlinx.android.synthetic.main.activity_stock.*

/**
 * Created by SKY on 2016/4/13.
 * 股票计算
 */
class StockActivity : BaseNoPActivity() {
    //etUnit:单价; etNum:数量; etTotal:总价;tvStampDuty:印花税,只在卖出的时候有
    //tvRealEstateTransferTax:过户税;tvBrokerageCommission:券商佣金;tvTotalCounterFee:总手续费
    //totalBuyOrSell:买还是卖;totalBS:收入与支出

    private var buyOrSell = "1"//1是买,2是卖
    private val type = "1"//1是单价与数量算总价,2是直接填写的总价

    override fun getLayoutResId() = R.layout.activity_stock
    override fun initialize(savedInstanceState: Bundle?) {
        checkTextChange(etUnit)
        checkTextChange(etNum)
        etTotal?.addTextChangedListener(object : TextWatcherAdapter() {
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) calculationCounter(s.toString().toDouble())
                else calculationCounter(0.0)
            }
        })
        btnBuy.setOnClickListener {
            buyOrSell = "1"
            setTotal()
        }
        btnSell.setOnClickListener {
            buyOrSell = "2"
            setTotal()
        }
    }
    override fun loadData() = Unit

    private fun checkTextChange(one: EditText?) {
        one?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(text: Editable) {
                setTotal()
            }
        })
    }

    private fun setTotal() {
        val strUnit = TextUtil.getText(etUnit)
        val strNum = TextUtil.getText(etNum)
        val strTotal = TextUtil.getText(etTotal)

        val unit = if (strUnit.isNotEmpty()) strUnit?.toDouble() else 0.0
        val num = if (strNum.isNotEmpty()) strNum?.toDouble() else 0.0

        var total = if ((unit == 0.0 || num == 0.0) && strTotal?.isNotEmpty() == true)
            strTotal.toDouble()
        else unit!! * num!!

        etTotal?.setText(total.toString())//股票总价
        calculationCounter(total)
    }

    private fun calculationCounter(total: Double) {
        var stampDuty = 0.0//印花税,只在卖出的时候有
        if (buyOrSell == "2") stampDuty = round(total * 0.001)
        tvStampDuty?.text = stampDuty.toString() + "元"//印花税

        //过户税
        val realEstateTransferTax = round(total * 0.00002)
        tvRealEstateTransferTax?.text = realEstateTransferTax.toString() + "元"

        //券商佣金
        var brokerageCommission = round(total * 0.00025)
        if (total != 0.0 && brokerageCommission < 5) brokerageCommission = 5.0
        tvBrokerageCommission?.text = brokerageCommission.toString() + "元"

        //总手续费
        val totalMoney = stampDuty + realEstateTransferTax + brokerageCommission
        tvTotalCounterFee?.text = round(totalMoney).toString() + "元"

        if (buyOrSell == "2") {//卖
            totalBuyOrSell?.text = "实际收入:"
            totalBS?.text = (total - totalMoney).toString() + "元"//实际的收入
        } else {//买
            totalBuyOrSell?.text = "实际支出:"
            totalBS?.text = (total + totalMoney).toString() + "元"//实际支出
        }
    }

    private fun round(num: Double): Double =
            if (num + 0.005 >= getDecimalFormat(num) + 0.01) getDecimalFormat(num) + 0.01
            else getDecimalFormat(num)


    private fun getDecimalFormat(num: Double) = TextUtil.formatDou().format(num).toDouble()
}
