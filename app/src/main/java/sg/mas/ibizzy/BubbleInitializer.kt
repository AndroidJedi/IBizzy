package sg.mas.ibizzy

/**
 * Created by Sergey on 25.02.17.
 */
class BubbleInitializer private constructor(val width: Float, val height: Float) {

    init {
        println("This ($this) is a singleton")
    }

    private constructor(builder: Builder) : this(builder.width, builder.height)


    class Builder private constructor() {

        constructor(init: Builder.() -> Unit) : this() {
            init()
            println(" Builder constructor")
        }

        var width: Float = 0.0f
        var height: Float = 0.0f

        fun width(init: Builder.() -> Float) = apply { width = init() }

        fun height(init: Builder.() -> Float) = apply { height = init() }


        fun build(): BubbleInitializer {
            println(" Builder build")
            return BubbleInitializer(this)
        }
    }

    fun getX(side: Side): Float {
        return when (side) {
            Side.LEFT -> 0f
            Side.RIGHT -> width
            Side.TOP -> MathHelper.randomRange(0f, width)
            Side.BOTTOM ->MathHelper.randomRange(0f, width)
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

    private object Holder {
        lateinit var BUILDER: Builder
        lateinit var INSTANCE: BubbleInitializer
    }


    companion object {

        val instance: BubbleInitializer by lazy { builder.build() }
        lateinit var builder: Builder
        var centerX:Float = 0.0f
        var centerY:Float = 0.0f

        fun create(init: Builder.() -> Unit): BubbleInitializer {
            // fun create(init: Builder): BubbleInitializer {
            builder = Builder(init)
            centerX = builder.width/2
            centerY = builder.height/2
            println(" create")
            return instance
        }


    }


    enum class Side() {
        LEFT, RIGHT, TOP, BOTTOM
    }
}


/*  BubbleInitializer.create {
      name { "Peter" }
      surname { "Slesarew" }
      age { 28 }
  }

  // OR

  BubbleInitializer.create {
      name = "Peter"
      surname = "Slesarew"
      age = 28
  }*/
