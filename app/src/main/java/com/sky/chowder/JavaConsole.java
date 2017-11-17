package com.sky.chowder;

import com.sky.utils.RegexUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JavaConsole {
    private static String id = "dd";

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        BufferedInputStream inputStream = new BufferedInputStream(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int count;
        String content;
        char[] chars = new char[1024];
        while ((count = reader.read(chars)) != -1) {
            content = new String(chars, 0, count);
//            System.out.println(content.trim() + "个" + count);
            if (RegexUtils.isEmail(content.trim())) {
                System.out.println("成功");
            } else System.out.println("失败");
        }
        reader.reset();
//        byte[] butes = new byte[1024];
//        while ((count = inputStream.read(butes)) != -1) {
//            content = new String(butes, 0, count);
//            content = content.replace('\n', ' ');
//            JavaConsole.id = content;
//            System.out.println("第" + content + "个=");
//            System.out.println("第" + JavaConsole.id + "个=");
////            System.out.println(f(Integer.parseInt(content.trim())));
//        }
    }

}
