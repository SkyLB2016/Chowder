package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.sky.chowder.R

/**
 * Created by SKY on 2015/12/3 11:24.
 */
class CircleProgress @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var area: RectF? = null//控件所占矩形
    var radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f, resources.displayMetrics).toInt()

    private var totalPaint: Paint? = null//总进度条画笔
    private var progressPaint: Paint? = null//进度条画笔
    private var totalColor = Color.BLACK//总进度条颜色
    private var progressColor = Color.RED//进度条颜色
    private var progressWidth = 5//进度条宽度

    private var textPaint: Paint? = null//字体画笔
    private var textBound: Rect? = null//字体所占空间
    private var mText: String? = ""
    var textColor = Color.BLACK//字体颜色
    var textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics).toInt()//字体大小
    private var textbackground: Drawable? = null//文字的背景

    /**
     * 按百分比计算
     */
    private var value = 50f //进度条所占百分比
        set(value) {
            field = value
            invalidate()
        }

    var startAngle = 140f//起始角度
    var sweepAngle = 260f//旋转角度

    init {
        val style = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress)
        (0 until style.indexCount)
                .asSequence()
                .map { style.getIndex(it) }
                .forEach {
                    when (it) {
                        R.styleable.CircleProgress_radius -> radius = style.getDimensionPixelSize(it, radius)
                        R.styleable.CircleProgress_totalColor -> totalColor = style.getColor(it, totalColor)
                        R.styleable.CircleProgress_proColor -> progressColor = style.getColor(it, progressColor)
                        R.styleable.CircleProgress_proWidth -> progressWidth = style.getDimensionPixelSize(it, progressWidth)
                        R.styleable.CircleProgress_text -> mText = style.getString(it)
                        R.styleable.CircleProgress_textsize -> textSize = style.getDimensionPixelSize(it, textSize)
                        R.styleable.CircleProgress_textcolor -> textColor = style.getColor(it, textColor)
                        R.styleable.CircleProgress_textBackground -> textbackground = style.getDrawable(it)
                    }
                }
        style.recycle()

        setTextPaint()
        setProgressPaint()
    }

    private fun setProgressPaint() {
        totalPaint = Paint()
        totalPaint!!.color = totalColor
        totalPaint!!.strokeWidth = progressWidth.toFloat()
        totalPaint!!.style = Paint.Style.STROKE
        totalPaint!!.isAntiAlias = true
        progressPaint = Paint()
        progressPaint!!.color = progressColor
        progressPaint!!.strokeWidth = progressWidth.toFloat()
        progressPaint!!.style = Paint.Style.STROKE
        progressPaint!!.isAntiAlias = true
        progressPaint!!.shader = LinearGradient(0f, 0f, 400f, 0f, intArrayOf(progressColor, Color.WHITE), null, Shader.TileMode.CLAMP)
    }

    /**
     * 设置画笔，并测量文件所占空间
     */
    private fun setTextPaint() {
        textPaint = Paint()
        textPaint!!.textSize = textSize.toFloat()
        textPaint!!.color = textColor
        textBound = Rect()
        textPaint!!.getTextBounds(mText, 0, mText!!.length, textBound)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var areaWidth = radius * 2//area所占空间
        areaWidth = Math.min(areaWidth, measuredWidth)
        areaWidth = Math.min(areaWidth, measuredHeight)

        radius = areaWidth / 2
        val left = measuredWidth / 2 - radius
        val top = measuredHeight / 2 - radius
        area = RectF(left.toFloat(), top.toFloat(), (left + areaWidth).toFloat(), (top + areaWidth).toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画出底层进度条
        canvas.drawArc(area!!, startAngle, sweepAngle, false, totalPaint!!)
        //按百分比画出进度条
        canvas.drawArc(area!!, startAngle, sweepAngle * this.value / 100, false, progressPaint!!)

        //开始计算文字所占的位置
        //计算出每份所占PI
        val angle = Math.PI / 180
        val endAngle = ((startAngle + sweepAngle) % 360).toDouble()//求出终点
        //计算起始点的XY的位置
        val startX = (radius * Math.cos(angle * startAngle)).toFloat()
        val startY = (radius * Math.sin(angle * startAngle)).toFloat()
        //计算终点的XY的位置
        val endX = (radius * Math.cos(angle * endAngle)).toFloat()
        val endY = (radius * Math.sin(angle * endAngle)).toFloat()
        //获取圆心XY
        val centerX = area!!.centerX()
        val centerY = area!!.centerY()
        //计算能分配给文字所占的最大空间，高设为文字自身高的三倍
        val textBoundy = centerY + startY - textBound!!.height() * 3 / 2
        val textRect = Rect((centerX + startX + 20f).toInt(), textBoundy.toInt(),
                (centerX + endX - 20).toInt(), (textBoundy + textBound!!.height() * 3).toInt())
        textbackground!!.bounds = textRect//为文字设置背景
        textbackground!!.draw(canvas)//画入画布中
        //让文字居于背景中间，计算文字的左距离与底部距离
        val left = textRect.left + (textRect.width() - textBound!!.width()) / 2
        val bottom = textRect.top + textRect.height() - textBound!!.height()
        canvas.drawText(mText!!, left.toFloat(), bottom.toFloat(), textPaint!!)//画入画布中

        //        for (int i = 0; i < 24; i++) {
        //            float startX = (float) (radius * Math.cos(angle * i));
        //            float startY = (float) (radius * Math.sin(angle * i));
        //            canvas.drawText("b", centerX + startX, centerY + startY, textPaint);
        //        }
    }
}