package com.sky.chowder.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sky.chowder.R;
import com.sky.chowder.ui.widget.calendar.CalendarView;
import com.sky.chowder.ui.widget.calendar.impl.OnInitListener;
import com.sky.chowder.ui.widget.calendar.layer.MonthLayer;
import com.sky.utils.LogUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by libin on 2018/9/6 上午10:41.
 */
public class TestActivity extends AppCompatActivity {

    CalendarView calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        calendar = findViewById(R.id.calendar);
        calendar.setOnInitListener(new OnInitListener() {
            @Override
            public void onInitBefore(CalendarView view) {
                Calendar cal = Calendar.getInstance();
                view.setTime(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            }

            @Override
            public void onInitFinished(CalendarView view) {

            }
        });

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MonthLayer.SelectDay> list = calendar.getMonthLayerManager().getLayer().getSelectDayList();
                for (int i = 0; i < list.size(); i++) {
                    LogUtils.i(list.get(i).lable);
                }

            }
        });
    }

}
