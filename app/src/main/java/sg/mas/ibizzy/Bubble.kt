package sg.mas.ibizzy

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.util.Log

/**
 * Created by Sergey on 14.02.17.
 */

class Bubble(startX: Float,
             startY: Float,
             val centerX: Float,
             val centerY: Float,
             radius: Float,color:Int) : Renderable(startX, startY, radius) {

    private val TAG = Bubble::class.java.simpleName

    var paint: Paint
    var rectF: RectF
    var alpha: Double
    private var distanceFromStartToCenter: Double

    init {
        rectF = RectF()
        paint = Paint()
        paint.color = ContextCompat.getColor(MainActivity.context,color)
        paint.flags = Paint.ANTI_ALIAS_FLAG
        val x = centerX - startX - radius*2
        val y = centerY - startY - radius*2
        distanceFromStartToCenter = Math.sqrt((x * x + y * y).toDouble())

        alpha = Math.toDegrees(Math.asin(y.toDouble() / distanceFromStartToCenter))


        Log.d(TAG, " distanceFromStartToCenter " + distanceFromStartToCenter)

    }

    private val range = 100
    // private val range = Array(100, { it })
    private var index = 0
    var velocity: Int = 1
    private var lastDraw: Long = System.currentTimeMillis()
    private val timeDelay = 50L

    private fun timeDelay(): Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastDraw > timeDelay) {
            lastDraw = currentTime
            return true
        }
        return false
    }

    fun nextValue() {

        val draw = timeDelay()

        if (!draw) {
            return
        }

        if (moveToCenter) {
            if (index >= range - velocity) {
                index = 0
            } else {
                index += velocity
            }
        } else {
            if (index <0) {
                index = 0
                moveToCenter = true
            } else {
                index -= velocity
            }
        }

        val value: Int

        value = index
        val pairXY = calculateDistance(value)

        move(pairXY.x.toFloat(), pairXY.y.toFloat())
    }


    private fun calculateDistance(value: Int): Pair {

        val k = (range - value).toFloat() / range

        val relativeDistance = distanceFromStartToCenter * (1 - k)

        var xR = relativeDistance * Math.cos(Math.toRadians(alpha))

        val yR = relativeDistance * Math.sin(Math.toRadians(alpha))
        if (startX > centerX) {
            xR *= -1
        }


        return Pair(xR, yR)
    }

    var moveToCenter = true

    override fun draw(canvas: Canvas) {
        val xp = startX + mX
        val yp = startY + mY
        canvas.drawCircle(xp, yp, radius, paint)
    }

    fun getX(): Float = startX + mX
    fun getY(): Float = startY + mY

    fun ricochet() {
        Log.d(TAG, "ricochet")
        moveToCenter = false
    }
    data class Pair(val x: Double, val y: Double)
}
