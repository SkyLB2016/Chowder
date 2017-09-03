package com.sky.demo.ui.bsc;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class MyContentProvider extends ContentProvider{
	//比如你实现了增和删
	@Override//根据Uri删除selection指定的条件所匹配的全部记录
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override//返回当前uri的MIME类型，如果该URI对应的数据可能包括多条记录
	//那么MIME类型字符串 就是以vnd.android.dir/开头
//	如果该URI对应的数据只有一条记录 该MIME类型字符串 就是以vnd.android.cursor.item/开头
	public String getType(Uri uri) {
		return null;
	}

	@Override//根据Uri插入Values对应的数据
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override//在ContetnProvider创建后被调用
	public boolean onCreate() {
		return false;
	}

	@Override//根据uri查询出selection指定的条件所匹配的全部记录，并且可以指定查询哪些列 以什么方式(order)排序
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override//根据uri修改selection指定的条件所匹配的全部记录
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
