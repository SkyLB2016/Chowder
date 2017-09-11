package com.sky.chowder.ui.widget

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface

import com.sky.chowder.R

/**
 * Created by SKY on 2017/6/21.
 */
class ProgressManager {
    private fun progressDialog(context: Context) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.setTitle("main")
        progressDialog.setMessage("main")
        progressDialog.setIcon(R.mipmap.ic_launcher)
        progressDialog.max = 100
        progressDialog.incrementProgressBy(50)
        progressDialog.isIndeterminate = false
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "ok") { dialog, which -> }
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ok") { dialog, which -> }
        progressDialog.setCancelable(true)
        progressDialog.show()
    }
}