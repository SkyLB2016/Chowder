package liuyongxiang.robert.com.testtime.activities;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import liuyongxiang.robert.com.testtime.R;
import liuyongxiang.robert.com.testtime.wheelview.DateUtils;
import liuyongxiang.robert.com.testtime.wheelview.JudgeDate;
import liuyongxiang.robert.com.testtime.wheelview.ScreenInfo;
import liuyongxiang.robert.com.testtime.wheelview.WheelMain;
import liuyongxiang.robert.com.testtime.wheelview.WheelView;

import static liuyongxiang.robert.com.testtime.R.id.tv_cancle;
import static liuyongxiang.robert.com.testtime.R.id.tv_ensure;
import static liuyongxiang.robert.com.testtime.R.id.tv_pop_title;

/**
 * Created by SKY on 2017/7/12.
 */
public class SelectTimeFragment extends DialogFragment {

    Unbinder unbinder;
    @BindView(tv_pop_title)
    TextView tvPopTitle;
    @BindView(R.id.year)
    WheelView year;
    @BindView(R.id.month)
    WheelView month;
    @BindView(R.id.day)
    WheelView day;
    @BindView(R.id.hour)
    WheelView hour;
    @BindView(R.id.mins)
    WheelView mins;
    @BindView(R.id.timePicker1)
    LinearLayout timePicker1;
    @BindView(tv_cancle)
    TextView tvCancle;
    @BindView(tv_ensure)
    TextView tvEnsure;
    @BindView(R.id.rel_select)
    LinearLayout relSelect;

    private WheelMain wheelMainDate;
    private OnDismissListener disOnClick;

    public SelectTimeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //去掉默认的标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.show_popup_window, container);
        unbinder = ButterKnife.bind(this, view);
        wheelMainDate = new WheelMain(view, true);
        ScreenInfo screenInfoDate = new ScreenInfo(getActivity());
        wheelMainDate.screenheight = screenInfoDate.getHeight();
        initData();
        return view;
    }

    private void initData() {

        String time = DateUtils.currentMonth().toString();
        Calendar calendar = Calendar.getInstance();
        if (JudgeDate.isDate(time, "yyyy-MM-DD")) {
            try {
                calendar.setTime(new Date(time));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        wheelMainDate.initDateTimePicker(year, month, day, hours, minute);


        tvPopTitle.setText("选择起始时间");
    }

    @Override
    public void onStart() {
        super.onStart();
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        getDialog().getWindow().setLayout(ScreenUtils.getWidthPX(getActivity()) / 8 * 7, getDialog().getWindow().getAttributes().height);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        dismiss();
        if (disOnClick != null) disOnClick.OnDismiss();
    }

    @OnClick(R.id.tv_ensure)
    public void sureonClick(View v) {
        String beginTime = wheelMainDate.getTime().toString();
        if (successOnClick != null) successOnClick.OnClick(beginTime);
        dismiss();
    }


    private OnClickListener successOnClick;

    public void setSuccessOnClick(OnClickListener successOnClick) {
        this.successOnClick = successOnClick;
    }

    public interface OnClickListener {
        void OnClick(String flag);
    }

    public interface OnDismissListener {
        void OnDismiss();
    }
}
