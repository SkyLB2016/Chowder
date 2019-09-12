package com.sky.chowder.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sky.chowder.R;
import com.sky.chowder.model.WorkerMonthModel;
import com.sky.chowder.model.WorkerTypeModel;
import com.sky.chowder.ui.widget.WorkerCircleView;
import com.sky.chowder.ui.widget.WorkerColumnView;
import com.sky.chowder.ui.widget.WorkerView;
import com.sky.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libin on 2018/9/6 上午10:41.
 */
public class TestActivity extends AppCompatActivity {
    @BindView(R.id.tv_attendance_statistics)
    TextView tvAttendanceStatistics;//出勤页签
    @BindView(R.id.linear_attendance)
    LinearLayout linearAttendance;//出勤统计
    @BindView(R.id.linear_leave)
    LinearLayout linearLeave;//请假统计
    @BindView(R.id.linear_over_time)
    LinearLayout linearOverTime;//加班统计
    @BindView(R.id.linear_rest)
    LinearLayout linearRest;//调休统计

    @BindView(R.id.tv_worker_info)
    TextView tvWorkerInfo;//人员动态
    @BindView(R.id.tv_entry)
    TextView tvEntry;//入职
    @BindView(R.id.tv_toworker)
    TextView tvToworker;//转正
    @BindView(R.id.tv_quit)
    TextView tvQuit;//离职
    //    @BindView(R.id.tv_worker_num)
//    TextView tvWorkerNum;
//    @BindView(R.id.tv_date_zhouqi_title)
//    TextView tvDateZhouqiTitle;
    @BindView(R.id.tv_date_zhouqi)
    TextView tvDateZhouqi;//人员走势时间
    @BindView(R.id.workerView)
    WorkerView workerView;//人员走势时间

    @BindView(R.id.tv_worker)
    TextView tvWorker;//在职
    @BindView(R.id.tv_worker_offical)
    TextView tvWorkerOffical;//正式
    @BindView(R.id.tv_worker_on_trial)
    TextView tvWorkerOnTrial;//试用
    @BindView(R.id.tv_worker_dispatch)
    TextView tvWorkerDispatch;//派遣

    @BindView(R.id.worker_in_office_age)
    WorkerColumnView workerInOfficeAge;//司龄分布
    @BindView(R.id.worker_education)
    WorkerColumnView workerEducation;//学历分布
    @BindView(R.id.worker_age)
    WorkerColumnView workerAge;//年龄分布
    @BindView(R.id.worker_gender)
    WorkerCircleView workerGender;//性别比例
    @BindView(R.id.worker_marriage)
    WorkerCircleView workerMarriage;//结婚比例
    @BindView(R.id.scroller)
    ScrollView scroller;

    private List<WorkerTypeModel> workers = new ArrayList<>();
    private List<WorkerMonthModel> workerMonths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        setData();

        setAttendanceStatistics();
        setLeaveStatistics();
        setOverTimeStatistics();
        setRestStatistics();
    }

    private void setData() {
        WorkerTypeModel worker = new WorkerTypeModel();
        worker.setValue(50);
        workers.add(worker);
        WorkerTypeModel worker02 = new WorkerTypeModel();
        worker02.setValue(98);
        workers.add(worker02);
        WorkerTypeModel worker03 = new WorkerTypeModel();
        worker03.setValue(78);
        workers.add(worker03);
        WorkerTypeModel worker04 = new WorkerTypeModel();
        worker04.setValue(60);
        workers.add(worker04);

        workerInOfficeAge.setWorkers(workers);
        workerEducation.setWorkers(workers);
        workerAge.setWorkers(workers);
        workerGender.setProgress(30);
        workerMarriage.setProgress(60);
//        workerMarriage.invalidate();


        WorkerMonthModel workerMonth = new WorkerMonthModel();
        workerMonth.setDate("2019.01");
        workerMonth.setWorker(388);
        workerMonth.setEntryWorker(10);
        workerMonth.setQuitWorker(2);
        workerMonths.add(workerMonth);
        WorkerMonthModel workerMonth02 = new WorkerMonthModel();
        workerMonth02.setDate("2019.02");
        workerMonth02.setWorker(398);
        workerMonth02.setEntryWorker(20);
        workerMonth02.setQuitWorker(12);
        workerMonths.add(workerMonth02);
        WorkerMonthModel workerMonth03 = new WorkerMonthModel();
        workerMonth03.setDate("2019.03");
        workerMonth03.setWorker(378);
        workerMonth03.setEntryWorker(10);
        workerMonth03.setQuitWorker(20);
        workerMonths.add(workerMonth03);
        WorkerMonthModel workerMonth04 = new WorkerMonthModel();
        workerMonth04.setDate("2019.04");
        workerMonth04.setWorker(370);
        workerMonth04.setEntryWorker(11);
        workerMonth04.setQuitWorker(23);
        workerMonths.add(workerMonth04);
        WorkerMonthModel workerMonth05 = new WorkerMonthModel();
        workerMonth05.setDate("2019.05");
        workerMonth05.setWorker(366);
        workerMonth05.setEntryWorker(8);
        workerMonth05.setQuitWorker(2);
        workerMonths.add(workerMonth05);
        WorkerMonthModel workerMonth06 = new WorkerMonthModel();
        workerMonth06.setDate("2019.06");
        workerMonth06.setWorker(360);
        workerMonth06.setEntryWorker(9);
        workerMonth06.setQuitWorker(7);
        workerMonths.add(workerMonth06);
        workerView.setWorkers(workerMonths);
    }

    /**
     * 考勤统计
     */
    private void setAttendanceStatistics() {
        View view;
        LinearLayout linear;
        TextView tv01;
        TextView tv02;
        TextView tv03;
        for (int i = 0; i < 4; i++) {
            view = LayoutInflater.from(this).inflate(R.layout.adapter_hrm, null);
            linear = view.findViewById(R.id.linear);
            tv01 = view.findViewById(R.id.tv01);
            tv02 = view.findViewById(R.id.tv02);
            tv03 = view.findViewById(R.id.tv03);

            if (i == 0) {
                linear.setShowDividers(0);
                linear.setBackgroundColor(getResources().getColor(R.color.color_F0F0F0));
                tv01.setText("统计维度");
                tv02.setText("人数");
                tv03.setText("次数");
            } else {
                tv01.setText("年假");
                tv02.setText("8");
                tv03.setText("8");
            }
            linearAttendance.addView(view);
        }
    }

    /**
     * 请假统计
     */
    private void setLeaveStatistics() {
        View view;
        LinearLayout linear;
        TextView tv01;
        TextView tv02;
        TextView tv03;
        for (int i = 0; i < 4; i++) {
            view = LayoutInflater.from(this).inflate(R.layout.adapter_hrm, null);
            linear = view.findViewById(R.id.linear);
            tv01 = view.findViewById(R.id.tv01);
            tv02 = view.findViewById(R.id.tv02);
            tv03 = view.findViewById(R.id.tv03);

            if (i == 0) {
                linear.setShowDividers(0);
                linear.setBackgroundColor(getResources().getColor(R.color.color_F0F0F0));
                tv01.setText("统计维度");
                tv02.setText("人数");
                tv03.setText("小时/天");
            } else {
                tv01.setText("年假");
                tv02.setText("8");
                tv03.setText("8");
            }
            linearLeave.addView(view);
        }
    }

    /**
     * 加班统计
     */
    private void setOverTimeStatistics() {
        View view;
        LinearLayout linear;
        TextView tv01;
        TextView tv02;
        TextView tv03;
        for (int i = 0; i < 4; i++) {
            view = LayoutInflater.from(this).inflate(R.layout.adapter_hrm, null);
            linear = view.findViewById(R.id.linear);
            tv01 = view.findViewById(R.id.tv01);
            tv02 = view.findViewById(R.id.tv02);
            tv03 = view.findViewById(R.id.tv03);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv02.getLayoutParams();
            lp.weight = 1.5F;
            tv02.setLayoutParams(lp);

            if (i == 0) {
                linear.setShowDividers(0);
                linear.setBackgroundColor(getResources().getColor(R.color.color_F0F0F0));
                tv01.setText("员工");
                tv02.setText("加班时间");
                tv03.setText("小时/天");
            } else {
                tv01.setText("年假");
                tv02.setText("2019.6.15-2019.6.16");
                tv03.setText("8");
            }
            linearOverTime.addView(view);
        }
    }

    /**
     * 调休统计
     */
    private void setRestStatistics() {
        View view;
        LinearLayout linear;
        TextView tv01;
        TextView tv02;
        TextView tv03;
        for (int i = 0; i < 4; i++) {
            view = LayoutInflater.from(this).inflate(R.layout.adapter_hrm, null);
            linear = view.findViewById(R.id.linear);
            tv01 = view.findViewById(R.id.tv01);
            tv02 = view.findViewById(R.id.tv02);
            tv03 = view.findViewById(R.id.tv03);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv02.getLayoutParams();
            lp.weight = 2;
            tv02.setLayoutParams(lp);

            if (i == 0) {
                linear.setShowDividers(0);
                linear.setBackgroundColor(getResources().getColor(R.color.color_F0F0F0));
                tv01.setText("员工");
                tv02.setText("加班时间");
                tv03.setText("小时/天");
            } else {
                tv01.setText("年假");
                tv02.setText("6.15 13:00-6.16 12:00");
                tv03.setText("8");
            }
            linearRest.addView(view);
        }
    }

    @OnClick({R.id.linear_entry, R.id.linear_worker, R.id.linear_quit,
            R.id.tv_over_time_more, R.id.tv_rest_more, R.id.worker_in_office_age, R.id.worker_education, R.id.worker_age})
    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.linear_entry:
                break;
            case R.id.linear_worker:
                break;
            case R.id.linear_quit:
                break;
            case R.id.tv_over_time_more:
                break;
            case R.id.tv_rest_more:
                break;
            case R.id.worker_in_office_age:
                WorkerChartActivity.toWorkerChart(this, "在职员工司龄分布");
                break;
            case R.id.worker_education:
                WorkerChartActivity.toWorkerChart(this, "在职员工学历分布");
                break;
            case R.id.worker_age:
                WorkerChartActivity.toWorkerChart(this, "在职员工年龄分布");
                break;
        }
    }

}