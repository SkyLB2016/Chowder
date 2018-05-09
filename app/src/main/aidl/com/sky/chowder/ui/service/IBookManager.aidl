// IBookManager.aidl
package com.sky.chowder.ui.service;

import com.sky.chowder.ui.service.Book;
import com.sky.chowder.ui.service.IOnNewBookArriveListener;

interface IBookManager {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//                double aDouble, String aString);
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArriveListener listener);
    void unRegisterListener(IOnNewBookArriveListener listener);

}
