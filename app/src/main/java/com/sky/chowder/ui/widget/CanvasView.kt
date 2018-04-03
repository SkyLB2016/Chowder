package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.sky.chowder.R
import java.util.*

/**
 * Created by SKY on 2017/3/9 20:52.
 */
class CanvasView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var lastX = -1f
    private var lastY = -1f
    private var cX = -1f
    private var cY = -1f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.rawX
                lastY = event.rawY
                cX = event.x
                cY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val dX = (event.rawX - lastX).toInt()
                val dY = (event.rawY - lastY).toInt()
                //移动view所在位置
                layout(left + dX, top + dY, right + dX, bottom + dY)
//                offsetLeftAndRight(dX)
//                offsetTopAndBottom(dY)
//                (parent as View).scrollBy(-dX, -dY)
                cX = event.x
                cY = event.y
                lastX = event.rawX
                lastY = event.rawY
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                lastX = -1f
                lastY = -1f
                cY = -1f
                cX = cY
            }
        }
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        moveLayout(canvas)//移动布局坐标
        drawOval(canvas)//渐变圆
        drawBezier(canvas)//画贝赛尔曲线与pathEffect应用
        setProgress(canvas)//环形进度条
        drawClock(canvas)//时钟
        maskFilter(canvas)

//        animate().alpha(1f)
//                .rotation(180f)
//                .setDuration(3000)
//                .withStartAction { }
//                .withEndAction { }
//                .start()
    }

    private fun maskFilter(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.RED
        paint.style = Paint.Style.FILL_AND_STROKE
        val textP = TextPaint(Paint.ANTI_ALIAS_FLAG)
        textP.textSize = 32f
        textP.color = Color.BLUE
        textP.textAlign = Paint.Align.CENTER
        canvas.save()
        canvas.translate(0f, 800f)

        paint.maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.NORMAL)
        canvas.drawCircle(150f, 150f, 100f, paint)
        canvas.drawText("NORMAL", 150f, 166f, textP)

        paint.maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.SOLID)
        canvas.drawCircle(400f, 150f, 100f, paint)
        canvas.drawText("SOLID", 400f, 166f, textP)

        paint.maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.OUTER)
        canvas.drawCircle(650f, 150f, 100f, paint)
        canvas.drawText("OUTER", 650f, 166f, textP)

        paint.maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.INNER)
        canvas.drawCircle(900f, 150f, 100f, paint)
        canvas.drawText("INNER", 900f, 166f, textP)
        canvas.restore()
    }

    private fun moveLayout(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.RED
        if (lastX != -1f) canvas.drawCircle(cX, cY, 88f, paint)
        paint.color = Color.RED
        canvas.drawCircle(940f, 550f, 100f, paint)
        val num = canvas.saveLayerAlpha(0f, 0f, 1100f, 700f, 122, Canvas.ALL_SAVE_FLAG)
        paint.color = Color.GREEN
        canvas.drawCircle(1000f, 600f, 100f, paint)
        canvas.drawCircle(1100f, 700f, 100f, paint)
        canvas.restoreToCount(num)//移除
    }

    private fun drawClock(canvas: Canvas) {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(600f, 600f, 200f, paint)
        paint.style = Paint.Style.FILL
        for (i in 0..11) {
            when (i) {
                0, 3, 6, 9 -> {
                    paint.strokeWidth = 8f
                    paint.textSize = 30f
                    canvas.drawLine(600f, 410f, 600f, 440f, paint)
                    val w = paint.measureText("$i")
                    canvas.drawText("$i", 600f - w / 2, 440f + paint.descent() - paint.ascent(), paint)
                }
                else -> {
                    paint.strokeWidth = 6f
                    paint.textSize = 20f
                    canvas.drawLine(600f, 410f, 600f, 420f, paint)
                    val w = paint.measureText("$i")
                    canvas.drawText("$i", 600f - w / 2, 420f + paint.descent() - paint.ascent(), paint)
                }
            }
            canvas.rotate(30f, 600f, 600f)
        }
        paint.strokeWidth = 12f
        canvas.save()
        canvas.translate(600f, 600f)
        canvas.drawCircle(0f, 0f, 16f, paint)
        canvas.drawLine(0f, 0f, 75f, 30f, paint)
        paint.strokeWidth = 8f
        canvas.drawLine(0f, 0f, 0f, 100f, paint)
        canvas.restore()
    }

    private fun drawOval(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        val mShader = LinearGradient(0f, 0f, 100f, 100f,
                intArrayOf(Color.RED, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.LTGRAY), null, Shader.TileMode.REPEAT) //一个材质,打造出一个线性梯度沿著一条线。
        paint.shader = mShader
        val rect = RectF(0f, 0f, 300f, 400f)
        canvas.drawOval(rect, paint)
    }

    //画贝赛尔曲线与pathEffect应用
    private fun drawBezier(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f
        paint.color = Color.GREEN
        val random = Random()
        val list = (0..20).mapTo(ArrayList()) { Point(400 + it * 30, random.nextInt(390) + 10) }
        val bezierPath = calculateCubicPath(list)//获取三次方贝赛尔曲线
        canvas.drawPath(bezierPath, paint)//画出贝塞尔曲线

        val effectPath = Path()
        effectPath.moveTo(list[0].x * 1f, list[0].y * 1f)//设置Path的起点
        for (i in 1 until list.size) effectPath.lineTo(list[i].x * 1f, list[i].y * 1f)
        paint.strokeWidth = 5f
        paint.color = Color.RED
        paint.pathEffect = CornerPathEffect(20f)//圆角
        //paint.pathEffect = DiscretePathEffect(2f, 3f)//毛茸茸的线
        //paint.pathEffect = DashPathEffect(floatArrayOf(20f, 10f, 5f, 10f), 0f)//虚线
        //val pp = Path()
        //pp.addRect(0f, 0f, 8f, 8f, Path.Direction.CCW)
        //paint.pathEffect = PathDashPathEffect(pp, 12f, 0f, PathDashPathEffect.Style.ROTATE)//自定义的虚线
        //paint.pathEffect = ComposePathEffect(CornerPathEffect(20f), DiscretePathEffect(2f, 3f))//组合
        //paint.pathEffect = SumPathEffect(CornerPathEffect(20f), DiscretePathEffect(2f, 3f))//组合
        canvas.drawPath(effectPath, paint)
    }

    private fun setProgress(canvas: Canvas) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = ContextCompat.getColor(context, R.color.darkyellow)
        paint.style = Paint.Style.FILL

        val area = RectF(0f, 400f, 400f, 800f)
        canvas.drawRect(area, paint)

        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        paint.strokeWidth = 10f

        var start = 140f//起始角度
        var sweep = 260f//旋转角度
        paint.maskFilter = BlurMaskFilter(25f, BlurMaskFilter.Blur.OUTER)
        canvas.drawArc(area, start, sweep, false, paint)

        val textP = TextPaint()
        textP.textSize = 60f
        textP.pathEffect = CornerPathEffect(20f)//圆角
        textP.textAlign = Paint.Align.CENTER
        val text = "进度条"
        val textBound = Rect()
        textP.getTextBounds(text, 0, text.length, textBound)

        val radius = area.height() / 2
        //计算的文字所在的背景框的左侧，顶部，右侧，底部
        val leftX = radius * Math.cos(Math.PI / 180 * start) + area?.centerX() + 20//(20的偏移量)
        val topY = radius * Math.sin(Math.PI / 180 * start) + area.centerY() - textBound.height()
        val rightX = radius * Math.cos(Math.PI / 180 * (start + sweep)) + area?.centerX() - 20//(20的偏移量)
        val bottomY = topY + textBound.height() * 2
        //文字背景所在矩形
        val textRect = Rect(leftX.toInt(), topY.toInt(), rightX.toInt(), bottomY.toInt())
        val textbg = ContextCompat.getDrawable(context, R.drawable.sel_shape_rect)
        textbg.bounds = textRect//为文字设置背景
        textbg.draw(canvas)//画入画布中
        //让文字居于背景中间，计算文字的左距离与底部距离
        val offset = textP.fontMetricsInt.ascent - textP.fontMetricsInt.top
        val tY = textRect.exactCenterY() + textBound.height() / 2 - offset / 2
        canvas.drawText(text, textRect.exactCenterX(), tY, textP)//画入画布中
    }

    /**
     * 三次贝赛尔曲线计算
     */
    private fun calculateCubicPath(list: ArrayList<Point>): Path {
        val bezierPath = Path()
        val control = calculateCubicContro(list)
        for (i in 0..list.size - 2) {
            when (i) {
                0 -> {
                    bezierPath.moveTo(list[i].x * 1f, list[i].y * 1f)//移动到Path的起点，画下一个点
                    //第一条为二次贝赛尔曲线
                    bezierPath.quadTo(control[i].x * 1f, control[i].y * 1f, list[i + 1].x * 1f, list[i + 1].y * 1f)
                }
            //最后一条为二次贝赛尔曲线
                list.size - 2 -> bezierPath.quadTo(control[i * 2 - 1].x * 1f, control[i * 2 - 1].y * 1f, list[i + 1].x * 1f, list[i + 1].y * 1f)
                else -> bezierPath.cubicTo(control[i * 2 - 1].x * 1f, control[i * 2 - 1].y * 1f,//控制点
                        control[i * 2].x * 1f, control[i * 2].y * 1f,//控制点
                        list[i + 1].x * 1f, list[i + 1].y * 1f)//终点
            }
        }
        return bezierPath
    }

    /**
     * 三次贝赛尔曲线计算方法
     */
    private fun calculateCubicContro(list: ArrayList<Point>): ArrayList<Point> {
        val control = ArrayList<Point>()
        for (i in 0 until list.size - 2) {
            //通过三个点abc来确定两个中点ef
            val eX = (list[i].x + list[i + 1].x) / 2
            val eY = (list[i].y + list[i + 1].y) / 2
            val fX = (list[i + 1].x + list[i + 2].x) / 2
            val fY = (list[i + 1].y + list[i + 2].y) / 2
            //计算中点的中点g
            val gX = (eX + fX) / 2
            val gY = (eY + fY) / 2
            //计算公式:已知abc三点，efg三中点，求xy；e-x=f-b=g-y
            //计算中中点与第二个点的偏移量
            val offsetX = gX - list[i + 1].x
            val offsetY = gY - list[i + 1].y
            //计算出第一个控制点，此点用于控制a到b
            val conX1 = eX - offsetX
            val conY1 = eY - offsetY
            //计算出第二个控制点，此点用于控制b到c
            val conX2 = fX - offsetX
            val conY2 = fY - offsetY

            control.add(Point(conX1, conY1))
            control.add(Point(conX2, conY2))
        }
        return control
    }
}