package com.o1.timemanager

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.ceil
import kotlin.math.floor

class PlaidsContainer(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var left: Float = 0f
    private var plaidSize: Float = resources.getDimension(R.dimen.plaid_size)
    private var marginAvatar: Float = resources.getDimension(R.dimen.margin_avatar)
    private var columnNum: Int = 0
    private var blankNum: Int = 50
    private val itemPadding: Int = 10.px
    private val radius: Float = resources.getDimension(R.dimen.plaid_radius)
    private val items = listOf(2, 3, 4)

    private val borderPen = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = radius
    }
    private val fillPen = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x55000000
        style = Paint.Style.FILL
        strokeWidth = radius
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var l = left
        var t = paddingTop.toFloat()
        canvas.apply {
            for (i in 1..blankNum) {
                drawRoundRect(
                    l, t, l + plaidSize, plaidSize + t,
                    radius, radius, fillPen
                )
                drawRoundRect(
                    l, t, l + plaidSize, plaidSize + t,
                    radius, radius, borderPen
                )
                if (i <= items.size) {
                    resources.getDrawable(
                        resources.getIdentifier(
                            "ic_item_${items[i-1]}_1",
                            "drawable",
                            context.packageName
                        ), null
                    ).apply {
                        println("$width - $height")
                        setBounds(
                            l.toInt() + itemPadding,
                            t.toInt() + itemPadding,
                            (l + plaidSize).toInt() - itemPadding,
                            (plaidSize + t).toInt() - itemPadding
                        )
                        draw(canvas)
                    }
                }
                l += plaidSize
                if (i % columnNum == 0) {
                    l = left
                    t += plaidSize
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // Account for padding
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val xpad = (paddingLeft + paddingRight).toFloat()

        val ww = w.toFloat() - xpad

        columnNum = floor(ww / plaidSize).toInt()
        left = paddingLeft.toFloat() + (ww - plaidSize * columnNum) / 2

        setMeasuredDimension(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(
                plaidSize.toInt() * ceil(blankNum.toFloat() / columnNum).toInt() + radius.toInt() + paddingTop + paddingBottom,
                MeasureSpec.EXACTLY
            )
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()
    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}