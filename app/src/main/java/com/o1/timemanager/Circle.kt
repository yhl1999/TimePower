package com.o1.timemanager

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

class Circle(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val array = context.obtainStyledAttributes(attrs, R.styleable.Circle)
    private var touchPoint: Boolean = false
    private val startValue: Float =
        array.getFloat(R.styleable.Circle_startValue, 10f)
    private val fullValue: Float =
        array.getFloat(R.styleable.Circle_fullValue, 120f)
    private val scrollStepValue: Float = 1f
    private val stepValue: Int =
        array.getInt(R.styleable.Circle_stepValue, 5)
    var filledValue: Float = startValue

    private var wCenter: Float = 0f
    private var hCenter: Float = 0f
    private val lineWidth: Float =
        array.getDimension(R.styleable.Circle_lineWidth, 15f.px)
    private val pointSize: Float = lineWidth
    private val fontSize: Float =
        array.getDimension(R.styleable.Circle_fontSize, 50f.px)
    private val circleTextMargin =
        array.getDimension(R.styleable.Circle_circleTextMargin, 30f.px)

    private var pointCenterX: Float = 0f
    private var pointCenterY: Float = 0f
    private val arcFillPen = Paint(ANTI_ALIAS_FLAG).apply {
        color = array.getColor(R.styleable.Circle_filledColor, 0xFF88C230.toInt())
        style = Paint.Style.STROKE
        strokeWidth = lineWidth
    }
    private val arcEmptyPen = Paint(ANTI_ALIAS_FLAG).apply {
        color = array.getColor(R.styleable.Circle_emptyColor, 0xFFCBC76C.toInt())
        style = Paint.Style.STROKE
        strokeWidth = lineWidth
    }
    private val pointPen = Paint(ANTI_ALIAS_FLAG).apply {
        color = array.getColor(R.styleable.Circle_filledColor, 0xFF88C230.toInt())
        style = Paint.Style.FILL_AND_STROKE
    }
    private val textPen = Paint(ANTI_ALIAS_FLAG).apply {
        color = array.getColor(R.styleable.Circle_textColor, Color.WHITE)
        style = Paint.Style.FILL
        textSize = fontSize
        textAlign = Paint.Align.CENTER
        typeface = Typeface.createFromAsset(context.assets, "fonts/Lato-Light.ttf")
    }
    private var diameter: Float = 0.0f
    private lateinit var oval: RectF

    var timerStarted: Boolean = false
    var minutes: Int = 0
    var seconds: Int = 0

    private lateinit var countdownTimer: CountDownTimer

    init {
        isClickable = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val fillAng = (filledValue + seconds.toFloat() / 60f - startValue)*360f/(fullValue - startValue)
        pointCenterX = (wCenter + diameter / 2 * cos((fillAng - 90f) * PI / 180f)).toFloat()
        pointCenterY = (hCenter + diameter / 2 * sin((fillAng - 90f) * PI / 180f)).toFloat()
        minutes = if (!timerStarted) {
            round(filledValue / stepValue).toInt() * stepValue
        } else {
            filledValue.toInt()
        }
        canvas.apply {
            drawArc(oval, -90f,fillAng,false, arcFillPen)
            drawArc(oval, fillAng - 90f, 360f-fillAng, false, arcEmptyPen)
            drawCircle(
                pointCenterX,
                pointCenterY,
                pointSize, pointPen
            )

            drawText(String.format("%02d", minutes) + ":" + String.format("%02d", seconds), wCenter, hCenter + diameter/2 + circleTextMargin + fontSize, textPen)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        println("sizeChanged")
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        println("Measure")
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec + fontSize.toInt() + circleTextMargin.toInt())
    }

    private val detector: GestureDetector = GestureDetector(context,
        object : GestureDetector.SimpleOnGestureListener(){
            override fun onScroll(
                e1: MotionEvent,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                if (touchPoint && !timerStarted) {
                    var filledAng = acos(
                        dotProd(e2.x - wCenter, e2.y - hCenter, 0f, -1f) /
                                distance(e2.x, e2.y, wCenter, hCenter)
                    )
                    if (e2.x - wCenter < 0) {
                        filledAng = (2*PI - filledAng).toFloat()
                    }
                    val tmpAng = (filledAng / PI/2 * (fullValue - startValue) + startValue).toFloat()
                    val tmpValue = round(tmpAng / scrollStepValue) * scrollStepValue
                    val eps = (fullValue - startValue) / 2
                    if (abs(filledValue - tmpValue) < eps) {
                        filledValue = tmpValue
                        postInvalidate()
                    }
                    else {
                        filledValue = if (filledValue > (fullValue + startValue) / 2) {
                            fullValue
                        } else {
                            startValue
                        }
                    }
                }
                return true
            }
        }
    )
    override fun onTouchEvent(event: MotionEvent): Boolean {
        detector.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchPoint = distance(pointCenterX, pointCenterY, event.x, event.y) < pointSize + 10f.px
            }
            else -> {
            }
        }
        return super.onTouchEvent(event)
    }

    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2))
    }
    private fun dotProd(a1: Float, b1: Float, a2: Float, b2: Float): Float {
        return a1*a2+b1*b2
    }

    fun countDown() {
        if (seconds == 0) {
            filledValue -= 1
            seconds = 59
        }
        else {
            seconds -= 1
        }
        postInvalidate()
    }

    fun startTimer() {
        countdownTimer = object : CountDownTimer(minutes.toLong()*60*1000, 1000) {
            override fun onFinish() {
                println("Finish")
            }

            override fun onTick(millisUntilFinished: Long) {
                countDown()
            }
        }
        timerStarted = true
        filledValue = minutes.toFloat()
        countdownTimer.start()
    }
    fun stopTimer() {
        timerStarted = false
        countdownTimer.cancel()
    }

    val Float.dp: Float
        get() = this / Resources.getSystem().displayMetrics.density
    val Float.px: Float
        get() = this * Resources.getSystem().displayMetrics.density
}