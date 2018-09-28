package com.sky.chowder.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sky.chowder.R;
import com.sky.chowder.ui.widget.calendar.CalendarView;
import com.sky.chowder.ui.widget.calendar.common.CalendarMode;
import com.sky.chowder.ui.widget.calendar.impl.OnClickListener;
import com.sky.chowder.ui.widget.calendar.impl.OnInitListener;
import com.sky.chowder.ui.widget.calendar.selecttime.DayInfo;
import com.sky.chowder.ui.widget.calendar.selecttime.SelectTime;
import com.sky.chowder.ui.widget.calendar.selecttime.WeekInfo;
import com.sky.chowder.ui.widget.calendar.selecttime.YearInfo;
import com.sky.utils.LogUtils;
import com.sky.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by libin on 2018/9/6 上午10:41.
 */
public class TestActivity extends AppCompatActivity {

    CalendarView calendar;
    CalendarMode mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        calendar = findViewById(R.id.calendar);
        mode = CalendarMode.MONTH;
        calendar.setOnInitListener(new OnInitListener() {
            @Override
            public void onInitBefore(CalendarView view) {
                SelectTime.getInstance().setSelectTime(mode);
                Calendar cal = Calendar.getInstance();
                view.setTime(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), mode);
                SelectTime.getInstance().setToday(view.getTodayInfo());
            }

            @Override
            public void onInitFinished(CalendarView view) {
                if (mode == CalendarMode.MONTH) {
                    SelectTime.getInstance().setLimit(40);
                    view.getMonthLayerManager().getLayer().setOnClickListener(hint);
                } else if (mode == CalendarMode.WEEK) {
                    SelectTime.getInstance().setLimit(4);
                    view.getWeekLayerManager().getLayer().setOnClickListener(hint);
                } else if (mode == CalendarMode.YEAR) {
                    SelectTime.getInstance().setLimit(2);
                    view.getYearLayerManager().getLayer().setOnClickListener(hint);
                }
            }
        });

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode == CalendarMode.MONTH) {
                    selectDay();
                } else if (mode == CalendarMode.WEEK) {
                    selectWeek();
                } else if (mode == CalendarMode.YEAR) {
                    selectMonth();
                }
            }
        });
    }

    OnClickListener hint = new OnClickListener() {
        @Override
        public void onClick(String hint) {
            ToastUtils.showShort(TestActivity.this, hint);
        }
    };

    private void selectMonth() {
        Map<String, List<YearInfo>> maps = (Map<String, List<YearInfo>>) SelectTime.getInstance().getSelectTime().getSelectMap();
        Set<Map.Entry<String, List<YearInfo>>> sets = maps.entrySet();
        ArrayList<YearInfo> all = new ArrayList<>();
        for (Map.Entry<String, List<YearInfo>> map : sets) {
            map.getKey();
            all.addAll(map.getValue());
//                    list = map.getValue();
        }
        Collections.sort(all, new Comparator<YearInfo>() {
            @Override
            public int compare(YearInfo o1, YearInfo o2) {
                return o1.getLable().compareTo(o2.getLable());
            }
        });
        for (int i = 0; i < all.size(); i++) {
            LogUtils.i(all.get(i).getLable());
        }
    }

    private void selectWeek() {
        Map<String, List<WeekInfo>> maps = (Map<String, List<WeekInfo>>) SelectTime.getInstance().getSelectTime().getSelectMap();
        Set<Map.Entry<String, List<WeekInfo>>> sets = maps.entrySet();
        ArrayList<WeekInfo> all = new ArrayList<>();
        for (Map.Entry<String, List<WeekInfo>> map : sets) {
            map.getKey();
            all.addAll(map.getValue());
//                    list = map.getValue();
        }
        Collections.sort(all, new Comparator<WeekInfo>() {
            @Override
            public int compare(WeekInfo o1, WeekInfo o2) {
                return o1.getFormatDate().compareTo(o2.getFormatDate());
            }
        });
        for (int i = 0; i < all.size(); i++) {
            LogUtils.i(all.get(i).getFormatDate());
        }
    }

    private void selectDay() {
        Map<String, List<DayInfo>> maps = (Map<String, List<DayInfo>>) SelectTime.getInstance().getSelectTime().getSelectMap();
        Set<Map.Entry<String, List<DayInfo>>> sets = maps.entrySet();
        List<DayInfo> all = new ArrayList<>();
        for (Map.Entry<String, List<DayInfo>> map : sets) {
            map.getKey();
            all.addAll(map.getValue());
//                    list = map.getValue();
        }
//                Collections.sort(all, new Comparator<DayInfo>() {
//                    @Override
//                    public int compare(DayInfo o1, DayInfo o2) {
//                        return Collator.getInstance().compare(o1.getLable(), o2.getLable());
//                    }
//                });
//                for (int i = 0; i < all.size(); i++) {
//                    LogUtils.i(all.get(i).getLable());
//                }
        Collections.sort(all, new Comparator<DayInfo>() {
            @Override
            public int compare(DayInfo o1, DayInfo o2) {
                return o1.getLable().compareTo(o2.getLable());
            }
        });
        for (int i = 0; i < all.size(); i++) {
            LogUtils.i(all.get(i).getLable());
        }
    }

}
