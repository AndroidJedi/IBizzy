package sg.mas.ibizzy.renderable.helpers

import sg.mas.ibizzy.renderable.Bubble
import sg.mas.ibizzy.renderable.Ring

/**
 * Created by Sergey on 23.02.17.
 */
class Ricochet {
    companion object {
        fun check(bubbleList: List<Bubble>, ringList: List<Ring>) {
                for (bubble in bubbleList) {
                    if (bubble.moveToCenter)
                        for (ring in ringList) {
                            if (ring.intersects(bubble)) {
                                bubble.ricochet()
                                return
                            }
                        }
                }
        }
        fun check(bubble: Bubble, ringList: List<Ring>) {
                if (bubble.moveToCenter)
                    for (ring in ringList) {
                        if (ring.intersects(bubble)) {
                            bubble.ricochet()
                            return
                        }
                    }
        }
    }
}