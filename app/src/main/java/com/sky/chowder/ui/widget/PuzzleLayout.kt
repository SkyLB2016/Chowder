package com.sky.chowder.ui.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.sky.chowder.R
import com.sky.chowder.model.ImagePiece
import java.util.*

/**
 * Created by SKY on 2015/12/24 10:58.
 * 拼图游戏
 */
class PuzzleLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr), View.OnClickListener {

    init {
        initView()
    }

    private var imageViews: Array<ImageView?>? = null
    private var imagePieces: List<ImagePiece>? = null
    private var margin = 2
    private var piece = 3

    private fun initView() {
        margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, margin.toFloat(), resources.displayMetrics).toInt()
    }

    private var once = true
    private var pieceWidth: Int = 0
    private var width: Int? = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        width = Math.min(measuredHeight, measuredWidth)
        width = 1080

        setMeasuredDimension(width!!, width!!)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (once) {
//            setView()
            once = false
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.color = Color.BLACK
        paint.strokeWidth = 200f
        paint.style = Paint.Style.STROKE
        canvas?.drawLine(0f, 0f, 900f, 450f, paint)
    }

    private fun setView() {
        imageViews = arrayOfNulls<ImageView>(piece * piece)
        imagePieces = jigsaw(BitmapFactory.decodeResource(resources, R.mipmap.ic_banner), piece)
        //随机打乱数组排序
        Collections.sort(imagePieces!!) { _, _ -> if (Math.random() > 0.5) 1 else -1 }
        pieceWidth = (width!! - margin * (piece + 1)) / piece
        for (i in imagePieces!!.indices) {
            val imageView = ImageView(context)
            imageView.setImageBitmap(imagePieces!![i].bitmap)
            imageView.setOnClickListener(this)
            imageView.id = i + 1
            imageView.tag = i.toString() + "," + imagePieces!![i].number
            imageViews!![i] = imageView
            val lp = RelativeLayout.LayoutParams(pieceWidth, pieceWidth)
            val leftMargin = i % piece * pieceWidth + (i % piece + 1) * margin
            val topMargin = i / piece * pieceWidth + (i / piece + 1) * margin
            lp.setMargins(leftMargin, topMargin, 0, 0)
            addView(imageView, lp)
        }
    }

    internal fun jigsaw(bitmap: Bitmap, piece: Int): List<ImagePiece> {
        val imagePieces = ArrayList<ImagePiece>()

        val width = bitmap.width
        val height = bitmap.height
        val pieceWidth = Math.min(width, height) / piece
        //行列
        for (i in 0 until piece) for (j in 0 until piece) {
            val image = ImagePiece()
            image.number = i * piece + j
            val x = j * pieceWidth
            val y = i * pieceWidth
            image.bitmap = Bitmap.createBitmap(bitmap, x, y, pieceWidth, pieceWidth)
            imagePieces.add(image)
        }
        return imagePieces
    }

    private var firstImg: ImageView? = null
    private var secondImg: ImageView? = null
    private var isAniming = false

    override fun onClick(v: View) {
        if (isAniming)
            return
        if (firstImg === v) {
            firstImg!!.colorFilter = null
            firstImg = null
            return

        }
        if (firstImg == null) {
            firstImg = v as ImageView
            firstImg!!.setColorFilter(Color.parseColor("#66ff0000"))

        } else {
            isAniming = true
            secondImg = v as ImageView
            firstImg!!.visibility = View.INVISIBLE
            secondImg!!.visibility = View.INVISIBLE
            val firBit = imagePieces!![Integer.parseInt(getViewIdByTag(firstImg!!))].bitmap
            val secBit = imagePieces!![Integer.parseInt(getViewIdByTag(secondImg!!))].bitmap

            val aniLayout = RelativeLayout(context)
            addView(aniLayout)


            val firstlp = firstImg!!.layoutParams as RelativeLayout.LayoutParams
            val firstLeft = firstlp.leftMargin
            val firstTop = firstlp.topMargin
            val secLP = secondImg!!.layoutParams as RelativeLayout.LayoutParams
            val secLeft = secLP.leftMargin
            val secTop = secLP.topMargin
            val aniFirst = ImageView(context)
            val aniSec = ImageView(context)
            aniFirst.layoutParams = firstlp
            aniSec.layoutParams = secLP
            aniFirst.setImageBitmap(firBit)
            aniSec.setImageBitmap(secBit)

            aniLayout.addView(aniFirst)
            aniLayout.addView(aniSec)
            //            firstImg.setLayoutParams(secLP);
            //            secondImg.setLayoutParams(firstlp);
            val firstX = ObjectAnimator.ofFloat(aniFirst, "translationX", 0f, (secLeft - firstLeft).toFloat())
            val firstY = ObjectAnimator.ofFloat(aniFirst, "translationY", 0f, (secTop - firstTop).toFloat())
            val secX = ObjectAnimator.ofFloat(aniSec, "translationX", 0f, (firstLeft - secLeft).toFloat())
            val secY = ObjectAnimator.ofFloat(aniSec, "translationY", 0f, (firstTop - secTop).toFloat())

            val set = AnimatorSet()
            set.interpolator = BounceInterpolator()
            set.playTogether(firstX, firstY, secX, secY)
            set.duration = 1000
            set.start()
            set.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    val firTag = getViewTag(firstImg!!)
                    val secTag = getViewTag(secondImg!!)

                    getViewTag(firstImg!!)
                    firstImg!!.setImageBitmap(secBit)
                    secondImg!!.setImageBitmap(firBit)
                    firstImg!!.tag = secTag
                    secondImg!!.tag = firTag
                    firstImg!!.visibility = View.VISIBLE
                    secondImg!!.visibility = View.VISIBLE

                    //                    secondImg
                    aniLayout.removeAllViews()
                    firstImg!!.colorFilter = null
                    secondImg = null
                    firstImg = secondImg
                    isAniming = false
                    checkSuccess()

                }
            })
        }
    }

    private fun getViewTag(view: ImageView): String {
        return view.tag as String
    }

    private fun getViewIdByTag(view: ImageView): String {
        return getViewTag(view).split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]

    }

    private fun getViewIndexByTag(view: ImageView): String {
        return getViewTag(view).split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
    }

    private fun checkSuccess() {
        val isSuccess = imagePieces!!.indices.none { getViewIndexByTag(imageViews!![it]!!) != "" + it }
        //上下两种写法效果一致
//        for (i in imagePieces!!.indices) {
//            if (getViewIndexByTag(imageViews!![i]!!) != "" + i) {
//                isSuccess = false
//            }
//        }
        if (isSuccess) {
            Toast.makeText(context, "success", Toast.LENGTH_LONG).show()
            removeAllViews()
            piece++
            setView()
        }
    }
}