package com.sky.chowder.ui.activity

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import com.sky.chowder.C
import com.sky.chowder.R
import com.sky.chowder.ui.presenter.SolarP
import com.sky.utils.ScreenUtils
import com.sky.widget.SolarSystem
import common.base.BasePActivity
import kotlinx.android.synthetic.main.activity_solar.*

/**
 * Created by SKY on 15/12/9 下午8:54.
 * 卫星菜单栏
 */
class SolarSystemActivity : BasePActivity<SolarP>(), Toolbar.OnMenuItemClickListener {

    private var layoutDraw: AnimationDrawable? = null

    private var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == C.SOLAR) layoutDraw?.stop()
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_solar
    override fun creatPresenter() = SolarP(this)

    override fun initialize(savedInstanceState: Bundle?) {
        val width = ScreenUtils.getWidthPX(this)
        val lp = relative.layoutParams
        lp.height = width / 3
        lp.width = lp.height
        relative.layoutParams = lp
        relative.setBackgroundResource(R.drawable.solar_rect_list)
        layoutDraw = relative.background as AnimationDrawable

        val childCount = solar!!.childCount
        val childParams = FrameLayout.LayoutParams(width / 5, width / 5)
        for (i in 0 until childCount - 1) solar!!.getChildAt(i).layoutParams = childParams

//        solar.position=SolarSystem.CENTER_BOTTOM
        solar?.radius = width / 3
        solar?.rotateMenu = true//按钮是否旋转
        solar?.isRecoverChild = false
        solar.menuState = { state ->
            if (state) showToast("打开") else showToast("关闭")
            layoutDraw?.start()
            handler.sendEmptyMessageDelayed(C.SOLAR, 600)
        }
        solar?.menuItemOnClick = { view, position ->
            //可以把所需要跳转的activity的全称写在tag里
            when (view.tag) {
                "flow" -> showToast("position==$position")
                "list" -> showToast("position==$position")
                "viewpager" -> showToast("position==$position")
                "recyclerview" -> showToast("position==$position")
                "bitmap" -> showToast("position==$position")
                "slidingmenu" -> showToast("position==$position")
            }
        }
        solar?.toggleMenu(300)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_solar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_center -> solar?.position = SolarSystem.CENTER
            R.id.action_left_top -> solar?.position = SolarSystem.LEFT_TOP
            R.id.action_left_bottom -> solar?.position = SolarSystem.LEFT_BOTTOM
            R.id.action_right_top -> solar?.position = SolarSystem.RIGHT_TOP
            R.id.action_right_bottom -> solar?.position = SolarSystem.RIGHT_BOTTOM
            R.id.action_center_top -> solar?.position = SolarSystem.CENTER_TOP
            R.id.action_center_bottom -> solar?.position = SolarSystem.CENTER_BOTTOM
            R.id.action_center_left -> solar?.position = SolarSystem.CENTER_LEFT
            R.id.action_center_right -> solar?.position = SolarSystem.CENTER_RIGHT
        }
        return false
    }

}