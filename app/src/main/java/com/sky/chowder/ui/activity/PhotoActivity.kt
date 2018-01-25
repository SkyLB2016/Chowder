package com.sky.chowder.ui.activity

import android.content.Intent
import android.view.View
import butterknife.OnClick
import com.sky.SkyApp
import com.sky.base.BasePActivity
import com.sky.chowder.R
import com.sky.chowder.ui.presenter.SolarP
import com.sky.utils.BitmapUtils
import com.sky.utils.LogUtils
import com.sky.utils.PhotoUtils
import kotlinx.android.synthetic.main.test_image.*
import java.io.File

/**
 * Created by SKY on 2017/6/15.
 */
class PhotoActivity : BasePActivity<SolarP>() {

    private lateinit var photoUtils: PhotoUtils

    override fun getLayoutResId(): Int = R.layout.test_image
    override fun creatPresenter(): SolarP = SolarP(this)
    override fun initialize() = Unit

    @OnClick(R.id.image)
    fun onClick(v: View) {
        getPhoto()
    }

    private fun getPhoto() {
        val photoPath = SkyApp.getInstance().picCacheDir + System.currentTimeMillis() + ".jpg"
        photoUtils = PhotoUtils(this, photoPath)
        photoUtils?.setUploadPicture { photoName, bitmap ->
            image.setImageBitmap(bitmap)
            LogUtils.i("photo==$photoName")
            LogUtils.i("压缩后所占内存大小==${bitmap.allocationByteCount / 1024}KB")
            LogUtils.i("原图所占内存大小==${BitmapUtils.getBitmapFromPath(photoName).allocationByteCount / 1024 / 1024}MB")

            val pathname = SkyApp.getInstance().picCacheDir + System.currentTimeMillis() % 1000 + ".jpg"
            BitmapUtils.saveBitmapToFile(bitmap, pathname)//保存照片到应用缓存文件目录下
            LogUtils.i("原图文件大小==${File(photoName).length() / 1024 / 1024}MB")
            LogUtils.i("压缩后文件大小==${File(pathname).length() / 1024}KB")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        photoUtils?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        photoUtils?.onActivityResult(requestCode, resultCode, data)
    }

}
