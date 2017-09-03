package com.sky.demo.ui.dialog;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.demo.R;
import com.sky.demo.utils.RegexUtils;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * @author sky QQ:1136096189
 * @Description: TODO
 * @date 16/1/11 下午1:14
 */
public class Pop extends BasePop<String> {
    @ViewInject(R.id.parent)
    private RelativeLayout parent;
    @ViewInject(R.id.item_popupwindows_cancel)
    private Button cancle;
    @ViewInject(R.id.phone01)
    private TextView tv01;
    @ViewInject(R.id.phone02)
    private TextView tv02;

    public Pop(View contentView, List<String> datas) {
        super(contentView);
        this.popDatas = datas;
    }

    public Pop(View view) {
        super(view);
    }


    @Override
    protected void initView() {
    }

    @Override
    protected void initEvent() {
        tv01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegexUtils.isChinesePhoneNumber(popDatas.get(0)) || RegexUtils.isChineseTel(popDatas.get(0))) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + popDatas.get(0))));
                } else
                    Toast.makeText(context, "电话号码错误", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        tv02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegexUtils.isChinesePhoneNumber(popDatas.get(1)) || RegexUtils.isChineseTel(popDatas.get(1))) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + popDatas.get(1))));
                } else
                    Toast.makeText(context, "电话号码错误", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected void initDatas() {
        tv01.setText(popDatas.get(0));
        tv02.setText(popDatas.get(1));
    }
}
