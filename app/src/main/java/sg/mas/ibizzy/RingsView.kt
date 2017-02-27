package sg.mas.ibizzy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import sg.mas.ibizzy.renderable.Ring.DIRECTION.*
import sg.mas.ibizzy.renderable.BubbleInitializer.*
import sg.mas.ibizzy.renderable.Bubble
import sg.mas.ibizzy.renderable.BubbleInitializer
import sg.mas.ibizzy.renderable.helpers.Ricochet
import sg.mas.ibizzy.renderable.Ring
import java.util.*

class RingsView : View, Runnable {

    private lateinit var paint: Paint
    private lateinit var textPaint: Paint
    private var density: Float = 1f
    private val ringList = mutableListOf<Ring>()
    private val bubbleList = Collections.synchronizedList(mutableListOf<Bubble>())
    private var radius = 200f
    private lateinit var ringsAnimator: RingsAnimator
    private var autoReply: Boolean = false
    private var threadAnimator: Thread? = null
    private var animateRings = false

    @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null,
                              defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        density = resources.displayMetrics.density
        paint = Paint()
        paint.color = ContextCompat.getColor(context, R.color.paint88)
        paint.flags = Paint.ANTI_ALIAS_FLAG
        textPaint = Paint()
        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f * density / 2
        textPaint.textAlign = Paint.Align.CENTER
        ringsAnimator = RingsAnimator(ringList)
    }

    override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(width, height, oldw, oldh)
        val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT)
        layoutParams.width = resources.getDimensionPixelSize(R.dimen.previewseekbar_indicator_width)
        layoutParams.height = layoutParams.width
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)

        val x = measuredWidth / 2.toFloat()
        val y = measuredHeight / 2.toFloat()

        radius = Math.min(x, y) * 2 / 3


        ringList.add(Ring(x, y, END_FORWARD, radius - 10 * density, -7 * density, R.color.paint1))
        ringList.add(Ring(x, y, START_FORWARD, radius + 2 * density, 7 * density, R.color.paint2))
        ringList.add(Ring(x, y, END_BACKWARD, radius - 10 * density, -10 * density, R.color.paint3))

        BubbleInitializer.create {
            width { measuredWidth.toFloat() }
            height { measuredHeight.toFloat() }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x > measuredWidth / 2 - 150 && event.x < measuredWidth / 2 + 150 && event.y > measuredHeight / 2 - 150 && event.y < measuredHeight / 2 + 150) {
                    autoReply = !autoReply
                    ringsAnimator.velocity = if (autoReply) 5 else 1
                    if (autoReply) {
                        val list = mutableListOf<Bubble>()
                        list.add(Bubble(Side.LEFT, 25f * density, R.color.green))
                        list.add(Bubble(Side.LEFT, 15f * density, R.color.red))
                        list.add(Bubble(Side.LEFT, 30f * density, R.color.colorAccent))
                        list.add(Bubble(Side.TOP, 10f * density, R.color.blue))
                        list.add(Bubble(Side.TOP, 25f * density, R.color.red))
                        list.add(Bubble(Side.TOP, 20f * density, R.color.paint1))
                        list.add(Bubble(Side.RIGHT, 10f * density, R.color.violet))
                        list.add(Bubble(Side.RIGHT, 25f * density, R.color.pink))
                        list.add(Bubble(Side.RIGHT, 35f * density, R.color.blue))
                        list.add(Bubble(Side.BOTTOM, 40f * density, R.color.violet))
                        list.add(Bubble(Side.BOTTOM, 15f * density, R.color.pink))
                        list.add(Bubble(Side.BOTTOM, 30f * density, R.color.blue))

                        bubbleList.addAll(list)
                    } else {
                        bubbleList.clear()
                    }
                }
            }
        }
        return true
    }

    fun startRingsAnimation() {
        animateRings = true
        threadAnimator?.interrupt()
        threadAnimator = Thread(this)
        threadAnimator?.start()
    }

    fun stopRingsAnimation() {
        animateRings = false
    }

    override fun run() {
        while (animateRings) {
            ringsAnimator.nextValue()
            if (autoReply) {
                synchronized(bubbleList) {
                    bubbleList.forEach {
                        it.nextValue()
                        Ricochet.check(it, ringList)
                    }
                }
            }
            Thread.sleep(10)
            postInvalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        ringList.forEach { it.draw(canvas) }

        if (autoReply) {
            synchronized(bubbleList) {
                bubbleList.forEach {
                    it.draw(canvas)
                }
            }
        }

        canvas.drawText("AUTOREPLY", canvas.width / 2.toFloat(), (canvas.height / 2 - (textPaint.descent() + textPaint.ascent()) / 2), textPaint)
    }

    private class RingsAnimator(val list: List<Ring>) {
        var velocity: Int = 1
        private val range = IntArray(360, { it })
        private var index = 0
        fun nextValue() {
            val value: Int
            if (index >= range.size - velocity) {
                index = 0
            } else {
                index += velocity
            }
            value = range[index]
            list.forEach {
                val sin = Math.sin(Math.toRadians((value).toDouble()))
                val cos = Math.cos(Math.toRadians((value).toDouble()))
                it.move(sin.toFloat(), cos.toFloat())
            }
        }
    }
}