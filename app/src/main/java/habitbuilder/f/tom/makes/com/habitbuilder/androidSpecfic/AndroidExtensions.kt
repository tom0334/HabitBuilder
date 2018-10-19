package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import nl.dionsegijn.konfetti.KonfettiView
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size


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

fun Int.toPixel(context: Context) :Int{
    val r = context.resources
    val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this.toFloat(), r.getDisplayMetrics())
    return  pixels.toInt()
}



//This shows a burts of confetti from the center of the amountTV.
//it is different from the one that shows confetti from the top!
fun KonfettiView.showConfettiInCenterWithRoot(centerView: View, parentView: View) {

    //Recursively finds the x location of a view relative to the habitFrag_Rootview.
    fun getRelativeX(myView: View): Float {
        return if (myView.parent === parentView)
            myView.x
        else
            myView.x + getRelativeX(myView.parent as View)
    }

    //same for the y location
    fun getRelativeY(myView: View): Float {
        return if (myView.parent === parentView)
            myView.y
        else
            myView.y + getRelativeY(myView.parent as View)
    }

    val centerX = getRelativeX(centerView) + centerView.width / 2
    val centerY = getRelativeY(centerView) + centerView.height / 2


    this.build()
            .addColors(Color.RED, Color.GREEN, Color.MAGENTA, Color.BLUE)
            .setDirection(0.0, 360.0)
            .setSpeed(10f, 20f)
            .setFadeOutEnabled(true)
            .setTimeToLive(1000L)
            .addShapes(Shape.CIRCLE)
            .addSizes(Size(12))
            .setPosition(centerX, centerY)
            .burst(200)
}




