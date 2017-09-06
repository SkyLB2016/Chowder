package com.sky.chowder.ui.widget.pending

import android.content.Context
import android.graphics.*
import android.view.View
import com.sky.chowder.R
import java.util.*

class DrawView(context: Context) : View(context) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /*
             * 方法 说明 drawRect 绘制矩形 drawCircle 绘制圆形 drawOval 绘制椭圆 drawPath 绘制任意多边形
             * drawLine 绘制直线 drawPoin 绘制点
             */
        // 创建画笔
        val p = Paint()
        p.color = Color.RED// 设置红色

        canvas.drawText("画圆：", 10f, 20f, p)// 画文本
        canvas.drawCircle(60f, 20f, 10f, p)// 小圆
        p.isAntiAlias = true// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
        canvas.drawCircle(120f, 20f, 20f, p)// 大圆

        canvas.drawText("画线及弧线：", 10f, 60f, p)
        p.color = Color.GREEN// 设置绿色
        canvas.drawLine(60f, 40f, 100f, 40f, p)// 画线
        canvas.drawLine(110f, 40f, 190f, 80f, p)// 斜线
        //画笑脸弧线
        p.style = Paint.Style.STROKE//设置空心
        val oval1 = RectF(150f, 20f, 180f, 40f)
        canvas.drawArc(oval1, 180f, 180f, false, p)//小弧形
        oval1.set(190f, 20f, 220f, 40f)
        canvas.drawArc(oval1, 180f, 180f, false, p)//小弧形
        oval1.set(160f, 30f, 210f, 60f)
        canvas.drawArc(oval1, 0f, 180f, false, p)//小弧形

        canvas.drawText("画矩形：", 10f, 80f, p)
        p.color = Color.GRAY// 设置灰色
        p.style = Paint.Style.FILL//设置填满
        canvas.drawRect(60f, 60f, 80f, 80f, p)// 正方形
        canvas.drawRect(60f, 90f, 160f, 100f, p)// 长方形

        canvas.drawText("画扇形和椭圆:", 10f, 120f, p)
        /* 设置渐变色 这个正方形的颜色是改变的 */
        val mShader = LinearGradient(0f, 0f, 100f, 100f,
                intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.LTGRAY), null, Shader.TileMode.REPEAT) // 一个材质,打造出一个线性梯度沿著一条线。
        p.shader = mShader
        // p.setColor(Color.BLUE);
        val oval2 = RectF(60f, 100f, 200f, 240f)// 设置个新的长方形，扫描测量
        canvas.drawArc(oval2, 200f, 130f, true, p)
        // 画弧，第一个参数是RectF：该类是第二个参数是角度的开始，第三个参数是多少度，第四个参数是真的时候画扇形，是假的时候画弧线
        //画椭圆，把oval改一下
        oval2.set(210f, 100f, 250f, 130f)
        canvas.drawOval(oval2, p)

        canvas.drawText("画三角形：", 10f, 200f, p)
        // 绘制这个三角形,你可以绘制任意多边形
        val path = Path()
        path.moveTo(80f, 200f)// 此点为多边形的起点
        path.lineTo(120f, 250f)
        path.lineTo(80f, 250f)
        path.close() // 使这些点构成封闭的多边形
        canvas.drawPath(path, p)

        // 你可以绘制很多任意多边形，比如下面画六连形
        p.reset()//重置
        p.color = Color.LTGRAY
        p.style = Paint.Style.STROKE//设置空心
        val path1 = Path()
        path1.moveTo(180f, 200f)
        path1.lineTo(200f, 200f)
        path1.lineTo(210f, 210f)
        path1.lineTo(200f, 220f)
        path1.lineTo(180f, 220f)
        path1.lineTo(170f, 210f)
        path1.close()//封闭
        canvas.drawPath(path1, p)
        /*
             * Path类封装复合(多轮廓几何图形的路径
             * 由直线段*、二次曲线,和三次方曲线，也可画以油画。drawPath(路径、油漆),要么已填充的或抚摸
             * (基于油漆的风格),或者可以用于剪断或画画的文本在路径。
             */

        //画圆角矩形
        p.style = Paint.Style.FILL//充满
        p.color = Color.LTGRAY
        p.isAntiAlias = true// 设置画笔的锯齿效果
        canvas.drawText("画圆角矩形:", 10f, 260f, p)
        val oval3 = RectF(80f, 260f, 200f, 300f)// 设置个新的长方形
        canvas.drawRoundRect(oval3, 20f, 15f, p)//第二个参数是x半径，第三个参数是y半径

        //        //画贝塞尔曲线
        //        canvas.drawText("画贝塞尔曲线:", 10, 310, p);
        //        p.reset();
        //        p.setStyle(Paint.Style.STROKE);
        //        p.setColor(Color.GREEN);
        //        p.setStrokeWidth(10);
        //        Path path2 = new Path();
        //        path2.moveTo(100, 320);//设置Path的起点
        //        path2.quadTo(150, 310, 170, 400); //设置贝塞尔曲线的控制点坐标和终点坐标
        //        canvas.drawPath(path2, p);//画出贝塞尔曲线
        //画贝塞尔曲线
        p.strokeWidth = 100f
        p.color = Color.RED
        p.textSize = 30f

        canvas.drawText("画贝塞尔曲线:", 10f, 310f, p)
        p.reset()
        p.style = Paint.Style.STROKE
        p.color = Color.GREEN
        p.strokeWidth = 10f

        val list = ArrayList()
        val random = Random()
        for (i in 0..30) {
            list.add(Point(100 + i * 30, random.nextInt(500) + 500))
        }
        val path2 = Path()
        path2.moveTo(list.get(0).x.toFloat(), list.get(0).y.toFloat())//设置Path的起点
        val point = Paint()
        point.color = Color.RED
        point.style = Paint.Style.FILL
        point.strokeWidth = 10f
        point.textSize = 40f
        val control = ArrayList<Point>()
        for (i in 0..list.size - 2 - 1) {
            val centerX = (list.get(i).x + list.get(i + 1).x) / 2
            val centerY = (list.get(i).y + list.get(i + 1).y) / 2
            val centerX1 = (list.get(i + 1).x + list.get(i + 2).x) / 2
            val centerY1 = (list.get(i + 1).y + list.get(i + 2).y) / 2

            val centerX2 = (centerX + centerX1) / 2
            val centerY2 = (centerY + centerY1) / 2


            val tranX = centerX2 - list.get(i + 1).x
            val tranY = centerY2 - list.get(i + 1).y

            val conX1 = centerX - tranX
            val conY1 = centerY - tranY

            val conX2 = centerX1 - tranX
            val conY2 = centerY1 - tranY
            control.add(Point(conX1, conY1))
            control.add(Point(conX2, conY2))
        }
        for (i in 0..list.size - 1 - 1) {

            if (i == 0) {
                path2.quadTo(control[i].x.toFloat(), control[i].y.toFloat(),
                        list.get(i + 1).x.toFloat(), list.get(i + 1).y.toFloat())
            } else if (i == list.size - 2) {
                path2.quadTo(control[i * 2 - 1].x.toFloat(), control[i * 2 - 1].y.toFloat(),
                        list.get(i + 1).x.toFloat(), list.get(i + 1).y.toFloat())

                canvas.drawCircle(list.get(i + 1).x.toFloat(), list.get(i + 1).y.toFloat(), 10f, point)// 小圆
                canvas.drawText("" + (i + 1), list.get(i + 1).x.toFloat(), list.get(i + 1).y.toFloat(), point)
            } else {
                path2.cubicTo(control[i * 2 - 1].x.toFloat(), control[i * 2 - 1].y.toFloat(),
                        control[i * 2].x.toFloat(), control[i * 2].y.toFloat(),
                        list.get(i + 1).x.toFloat(), list.get(i + 1).y.toFloat())

            }

            canvas.drawCircle(list.get(i).x.toFloat(), list.get(i).y.toFloat(), 10f, point)// 小圆
            canvas.drawText("" + i, list.get(i).x.toFloat(), list.get(i).y.toFloat(), point)
        }
        canvas.drawPath(path2, p)//画出贝塞尔曲线
        p.style = Paint.Style.FILL
        canvas.drawCircle(list.get(0).x.toFloat(), list.get(0).y.toFloat(), 22f, p)// 小圆


        //画点
        p.style = Paint.Style.FILL
        canvas.drawText("画点：", 10f, 390f, p)
        canvas.drawPoint(60f, 390f, p)//画一个点
        canvas.drawPoints(floatArrayOf(60f, 400f, 65f, 400f, 70f, 400f), p)//画多个点

        //画图片，就是贴图
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        canvas.drawBitmap(bitmap, 250f, 360f, p)
    }

    internal inner class Point(var x: Int, var y: Int)
}