package com.example.jnilib;

import android.util.Log;

import java.io.IOException;

/**
 * Created by SKY on 2018/5/22 14:48.
 */
public class NDKString {
    static {
        System.loadLibrary("jnilib");//为lib包的名称
    }

    public static void main(String[] args) {
        NDKString ndk = new NDKString();
        System.out.print( ndk.getName());
//        ndk.setName("名字");
        System.out.print( ndk.getName());
    }

    public static native String getName();//方法名称
//
//    public static native void setName(String name);//方法名称
}
