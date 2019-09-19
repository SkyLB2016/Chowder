package com.sky.chowder.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.chowder.R;
import com.sky.chowder.model.PCommon;
import com.sky.chowder.ui.widget.WorkerColumnHorizontalView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by libin on 2019/09/02 18:25 Monday.
 */
public class WorkerChartActivity extends AppCompatActivity {
    public static final String CUSTOM_TITLE = "custom_title";
    public static final String CUSTOM_TYPE = "custom_type";
    public static final int ENTRY = 1;
    //    public static final int TO_WORK = 2;
//    public static final int QUIT = 3;
    public static final int IN_OFFICE_AGE = 4;
//    public static final int EDUCATION = 5;
//    public static final int AGE = 6;


    @BindView(R.id.tv_worker_switch)
    TextView tvWorkerSwitch;
    @BindView(R.id.worker_chart)
    WorkerColumnHorizontalView workerChart;
    @BindView(R.id.linear_biaoge)
    LinearLayout linearBiaoge;

    private boolean workerSwitch = true;//默认图表

    public static void toWorkerChart(Context context, String title) {
        Intent intent = new Intent(context, WorkerChartActivity.class);
        intent.putExtra(CUSTOM_TITLE, title);
        context.startActivity(intent);
    }

    public static void toWorkerChart(Context context, String title, int type) {
        Intent intent = new Intent(context, WorkerChartActivity.class);
        intent.putExtra(CUSTOM_TITLE, title);
        intent.putExtra(CUSTOM_TYPE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workerchart);
        ButterKnife.bind(this);

//        int type = getIntent().getExtras().getInt(CUSTOM_TYPE, 1);
//        if (type == 1) {
//            tvWorkerSwitch.setVisibility(View.GONE);
//            workerChart.setVisibility(View.GONE);
//            linearBiaoge.setVisibility(View.VISIBLE);
//
//        } else {
//            tvWorkerSwitch.setVisibility(View.VISIBLE);
//            workerChart.setVisibility(View.VISIBLE);
//            linearBiaoge.setVisibility(View.GONE);
        setChart();
//        }
    }

    private void setChart() {
        String title = getIntent().getExtras().getString(CUSTOM_TITLE);
        tvWorkerSwitch.setText(title);
        setData();
        setWorkerBiaoGe();
        setWorkerChart();

        findViewById(R.id.tv_worker_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workerSwitch) {
                    workerChart.setVisibility(View.GONE);
                    linearBiaoge.setVisibility(View.VISIBLE);
                    tvWorkerSwitch.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.ic_worker_chart, 0);
                } else {
                    workerChart.setVisibility(View.VISIBLE);
                    linearBiaoge.setVisibility(View.GONE);
                    tvWorkerSwitch.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.mipmap.ic_worker_biao, 0);
                }
                workerSwitch = !workerSwitch;
            }
        });
    }

    List<PCommon> workers = new ArrayList<>();//员工集合

    private void setData() {
        workers.add(new PCommon("3个月内",50));
        workers.add(new PCommon("6个月内",98));
        workers.add(new PCommon("6个月内-1年",78));
        workers.add(new PCommon("1年-3年",60));
        workers.add(new PCommon("3年-5年",60));
        workers.add(new PCommon("5年-10年",60));
        workers.add(new PCommon("10年以上",60));
        workers.add(new PCommon("未填写",60));
        workerChart.setData(workers);
    }

    private void setWorkerChart() {
        workerChart.setData(workers);
    }

    private void setWorkerBiaoGe() {
        int max = 0;
        for (int i = 0; i < workers.size(); i++) {
            max += workers.get(i).getValue();
        }
        View view;
        LinearLayout linear;
        TextView tv01;
        TextView tv02;
        TextView tv03;
        PCommon worker;
        for (int i = 0; i < workers.size(); i++) {
            view = LayoutInflater.from(this).inflate(R.layout.adapter_hrm, null);
            linear = view.findViewById(R.id.linear);
            tv01 = view.findViewById(R.id.tv01);
            tv02 = view.findViewById(R.id.tv02);
            tv03 = view.findViewById(R.id.tv03);
            if (i == 0) {
                linear.setShowDividers(0);
                linear.setBackgroundColor(getResources().getColor(R.color.color_F0F0F0));
                tv01.setText("司龄");
                tv02.setText("人数");
                tv03.setText("占比");
            } else {
                worker = workers.get(i);
                tv01.setText(worker.getName());
                tv02.setText("" + worker.getValue());
                tv03.setText(saveTwo(worker.getValue() * 100.0 / max) + "%");
            }
            linearBiaoge.addView(view);
        }
    }

    public String saveTwo(double num) {
        return new DecimalFormat("#0.00").format(num);
    }


}
