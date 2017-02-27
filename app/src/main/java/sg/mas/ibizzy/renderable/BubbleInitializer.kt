package sg.mas.ibizzy.renderable

import sg.mas.ibizzy.renderable.helpers.MathHelper

/**
 * Created by Sergey on 25.02.17.
 */
class BubbleInitializer private constructor(val width: Float, val height: Float) {

    private constructor(builder: Builder) : this(builder.width, builder.height)

    class Builder private constructor() {

        constructor(init: Builder.() -> Unit) : this() {
            init()
        }
        var width: Float = 0.0f
        var height: Float = 0.0f

        fun width(init: Builder.() -> Float) = apply { width = init() }

        fun height(init: Builder.() -> Float) = apply { height = init() }

        fun build(): BubbleInitializer {
            return BubbleInitializer(this)
        }
    }

    fun getX(side: Side): Float {
        return when (side) {
            Side.LEFT -> 0f
            Side.RIGHT -> width
            Side.TOP -> MathHelper.randomRange(0f, width)
            Side.BOTTOM -> MathHelper.randomRange(0f, width)
            else -> throw UnsupportedOperationException()
        }
    }

    fun getY(side: Side): Float {
        return when (side) {
            Side.LEFT -> MathHelper.randomRange(0f, height)
            Side.RIGHT -> MathHelper.randomRange(0f, height)
            Side.TOP -> 0f
            Side.BOTTOM -> height
            else -> throw UnsupportedOperationException()
        }
    }

    companion object {

        lateinit var instance: BubbleInitializer
        lateinit var builder: Builder
        var centerX:Float = 0.0f
        var centerY:Float = 0.0f

        fun create(init: Builder.() -> Unit): BubbleInitializer {
            builder = Builder(init)
            centerX = builder.width/2
            centerY = builder.height/2
            instance = builder.build()
            return instance
        }
    }

    enum class Side() {
        LEFT, RIGHT, TOP, BOTTOM
    }
}
