package com.sky.chowder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JavaConsole {
    private static String id = "dd";

    public static void main(String[] args) throws IOException, ClassNotFoundException {

//        BufferedInputStream inputStream = new BufferedInputStream(System.in);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        int count;
//        String content;
//        char[] chars = new char[1024];
//        while ((count = reader.read(chars)) != -1) {
//            content = new String(chars, 0, count);
////            System.out.println(content.trim() + "个" + count);
//            if (RegexUtils.isEmail(content.trim())) {
//                System.out.println("成功");
//            } else System.out.println("失败");
//        }
//        reader.reset();
//        byte[] butes = new byte[1024];
//        while ((count = inputStream.read(butes)) != -1) {
//            content = new String(butes, 0, count);
//            content = content.replace('\n', ' ');
//            JavaConsole.id = content;
//            System.out.println("第" + content + "个=");"C:\WorkSpace\Chowder\app"
//            System.out.println("第" + JavaConsole.id + "个=");
////            System.out.println(f(Integer.parseInt(content.trim())));
//        }
        readSdFile(new File("C:\\WorkSpace\\Chowder\\app\\total"));
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
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) writer.close();
                if (bufferedIn != null) bufferedIn.close();
                if (fileIn != null) fileIn.close();
            } catch (IOException e) {
                e.printStackTrace();
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
//            e.printStackTrace();
//            System.out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                if (writer != null) writer.close();

                if (bufferedIn != null) bufferedIn.close();
                if (fileIn != null) fileIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

}
