package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic

import android.support.v4.content.ContextCompat
import android.view.View


/**
 * This function implements one frame of a color background animation.
 *
 * It is used for the main content view when sliding the bottom sheet up.
 *
 * @param progress the a number between 0 and 1 representing how far the animation should be.
 * @param colorFromId the resource ID(!) of the color to start from
 * @param colorToId the resource ID(!) of the color to show at 100% animation
 *
 */
fun View.transitionBackGroundColor(progress : Float, colorFromId:Int, colorToId: Int) {
    /**
     * This function returns the calculated in-between value for a color
     * given integers that represent the start and end values in the four
     * bytes of the 32-bit int. Each channel is separately linearly interpolated
     * and the resulting calculated values are recombined into the return value.
     *
     * @param fraction The fraction from the starting to the ending values
     * @param startValue A 32-bit int value representing colors in the
     * separate bytes of the parameter
     * @param endValue A 32-bit int value representing colors in the
     * separate bytes of the parameter
     * @return A value that is calculated to be the linearly interpolated
     * result, derived by separating the start and end values into separate
     * color channels and interpolating each one separately, recombining the
     * resulting values in the same way.
     */
    fun interpolateColor(fraction: Float, startValue: Int, endValue: Int): Int {
        val startA = startValue shr 24 and 0xff
        val startR = startValue shr 16 and 0xff
        val startG = startValue shr 8 and 0xff
        val startB = startValue and 0xff
        val endA = endValue shr 24 and 0xff
        val endR = endValue shr 16 and 0xff
        val endG = endValue shr 8 and 0xff
        val endB = endValue and 0xff
        return startA + (fraction * (endA - startA)).toInt() shl 24 or
                (startR + (fraction * (endR - startR)).toInt() shl 16) or
                (startG + (fraction * (endG - startG)).toInt() shl 8) or
                startB + (fraction * (endB - startB)).toInt()
    }

    val colorFrom = ContextCompat.getColor(this.context, colorFromId)
    val colorTo = ContextCompat.getColor(this.context, colorToId)
    this.setBackgroundColor(interpolateColor(progress, colorFrom, colorTo))
}

