package com.sky.chowder.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import android.os.Process
import com.sky.SkyApp
import com.sky.utils.DateUtil
import com.sky.utils.LogUtils
import com.sky.utils.ToastUtils
import java.io.*

/**
 * Created by SKY on 2017/12/11 17:09.
 */
class CrashHandler : Thread.UncaughtExceptionHandler {
    private var handler: Thread.UncaughtExceptionHandler? = null
    internal var context: Context? = null

    fun init(context: Context) {
        handler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        this.context = context.applicationContext
    }

    override fun uncaughtException(t: Thread, e: Throwable) {

        if (!handlerException(e) && handler != null) handler!!.uncaughtException(t, e)
        else {
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {

            }
            Process.killProcess(Process.myPid())
            System.exit(1)
        }


//        StackTraceElement[] stacks = e.getStackTrace();
//        while (stacks.length < 10) {
//            e = e.getCause();
//            stacks = e.getStackTrace();
//        }
//        LogUtils.d(e.toString());
//        printError(stacks);
    }

    private fun handlerException(ex: Throwable?): Boolean {
        ex ?: return false
        sendCrash(ex)//发送错误信息到服务器
        Thread(Runnable {
            Looper.prepare()
            ToastUtils.showShort(context, "很抱歉，程序出现异常，即将退出")
            Looper.loop()
        }).start()
        saveExceptionToSDCard(ex)//保存到本地
        return true
    }

    /**
     * 上传到服务器
     */
    private fun sendCrash(ex: Throwable?) {


    }

    /**
     * 保存错误信息到本地
     *
     * @param e
     */
    private fun saveExceptionToSDCard(e: Throwable) {
        val time = DateUtil.timeStampToDate(System.currentTimeMillis(), DateUtil.YMDHMS)
        val file = File(SkyApp.getInstance().fileCacheDir + "crash" + time + ".trace")
        try {
            val pw = PrintWriter(BufferedWriter(FileWriter(file)))
            pw.println(time)
            dumpPhoneInfo(pw)
            pw.println()
            e.printStackTrace(pw)
            pw.close()
        } catch (e1: IOException) {

        } catch (e1: PackageManager.NameNotFoundException) {

        }

    }

    /**
     * 保存手机基本信息
     *
     * @param pw
     * @throws PackageManager.NameNotFoundException
     */
    @Throws(PackageManager.NameNotFoundException::class)
    private fun dumpPhoneInfo(pw: PrintWriter) {
        val pm = context?.packageManager
        val pi = pm!!.getPackageInfo(context?.packageName, PackageManager.GET_ACTIVITIES)
        pw.println("App Version：" + pi.versionName + "-" + pi.versionCode)
        pw.println("OS Version：" + Build.VERSION.RELEASE + "-" + Build.VERSION.SDK_INT)
        pw.println("Vendor：" + Build.MANUFACTURER)
        pw.println("Model：" + Build.MODEL)
        pw.println("CPU ABI：" + Build.CPU_ABI)

    }

    companion object {
        internal var crash: CrashHandler? = null

        val intance: CrashHandler
            get() {
                if (crash == null) crash = CrashHandler()
                return crash!!
            }
    }

    private fun printError(stacks: Array<StackTraceElement>) {
        for (i in stacks.indices) {
            val stack = stacks[i]
            var tag = "第%d个%s.%s(L:%d)"
            var className = stack.className
            className = className.substring(className.lastIndexOf(".") + 1)
            tag = String.format(tag, i, className, stack.methodName, stack.lineNumber)
            LogUtils.d(tag)
        }
    }
}