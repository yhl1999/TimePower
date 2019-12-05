package com.o1.timemanager

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import java.lang.Exception
import kotlin.math.ceil
import kotlin.math.floor

class PlaidsContainer(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val array = context.obtainStyledAttributes(attrs, R.styleable.PlaidsContainer)
    private var left: Float = 0f
    private var plaidSize: Float =
        array.getDimension(R.styleable.PlaidsContainer_plaidSize, 80.px.toFloat())
    private var marginAvatar: Float = resources.getDimension(R.dimen.margin_avatar)
    private var columnNum: Int = 0
    private var blankNum: Int = 50
    private val itemPadding: Int = 10.px
    private val plaidRadius: Float = resources.getDimension(R.dimen.plaid_radius)
    var items = mutableListOf(1, 2, 3)
    private var onItemClickListener: ((it: Int) -> Unit)? = null

    init {
        isClickable = true
    }

    private val borderPen = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = plaidRadius
    }
    private val fillPen = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0x55000000
        style = Paint.Style.FILL
        strokeWidth = plaidRadius
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var l = left
        var t = paddingTop.toFloat()
        canvas.apply {
            for (i in 1..blankNum) {
                drawRoundRect(
                    l, t, l + plaidSize, plaidSize + t,
                    plaidRadius, plaidRadius, fillPen
                )
                drawRoundRect(
                    l, t, l + plaidSize, plaidSize + t,
                    plaidRadius, plaidRadius, borderPen
                )
                if (i <= items.size) {
                    try {
                        resources.getDrawable(
                            resources.getIdentifier(
                                "ic_item_${items[i - 1]}_1",
                                "drawable",
                                context.packageName
                            ), null
                        ).apply {
                            setBounds(
                                l.toInt() + itemPadding,
                                t.toInt() + itemPadding,
                                (l + plaidSize).toInt() - itemPadding,
                                (plaidSize + t).toInt() - itemPadding
                            )
                            draw(canvas)
                        }
                    } catch (ignore: Exception) {
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
                plaidSize.toInt() * ceil(blankNum.toFloat() / columnNum).toInt() + plaidRadius.toInt() + paddingTop + paddingBottom,
                MeasureSpec.EXACTLY
            )
        )
    }

    private val detector: GestureDetector = GestureDetector(context,
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                val x = floor((e.y - paddingTop) / plaidSize).toInt()
                var y = floor((e.x - paddingLeft) / plaidSize).toInt()
                if (y < 0) y = 0
                if (y > columnNum - 1) y = columnNum - 1
                val index = x * columnNum + y
                if (index < items.size && index >= 0) {
                    onItemClick(index)
                }
                return true
            }
        }
    )

    override fun onTouchEvent(event: MotionEvent): Boolean {
        detector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    fun setOnItemClickListener(listener: (item: Int) -> Unit) {
        onItemClickListener = listener
    }

    fun onItemClick(index: Int) {
        onItemClickListener?.let { it(items[index]) }
    }

    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()
    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}