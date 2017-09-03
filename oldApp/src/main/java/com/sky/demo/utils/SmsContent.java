package com.sky.demo.utils;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 彬 QQ 1136096189
 * @Description: 短信监听
 * @date 2015/8/17 15:30
 */
public class SmsContent extends ContentObserver {
    //使用方法
//	SmsContent content = new SmsContent(RegisterSecondActivity.this, new Handler(), captcha);
	// 注册短信变化监听
//	RegisterSecondActivity.this.getContentResolver()
//			.registerContentObserver(Uri.parse("content://sms/"),true, content);
	public static final String SMS_URI_INBOX = "content://sms/inbox";

	private Activity activity = null;

	private String smsContent = "";

	private EditText verifyText = null;

	public SmsContent(Activity activity, Handler handler, EditText verifyText) {
		super(handler);
		this.activity = activity;
		this.verifyText = verifyText;
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Cursor cursor = null;// 光标
		// 读取收件箱中指定号码的短信
		cursor = activity.managedQuery(Uri.parse(SMS_URI_INBOX), new String[] {
				"_id", "address", "body", "read" }, "address=? and read=?",
				new String[] { "1069057059317640", "0" }, "date desc");

		if (cursor != null) {// 如果短信为未读模式
			cursor.moveToFirst();
			if (cursor.moveToFirst()) {

				String smsbody = cursor
						.getString(cursor.getColumnIndex("body"));
				LogUtils.i("smsbody=======================" + smsbody);
				String regEx = "[^0-9]";
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(smsbody.toString());
				smsContent = m.replaceAll("").trim().toString();
				verifyText.setText(smsContent);
			}

		}

	}

}