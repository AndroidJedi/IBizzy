package sg.mas.ibizzy.renderable

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import sg.mas.ibizzy.App

/**
 * Created by Sergey on 31.01.17.
 */
open class Ring(val startX: Float, val startY: Float, val direction: DIRECTION,
                val radius: Float,
                var threshold: Float,
                color: Int) {

    private var paint: Paint
    private var rectF: RectF
    private var xp: Float = 0f
    private var yp: Float = 0f
    private var mX = 0f
    private var mY = 0f

    init {
        rectF = RectF()
        paint = Paint()
        paint.color = ContextCompat.getColor(App.context, color)
        paint.flags = Paint.ANTI_ALIAS_FLAG
      //  threshold = (startX/15.toInt()).toInt()
    }

    fun draw(canvas: Canvas) {
        xp = startX + threshold * direction.start * if (direction.dir) mX else mY
        yp = startY + threshold * direction.start * if (direction.dir) mY else mX
       // canvas.drawCircle(xp, yp, radius + 5 * if (direction.dir) mX else mY, paint)
        canvas.drawCircle(xp, yp, radius, paint)
    }


    fun intersects(b: Bubble): Boolean {
        val distance = Math.sqrt(((xp - b.getX()) * (xp - b.getX()) + (yp - b.getY()) * (yp - b.getY())).toDouble())
        return Math.abs(radius - b.radius) <= distance && distance <= radius + b.radius
    }

    fun move(x: Float, y: Float) {
        this.mX = x
        this.mY = y
    }

    enum class DIRECTION(val start: Int, val dir: Boolean) {
        START_FORWARD(1, true),
        END_FORWARD(1, false),
        START_BACKWARD(-1, true),
        END_BACKWARD(-1, false)
    }
}