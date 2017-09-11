package com.sky.chowder.utils

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import com.sky.utils.LogUtils
import java.io.Serializable
import java.util.*

class MemoryUtils {

    private lateinit var processEntities: MutableList<ProcessEntity>

    fun getRunningAppProcessInfo(context: Context) {
        processEntities = ArrayList()
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appRunProList = manager.runningAppProcesses
        for (pro in appRunProList) {
            // 进程ID号
            val pid = pro.pid
            // 用户ID 类似于Linux的权限不同，ID也就不同 比如 root等
            val uid = pro.uid
            // 进程名，默认是包名或者由属性android：process=""指定
            val processName = pro.processName
            // 获得该进程占用的内存
            val myMempid = intArrayOf(pid)
            // 此MemoryInfo位于android.os.Debug.MemoryInfo包中，用来统计进程的内存信息
            val memoryInfo = manager.getProcessMemoryInfo(myMempid)
            // 获取进程占内存用信息 kb单位
            val memSize = memoryInfo[0].dalvikPrivateDirty

            var appName = ""
            try {
                val appinfo = context.packageManager.getApplicationInfo(processName, 0)
                appName = appinfo.loadLabel(context.packageManager) as String
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            // 构造一个ProcessInfo对象
            val processInfo = ProcessEntity()
            processInfo.pid = pid
            processInfo.uid = uid
            processInfo.memSize = memSize
            processInfo.processName = processName
            processInfo.appName = appName
            processEntities.add(processInfo)

            // 获得每个进程里运行的应用程序(包),即每个应用程序的包名
            val packageList = pro.pkgList
            LogUtils.i("process id is " + pid + "has " + packageList.size)
            for (pkg in packageList) {
                LogUtils.i("packageName $pkg in process id is -->$pid")
            }
        }
    }
}

internal class ProcessEntity : Serializable {
    var uid: Int = 0//进程id，Android规定android.system.uid=1000
    var pid: Int = 0//进程所在的用户id ，即该进程是有谁启动的 root/普通用户等
    var memSize: Int = 0//进程占用的内存大小,单位为kb
    var processName: String? = null//进程名，包名
    var appName: String? = null//进程名，包名
}
