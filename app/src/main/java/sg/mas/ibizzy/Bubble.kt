package sg.mas.ibizzy

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log

/**
 * Created by Sergey on 14.02.17.
 */

class Bubble(startX: Float,
             startY: Float,
             val centerX: Float,
             val centerY: Float,
             radius: Float) : Renderable(startX, startY, radius) {

    private val TAG = Bubble::class.java.simpleName

    var paint: Paint
    var rectF: RectF
    var fi: Double
    private var distanceFromStartToCenter: Double

    init {
        rectF = RectF()
        paint = Paint()
        paint.color = Color.RED
        paint.flags = Paint.ANTI_ALIAS_FLAG
        val x = centerX - startX
        val y = centerY - startY
        distanceFromStartToCenter = Math.sqrt((x * x + y * y).toDouble())

        fi = Math.toDegrees(Math.asin(y.toDouble() / distanceFromStartToCenter))


        Log.d(TAG, " distanceFromStartToCenter " + distanceFromStartToCenter)

    }

    private val range = Array(100, { it })
    private var index = 0
    var velocity: Int = 1
    private var lastDraw: Long = System.currentTimeMillis()
    private val timeDelay = 25L

    private fun timeDelay(): Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastDraw > timeDelay) {
            lastDraw = currentTime
            return true
        }
        return false
    }

    fun nextValue() {
/*
        val draw = timeDelay()

        if (!draw) {
            return
        }*/

        val value: Int
        if (index >= range.size - velocity) {
            index = 0
        } else {
            index += velocity
        }
        value = range[index]
        val pairXY = calculateDistance(value)

        Log.d(TAG, " x " + pairXY.x.toFloat() + " y " + pairXY.y.toFloat())

        move(pairXY.x.toFloat(), pairXY.y.toFloat())
    }


    private fun calculateDistance(value: Int): Pair {

        val k = (range.size - value).toFloat() / range.size

        val relativeDistance = distanceFromStartToCenter * (1 - k)

        var xR = relativeDistance * Math.cos(Math.toRadians(fi))

        val yR = relativeDistance * Math.sin(Math.toRadians(fi))
        if(startX>centerX){
            xR *= -1
        }


        return Pair(xR, yR)
    }

    override fun draw(canvas: Canvas) {
        val xp = startX + mX
        val yp = startY + mY
        canvas.drawCircle(xp, yp, radius, paint)
    }

    data class Pair(val x: Double, val y: Double)
}
