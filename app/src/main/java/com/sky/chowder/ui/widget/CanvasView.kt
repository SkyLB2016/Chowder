package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.*
import android.view.View
import java.util.*

/**
 * Created by SKY on 2017/3/9 20:52.
 */
class CanvasView(context: Context) : View(context) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint()
        val mShader = LinearGradient(0f, 0f, 100f, 100f,
                intArrayOf(Color.RED, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.LTGRAY), null, Shader.TileMode.REPEAT) // 一个材质,打造出一个线性梯度沿著一条线。
        paint.shader = mShader
        val rect = RectF(0f, 0f, 300f, 400f)
        canvas.drawOval(rect, paint)

        paint.reset()
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 20f
        paint.color = Color.GREEN

        val random = Random()
        val list = (0..20).mapTo(ArrayList()) { Point(400 + it * 30, random.nextInt(400) + 10) }
        val bezierPath = calculateCubicPath(list)//获取三次方贝赛尔曲线
        canvas.drawPath(bezierPath, paint)//画出贝塞尔曲线
        //待计算二次贝塞尔曲线控制点

        val path3 = Path()
        path3.moveTo(list[0].x * 1f, list[0].y * 1f)//设置Path的起点
        for (i in 1 until list.size) {
            path3.lineTo(list[i].x * 1f, list[i].y * 1f)
        }
        paint.strokeWidth = 5f
        paint.color = Color.RED
        paint.pathEffect = CornerPathEffect(20f)//圆角
//        paint.pathEffect = DiscretePathEffect(2f, 3f)//毛茸茸的线
//        paint.pathEffect = DashPathEffect(floatArrayOf(20f,10f,5f,10f),0f)//虚线
//        val pp = Path()
//        pp.addRect(0f, 0f, 8f, 8f, Path.Direction.CCW)
//        paint.pathEffect = PathDashPathEffect(pp, 12f, 0f, PathDashPathEffect.Style.ROTATE)//自定义的虚线
//        paint.pathEffect = ComposePathEffect(CornerPathEffect(20f),DiscretePathEffect(2f,3f))//组合
//        paint.pathEffect = SumPathEffect(CornerPathEffect(20f),DiscretePathEffect(2f,3f))//组合
        canvas.drawPath(path3, paint)

        paint.style = Paint.Style.FILL
        paint.textSize = 40f
        for (i in 0 until list.size) {
            canvas.drawCircle(list[i].x * 1f, list[i].y * 1f, 10f, paint)// 小圆
            canvas.drawText("" + i, list[i].x * 1f, list[i].y * 1f, paint)
        }
    }

    /**
     * 三次贝赛尔曲线计算
     */
    private fun calculateCubicPath(list: ArrayList<Point>): Path {
        val bezierPath = Path()
        bezierPath.moveTo(list[0].x * 1f, list[0].y * 1f)//设置Path的起点
        val control = calculateCubicContro(list)
        for (i in 0 until list.size - 1) {
            when (i) {
                0 -> bezierPath.quadTo(control[i].x * 1f, control[i].y * 1f, list[i + 1].x * 1f, list[i + 1].y * 1f)
                list.size - 2 -> bezierPath.quadTo(control[i * 2 - 1].x * 1f, control[i * 2 - 1].y * 1f, list[i + 1].x * 1f, list[i + 1].y * 1f)
                else -> bezierPath.cubicTo(control[i * 2 - 1].x * 1f, control[i * 2 - 1].y * 1f,
                        control[i * 2].x * 1f, control[i * 2].y * 1f,
                        list[i + 1].x * 1f, list[i + 1].y * 1f)
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
            val centerX = (list[i].x + list[i + 1].x) / 2
            val centerY = (list[i].y + list[i + 1].y) / 2
            val centerX1 = (list[i + 1].x + list[i + 2].x) / 2
            val centerY1 = (list[i + 1].y + list[i + 2].y) / 2

            val centerX2 = (centerX + centerX1) / 2
            val centerY2 = (centerY + centerY1) / 2

            val tranX = centerX2 - list[i + 1].x
            val tranY = centerY2 - list[i + 1].y

            val conX1 = centerX - tranX
            val conY1 = centerY - tranY

            val conX2 = centerX1 - tranX
            val conY2 = centerY1 - tranY
            control.add(Point(conX1, conY1))
            control.add(Point(conX2, conY2))
        }
        return control
    }
}