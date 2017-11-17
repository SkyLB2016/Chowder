package com.sky.chowder.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.sky.chowder.R
import com.sky.chowder.ui.widget.TimeFragment
import kotlinx.android.synthetic.main.time_picker_legacy.*


/**
 * Created by SKY on 2016/8/28.
 */
class TimeActivity : AppCompatActivity() {
    var monthN = 0
    var dateN = 0
    var hourN = 0
    var minuteN = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_picker_legacy)
//        val currentTimeMillis = oldCalendar.getTimeInMillis()
//        val newCalendar = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE)
//        newCalendar.setTimeInMillis(currentTimeMillis)
    }

    fun xianshi(v: View) {
        val time = TimeFragment()
        time.show(supportFragmentManager, "checkItems")
        time.onClick = object : TimeFragment.OnClickListener {
            override fun onClick(time: String) {
                btTime.text = time
            }
        }
    }
}