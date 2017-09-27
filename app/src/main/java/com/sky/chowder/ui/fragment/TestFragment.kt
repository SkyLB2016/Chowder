package com.sky.chowder.ui.fragment


import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import com.sky.chowder.R
import com.sky.utils.ScreenUtils

/**
 * Created by SKY on 2017/7/12.
 */
class TestFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
        //去掉默认的标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false

        return inflater.inflate(R.layout.activity_detail, container)
    }

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        //        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.window!!.setLayout(ScreenUtils.getWidthPX(activity) / 6 * 5, dialog.window!!.attributes.height)
    }
}