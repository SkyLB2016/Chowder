package com.sky.chowder;

import com.sky.utils.RegexUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class JavaConsole {
    private static String id = "dd";

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


    private static void printWriter(File file) {
        try {
            int count;
            String content;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter pw = new PrintWriter(new FileWriter("C:\\WorkSpace\\Chowder\\app\\道德经.txt", true));
            char[] chars = new char[1024];
            while ((count = reader.read(chars)) != -1) {
                content = new String(chars, 0, count);
                if (content.equals("exit")) break;
                pw.write(content);
                pw.flush();
            }
            reader.reset();
        } catch (IOException e) {

        }
    }

    private static String readSdFile(File file) {
        StringBuilder result = new StringBuilder();
        FileInputStream fileIn = null;
        BufferedReader bufferedIn = null;
        FileWriter writer = null;
        String line = null;
        List<String> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        boolean flag;
        try {
            fileIn = new FileInputStream(file);
            bufferedIn = new BufferedReader(new InputStreamReader(fileIn));
            writer = new FileWriter("C:\\WorkSpace\\Chowder\\app\\total1", true);
            while ((line = bufferedIn.readLine()) != null) {
//                System.out.println(line);
                list.add(line);
            }
            long time = System.currentTimeMillis();
            for (int i = 0; i < list.size(); i++) {
                flag = false;
                for (int j = 0; j < list1.size(); j++) {
                    if (list.get(i).equals(list1.get(j))) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    list1.add(list.get(i));
                    writer.write(list.get(i) + "\n");
                }
            }
            System.out.println("消耗时间==" + (System.currentTimeMillis() - time));
        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) writer.close();
                if (bufferedIn != null) bufferedIn.close();
                if (fileIn != null) fileIn.close();
            } catch (IOException e) {
            }
        }
        return result.toString();
    }

    private static String readSdFile1(File file) {
        StringBuilder result = new StringBuilder();
        FileInputStream fileIn = null;
        BufferedReader bufferedIn = null;
        FileWriter writer = null;
        String line = null;
        int position = 0;
        int filePos = 2;
        try {
            fileIn = new FileInputStream(file);
            bufferedIn = new BufferedReader(new InputStreamReader(fileIn));
            writer = new FileWriter("C:\\WorkSpace\\Chowder\\app\\total1", true);
            while ((line = bufferedIn.readLine()) != null) {
                if (line.contains("NULL;"))
                    line = line.substring(5);
                else if (line.contains(";") && line.length() > 11)
                    line = line.substring(line.indexOf(";") + 1);
                System.out.println(line);
                line += "\n";
                if (position == 4000) {
                    writer.flush();
                    writer.close();
                    writer = new FileWriter("C:\\WorkSpace\\Chowder\\app\\total" + filePos, true);
                    filePos++;
                    position = 0;
                }
                writer.write(line);
                position++;
            }
//        } catch (IndexOutOfBoundsException e) {
//            LogUtils.d(e.toString());
//            System.out.println(line);
        } catch (IOException e) {
        } finally {
            try {

                if (writer != null) writer.close();

                if (bufferedIn != null) bufferedIn.close();
                if (fileIn != null) fileIn.close();
            } catch (IOException e) {
            }
        }
        return result.toString();
    }

    private static String readSdFile2(File file) {
        StringBuilder result = new StringBuilder();
        FileInputStream fileIn = null;
        DataInputStream dataIn = null;
        DataOutputStream writer = null;
        long num = 0;
        int position = 0;
        int filePos = 2;
        try {
            fileIn = new FileInputStream(file);
            dataIn = new DataInputStream(fileIn);
            writer = new DataOutputStream(new FileOutputStream("C:\\WorkSpace\\Chowder\\app\\data1"));
            while ((num = dataIn.readLong()) != 0) {

                System.out.println(num);
                writer.writeLong(num);
            }
//        } catch (IndexOutOfBoundsException e) {
//            LogUtils.d(e.toString());
//            System.out.println(line);
        } catch (IOException e) {
        } finally {
            try {

                if (writer != null) writer.close();

                if (dataIn != null) dataIn.close();
                if (fileIn != null) fileIn.close();
            } catch (IOException e) {
            }
        }
        return result.toString();
    }

}
