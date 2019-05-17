package com.sky.chowder.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.sky.chowder.R;
import com.sky.chowder.ui.widget.Progress;

import java.util.List;

/**
 * Created by libin on 2018/9/6 上午10:41.
 */
public class TestActivity extends AppCompatActivity {
    TextView tv;
    Progress progress;
    private ListView lv;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);
        tv = findViewById(R.id.tv_receive_progress_text);
        progress = findViewById(R.id.tv_receive_progress);
        tv.post(new Runnable() {
            @Override
            public void run() {
                int width = tv.getWidth();
                progress.setLayoutWidth(width);
                progress.setLayoutHeight(width);
                progress.setProgress(60);
                progress.setCircleOutColor(getResources().getColor(R.color.color_1AB394));
                progress.invalidate();
            }
        });
//        lv = (ListView) findViewById(R.id.lv);
//        list = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            list.add("第" + (i + 1) + "项数据！");
//        }
//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
//        lv.setAdapter(adapter);
    }

}