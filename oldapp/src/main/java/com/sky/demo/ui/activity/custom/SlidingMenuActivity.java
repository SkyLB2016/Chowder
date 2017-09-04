package com.sky.demo.ui.activity.custom;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sky.demo.R;
import com.sky.demo.api.IDataCallback;
import com.sky.demo.common.Constants;
import com.sky.demo.model.Employee;
import com.sky.demo.model.MData;
import com.sky.demo.model.Person;
import com.sky.demo.ui.BaseActivity;
import com.sky.demo.ui.activity.MainActivity;
import com.sky.demo.ui.activity.viewpager.TabLayoutActivity;
import com.sky.demo.ui.widget.ActionBar;
import com.sky.demo.ui.widget.FlowLayout;
import com.sky.demo.utils.LogUtils;
import com.sky.utils.ActivityLifecycle;
import com.sky.utils.JumpAct;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.LinkedList;

/**
 * Created by sky on 15/12/9 下午8:54.
 * 横向侧滑栏  流式布局
 */
@ContentView(R.layout.activity_sliding)
public class SlidingMenuActivity extends BaseActivity implements View.OnClickListener {

    private String[] mVals = new String[]
            {"start_progress", "stop_progress", "remove_allactions", "add_action",
                    "remove_action", "remove_share_action",
                    "bottomtabbar", "Android", "Weclome Hi ", "Button", "TextView", "Hello",
                    "Android", "Weclome", "Button ImageView", "TextView", "Helloworld",
                    "Android", "Weclome Hello", "Button Text", "TextView"};
    @ViewInject(R.id.slidingMenu)
    private ViewGroup menu;
    @ViewInject(R.id.id_flowlayout)
    private FlowLayout mFlowLayout;
    @ViewInject(R.id.actionbar)
    private ActionBar actionBar;
    private ActionBar.Action shareAction;

    public static Intent createIntent(Context context) {
        Intent i = new Intent(context, SlidingMenuActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

    private Intent createShareIntent() {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Shared from the ActionBar widget.");
        return Intent.createChooser(intent, "Share");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sliding);
        initActionBar();
        initMenu();
        initData();
    }

    //侧滑栏
    private void initMenu() {
        ViewGroup menu_content = (ViewGroup) menu.getChildAt(1);
        int count = menu_content.getChildCount();
        for (int i = 0; i < count; i++) {
            menu_content.getChildAt(i).setOnClickListener(this);
        }
    }

    //自定义actionbar
    private void initActionBar() {
        actionBar.setHomeAction(new ActionBar.AbstractAction(R.mipmap.ic_title_home_default) {
            @Override
            public void performAction(View view) {
                ActivityLifecycle.getInstance().backToAppointActivity(MainActivity.class);
            }
        });
        //actionBar.setHomeLogo(R.mipmap.ic_title_home_default);
        actionBar.setTitle("SlidingMenuAndScrollTextView");
        actionBar.setDisplayHomeAsUpEnabled(true);

        shareAction = new ActionBar.IntentAction(this, createShareIntent(),
                R.mipmap.ic_title_share_default);
        actionBar.addAction(shareAction);
        final ActionBar.Action otherAction = new ActionBar.IntentAction(this, new Intent(this,
                MainActivity.class), R.mipmap.ic_title_export_default);
        actionBar.addAction(otherAction);
    }

    @Override
    public void onClick(View v) {
        String tag = (String) v.getTag();
        if (tag.equals(getResources().getString(R.string.qq_menu01))) {
            JumpAct.jumpActivity(SlidingMenuActivity.this,CircleProgressActivity.class);
        } else if (tag.equals(getResources().getString(R.string.qq_menu02))) {
            showToast("Battery=" + getBattery());
        } else if (tag.equals(getResources().getString(R.string.qq_menu03))) {
            sendBroadcast();
        } else if (tag.equals(getResources().getString(R.string.qq_menu04))) {
            JumpAct.jumpActivity(SlidingMenuActivity.this, TabLayoutActivity.class);
        } else if (v.getTag().equals(getResources().getString(R.string.qq_menu05))) {
            JumpAct.jumpActivity(this,new Intent(SlidingMenuActivity.this, PullDownActivity.class));
        }
    }

    private void sendBroadcast() {
        send();
        setDataCallback(new IDataCallback<MData<? extends Person>>() {
            @Override
            public void onNewData(final MData<? extends Person> data) {
                handler.sendEmptyMessage(Constants.handler_menu);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.i(data.dataList.toString());
                    }
                });
            }

            @Override
            public void onError(String msg, int code) {

            }
        });
        Employee employee = new Employee();
        employee.setId(12);
        employee.setName("sky");
        employee.setAge(25);
        employee.setDepartmentName("技术");
        employee.setGender("男");
        employee.setPosition("android");
        Intent data = new Intent(Constants.ACTION_PUSH_DATA);
        data.putExtra("data", employee);
        sendBroadcast(data);
    }

    @Override
    protected void handler(Message msg) {
        if (msg.what == Constants.handler_menu)
            showToast("onNewData handler");
    }

    public void initData() {
        LayoutInflater mInflater = LayoutInflater.from(this);
        for (int i = 0; i < mVals.length; i++) {
            TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                    mFlowLayout, false);
            ViewGroup.LayoutParams lp = tv.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            tv.setLayoutParams(lp);
            tv.setText(mVals[i]);
            mFlowLayout.addView(tv);
//            ObjectAnimator.ofFloat(tv, "rotation", 0F, 360F).setDuration(3000).start();
            tv.setTag(mVals[i]);
            tv.setOnClickListener(new FlowOnclick());
        }
        for (int i = '!'; i <= 'z'; i++) {
            TextView tv = (TextView) mInflater.inflate(R.layout.tv, mFlowLayout, false);
            tv.setText((char) i + "");
            mFlowLayout.addView(tv);
            ObjectAnimator.ofFloat(tv, "rotation", 0F, 360F).setDuration(3000).start();
            setOnclicK(tv);
        }
        getNum();
    }
    //一组数据从1开始数，到13时移除此数，之后继续从1开始数
    public void getNum() {
        LinkedList<String> nums = new LinkedList<>();
        for (int i = 1; i < 15; i++) {
            nums.add(i + "");
        }
        int a = 1;

        while (!nums.isEmpty()) {
            if (a != 13) {
                String temp = nums.getFirst();
                nums.removeFirst();
                nums.addLast(temp);
                a++;
            } else {
                TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.tv, mFlowLayout, false);
                tv.setText(nums.getFirst()+ "");
                mFlowLayout.addView(tv);
                ObjectAnimator.ofFloat(tv, "rotation", 0F, 360F).setDuration(3000).start();
                setOnclicK(tv);
                nums.removeFirst();
                a=1;
            }
        }
    }

    private void setOnclicK(final TextView tv) {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                char c = tv.getText().toString().trim().charAt(0);
                isExit(c % 6);
                showToast(c + "=" + (int) c+";dialogid="+c%6);
            }
        });
    }

    class FlowOnclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String tag = (String) v.getTag();
            if (tag.equals(mVals[0]))
                actionBar.setProgressBarVisibility(View.VISIBLE);
            else if (tag.equals(mVals[1]))
                actionBar.setProgressBarVisibility(View.GONE);
            else if (tag.equals(mVals[2]))
                actionBar.removeAllActions();
            else if (tag.equals(mVals[3])) {
                actionBar.addAction(new ActionBar.Action() {
                    @Override
                    public int getDrawable() {
                        return R.mipmap.ic_title_share_default;
                    }

                    @Override
                    public void performAction(View view) {
                        showToast("Added action.");
                    }
                });
//                actionBar.addAction(new ActionBar.AbstractAction(R.mipmap.ic_title_share_default) {
//                    @Override
//                    public void performAction(View view) {
//                        showToast("Added action.");
//                    }
//                });
            } else if (tag.equals(mVals[4])) {
                int actionCount = actionBar.getActionCount();
                if (actionCount > 0) {
                    actionBar.removeActionAt(actionCount - 1);
                    showToast("Removed action.");
                }
            } else if (tag.equals(mVals[5])) {
                actionBar.removeAction(shareAction);
            } else {
                showLoading();
            }
        }
    }
}