package com.sky.oa.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import com.sky.design.app.BaseActivity
import com.sky.design.widget.SolarSystem
import com.sky.oa.C
import com.sky.oa.R
import com.sky.sdk.utils.BitmapUtils
import com.sky.sdk.utils.LogUtils
import com.sky.sdk.utils.ScreenUtils
import kotlinx.android.synthetic.main.activity_solar.*

/**
 * Created by SKY on 15/12/9 下午8:54.
 * 卫星菜单栏
 */
class SolarSystemActivity : BaseActivity(), Toolbar.OnMenuItemClickListener {
    private var layoutDraw: AnimationDrawable? = null

    private var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if (msg.what == C.SOLAR) layoutDraw?.stop()
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_solar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

//        solar_svg.position=SolarSystem.CENTER_BOTTOM
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
                "slidingmenu" -> {
                    pickFile()
                    showToast("position==$position")
                }
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

    // 打开系统的文件选择器
    private fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "*/*"
        this.startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val error = "文件获取失败"
        if (resultCode != Activity.RESULT_OK) return
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val uri = data!!.data //获取uri
            if (uri == null) {
                showToast(error)
                return
            }

//                int index = cursor.getColumnIndexOrThrow(column);
//                return cursor.getString(index);
            LogUtils.i("columnValue==" + uri.path)

            val path = BitmapUtils.getRealPathFromURI(this, uri) //获取路径
            if (TextUtils.isEmpty(path)) {
                showToast(error)
                return
            }
            if (path.endsWith(".xlsx") || path.endsWith(".xls")) {
                showToast(path)
                showLoading()
            } else {
                showToast("文件不是Excel格式")
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1212) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                showToast("选择文件需要读写文件权限")
            }
        }
    }

}