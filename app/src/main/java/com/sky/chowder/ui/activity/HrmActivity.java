package com.sky.chowder.ui.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.sky.chowder.R;
import com.sky.chowder.model.PCommon;
import com.sky.chowder.ui.widget.WorkerCircleView;
import com.sky.chowder.ui.widget.WorkerColumnView;
import com.sky.chowder.ui.widget.WorkerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libin on 2018/9/6 上午10:41.
 */
public class HrmActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hrm);
        ButterKnife.bind(this);
        setData();

        setAttendanceStatistics();
        setLeaveStatistics();
        setOverTimeStatistics();
        setRestStatistics();
    }

    private void setData() {
        workerInOfficeAge.setData(getWorkers(24, 18, 12, 6));
        workerEducation.setData(getWorkers(12, 18, 24, 18));
        workerAge.setData(getWorkers(6, 12, 18, 24));
        List<PCommon> data = new ArrayList<>();
        data.add(new PCommon("未婚", 10));
        data.add(new PCommon("已婚", 15));
        data.add(new PCommon("离异", 5));
        data.add(new PCommon("丧偶", 20));

        workerGender.setData(data);
        workerMarriage.setData(data);


        List<PCommon> workers = new ArrayList<>();
        workers.add(new PCommon(2019, 1, 388, 10, 2));
        workers.add(new PCommon(2019, 2, 398, 20, 12));
        workers.add(new PCommon(2019, 3, 378, 10, 20));
        workers.add(new PCommon(2019, 4, 370, 11, 23));
        workers.add(new PCommon(2019, 5, 366, 8, 2));
        workers.add(new PCommon(2019, 6, 360, 9, 7));
        workerView.setWorkers(workers);
    }

    private List<PCommon> getWorkers(int... num) {
        List<PCommon> list = new ArrayList<>();
        for (int i = 0; i < num.length; i++) {
            list.add(new PCommon(num[i]));
        }
        return list;
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
    public void onClick(View v) {
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