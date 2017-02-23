package sg.mas.ibizzy

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.graphics.RectF
import android.util.Pair

/**
 * Created by Sergey on 31.01.17.
 */
open class Ring(startX: Float,
                startY: Float,
                val direction: DIRECTION,
                radius: Float,
                val threashold: Int,
                color: Int) : Renderable(startX, startY, radius) {

    var paint: Paint
    var rectF: RectF
    var xp: Float = 0f
    var yp: Float = 0f

    init {
        rectF = RectF()
        paint = Paint()
        paint.color = color
        paint.flags = Paint.ANTI_ALIAS_FLAG
    }

    override fun draw(canvas: Canvas) {
        xp = startX + threashold * direction.start * if (direction.dir) mX else mY
        yp = startY + threashold * direction.start * if (direction.dir) mY else mX
        canvas.drawCircle(xp, yp, radius + 5 * if (direction.dir) mX else mY, paint)
    }


    fun contains(b: Bubble):Boolean {
        val distance = Math.sqrt(((xp - b.getX()) * (xp - b.getX()) + (yp - b.getY()) * (yp - b.getY())).toDouble())
        if (Math.abs(radius - b.radius) <= distance && distance <= radius + b.radius) {
            return true
        } else {
            return false
        }
    }

    enum class DIRECTION(val start: Int, val dir: Boolean) {
        START_FORWARD(1, true),
        END_FORWARD(1, false),
        START_BACKWARD(-1, true),
        END_BACKWARD(-1, false)
    }
}