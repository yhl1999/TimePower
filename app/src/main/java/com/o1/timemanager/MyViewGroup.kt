package com.o1.timemanager

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.math.floor


class MyViewGroup(context: Context, attrs: AttributeSet) : ViewGroup(context, attrs) {
    private val horizontalSpace = 10
    private val verticalSpace = 10
    private val itemSize: Int = 80.px
    private var colNumber: Int = 0

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var hadUsedHorizontal = (r - l - colNumber * itemSize) / 2
        println("layout: $l, $r")
        var hadUsedVertical = 0
        val width = measuredWidth
        for (i in 0 until childCount) {
            val child: View = getChildAt(i)
            if (child.measuredWidth + hadUsedHorizontal > width) {
                hadUsedVertical += child.measuredHeight + verticalSpace
                hadUsedHorizontal = (r - l - colNumber * itemSize) / 2
            }
            child.layout(
                hadUsedHorizontal,
                hadUsedVertical,
                hadUsedHorizontal + child.measuredWidth,
                hadUsedVertical + child.measuredHeight
            )
            hadUsedHorizontal += horizontalSpace + child.measuredWidth
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        println("measure: ${MeasureSpec.getSize(widthMeasureSpec)}, ${MeasureSpec.getSize(heightMeasureSpec)}")
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val w = MeasureSpec.getSize(widthMeasureSpec)
        val xpad = (paddingLeft + paddingRight).toFloat()
        val ww = w.toFloat() - xpad
        colNumber = floor(ww / itemSize).toInt()

        for (i in 0 until childCount) {
            val view: View = getChildAt(i)
            val measureSpec = MeasureSpec.makeMeasureSpec(itemSize, MeasureSpec.EXACTLY)
            measureChild(view, measureSpec, measureSpec)
        }
    }

    private val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()
    private val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()
}