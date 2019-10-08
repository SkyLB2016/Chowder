package com.sky.chowder.utils

import android.graphics.Bitmap
import android.os.AsyncTask
import android.view.View

import com.sky.utils.BitmapUtils

internal class AsyncTaskTest(var view: View) : AsyncTask<String, Void, Bitmap>() {
    var url: String=""

    /**
     * 执行后台耗时操作前被调用，完成一些初始化操作
     */
    override fun onPreExecute() {
        super.onPreExecute()
    }

    /**
     * 异步任务，并且asyncTask中只有他是异步任务，其他都不是；必须重写
     *
     * @param params
     * @return
     */
    override fun doInBackground(vararg params: String): Bitmap? {
        if (isCancelled)
            return null
        url = params[0]
        //            publishProgress();
        return BitmapUtils.getBitmapUP(url)
    }

    /**
     * 可在此设置进度条更新，在doInBackground()中调用publishProgress()
     *
     * @param values
     */
    override fun onProgressUpdate(vararg values: Void) {
        super.onProgressUpdate(*values)
    }

    /**
     * 完成后，系统自动调用，在此更新UI
     *
     * @param bitmap
     */
    override fun onPostExecute(bitmap: Bitmap) {
        super.onPostExecute(bitmap)
    }
}
