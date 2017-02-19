package sg.mas.ibizzy

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

/**
 * Created by Sergey on 31.01.17.
 */
open class Ring( startX: Float,
                 startY: Float,
                 val direction: DIRECTION,
                 radius: Float,
                 val threashold: Int,
                 color: Int):Renderable(startX,startY,radius) {

    var paint: Paint
    var rectF: RectF

    init {
        rectF = RectF()
        paint = Paint()
        paint.color = color
        paint.flags = Paint.ANTI_ALIAS_FLAG
    }

    override fun draw(canvas: Canvas) {
        val xp = startX + threashold * direction.start * if (direction.dir) mX else mY
        val yp = startY + threashold * direction.start * if (direction.dir) mY else mX
        canvas.drawCircle(xp, yp, radius + 5 * if (direction.dir) mX else mY, paint)
    }

    enum class DIRECTION(val start: Int, val dir: Boolean) {
        START_FORWARD(1, true),
        END_FORWARD(1, false),
        START_BACKWARD(-1, true),
        END_BACKWARD(-1, false)
    }
}