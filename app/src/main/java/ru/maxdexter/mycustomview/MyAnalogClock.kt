package ru.maxdexter.mycustomview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import java.util.*
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class MyAnalogClock(context: Context, attr: AttributeSet) :
    androidx.appcompat.widget.AppCompatTextView(context, attr) {

    private var clockHeight = 300
    private var clockWidth = 300
    private var radius = 150f
    private var angle = 0.0
    private var centreX = 0f
    private var centreY = 0f
    private var padding = 0
    private var paint: Paint? = null
    private var path: Path? = null
    private var minimum = 0
    private var hour = 0
    private var minute = 0
    private var second = 0
    private var hourArrowSize = 0f
    private var handSize = 0f
    private lateinit var rect: Rect
    private val isInit = false


    private fun init() {
        clockHeight = height
        clockWidth = width
        padding = 16
        centreX = clockWidth / 2f
        centreY = clockHeight / 2f
        minimum = min(clockHeight, clockWidth)
        radius = minimum / 2F - padding
        angle = (Math.PI / 30 - Math.PI / 2)
        paint = Paint()
        path = Path()
        rect = Rect()
        hourArrowSize = radius - radius / 2f
        handSize = radius - radius / 4f
    }

    private fun createCircle(canvas: Canvas) {
        setAttrs(Color.BLACK, Paint.Style.STROKE, 8)
        paint?.let { canvas.drawCircle(centreX, centreY, radius, it) }
    }


    private fun setAttrs(colour: Int, stroke: Paint.Style, strokeWidt: Int) {
        paint?.apply {
            reset()
            color = colour
            style = stroke
            strokeWidth = strokeWidt.toFloat()
            isAntiAlias = true
        }
    }

    private fun drawArrows(canvas: Canvas) {
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        hour = if (hour > 12) hour - 12 else hour
        minute = calendar.get(Calendar.MINUTE)
        second = calendar.get(Calendar.SECOND)
        drawHourArrow(canvas, (hour + minute / 60.0) * 5f)
        drawMinuteArrow(canvas, minute)
        drawSecondsArrow(canvas, second)
    }

    private fun drawSecondsArrow(canvas: Canvas, second: Int) {
        paint?.reset()
        setAttrs(Color.RED, Paint.Style.STROKE, 8)
        angle = Math.PI * second / 30 - Math.PI / 2
        canvas.drawLine(
            centreX,
            centreY,
            (centreX + cos(angle) * handSize).toFloat(),
            (centreY + sin(angle) * hourArrowSize).toFloat(),
            paint!!
        )
    }

    private fun drawMinuteArrow(canvas: Canvas, minute: Int) {
        paint?.reset()
        setAttrs(Color.BLACK, Paint.Style.STROKE, 8)
        angle = Math.PI * minute / 30 - Math.PI / 2
        canvas.drawLine(
            centreX,
            centreY,
            (centreX + cos(angle) * handSize).toFloat(),
            (centreY + sin(angle) * hourArrowSize).toFloat(),
            paint!!
        )
    }

    private fun drawHourArrow(canvas: Canvas, hour: Double) {
        paint?.reset()
        setAttrs(Color.BLACK, Paint.Style.STROKE, 10)
        angle = Math.PI * hour / 30 - Math.PI / 2
        canvas.drawLine(
            centreX,
            centreY,
            (centreX + cos(angle) * hourArrowSize).toFloat(),
            (centreY + sin(angle) * hourArrowSize).toFloat(),
            paint!!
        )
    }

    private fun drawNumbers(canvas: Canvas) {
        paint?.textSize = 100f

        for (number in 1..12) {
            val num = number.toString()
            paint?.getTextBounds(num, 0, num.length, rect)
            val angle = Math.PI / 6 * (number - 3)
            canvas.drawText(
                num,
                (centreX + cos(angle) * radius - rect.width() / 2).toFloat(),
                (centreY + sin(angle) * radius + rect.height() / 2).toFloat(),
                paint!!
            )
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!isInit) {
            init()
        }
        canvas?.let {
            createCircle(canvas)
            drawArrows(canvas)
            drawNumbers(canvas)
        }
        postInvalidateDelayed(500)
    }
}