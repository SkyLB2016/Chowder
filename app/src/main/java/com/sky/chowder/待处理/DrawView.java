package com.sky.chowder.待处理;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;

import com.sky.chowder.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DrawView extends View {

    public DrawView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
            /* 
             * 方法 说明 drawRect 绘制矩形 drawCircle 绘制圆形 drawOval 绘制椭圆 drawPath 绘制任意多边形 
             * drawLine 绘制直线 drawPoin 绘制点 
             */
        // 创建画笔
        Paint p = new Paint();
        p.setColor(Color.RED);// 设置红色

        canvas.drawText("画圆：", 10, 20, p);// 画文本
        canvas.drawCircle(60, 20, 10, p);// 小圆
        p.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
        canvas.drawCircle(120, 20, 20, p);// 大圆

        canvas.drawText("画线及弧线：", 10, 60, p);
        p.setColor(Color.GREEN);// 设置绿色
        canvas.drawLine(60, 40, 100, 40, p);// 画线
        canvas.drawLine(110, 40, 190, 80, p);// 斜线
        //画笑脸弧线
        p.setStyle(Paint.Style.STROKE);//设置空心
        RectF oval1 = new RectF(150, 20, 180, 40);
        canvas.drawArc(oval1, 180, 180, false, p);//小弧形
        oval1.set(190, 20, 220, 40);
        canvas.drawArc(oval1, 180, 180, false, p);//小弧形
        oval1.set(160, 30, 210, 60);
        canvas.drawArc(oval1, 0, 180, false, p);//小弧形

        canvas.drawText("画矩形：", 10, 80, p);
        p.setColor(Color.GRAY);// 设置灰色
        p.setStyle(Paint.Style.FILL);//设置填满
        canvas.drawRect(60, 60, 80, 80, p);// 正方形
        canvas.drawRect(60, 90, 160, 100, p);// 长方形

        canvas.drawText("画扇形和椭圆:", 10, 120, p);
            /* 设置渐变色 这个正方形的颜色是改变的 */
        Shader mShader = new LinearGradient(0, 0, 100, 100,
                new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                        Color.LTGRAY}, null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。
        p.setShader(mShader);
        // p.setColor(Color.BLUE);
        RectF oval2 = new RectF(60, 100, 200, 240);// 设置个新的长方形，扫描测量
        canvas.drawArc(oval2, 200, 130, true, p);
        // 画弧，第一个参数是RectF：该类是第二个参数是角度的开始，第三个参数是多少度，第四个参数是真的时候画扇形，是假的时候画弧线
        //画椭圆，把oval改一下
        oval2.set(210, 100, 250, 130);
        canvas.drawOval(oval2, p);

        canvas.drawText("画三角形：", 10, 200, p);
        // 绘制这个三角形,你可以绘制任意多边形
        Path path = new Path();
        path.moveTo(80, 200);// 此点为多边形的起点
        path.lineTo(120, 250);
        path.lineTo(80, 250);
        path.close(); // 使这些点构成封闭的多边形
        canvas.drawPath(path, p);

        // 你可以绘制很多任意多边形，比如下面画六连形
        p.reset();//重置
        p.setColor(Color.LTGRAY);
        p.setStyle(Paint.Style.STROKE);//设置空心
        Path path1 = new Path();
        path1.moveTo(180, 200);
        path1.lineTo(200, 200);
        path1.lineTo(210, 210);
        path1.lineTo(200, 220);
        path1.lineTo(180, 220);
        path1.lineTo(170, 210);
        path1.close();//封闭
        canvas.drawPath(path1, p);
            /*
             * Path类封装复合(多轮廓几何图形的路径
             * 由直线段*、二次曲线,和三次方曲线，也可画以油画。drawPath(路径、油漆),要么已填充的或抚摸
             * (基于油漆的风格),或者可以用于剪断或画画的文本在路径。
             */

        //画圆角矩形
        p.setStyle(Paint.Style.FILL);//充满
        p.setColor(Color.LTGRAY);
        p.setAntiAlias(true);// 设置画笔的锯齿效果
        canvas.drawText("画圆角矩形:", 10, 260, p);
        RectF oval3 = new RectF(80, 260, 200, 300);// 设置个新的长方形
        canvas.drawRoundRect(oval3, 20, 15, p);//第二个参数是x半径，第三个参数是y半径

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
        p.setStrokeWidth(100);
        p.setColor(Color.RED);
        p.setTextSize(30);

        canvas.drawText("画贝塞尔曲线:", 10, 310, p);
        p.reset();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.GREEN);
        p.setStrokeWidth(10);

        List<Point> list = new ArrayList();
        Random random = new Random();
        for (int i = 0; i < 31; i++) {
            list.add(new Point(100 + i * 30, random.nextInt(500) + 500));
        }
        Path path2 = new Path();
        path2.moveTo(list.get(0).getX(), list.get(0).getY());//设置Path的起点
        Paint point = new Paint();
        point.setColor(Color.RED);
        point.setStyle(Paint.Style.FILL);
        point.setStrokeWidth(10);
        point.setTextSize(40);
        List<Point> control = new ArrayList<>();
        for (int i = 0; i < list.size() - 2; i++) {
            int centerX = (list.get(i).getX() + list.get(i + 1).getX()) / 2;
            int centerY = (list.get(i).getY() + list.get(i + 1).getY()) / 2;
            int centerX1 = (list.get(i + 1).getX() + list.get(i + 2).getX()) / 2;
            int centerY1 = (list.get(i + 1).getY() + list.get(i + 2).getY()) / 2;

            int centerX2 = (centerX + centerX1) / 2;
            int centerY2 = (centerY + centerY1) / 2;


            int tranX = centerX2 - list.get(i + 1).getX();
            int tranY = centerY2 - list.get(i + 1).getY();

            int conX1 = centerX - tranX;
            int conY1 = centerY - tranY;

            int conX2 = centerX1 - tranX;
            int conY2 = centerY1 - tranY;
            control.add(new Point(conX1, conY1));
            control.add(new Point(conX2, conY2));
        }
        for (int i = 0; i < list.size() - 1; i++) {

            if (i == 0) {
                path2.quadTo(control.get(i).getX(), control.get(i).getY(),
                        list.get(i + 1).getX(), list.get(i + 1).getY());
            } else if (i == (list.size() - 2)) {
                path2.quadTo(control.get(i * 2 - 1).getX(), control.get(i * 2 - 1).getY(),
                        list.get(i + 1).getX(), list.get(i + 1).getY());

                canvas.drawCircle(list.get(i + 1).getX(), list.get(i + 1).getY(), 10, point);// 小圆
                canvas.drawText("" + (i + 1), list.get(i + 1).getX(), list.get(i + 1).getY(), point);
            } else {
                path2.cubicTo(control.get(i * 2 - 1).getX(), control.get(i * 2 - 1).getY(),
                        control.get(i * 2).getX(), control.get(i * 2).getY(),
                        list.get(i + 1).getX(), list.get(i + 1).getY());

            }

            canvas.drawCircle(list.get(i).getX(), list.get(i).getY(), 10, point);// 小圆
            canvas.drawText("" + (i), list.get(i).getX(), list.get(i).getY(), point);
        }
        canvas.drawPath(path2, p);//画出贝塞尔曲线
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(list.get(0).getX(), list.get(0).getY(), 22, p);// 小圆


        //画点
        p.setStyle(Paint.Style.FILL);
        canvas.drawText("画点：", 10, 390, p);
        canvas.drawPoint(60, 390, p);//画一个点
        canvas.drawPoints(new float[]{60, 400, 65, 400, 70, 400}, p);//画多个点

        //画图片，就是贴图
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        canvas.drawBitmap(bitmap, 250, 360, p);
    }

    class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }
}