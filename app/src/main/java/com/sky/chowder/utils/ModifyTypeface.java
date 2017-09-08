package com.sky.chowder.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author sky
 * @ClassName: ActivityLifecycle
 * @Description: TODO 修改字体
 * @date 2015年4月12日 下午1:33:47
 */
public class ModifyTypeface {
    public static void changeFonts(ViewGroup root, String path, Activity act) {
        //path是字体路径
        Typeface tf = Typeface.createFromAsset(act.getAssets(), path);
        for (int i = 0; i < root.getChildCount(); i++) {
            View childAt = root.getChildAt(i);
            if (childAt instanceof TextView) {
                ((TextView) childAt).setTypeface(tf);
            } else if (childAt instanceof Button) {
                ((Button) childAt).setTypeface(tf);
            } else if (childAt instanceof EditText) {
                ((EditText) childAt).setTypeface(tf);
            } else if (childAt instanceof ViewGroup) {
                changeFonts((ViewGroup) childAt, path, act);
            }
        }
    }
}