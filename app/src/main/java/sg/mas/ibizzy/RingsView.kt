package sg.mas.ibizzy

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Pair
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import sg.mas.ibizzy.Ring.DIRECTION.*

class RingsView : View {

    @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null,
                              defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init()

    }

    private lateinit var paint: Paint
    private lateinit var bubble:Bubble
    private lateinit var bubble2:Bubble

    private fun init() {
        paint = Paint()
        paint.color = ContextCompat.getColor(context, R.color.paint88)
        paint.flags = Paint.ANTI_ALIAS_FLAG
        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f
        textPaint.textAlign = Paint.Align.CENTER

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (width == 0 || height == 0) {
            return
        } else if (firstLayout) {
            val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT)
            layoutParams.width = resources.getDimensionPixelSize(R.dimen.previewseekbar_indicator_width)
            layoutParams.height = layoutParams.width
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
            firstLayout = false
            val rectF = RectF()
            rectF.top = (measuredHeight / 2 - (resources.getDimensionPixelSize(R.dimen.previewseekbar_indicator_width) + 10)).toFloat()
            rectF.bottom = (measuredHeight / 2 + (resources.getDimensionPixelSize(R.dimen.previewseekbar_indicator_width) + 10)).toFloat()
            rectF.left = (measuredWidth / 2 - (resources.getDimensionPixelSize(R.dimen.previewseekbar_indicator_width) + 10)).toFloat()
            rectF.right = (measuredWidth / 2 + (resources.getDimensionPixelSize(R.dimen.previewseekbar_indicator_width) + 10)).toFloat()


            val x = measuredWidth / 2.toFloat()
            val y = measuredHeight / 2.toFloat()


            ringList.add(Ring(x, y, END_FORWARD, radius - 10, -10, ContextCompat.getColor(context, R.color.paint1)))
            ringList.add(Ring(x, y, START_FORWARD, radius + 15, 10, ContextCompat.getColor(context, R.color.paint2)))
            ringList.add(Ring(x, y, END_BACKWARD, radius, -10, ContextCompat.getColor(context, R.color.paint3)))

            bubbleList.add(Bubble(300f,0f,x,y,50f))
            bubbleList.add(Bubble(0f,300f,x,y,50f))
            bubbleList.add(Bubble(0f,500f,x,y,50f))
            bubbleList.add(Bubble(0f,600f,x,y,50f))


            ringsAnimator = RingsAnimator(ringList)
        }
    }

    private var isFast: Boolean = false


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x > measuredWidth / 2 - 150 && event.x < measuredWidth / 2 + 150 && event.y > measuredHeight / 2 - 150 && event.y < measuredHeight / 2 + 150) {
                    isFast = !isFast
                    ringsAnimator.velocity = if (isFast) 5 else 1
                }
            }
        }

        return true
    }

    val textPaint = Paint()

    val ringList = mutableListOf<Ring>()
    val bubbleList = mutableListOf<Bubble>()
    val radius = 150f

    lateinit var ringsAnimator: RingsAnimator

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        bubbleList.forEach {
            it.nextValue()
            it.draw(canvas)
        }

        ringsAnimator.nextValue()
        ringList.forEach { it.draw(canvas) }

     //   canvas.drawCircle(measuredWidth / 2.toFloat(), measuredHeight / 2.toFloat(), radius, paint)
        val xPos = canvas.width / 2.toFloat()
        val yPos = (canvas.height / 2 - (textPaint.descent() + textPaint.ascent()) / 2)

        canvas.drawText("AUTOREPLY", xPos, yPos, textPaint)
        invalidate()
    }

    class RingsAnimator(val list: List<Ring>) {
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

    class BubbleAnimator(val list: List<Bubble>) {
        var velocity: Int = 1

     //   private val range = Array(80, { calculateDistance(it) })
        private val range = Array(80, { it })

        private fun calculateDistance(value:Int,bubble:Bubble):Pair<Int,Int>{




            return Pair(value,value)
        }
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

                val pairXY = calculateDistance(value, it)
                it.move(pairXY.first.toFloat(), pairXY.second.toFloat())
            }
        }
    }


    private var firstLayout = true
}