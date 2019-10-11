package com.sky.oa.service

import com.sky.sdk.utils.LogUtils

class AliveBinder : IBookManager.Stub() {
    override fun registerListener(listener: IOnNewBookArriveListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unRegisterListener(listener: IOnNewBookArriveListener?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBookList(): MutableList<Book> {
        LogUtils.i("==getBookList")

        return ArrayList()
    }

    override fun addBook(book: Book?) {
        LogUtils.i("==addBook")
    }
}