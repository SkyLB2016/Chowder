// IOnNewBookArriveListener.aidl
package com.sky.oa.service;

import com.sky.oa.service.Book;

interface IOnNewBookArriveListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
    void onNewBookArrived(in Book newBook);
}
