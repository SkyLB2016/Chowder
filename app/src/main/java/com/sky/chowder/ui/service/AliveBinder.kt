package com.sky.chowder.ui.service

import com.sky.utils.LogUtils

class AliveBinder : IBookManager.Stub() {
    override fun getBookList(): MutableList<Book> {
        LogUtils.i("==getBookList")

        return ArrayList()
    }

    override fun addBook(book: Book?) {
        LogUtils.i("==addBook")
    }
}