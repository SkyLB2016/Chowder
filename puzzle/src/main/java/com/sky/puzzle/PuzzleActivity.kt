package com.sky.puzzle

import android.app.Activity
import android.content.Intent
import com.sky.base.BaseNoPActivity
import com.sky.utils.BitmapUtils
import com.sky.utils.PhotoUtils
import com.sky.utils.ScreenUtils
import com.sky.utils.TextUtil
import kotlinx.android.synthetic.main.activity_puzzle.*

/**
 * Created by SKY on 2015/8/19 15:31.
 * 拼图游戏
 */
class PuzzleActivity : BaseNoPActivity() {
    override fun getLayoutResId(): Int = R.layout.activity_puzzle
    override fun initialize() {
        btUp?.setOnClickListener { puzzle.piece = -1 }
        btChange?.setOnClickListener { getPhoto() }
        btNext?.setOnClickListener { puzzle.piece = 1 }
        val path = getObject("puzzle", "")
        if (path.isNotEmpty()) puzzle?.bitmap = BitmapUtils.getBitmapFromPath(path, 2048, 2048)
        val lp = ImgOriginal?.layoutParams
        lp?.width = ScreenUtils.getWidthPX(this) / 5
        lp?.height = ScreenUtils.getHeightPX(this) / 5
//        ImgOriginal.layoutParams = lp
        ImgOriginal?.setImageBitmap(puzzle.bitmap)

//        puzzle.checkSuccess = { b -> }
    }

    private var photoUtils: PhotoUtils? = null

    private fun getPhoto() {
        photoUtils = PhotoUtils(this, "", false)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        photoUtils?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        val error = getString(R.string.photo_fail)
        when (requestCode) {
            PhotoUtils.LOCAL_PHOTO -> {
                if (data == null) return
                val uri = data.data //获得图片的uri
                if (TextUtil.notNullObj(uri, error)) return
                val path = BitmapUtils.getRealPathFromURI(this, uri)//获取路径
                if (TextUtil.notNull(path, error)) return
                puzzle.bitmap = BitmapUtils.getBitmapFromPath(path, 1024, 1024)//获取bitmap
                ImgOriginal.setImageBitmap(puzzle.bitmap)
                setObject("puzzle", path)
            }
        }
    }

}