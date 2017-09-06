package com.sky.chowder.ui.activity

import android.graphics.drawable.AnimationDrawable
import android.os.Handler
import android.os.Message
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.RelativeLayout
import butterknife.BindView
import com.sky.base.BasePActivity
import com.sky.chowder.C
import com.sky.chowder.R
import com.sky.chowder.ui.presenter.SolarP
import com.sky.chowder.ui.widget.SolarSystem
import com.sky.utils.LogUtils
import com.sky.utils.SPUtils
import com.sky.utils.ScreenUtils

/**
 * Created by sky on 15/12/9 下午8:54.
 * 卫星菜单栏
 */
class SolarSystemActivity : BasePActivity<SolarP>(), Toolbar.OnMenuItemClickListener {

    @BindView(R.id.solar)
    internal var solarSystem: SolarSystem? = null
    private var layoutDraw: AnimationDrawable? = null
    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == C.handler_0x002)
                layoutDraw!!.stop()
        }
    }

    public override fun getLayoutResId(): Int {
        return R.layout.activity_solar
    }

    public override fun initialize() {
        val relative = getView<RelativeLayout>(R.id.relative)
        val width = ScreenUtils.getWH(this)[0]
        val lp = relative.layoutParams
        lp.height = width / 3
        lp.width = lp.height
        relative.layoutParams = lp
        relative.setBackgroundResource(R.drawable.solar_rect_list)
        layoutDraw = relative.background as AnimationDrawable

        val childCount = solarSystem!!.childCount
        val childParams = FrameLayout.LayoutParams(width / 5, width / 5)
        for (i in 0..childCount - 1 - 1) {
            solarSystem!!.getChildAt(i).layoutParams = childParams
        }
        //solarSystem.setPosition(SolarSystem.CENTER_BOTTOM);
        solarSystem!!.setRadius(width / 3)
        solarSystem!!.setRotaMenu(true)//按钮是否旋转
        solarSystem!!.setIsRotate(true)//混合还是单次执行
        solarSystem!!.isRecoverChildView = false
        solarSystem!!.setOnMenuState(object : SolarSystem.MenuState {
            override fun openMenu() {
                layoutDraw!!.start()
                presenter.sendEvent(2001)
                handler.sendEmptyMessageDelayed(C.handler_0x002, 600)
            }

            override fun closeMenu() {
                layoutDraw!!.start()
                handler.sendEmptyMessageDelayed(C.handler_0x002, 600)
            }
        })
        solarSystem!!.setOnMenuItemClickListener { view, position ->
            val tag = view.tag as String
            //可以把所需要跳转的activity的全称写在tag里
            //                JumpAct.jumpActivity(SolarSystemActivity.this, tag);
            when (tag) {
                "flow" -> testSP()
                "list" -> testSPIn()
                "viewpager" -> {
                }
                "recyclerview" -> {
                }
                "bitmap" -> {
                }
                "slidingmenu" -> {
                }
            }//JumpAct.jumpActivity(SolarSystemActivity.this, CarouselActivity.class);
            //                    JumpAct.jumpActivity(SolarSystemActivity.this, RefreshListActivity.class);
            //                    JumpAct.jumpActivity(SolarSystemActivity.this, TabLayoutActivity.class);
            //                    JumpAct.jumpActivity(SolarSystemActivity.this, ImageUriActivity.class);
            //                    JumpAct.jumpActivity(SolarSystemActivity.this, BottomTabBarActivity.class);
            //                    JumpAct.jumpActivity(SolarSystemActivity.this, SlidingMenuActivity.class);
        }
        solarSystem!!.toggleMenu(300)
    }

    private fun testSP() {
        var time = System.currentTimeMillis()
        for (i in 0..9) {
            //            SPUtilsIn.put(SolarSystemActivity.this,"testsp" + i, "test+" + i);
        }
        time = System.currentTimeMillis() - time
        LogUtils.i("SPUtilsIn==" + time)

    }

    private fun testSPIn() {
        var time = System.currentTimeMillis()
        for (i in 0..9) {
            SPUtils.getInstance().put("testsp" + i, "test+" + i)
        }
        time = System.currentTimeMillis() - time
        LogUtils.i("SPUtils==" + time)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_solar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_center -> solarSystem!!.setPosition(SolarSystem.CENTER)
            R.id.action_left_top -> solarSystem!!.setPosition(SolarSystem.LEFT_TOP)
            R.id.action_left_bottom -> solarSystem!!.setPosition(SolarSystem.LEFT_BOTTOM)
            R.id.action_right_top -> solarSystem!!.setPosition(SolarSystem.RIGHT_TOP)
            R.id.action_right_bottom -> solarSystem!!.setPosition(SolarSystem.RIGHT_BOTTOM)
            R.id.action_center_top -> solarSystem!!.setPosition(SolarSystem.CENTER_TOP)
            R.id.action_center_bottom -> solarSystem!!.setPosition(SolarSystem.CENTER_BOTTOM)
            R.id.action_center_left -> solarSystem!!.setPosition(SolarSystem.CENTER_LEFT)
            R.id.action_center_right -> solarSystem!!.setPosition(SolarSystem.CENTER_RIGHT)
        }
        return false
    }

    override fun creatPresenter() {
        presenter = SolarP(this)
    }
}