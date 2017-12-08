package com.sky.chowder.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sky.chowder.R
import com.sky.chowder.ui.fragment.AddressFragment
import com.sky.chowder.ui.fragment.TimeFragment
import com.sky.utils.DateUtil
import kotlinx.android.synthetic.main.activity_address.*


/**
 * Created by SKY on 2016/8/28.
 */
class SelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
    }

    fun xianshi(v: View) {
        val time = TimeFragment()
        time.show(supportFragmentManager, "time")
        time.time = DateUtil.dateToStamp(btTime.text.toString().trim())
        time.onClick = object : TimeFragment.OnClickListener {
            override fun onClick(time: String) {
                btTime.text = time
            }
        }
    }

    fun address(v: View) {
        val time = AddressFragment()
        time.show(supportFragmentManager, "address")
        time.onClick = object : AddressFragment.OnClickListener {
            override fun onClick(address: String) {
                btAddress.text = address
            }
        }
    }
}