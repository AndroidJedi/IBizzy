package sg.mas.ibizzy

import android.util.Pair

/**
 * Created by Sergey on 23.02.17.
 */
class Ricochet {
    companion object {
        fun check(bubbleList: List<Bubble>, ringList: List<Ring>) {
            for (bubble in bubbleList) {
                for (ring in ringList) {
                   if(ring.contains(bubble)){
                       bubble.ricochet()
                   }
                }
            }
        }
    }
}