package com.sky.oa.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.oa.R;
import com.sky.oa.model.PCommon;
import com.sky.oa.widget.WorkerColumnHorizontalView;

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


    @BindView(R.id.tv_worker_switch)
    TextView tvWorkerSwitch;
    @BindView(R.id.worker_chart)
    WorkerColumnHorizontalView workerChart;

    public static void toWorkerChart(Context context, String title) {
        Intent intent = new Intent(context, WorkerChartActivity.class);
        intent.putExtra(CUSTOM_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workerchart);
        ButterKnife.bind(this);
        String title = getIntent().getExtras().getString(CUSTOM_TITLE);
        tvWorkerSwitch.setText(title);
        setWorkerChart();
    }

    List<PCommon> workers = new ArrayList<>();//员工集合
    private void setWorkerChart() {
        workers.add(new PCommon("3个月内",50));
        workers.add(new PCommon("6个月内",98));
        workers.add(new PCommon("6个月内-1年",78));
        workers.add(new PCommon("1年-3年",60));
        workers.add(new PCommon("3年-5年",60));
        workers.add(new PCommon("5年-10年",60));
        workers.add(new PCommon("10年以上",60));
        workers.add(new PCommon("未填写",60));
        workerChart.setData(workers);
        workerChart.setData(workers);
    }
}
