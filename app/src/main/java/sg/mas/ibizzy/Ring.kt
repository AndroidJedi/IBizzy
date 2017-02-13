package sg.mas.ibizzy

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

/**
 * Created by Sergey on 31.01.17.
 */
open class Ring(var centerX: Float,
                var centerY: Float,
                val direction: DIRECTION,
                val radius: Float,
                val threashold: Int,
                val color: Int) {
    var paint: Paint
    var rectF: RectF

    init {
        rectF = RectF()
        paint = Paint()
        paint.color = color
        paint.flags = Paint.ANTI_ALIAS_FLAG
    }

    fun move(x: Float, y: Float) {
        this.mX = x
        this.mY = y
    }

    var mX = 0f
    var mY = 0f

    open fun draw(canvas: Canvas) {
        val xp = centerX + threashold * direction.start * if (direction.dir) mX else mY
        val yp = centerY + threashold * direction.start * if (direction.dir) mY else mX
        canvas.drawCircle(xp, yp, radius + 5 * if (direction.dir) mX else mY, paint)
    }

    enum class DIRECTION(val start: Int, val dir: Boolean) {
        START_FORWARD(1, true),
        END_FORWARD(1, false),
        START_BACKWARD(-1, true),
        END_BACKWARD(-1, false)
    }

}