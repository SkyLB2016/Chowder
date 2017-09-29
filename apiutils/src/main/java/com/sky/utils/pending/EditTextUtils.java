package com.sky.utils.pending;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.sky.R;

/**
 * Created by SKY on 2015/11/28.
 */
public class EditTextUtils {
    //EditText获取焦点
    public static void getFocus(EditText et) {
        et.setFocusable(true);
        et.setSelection(et.getText().length());
        Animation shakeAnim = AnimationUtils.loadAnimation(et.getContext(), R.anim.anim_shake);
        et.startAnimation(shakeAnim);
        et.requestFocus();
    }
}
