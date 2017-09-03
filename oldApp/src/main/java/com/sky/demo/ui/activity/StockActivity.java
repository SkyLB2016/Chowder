package com.sky.demo.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sky.demo.R;
import com.sky.demo.ui.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;

/**
 * @author sky QQ:1136096189
 * @Description: TODO
 * @date 16/4/13 下午1:24
 */
@ContentView(R.layout.activity_stock)
public class StockActivity extends BaseActivity {

    @ViewInject(R.id.btn_buy)
    private Button buy;
    @ViewInject(R.id.btn_sell)
    private Button sell;

    @ViewInject(R.id.et_unit)
    private EditText etUnit;//单价
    @ViewInject(R.id.et_num)
    private EditText etNum;//数量
    @ViewInject(R.id.et_total)
    private EditText etTotal;//总价
    @ViewInject(R.id.btn_ok)
    private Button btnOk;
    @ViewInject(R.id.tv_stamp_duty)
    private TextView tvStampDuty;//印花税,只在卖出的时候有
    @ViewInject(R.id.tv_real_estate_transfer_tax)
    private TextView tvRealEstateTransferTax;//过户税
    @ViewInject(R.id.tv_brokerage_commission)
    private TextView tvBrokerageCommission;//券商佣金
    @ViewInject(R.id.tv_total_counter_fee)
    private TextView tvTotalCounterFee;//总手续费
    @ViewInject(R.id.tv_total_buyOrSell)
    private TextView totalBuyOrSell;//买还是卖
    @ViewInject(R.id.tv_total_bs)
    private TextView totalBS;//收入与支出

    private String buyOrSell = "1";//1是买,2是卖

    private String type = "1";//1是单价与数量算总价,2是直接填写的总价


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle();
        checkTextChange(etUnit);
        checkTextChange(etNum);
        etTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 1)
                    calculationCounter(Double.parseDouble(s.toString()));
                else calculationCounter(0);
            }
        });
    }

    public void checkTextChange(EditText one) {
        one.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable text) {
                setTotal();
            }
        });
    }


    @Event({R.id.btn_buy, R.id.btn_sell, R.id.btn_ok})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_buy:
                buyOrSell = "1";
                break;
            case R.id.btn_sell:
                buyOrSell = "2";
                break;
            case R.id.btn_ok:
                break;
        }
        setTotal();
    }

    private void setTotal() {
        String str_unit = etUnit.getText().toString().trim();
        String str_num = etNum.getText().toString().trim();
        if (str_unit == null || str_unit.length() == 0) str_unit = "0";
        double unit = Double.parseDouble(str_unit);

        if (str_num == null || str_num.length() == 0) str_num = "0";
        double num = Double.parseDouble(str_num);
        String str_total = etTotal.getText().toString().trim();

        double total = 0;
        if ((unit == 0 || num == 0) && str_total != null && str_total.length() >= 1)
            total = Double.parseDouble(str_total);
        else total = unit * num;

        etTotal.setText(total + "");//股票总价
        calculationCounter(total);
    }

    private void calculationCounter(double total) {
        double stampDuty = 0;//印花税,只在卖出的时候有
        if (buyOrSell.equals("2")) stampDuty = round(total * 0.001);
        tvStampDuty.setText(stampDuty + "元");//印花税

        //过户税
        double realEstateTransferTax = round(total * 0.00002);
        tvRealEstateTransferTax.setText(realEstateTransferTax + "元");

        //券商佣金
        double brokerageCommission = round(total * 0.00025);
        if (total != 0 && brokerageCommission < 5) brokerageCommission = 5;
        tvBrokerageCommission.setText(brokerageCommission + "元");

        //总手续费
        double totalMoney = stampDuty + realEstateTransferTax + brokerageCommission;
        tvTotalCounterFee.setText(round(totalMoney) + "元");

        if (buyOrSell.equals("2")) {//卖
            totalBuyOrSell.setText("实际收入:");
            totalBS.setText(total - totalMoney + "元");//实际的收入
        } else {//买
            totalBuyOrSell.setText("实际支出:");
            totalBS.setText(total + totalMoney + "元");//实际支出
        }


    }

    public double round(double num) {
        return num + 0.005 >= getDecimalFormat(num) + 0.01
                ? getDecimalFormat(num) + 0.01
                : getDecimalFormat(num);
    }

    @NonNull
    private double getDecimalFormat(double num) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return Double.parseDouble(df.format(num));
    }
}
