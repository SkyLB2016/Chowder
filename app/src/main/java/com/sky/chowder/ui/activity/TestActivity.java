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

// 回复类（补充HP、解除异常状态）共8种
//         大回复术　　　◆水　水６　　　　　　　回复　单体　　４０　　HP2000回复　　　　　0.6
//         中回复术·复　◆水　水５空２　　　　　回复　中圆３　５０　　HP1000回复　　　　　0.7
//         治愈术·复　　◆水　水８幻４空２　　　回复　大圆５　３０　　状态异常解除　　　　１
//         复苏术　　　　◆水　水４地２幻１　　　回复　单体　　１０　　战斗不能解除+HP回复 0.5
//
//         大地之墙　　　◆地　地４　　　　　　　辅助　小圆２　３０　　完全防御１次　　　　１
//         时间加速·改　◆时　时９　　　　　　　辅助　单体　　３０　　ＳＰＤ＋５０％　　　１
//
//         泰坦之咆哮　　◆地　地８空４　　　　　攻击　全体　　５０　　　　　　　　　10　　２
//         钻石星尘　　　◆水　水４风２空１　　　攻击　小圆２◇４０　　20％几率冻结　１　　1.5
//         火山之咆哮　　◆火　火８空２地４　　　攻击　中圆３　５０　　　　　　　　　１　　２
//         等离子之波　　◆风　风８空４　　　　　攻击　直线２◇５０　　20％几率封技　１　　1.5
//         暗影之矛　　　◆时　时５　　　　　　　攻击　单体　　２０　　20％几率即死　１　　１
//         地狱之门　　　◆时　时４空２幻１　　　攻击　小圆２　３０　　20％几率气绝　１　　１
//         炽炎地狱　　　◆时　时８空４幻２　　　攻击　中圆３◇４０　　20％几率气绝　１　　２