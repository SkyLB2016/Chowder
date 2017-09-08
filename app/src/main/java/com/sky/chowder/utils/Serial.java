package com.sky.chowder.utils;

import com.sky.chowder.model.CourseEntity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serial {
    private static String id = "dd";

    public static void main(String[] args) throws IOException, ClassNotFoundException { 
        //序列化 serial
        serial();

        //反序列化 Deserial
        deserial();
        String ss = new String("ssss");
        String sss = new String("ssss");
        System.out.print(ss == sss);
        System.out.print(ss.equals(sss));
        BufferedInputStream inputStream = new BufferedInputStream(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int count;
        int count1;
        String content;
        byte[] butes = new byte[1024];
        char[] chars = new char[1024];
        while ((count1 = reader.read(chars)) != -1) {
            content = new String(chars, 0, count1);
            System.out.println(content + "个");
        }
        reader.reset();
        while ((count = reader.read(chars)) != -1) {
            content = new String(chars, 0, count);

            System.out.println(content + "个");
        }
        while ((count = inputStream.read(butes)) != -1) {
            content = new String(butes, 0, count);
            content = content.replace('\n', ' ');
            Serial.id = content;
            System.out.println("第" + content + "个=");
            System.out.println("第" + Serial.id + "个=");
//            System.out.println(f(Integer.parseInt(content.trim())));
        }
    }

    /**
     * 序列化 serial
     *
     * @throws IOException
     */
    private static void serial() throws IOException {
        CourseEntity person = new CourseEntity();
        person.setName("sky");
        person.setId(152);
        System.out.println("Employee Serial" + "\n" + person);
        FileOutputStream fos = new FileOutputStream("Employee.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(person);
        oos.flush();
        oos.close();
    }

    /**
     * 反序列化 Deserial
     *
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static void deserial() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("Employee.txt");
        ObjectInputStream ois = new ObjectInputStream(fis);
        CourseEntity person = (CourseEntity) ois.readObject();
        ois.close();
        System.out.println("Employee Deserial" + "\n" + person);
    }

//    public static long f(int n) {
//        if (n == 1 || n == 2) {
//            return 1;
//        } else
//            return (f(n - 1) + f(n - 2))*1l;
//
//    }
}
