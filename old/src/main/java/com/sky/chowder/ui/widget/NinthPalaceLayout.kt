package com.sky.chowder.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.sky.chowder.R
import com.sky.chowder.model.NinthPalaceEntity
import com.sky.utils.ScreenUtils

/**
 * Created by SKY on 2015/12/24 10:58.
 * 拼图游戏
 */
class NinthPalaceLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private val orginal = ArrayList<NinthPalaceEntity>()//原数据
    private val select = ArrayList<NinthPalaceEntity>()//选中的
    private var passWord = ArrayList<NinthPalaceEntity>()//密码

    var onSuccess: ((Boolean) -> Unit)? = null//

    private val piece = 3//几行几列
    private var once = true

    init {
        setBackgroundResource(R.color.alpha_99)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (once) {
            setView()
            once = false
        }
        setMeasuredDimension(ScreenUtils.getWidthPX(context), ScreenUtils.getHeightPX(context)!!)
    }

    private fun setView() {
        val width = ScreenUtils.getWidthPX(context)
        val pieceWidth = width / piece
        val top = (ScreenUtils.getHeightPX(context) - width) / 2
        for (i in 0..8) {
            val left = i % piece * pieceWidth
            val top = i / piece * pieceWidth + top
            val rect = Rect(left + pieceWidth / 4, top + pieceWidth / 4, left + pieceWidth * 3 / 4, top + pieceWidth * 3 / 4)
            orginal.add(NinthPalaceEntity(i, rect))
        }
        butterfly()
    }

    fun shuffle() {
        select.clear()
        for (i in orginal) select.add(i)
        select.shuffle()
        invalidate()
    }

    private fun fiveStar() {
        select.clear()
        //五角星
        select.add(orginal[3])
        select.add(orginal[4])
        select.add(orginal[5])
        select.add(orginal[6])
        select.add(orginal[1])
        select.add(orginal[8])
        select.add(orginal[3])
        invalidate()
    }

    //蜻蜓dragonfly或者蝴蝶
    private fun butterfly() {
        select.clear()
        select.add(orginal[0])
        //        select.add(orginal[8])
        select.add(orginal[4])
        select.add(orginal[7])
        select.add(orginal[1])
        select.add(orginal[6])
        select.add(orginal[2])
        select.add(orginal[3])
        select.add(orginal[5])

        passWord = select.clone() as ArrayList<NinthPalaceEntity>
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.color = Color.BLUE
        //原点
        for (i in orginal) canvas?.drawCircle(i.rect.centerX() * 1f, i.rect.centerY() * 1f, i.radius, paint)

        paint.color = Color.CYAN
        paint.strokeWidth = 5f
        //选中的点与线
        for (i in select.indices) {
            canvas?.drawCircle(select[i].rect.centerX() * 1f, select[i].rect.centerY() * 1f, select[i].radius, paint)
            if (i + 1 < select.size) canvas?.drawLine(select[i].rect.centerX() * 1f, select[i].rect.centerY() * 1f, select[i + 1].rect.centerX() * 1f, select[i + 1].rect.centerY() * 1f, paint)
            else if (endX !== 0f && endY !== 0f) canvas?.drawLine(select[i].rect.centerX() * 1f, select[i].rect.centerY() * 1f, endX, endY, paint)
        }
    }

    private var downX = 0f
    private var downY = 0f
    private var endX = 0f
    private var endY = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                select.clear()
                for (i in orginal) if (i.rect.contains(downX.toInt(), downY.toInt())) {
                    select.add(i)
                    invalidate()
                }
            }
            MotionEvent.ACTION_MOVE -> {
                endX = event.x
                endY = event.y
                if (select.isEmpty()) return false
                for (i in orginal)
                    if (i.rect.contains(endX.toInt(), endY.toInt()) && !select.contains(i)) {
                        val sId = select.last().id//上一个点的id
                        val eId = i.id//后一个点的id
                        val eq = EqualEntity(sId, eId)
                        //获取中间点0-8,1-7,2-6,3-5中间点为4；0-2为1；0-6为3；8-2为5；8-6为7；
                        val contains = if (eq.equalsTwo(0, 8) || eq.equalsTwo(1, 7) || eq.equalsTwo(2, 6) || eq.equalsTwo(3, 5)) orginal[4]
                        else if (eq.equalsTwo(0, 2)) orginal[1]
                        else if (eq.equalsTwo(0, 6)) orginal[3]
                        else if (eq.equalsTwo(8, 2)) orginal[5]
                        else if (eq.equalsTwo(8, 6)) orginal[7]
                        else null
                        //判断是否已包含此点，为包含则包含
                        if (contains != null && !select.contains(contains)) select.add(contains)
                        select.add(i)
                    }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                endX = 0f
                endY = 0f
                invalidate()
                if (passWord.isEmpty())
                    passWord = select.clone() as ArrayList<NinthPalaceEntity>
                else
                    onSuccess?.invoke(select == passWord)
            }
        }
        return true
    }

    class EqualEntity(var a: Int, var b: Int) {
        fun equalsTwo(c: Int, d: Int): Boolean = a == c && b == d || a == d && b == c
    }
}
