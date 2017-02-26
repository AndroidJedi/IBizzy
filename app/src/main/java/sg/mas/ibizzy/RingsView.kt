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
import sg.mas.ibizzy.BubbleInitializer.*

class RingsView : View {

    @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null,
                              defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init()

    }

    private lateinit var paint: Paint

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
            /*val rectF = RectF()
            rectF.top = (measuredHeight / 2 - (resources.getDimensionPixelSize(R.dimen.previewseekbar_indicator_width) + 10)).toFloat()
            rectF.bottom = (measuredHeight / 2 + (resources.getDimensionPixelSize(R.dimen.previewseekbar_indicator_width) + 10)).toFloat()
            rectF.left = (measuredWidth / 2 - (resources.getDimensionPixelSize(R.dimen.previewseekbar_indicator_width) + 10)).toFloat()
            rectF.right = (measuredWidth / 2 + (resources.getDimensionPixelSize(R.dimen.previewseekbar_indicator_width) + 10)).toFloat()*/

            val x = measuredWidth / 2.toFloat()
            val y = measuredHeight / 2.toFloat()

            ringList.add(Ring(x, y, END_FORWARD, radius - 10, -10, ContextCompat.getColor(context, R.color.paint1)))
            ringList.add(Ring(x, y, START_FORWARD, radius + 15, 10, ContextCompat.getColor(context, R.color.paint2)))
            ringList.add(Ring(x, y, END_BACKWARD, radius, -10, ContextCompat.getColor(context, R.color.paint3)))

            ringsAnimator = RingsAnimator(ringList)

            BubbleInitializer.create {
                width { measuredWidth.toFloat() }
                height { measuredHeight.toFloat() }
            }
        }
    }

    private var autoReply: Boolean = false


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (event.x > measuredWidth / 2 - 150 && event.x < measuredWidth / 2 + 150 && event.y > measuredHeight / 2 - 150 && event.y < measuredHeight / 2 + 150) {
                    autoReply = !autoReply
                    ringsAnimator.velocity = if (autoReply) 5 else 1
                    if (autoReply) {

                        bubbleList.add(Bubble(Side.LEFT, 25f, R.color.green))
                        bubbleList.add(Bubble(Side.LEFT, 15f, R.color.red))
                        bubbleList.add(Bubble(Side.LEFT, 30f, R.color.colorAccent))

                        bubbleList.add(Bubble(Side.TOP, 10f, R.color.blue))
                        bubbleList.add(Bubble(Side.TOP, 25f, R.color.red))
                        bubbleList.add(Bubble(Side.TOP, 20f, R.color.paint1))
                        bubbleList.add(Bubble(Side.RIGHT, 10f, R.color.violet))
                        bubbleList.add(Bubble(Side.RIGHT, 25f, R.color.pink))
                        bubbleList.add(Bubble(Side.RIGHT, 35f, R.color.blue))
                        bubbleList.add(Bubble(Side.BOTTOM, 30f, R.color.red))
                        bubbleList.add(Bubble(Side.BOTTOM, 20f, R.color.yellow))
                        bubbleList.add(Bubble(Side.BOTTOM, 35f, R.color.dark_green))

                        bubbleList.add(Bubble(Side.LEFT, 25f, R.color.green))
                        bubbleList.add(Bubble(Side.LEFT, 15f, R.color.red))
                        bubbleList.add(Bubble(Side.LEFT, 30f, R.color.colorAccent))

                        bubbleList.add(Bubble(Side.TOP, 10f, R.color.blue))
                        bubbleList.add(Bubble(Side.TOP, 25f, R.color.red))
                        bubbleList.add(Bubble(Side.TOP, 20f, R.color.paint1))
                        bubbleList.add(Bubble(Side.RIGHT, 10f, R.color.violet))
                        bubbleList.add(Bubble(Side.RIGHT, 25f, R.color.pink))
                        bubbleList.add(Bubble(Side.RIGHT, 35f, R.color.blue))
                        bubbleList.add(Bubble(Side.BOTTOM, 30f, R.color.red))
                        bubbleList.add(Bubble(Side.BOTTOM, 20f, R.color.yellow))
                        bubbleList.add(Bubble(Side.BOTTOM, 35f, R.color.dark_green))

                    } else {
                        bubbleList.clear()
                    }
                }
            }
        }

        return true
    }

    val textPaint = Paint()

    val ringList = mutableListOf<Ring>()
    val bubbleList = mutableListOf<Bubble>()
    val radius = 200f

    lateinit var ringsAnimator: RingsAnimator

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        ringsAnimator.nextValue()
        ringList.forEach { it.draw(canvas) }

        if (autoReply) {
            bubbleList.forEach {
                it.nextValue()
                it.draw(canvas)
            }

            Ricochet.check(bubbleList, ringList)
        }
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


    private var firstLayout = true
}