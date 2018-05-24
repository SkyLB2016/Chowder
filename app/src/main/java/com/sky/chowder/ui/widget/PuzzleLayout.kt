package com.sky.chowder.ui.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.sky.chowder.R
import com.sky.chowder.model.ImagePiece
import com.sky.utils.LogUtils
import com.sky.utils.ScreenUtils
import java.util.*

/**
 * Created by SKY on 2015/12/24 10:58.
 * 拼图游戏
 */
class PuzzleLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener {

    //private lateinit var imageViews: Array<ImageView?>
    private lateinit var images: List<ImagePiece>
    var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_puzzle)
        set(value) {
            LogUtils.i("总内存==${value.allocationByteCount / 1024}")
            field = value
            images = cutImage(value, piece)
            resetView()
            invalidate()
        }
    private var margin = 2//分割后图片之间的间隔
    var piece = 3//几行几列
        set(value) {
//            if (isSuccess) Toast.makeText(context, "请先完成本次拼图", Toast.LENGTH_LONG).show()
            field += value
            when {
                field < 2 -> field = 2
                field > 10 -> field = 10
                else -> {
                    removeAllViews()
                    setView()
                }
            }
        }
    private var once = true
    private var pieceWidth: Int = 0
    private var pieceH: Int = 0
    private var width: Int? = 0
    private var height: Int? = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        width = ScreenUtils.getWidthPX(context)
        height = ScreenUtils.getHeightPX(context) / 4 * 3
        setMeasuredDimension(width!!, height!!)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (once) {
            setView()
            once = false
        }
    }

    private fun setView() {
//        imageViews = arrayOfNulls<ImageView>(piece * piece)
        images = cutImage(bitmap, piece)

        //随机打乱数组排序
//        Collections.sort(images) { _, _ -> if (Math.random() > 0.5) 1 else -1 }
//        Collections.shuffle(images)
        pieceWidth = (width!! - margin * (piece + 1)) / piece
        pieceH = (height!! - margin * (piece + 1)) / piece
        for (i in images!!.indices) {
            val child = ImageView(context)
            child.setImageBitmap(images!![i].bitmap)
            child.scaleType = ImageView.ScaleType.FIT_XY
            child.setOnClickListener(this)
//            child.id = i + 1
            child.tag = "$i,${images!![i].number}"//第一个数代表此图片在数组中的位置，第二个数是此图片正确的顺序
            val lp = RelativeLayout.LayoutParams(pieceWidth, pieceH)
            val leftMargin = i % piece * pieceWidth + (i % piece + 1) * margin
            val topMargin = i / piece * pieceH + (i / piece + 1) * margin
            lp.setMargins(leftMargin, topMargin, 0, 0)
            addView(child)
            child.layoutParams = lp//一定要放在addview之后
//            imageViews[i] = child
        }
    }

    private fun resetView() {
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i) as ImageView
            child.tag = "$i,${images!![i].number}"//第一个数代表此图片在数组中的位置，第二个数是此图片正确的顺序
            child.setImageBitmap(images!![i].bitmap)
        }
    }

    private fun cutImage(bitmap: Bitmap, piece: Int): List<ImagePiece> {
        val pieces = ArrayList<ImagePiece>()
        val pW = bitmap.width / piece
        val pH = bitmap.height / piece
        val total = piece * piece
        var image: ImagePiece
        for (i in 0 until total) {
            image = ImagePiece()
            image.number = i
//            image.bitmap = Bitmap.createBitmap(bitmap, i % piece * pW, i / piece * pW, pW, pW)
            image.bitmap = Bitmap.createBitmap(bitmap, i % piece * pW, i / piece * pH, pW, pH)

            LogUtils.i("分内存==${image.bitmap!!.allocationByteCount / 1024}")

            pieces.add(image)
        }
        pieces.shuffle()
        return pieces
    }

    private var firstImg: ImageView? = null
    private var secondImg: ImageView? = null
    private var isAniming = false

    override fun onClick(v: View) {
        if (isAniming) return
        if (firstImg === v) {
            firstImg?.colorFilter = null
            firstImg = null
            return
        }
        if (firstImg === null) {
            firstImg = v as ImageView
            firstImg?.setColorFilter(Color.parseColor("#66ff0000"))
        } else {
            isAniming = true
            secondImg = v as ImageView
            firstImg?.visibility = View.INVISIBLE
            secondImg?.visibility = View.INVISIBLE
            val firBitmap = images!![getPosition(firstImg!!, 0)].bitmap
            val secBitmap = images!![getPosition(secondImg!!, 0)].bitmap

            val rl = RelativeLayout(context)//遮罩层
            addView(rl)

            val firstlp = firstImg?.layoutParams as RelativeLayout.LayoutParams
            val seclp = secondImg?.layoutParams as RelativeLayout.LayoutParams
            //替换的两个view
            val first = ImageView(context)
            first.layoutParams = firstlp
            first.setImageBitmap(firBitmap)

            val second = ImageView(context)
            second.layoutParams = seclp
            second.setImageBitmap(secBitmap)
            //加入布局中
            rl.addView(first)
            rl.addView(second)
            //移动的距离
            val tranX = seclp.leftMargin - firstlp.leftMargin
            val tranY = seclp.topMargin - firstlp.topMargin
            val set = AnimatorSet()
            set.interpolator = BounceInterpolator()
            set.playTogether(ObjectAnimator.ofFloat(first, "translationX", 0f, tranX * 1f),
                    ObjectAnimator.ofFloat(first, "translationY", 0f, tranY * 1f),
                    ObjectAnimator.ofFloat(second, "translationX", 0f, tranX * -1f),
                    ObjectAnimator.ofFloat(second, "translationY", 0f, tranY * -1f))
            set.duration = 200
            set.start()
            set.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    val firstTag = firstImg?.tag
                    val secondTag = secondImg?.tag
                    //设置bitmap，交换tag，并显示view
                    firstImg?.setImageBitmap(secBitmap)
                    firstImg?.tag = secondTag
                    firstImg?.visibility = View.VISIBLE

                    secondImg?.setImageBitmap(firBitmap)
                    secondImg?.tag = firstTag
                    secondImg?.visibility = View.VISIBLE

                    //移除新加的布局
                    rl.removeAllViews()
                    removeView(rl)

                    secondImg = null
                    firstImg?.colorFilter = null
                    isAniming = false
                    firstImg = null

                    //检测拼图是否完成
                    checkSuccess()
                }
            })
        }
    }

    private fun getPosition(view: View, index: Int): Int = view.tag.toString().split(",")[index].toInt()

    var checkSuccess: ((Boolean) -> Unit)? = null
    private fun checkSuccess() {
        val isSuccess = (0 until childCount).none { getPosition(getChildAt(it), 1) != it }
        if (isSuccess) {
            Toast.makeText(context, "拼图完成", Toast.LENGTH_LONG).show()
            piece = 1
//            checkSuccess?.invoke(isSuccess)
        }
    }
}