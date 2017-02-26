package sg.mas.ibizzy

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.util.Log
import sg.mas.ibizzy.BubbleInitializer.*

/**
 * Created by Sergey on 14.02.17.
 */

class Bubble(side: BubbleInitializer.Side, val radius: Float, color: Int) {

    private val TAG = Bubble::class.java.simpleName


    private var mX = 0f
    private var mY = 0f

    private var paint: Paint
    private var rectF: RectF
    private var alpha: Double
    private var distanceFromStartToCenter: Double
    private var startY: Float
    private var startX: Float

    init {
        rectF = RectF()
        paint = Paint()
        paint.color = ContextCompat.getColor(MainActivity.context, color)
        paint.flags = Paint.ANTI_ALIAS_FLAG

        startX = BubbleInitializer.instance.getX(side)
        startY = BubbleInitializer.instance.getY(side)

        when (side) {
            Side.LEFT -> startX -= radius
            Side.RIGHT -> startX += radius
            Side.TOP -> startY -= radius
            Side.BOTTOM -> startY += radius
        }

        val x = BubbleInitializer.centerX - startX
        val y = BubbleInitializer.centerY - startY
        distanceFromStartToCenter = Math.sqrt((x * x + y * y).toDouble())
        alpha = Math.toDegrees(Math.asin(y.toDouble() / distanceFromStartToCenter))
    }


    private val range = 100
    private var index = 0
    private var velocity: Int = 1
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
        /*
          if (!timeDelay()) {
              return
          }
  */
        if (moveToCenter) {
            if (index >= range - velocity) {
                index = 0
            } else {
                index += velocity
            }
        } else {
            if (index < 0) {
                index = 0
                moveToCenter = true
            } else {
                index -= velocity
            }
        }

        val k = (range - index).toFloat() / range

        val relativeDistance = distanceFromStartToCenter * (1 - k)

        var xR = relativeDistance * Math.cos(Math.toRadians(alpha))
        val yR = relativeDistance * Math.sin(Math.toRadians(alpha))
        if (startX > BubbleInitializer.centerX) {
            xR *= -1
        }
        this.mX = xR.toFloat()
        this.mY = yR.toFloat()
    }

    var moveToCenter = true

    fun draw(canvas: Canvas) {
        val xp = startX + mX
        val yp = startY + mY
        canvas.drawCircle(xp, yp, radius, paint)
    }

    fun getX(): Float = startX + mX
    fun getY(): Float = startY + mY

    fun ricochet() {
        moveToCenter = false
    }
}
