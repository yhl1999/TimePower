package com.o1.timemanager

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView
import kotlin.math.min

class BackpackPlaid(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {
    private var wCenter: Float = 0f
    private var hCenter: Float = 0f
    private var diameter: Float = 0f
    private lateinit var oval: RectF

    init {
        setImageResource(R.drawable.ic_item_1_1)
        setBackgroundResource(R.drawable.background_plaid)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        wCenter = w.toFloat() / 2
        hCenter = h.toFloat() / 2

        // Account for padding
        val xpad = (paddingLeft + paddingRight).toFloat()
        val ypad = (paddingTop + paddingBottom).toFloat()

        val ww = w.toFloat() - xpad
        val hh = h.toFloat() - ypad

        // Figure out how big we can make the pie.
        diameter = min(ww, hh)
        oval = RectF(wCenter-diameter/2, hCenter-diameter/2, wCenter+diameter/2, hCenter+diameter/2)
    }
}