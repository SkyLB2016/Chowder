package com.sky.chowder.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.support.v4.view.GravityCompat
import android.view.View
import butterknife.OnClick
import com.sky.base.BaseNoPActivity
import com.sky.chowder.R
import kotlinx.android.synthetic.main.activity_drawer.*

/**
 * Created by SKY on 2015年3月26日 下午4:01:00.
 * DrawerLayout的应用
 */
class DrawerActivity : BaseNoPActivity() {

    override fun getLayoutResId(): Int = R.layout.activity_drawer

    override fun initialize() {
        super.initialize()
        baseTitle.setLeftButton(R.mipmap.ic_menu)

        //侧划栏中的点击事件
        navigation!!.setNavigationItemSelectedListener { item ->
            SetIntent()
            item.isChecked = true
            drawer!!.closeDrawers()
            true
        }
    }

    @OnClick(R.id.tv_left)
    fun onClick(v: View) {
        drawer!!.openDrawer(GravityCompat.START)
    }

    private fun SetIntent() {
        val intent = Intent()
        intent.action = "com.sky.action"
        intent.action = Intent.ACTION_MAIN
        //        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addCategory(Intent.CATEGORY_SAMPLE_CODE)
        startActivity(intent)
    }

    private fun getViewBitmap(addViewContent: View): Bitmap {

        addViewContent.isDrawingCacheEnabled = true

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        addViewContent.layout(0, 0,
                addViewContent.measuredWidth,
                addViewContent.measuredHeight)

        addViewContent.buildDrawingCache()
        val cacheBitmap = addViewContent.drawingCache

        return Bitmap.createBitmap(cacheBitmap)
    }
}
