package com.sky.oa.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by SKY on 2018/6/25 22:26 六月.
 */
public class CloseUtils {

    public static void close(Closeable close) {
        try {
            if (close != null) close.close();
        } catch (IOException e) {

        }
    }
}
