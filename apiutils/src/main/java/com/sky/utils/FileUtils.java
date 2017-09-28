package com.sky.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.nio.channels.FileChannel;

/**
 * Created by SKY on 2015/11/28.
 */
public class FileUtils {

    /**
     * 从路径中获取最后一个斜杠/之后的名称
     */
    public static String getFileName(String path) {
        int start = path.lastIndexOf(File.separator);
        if (start != -1) return path.substring(start + 1, path.length());
        else return null;
    }

    /**
     * 读取文件中的内容
     *
     * @param path
     * @param filename
     * @return
     */
    public static String readSdFile(String path, String filename) {
        return readSdFile(new File(path, filename));
    }

    @NonNull
    private static String readSdFile(File file) {
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
            e.printStackTrace();
        } finally {
            try {
                if (bufferedIn != null) bufferedIn.close();
                if (fileIn != null) fileIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }

    /**
     * @return 读取assest文件，并返回字符串
     */
    public static String readAssestToStr(Context context, String fileName) {
        //先初始化输入输出流。防止处理失败，不能关闭
        InputStream input = null;
        ByteArrayOutputStream byteArrayOut = null;
        try {
            input = context.getAssets().open(fileName);
            //int length = input.available();//输入流的总长度
            byteArrayOut = new ByteArrayOutputStream();// 创建字节输出流对象
            int len;//每次读取到的长度
            byte buffer[] = new byte[1024];//定义缓冲区
            // 按照缓冲区的大小，循环读取，
            while ((len = input.read(buffer)) != -1) {
                byteArrayOut.write(buffer, 0, len);//根据读取的长度写入到os对象中
            }
            return new String(byteArrayOut.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteArrayOut != null) byteArrayOut.close();
                if (input != null) input.close();
            } catch (IOException e) {
            }
        }
        return "";
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
     * 把序列化的对象保存到本地
     *
     * @param dir    文件夹带斜杠的
     * @param name   文件名，后边会自动拼接“ser.serial”
     * @param object 要保存的对象
     */
    public static <T> void serialToFile(String dir, String name, T object) {
        ObjectOutputStream objectOut = null;
        try {
            objectOut = new ObjectOutputStream(new FileOutputStream(new File(dir, name + "ser.serial")));
            objectOut.writeObject(object);// 写入到本地
        } catch (FileNotFoundException e) {
//            LogUtils.i(e.getMessage());
        } catch (IOException e) {
//            LogUtils.i(e.getMessage());
        } finally {
            try {
                if (objectOut != null) objectOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 反序列化
     *
     * @param dir  文件夹带斜杠的
     * @param name 文件名，后边会自动拼接“ser.serial”
     * @return 解析好的数据对象
     */
    public static <T> T getFileToSerialObj(String dir, String name) {
        ObjectInputStream objectIn = null;
        try {
            objectIn = new ObjectInputStream(new FileInputStream(new File(dir, name + "ser.serial")));
            return (T) objectIn.readObject();//从本地获取数据并返回
        } catch (StreamCorruptedException e) {
//            LogUtils.i(e.getMessage());
        } catch (OptionalDataException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        } finally {
            try {
                if (objectIn != null) objectIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
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
     * 获取文件个数
     */
    public static long getlistSize(File f) {
        File flist[] = f.listFiles();
        long size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getlistSize(flist[i]);
                size--;
            }
        }
        return size;
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
     * 复制文件(以超快的速度复制文件)
     *
     * @param srcFile     源文件File
     * @param destDir     目标目录File
     * @param newFileName 新文件名
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
     */
    public static long copyFile(File srcFile, File destDir, String newFileName, CopyFileListener listener) {
        long copySizes = 0;
        if (!srcFile.exists()) {
            if (listener != null) listener.onFail("源文件不存在");
            copySizes = -1;
        } else if (!destDir.exists()) {
            if (listener != null) listener.onFail("目标目录不存在");
            copySizes = -1;
        } else if (newFileName == null) {
            if (listener != null) listener.onFail("文件名为null");
            copySizes = -1;
        } else {
            try {
                File dstFile = new File(destDir, newFileName);//目标文件
                FileChannel fcin = new FileInputStream(srcFile).getChannel();
                FileChannel fcout = new FileOutputStream(dstFile).getChannel();
                long size = fcin.size();
                fcin.transferTo(0, size, fcout);
                fcin.close();
                fcout.close();
                copySizes = size;
                if (listener != null) listener.success(dstFile.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return copySizes;
    }

    public interface CopyFileListener {
        void onFail(String msg);

        void success(String path);
    }
}
