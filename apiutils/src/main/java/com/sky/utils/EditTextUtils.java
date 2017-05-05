package com.sky.utils;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.sky.R;

/**
 * author ShenZhenWei
 * time   15/11/25.
 * email  826337999@qq.com
 */
public class EditTextUtils {
    //EditText获取焦点
    public static void getFocus(EditText et) {
        et.setFocusable(true);
        et.setSelection(et.getText().length());
        Animation shakeAnim = AnimationUtils.loadAnimation(et.getContext(), R.anim.shake);
        et.startAnimation(shakeAnim);
        et.requestFocus();
    }
}
