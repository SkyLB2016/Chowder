package com.sky.oa.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import com.sky.oa.BuildConfig
import com.sky.sdk.utils.DateUtil
import com.sky.sdk.utils.LogUtils
import com.sky.sdk.utils.ToastUtils
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

    override fun uncaughtException(t: Thread, ex: Throwable) {
        if (!handlerException(ex) && handler != null) handler?.uncaughtException(t, ex)
        else {
            try {
                Thread.sleep(3000)
            } catch (e: InterruptedException) {

            }
            if (BuildConfig.DEBUG) ex.printStackTrace()
//            handler?.uncaughtException(t, e)
            android.os.Process.killProcess(android.os.Process.myPid())
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
        val pm = context?.packageManager
        val pi = pm!!.getPackageInfo(context!!.packageName, PackageManager.GET_ACTIVITIES)
        "App Version：" + pi.versionName + "-" + pi.versionCode
        "OS Version：" + Build.VERSION.RELEASE + "-" + Build.VERSION.SDK_INT
        "Vendor：" + Build.MANUFACTURER
        "Model：" + Build.MODEL
        "CPU ABI：" + Build.CPU_ABI

        val sw = StringWriter()
        val pw = PrintWriter(BufferedWriter(sw))
        ex?.printStackTrace(pw)
        pw.flush()
        sw.flush()
        val error = sw.toString()
        LogUtils.i(error)
        pw.close()
        sw.close()
    }

    /**
     * 保存错误信息到本地
     *
     * @param e
     */
    private fun saveExceptionToSDCard(e: Throwable) {
        val time = DateUtil.timeStampToDate(System.currentTimeMillis(), DateUtil.YMDHMS)
        val file = File("/storage/emulated/0/crash/crash$time.trace")
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
        val pi = pm!!.getPackageInfo(context!!.packageName, PackageManager.GET_ACTIVITIES)
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
                crash = crash ?: CrashHandler()
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