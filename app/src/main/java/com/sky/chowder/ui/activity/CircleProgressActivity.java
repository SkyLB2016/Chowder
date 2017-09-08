package com.sky.chowder.ui.activity;

import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.sky.base.BaseNoPActivity;
import com.sky.chowder.R;

/**
 * Created by sky on 15/12/9 下午8:54.
 * 自定义进度条布局,与textview字体大小颜色背景设置
 */
public class CircleProgressActivity extends BaseNoPActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_circleprogress;
    }

    @Override
    protected void initialize() {
        TextView tel = (TextView) findViewById(R.id.tv);
        tel.setText("金额(元)" + "\n" + "20,000");

        Spannable span = new SpannableString(tel.getText());
        //字体大小
        span.setSpan(new AbsoluteSizeSpan((int) getResources().getDimension(R.dimen.text_XXXXlarge)),
                6, tel.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //字体颜色
        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.main_color)),
                6, tel.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //字体背景
        span.setSpan(new BackgroundColorSpan(Color.YELLOW), 6, tel.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tel.setText(span);

        TextView tv1 = (TextView) findViewById(R.id.tv1);
        TextView tv2 = (TextView) findViewById(R.id.tv2);
        TextView tv3 = (TextView) findViewById(R.id.tv3);
        TextView tv4 = (TextView) findViewById(R.id.tv4);
        tv1.setText("天行健，君子以自强不息；地势坤，君子以厚德载物。");
        SpannableStringBuilder builder = new SpannableStringBuilder(tv1.getText().toString());
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        builder.setSpan(redSpan, 5, tv1.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(89);
        builder.setSpan(sizeSpan, 5, tv1.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv1.setText(builder);
        //两次加大字体，设置字体为红色（big会加大字号，font可以定义颜色）
        tv1.append("\n");
        tv1.append(Html.fromHtml("天行健，君子以自强不息；" +
                "<font color='#00ff00'><big><big>地势坤，君子以厚德载物。</big></big></font>"));

        //设置字体大小为3级标题，设置字体为红色
        tv2.setText(Html.fromHtml("天行健，君子以自强不息；" +
                "<h3><font color='#ffff00'>地势坤，君子以厚德载物。</font></h3>口罩"));

        //设置字体大小为58（单位为物理像素），设置字体为红色，字体背景为黄色
        tv3.setText("天行健，君子以自强不息；地势坤，君子以厚德载物。");
        Spannable span3 = new SpannableString(tv3.getText());
        span3.setSpan(new AbsoluteSizeSpan(58), 11, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span3.setSpan(new ForegroundColorSpan(Color.GREEN), 11, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span3.setSpan(new BackgroundColorSpan(Color.rgb(0, 255, 255)), 11, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv3.setText(span3);

        //两次缩小字体，设置字体为红色（small可以减小字号）
        tv4.setText(Html.fromHtml("天行健，君子以自强不息；<font color='#00ffff'><small><small>地势坤，君子以厚德载物。</small></small></font>sky"));
    }
}
