package com.sky.chowder;

import com.sky.utils.RegexUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JavaConsole {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        int count;
        String content;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        char[] chars = new char[1024];
        while ((count = reader.read(chars)) != -1) {
            content = new String(chars, 0, count);
            if (RegexUtils.isEmail(content.trim())) {
                System.out.println("成功");
            } else System.out.println("失败");
        }
        reader.reset();

//        BufferedInputStream inputStream = new BufferedInputStream(System.in);
//        byte[] butes = new byte[1024];
//        while ((count = inputStream.read(butes)) != -1) {
//            content = new String(butes, 0, count);
//            content = content.replace('\n', ' ');
//            System.out.println("第" + content + "个=");
//        }
//        readSdFile(new File("C:\\WorkSpace\\Chowder\\app\\total"));
    }


}
