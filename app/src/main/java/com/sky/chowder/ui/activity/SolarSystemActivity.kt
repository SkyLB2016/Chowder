package com.sky.chowder.ui.activity

import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.os.Message
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import com.sky.base.BasePActivity
import com.sky.chowder.C
import com.sky.chowder.R
import com.sky.chowder.ui.presenter.SolarP
import com.sky.chowder.ui.widget.SolarSystem
import com.sky.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_solar.*

/**
 * Created by SKY on 15/12/9 下午8:54.
 * 卫星菜单栏
 */
class SolarSystemActivity : BasePActivity<SolarP>(), Toolbar.OnMenuItemClickListener {

    private var layoutDraw: AnimationDrawable? = null

    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == C.handler_0x002) layoutDraw?.stop()
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_solar

    override fun creatPresenter() {
        presenter = SolarP(this)
    }

    override fun initialize() {
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

        //solarSystem.setPosition(SolarSystem.CENTER_BOTTOM);
        solar?.setRadius(width / 3)
        solar?.setRotaMenu(true)//按钮是否旋转
        solar?.setIsRotate(true)//混合还是单次执行
        solar?.isRecoverChildView = false
        solar?.setOnMenuState(object : SolarSystem.MenuState {
            override fun openMenu() {
                layoutDraw?.start()
                handler.sendEmptyMessageDelayed(C.handler_0x002, 600)
            }

            override fun closeMenu() {
                layoutDraw?.start()
                handler.sendEmptyMessageDelayed(C.handler_0x002, 600)
            }
        })
        solar?.setOnMenuItemClickListener { view, position ->
            //可以把所需要跳转的activity的全称写在tag里
            //JumpAct.jumpActivity(SolarSystemActivity.this, tag);
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
            R.id.action_center -> solar?.setPosition(SolarSystem.CENTER)
            R.id.action_left_top -> solar?.setPosition(SolarSystem.LEFT_TOP)
            R.id.action_left_bottom -> solar?.setPosition(SolarSystem.LEFT_BOTTOM)
            R.id.action_right_top -> solar?.setPosition(SolarSystem.RIGHT_TOP)
            R.id.action_right_bottom -> solar?.setPosition(SolarSystem.RIGHT_BOTTOM)
            R.id.action_center_top -> solar?.setPosition(SolarSystem.CENTER_TOP)
            R.id.action_center_bottom -> solar?.setPosition(SolarSystem.CENTER_BOTTOM)
            R.id.action_center_left -> solar?.setPosition(SolarSystem.CENTER_LEFT)
            R.id.action_center_right -> solar?.setPosition(SolarSystem.CENTER_RIGHT)
        }
        return false
    }

}