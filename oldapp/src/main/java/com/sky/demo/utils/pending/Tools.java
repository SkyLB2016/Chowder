package com.sky.demo.utils.pending;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sky.demo.utils.LogUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sky
 * @ClassName: ActivityLifecycle
 * @Description: TODO 待修改
 * @date 2015年4月12日 下午1:33:47
 */
public class Tools {
    // MD5加密
    public static String makeMD5(String password) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(password.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    public boolean check(String phoneNum) {
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(phoneNum);
        if (!matcher.matches()) {
            LogUtils.i("手机号不正确");
            return false;
        }
        return true;
    }

    /**
     * @param str 需要编码的数据
     * @return
     */
    public static String convertToUTF8(String str) {
        try {
            return new String(str.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 自动处理double数据,保留非0的2位小数
     *
     * @param price
     * @return
     */
    public static String handleDouble(double price) {
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        return decimalFormat.format(price);
    }

    /**
     * 通过路径生成Base64文件
     *
     * @param path
     * @return
     */
    public static String getBase64FromPath(String path) {
        String base64 = "";
        try {
            File file = new File(path);
            byte[] buffer = new byte[(int) file.length() + 100];
            @SuppressWarnings("resource")
            int length = new FileInputStream(file).read(buffer);
            base64 = Base64.encodeToString(buffer, 0, length, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64;
    }

    /**
     * 把Stream转换成String
     *
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 修改整个界面所有控件的字体
     *
     * @param root
     * @param path
     * @param act
     */
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

    /**
     * 修改整个界面所有控件的字体大小
     *
     * @param root
     * @param size
     * @param act
     */
    public static void changeTextSize(ViewGroup root, int size, Activity act) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View childAt = root.getChildAt(i);
            if (childAt instanceof TextView) {
                ((TextView) childAt).setTextSize(size);
            } else if (childAt instanceof Button) {
                ((Button) childAt).setTextSize(size);
            } else if (childAt instanceof EditText) {
                ((EditText) childAt).setTextSize(size);
            } else if (childAt instanceof ViewGroup) {
                changeTextSize((ViewGroup) childAt, size, act);
            }
        }
    }
}