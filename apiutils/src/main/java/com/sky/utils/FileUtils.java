package com.sky.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by SKY on 2015/11/28.
 */
public class FileUtils {

    /**
     * 从路径中获取最后一个斜杠/之后的名称
     *
     * @param path 文件路径名称
     * @return
     */
    public static String getFileName(String path) {
        int start = path.lastIndexOf(File.separator);
        if (start != -1) return path.substring(start + 1, path.length());
        else return null;
    }

    /**
     * 删除文件 或者文件夹下所有文件
     *
     * @param path 文件夹完整绝对路径
     * @return
     */
    public static boolean deleteFile(String path) {
        return deleteFile(new File(path));
    }

    /**
     * 删除文件 或者文件夹下所有文件
     */
    public static boolean deleteFile(File dirOrFile) {
        if (dirOrFile == null || !dirOrFile.exists()) return false;
        if (dirOrFile.isFile()) dirOrFile.delete();
        else if (dirOrFile.isDirectory())
            for (File file : dirOrFile.listFiles()) {
                deleteFile(file);// 递归
            }
        dirOrFile.delete();
        return true;
    }

    /**
     * 获取文件夹所占空间大小,字节byte
     */
    public static long getDirSize(File dir) {
        long size = 0;
        File[] flist = dir.listFiles();//获取当前文件夹下的文件
        if (flist == null) return size;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) //是文件夹，搜索文件夹内的问价
                size = size + getDirSize(flist[i]);
            else size = size + flist[i].length();
        }
        return size;
    }

    /**
     * 获取文件夹内文件个数
     *
     * @param folder
     * @return
     */
    public static long getNumberOfFiles(File folder) {
        File flist[] = folder.listFiles();
        long size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getNumberOfFiles(flist[i]);
                size--;
            }
        }
        return size;
    }

    /**
     * 复制文件(以超快的速度复制文件)
     *
     * @param srcFile 源文件File
     * @param destDir 目标目录File
     * @param newName 新文件名
     */
    public static void copyFile(File srcFile, File destDir, String newName) {
        try {
            File dstFile = new File(destDir, newName);//创建目标文件
            FileChannel fcin = new FileInputStream(srcFile).getChannel();//源文件通道
            FileChannel fcout = new FileOutputStream(dstFile).getChannel();
            long size = fcin.size();
            fcin.transferTo(0, size, fcout);//写入目标文件
            fcin.close();
            fcout.close();
        } catch (FileNotFoundException e) {
            LogUtils.d(e.getMessage());
        } catch (IOException e) {
            LogUtils.d(e.getMessage());
        }
    }

    /**
     * 把序列化的对象保存到本地
     *
     * @param pathname 文件路径，后边会自动拼接“ser.serial”
     * @param object   要保存的对象
     */
    public static <T> void serialToFile(String pathname, T object) {
        ObjectOutputStream objectOut = null;
        try {
            objectOut = new ObjectOutputStream(new FileOutputStream(new File(pathname + "ser.serial")));
            objectOut.writeObject(object);// 写入到本地
        } catch (FileNotFoundException e) {
            LogUtils.d(e.getMessage());
        } catch (IOException e) {
            LogUtils.d(e.getMessage());
        } finally {
            try {
                if (objectOut != null) objectOut.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 反序列化
     *
     * @param pathname 文件路径，后边会自动拼接“ser.serial”
     * @return 解析好的数据对象
     */
    public static <T> T fileToSerialObj(String pathname) {
        ObjectInputStream objectIn = null;
        try {
            objectIn = new ObjectInputStream(new FileInputStream(new File(pathname + "ser.serial")));
            return (T) objectIn.readObject();//从本地获取数据并返回
        } catch (IOException e) {
            LogUtils.d(e.getMessage());
        } catch (ClassNotFoundException e) {
            LogUtils.d(e.getMessage());
        } finally {
            try {
                if (objectIn != null) objectIn.close();
            } catch (IOException e) {
            }
        }
        return null;
    }

    /**
     * @param pathname 绝对路径
     * @param content  要保存的文本内容
     */
    public static void saveCharFile(String pathname, String content) {
        saveCharFile(pathname, content, false);
    }

    /**
     * @param pathname 绝对路径
     * @param content  要保存的文本内容
     * @param content  是否追加,
     */
    public static void saveCharFile(String pathname, String content, Boolean append) {
        BufferedWriter output = null;
        try {
            File file = new File(pathname);
            output = new BufferedWriter(new FileWriter(file, append));
            output.write(content);
            output.flush();
//            int start = 0;
//            int interval = 100;
//            int end = interval;
//            int index = content.length() / interval;
//            for (int i = 0; i < index + 1; i++) {
//                start = (i) * interval;
//                end = (i + 1) * interval;
//                if (content.length() < end) end = content.length();
//                String text = content.substring(start, end);
//                LogUtils.i(text);
//                output.write(text);
//                output.flush();
//            }
        } catch (Exception e) {
            LogUtils.d(e.getMessage());
        } finally {
            try {
                if (output != null) output.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 保存数据流
     *
     * @param pathname 文件绝对路径
     * @param stream   数据流
     */

    public static void saveByteFile(String pathname, InputStream stream) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            File file = new File(pathname);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = stream.read(buffer)) > -1) {
                bos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            LogUtils.d(e.getMessage());
        } finally {
            try {
                if (bos != null) bos.close();
                if (fos != null) fos.close();
                if (stream != null) stream.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 通过字节流读取文件中的内容，适用于流媒体文件
     *
     * @param pathname 绝对路径
     * @return
     */
    public static String readByteFile(String pathname) {
        return readByteFile(new File(pathname));
    }

    /**
     * 通过字节流读取文件中的内容，适用于流媒体文件
     *
     * @param file
     * @return
     */
    @NonNull
    private static String readByteFile(File file) {
        StringBuilder result = new StringBuilder();
        FileInputStream fileIn = null;
        BufferedInputStream bufferedIn = null;
        try {
            fileIn = new FileInputStream(file);
            bufferedIn = new BufferedInputStream(fileIn);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufferedIn.read(buffer)) > -1) {
                result.append(new String(buffer, 0, len));
            }
        } catch (IOException e) {
            LogUtils.d(e.getMessage());
        } finally {
            try {
                if (bufferedIn != null) bufferedIn.close();
                if (fileIn != null) fileIn.close();
            } catch (IOException e) {
            }
        }
        return result.toString();
    }

    /**
     * 通过字符流读取文件中的内容，适用于文本文件的读取
     *
     * @param pathname 绝对路径
     * @return
     */
    public static String readCharFile(String pathname) {
        return readByteFile(new File(pathname));
    }

    /**
     * 通过字符流读取文件中的内容，适用于文本文件的读取
     *
     * @param file 文本文件
     * @return
     */
    public static String readCharFile(File file) {
        StringBuilder result = new StringBuilder();
        FileReader fileReader = null;
        BufferedReader bReader = null;
        try {
            fileReader = new FileReader(file);
            bReader = new BufferedReader(fileReader);
            char[] buffer = new char[1024];
            int len;
            while ((len = bReader.read(buffer)) > -1) {
                result.append(new String(buffer, 0, len));
            }
        } catch (IOException e) {
            LogUtils.d(e.getMessage());
        } finally {
            try {
                if (bReader != null) bReader.close();
                if (fileReader != null) fileReader.close();
            } catch (IOException e) {
            }
        }
        return result.toString();
    }

    /**
     * 读取输入流中数据
     *
     * @param stream
     * @return
     */
    public static String readInput(InputStream stream) {
        StringBuilder result = new StringBuilder();
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(stream);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = bis.read(buffer)) > -1) {
                result.append(new String(buffer, 0, len));
            }
        } catch (IOException e) {
            LogUtils.d(e.getMessage());
        } finally {
            try {
                if (bis != null) bis.close();
                if (stream != null) stream.close();
            } catch (IOException e) {
            }
        }
        return result.toString();
    }

    /**
     * 读取assest文件，并返回字符串
     *
     * @param context
     * @param pathname 路径
     * @return
     */
    public static String readAssestToStr(Context context, String pathname) {
        try {
            return readInput(context.getAssets().open(pathname));
        } catch (IOException e) {

        }
        return "";
    }
}
