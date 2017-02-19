package sg.mas.ibizzy

import android.graphics.Canvas

/**
 * Created by Sergey on 14.02.17.
 */
abstract class Renderable(var startX: Float,
                          var startY: Float,
                          val radius: Float) {

    abstract fun draw(canvas: Canvas)

   /// abstract fun getAnimator () : Animator

    fun move(x: Float, y: Float) {
        this.mX = x
        this.mY = y
    }

    var mX = 0f
    var mY = 0f

   /* interface Animator {
        fun nextValue()
    }*/
}