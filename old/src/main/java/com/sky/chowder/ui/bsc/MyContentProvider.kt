package com.sky.chowder.ui.bsc

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class MyContentProvider : ContentProvider() {
    //比如你实现了增和删
    override //根据Uri删除selection指定的条件所匹配的全部记录
    fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override //返回当前uri的MIME类型，如果该URI对应的数据可能包括多条记录
            //那么MIME类型字符串 就是以vnd.android.dir/开头
            //	如果该URI对应的数据只有一条记录 该MIME类型字符串 就是以vnd.android.cursor.item/开头
    fun getType(uri: Uri): String? {
        return null
    }

    override //根据Uri插入Values对应的数据
    fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override //在ContetnProvider创建后被调用
    fun onCreate(): Boolean {
        return false
    }

    override //根据uri查询出selection指定的条件所匹配的全部记录，并且可以指定查询哪些列 以什么方式(order)排序
    fun query(uri: Uri, projection: Array<String>?, selection: String?,
              selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return null
    }

    override //根据uri修改selection指定的条件所匹配的全部记录
    fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

}
