package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.View
import android.widget.TextView
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.showConfettiInCenterWithRoot
import nl.dionsegijn.konfetti.KonfettiView


import java.util.*

/**
 * This Class manages the animations of the AmountView and the HabitDayView when a habit is completed.
 * It handles queueing up animations when a previous one is still running perfectly.
 *
 * Note that you NEED to call the pause function in your onPause and the resunme in onResume.
 *
 * @param confettiView the KonFettiView that should show the confetti.
 * @param konfettiViewParent the parent of the konFettiView. This is used to find the location of
 * the tap, relative to the parentView.
 */
class CelebrationAnimationManager(val confettiView: KonfettiView, val konfettiViewParent: View) {

    //This queue is used for the rotate animations. It keeps a list of animators
    //to show after the current is played.
    private val animationQueue = LinkedList<ValueAnimator>()


    /**
     * This animates the change of amount. It does so with a rotation. If an animation is already
     * playing, it will add it to the queue of animations to play. When an animation finishes it
     * starts the next one automatically.
     *
     * When an animation starts playing, the confetti is also started.
     *
     * @param text the new text to show.
     */
    fun startAnimation(viewToAnimate: TextView, text: String, duration: Long) {
        val anim = ValueAnimator.ofFloat(0f, 360f)
        anim.duration = duration

        anim.addUpdateListener {
            val animatedValue = it.animatedValue as Float
            viewToAnimate.rotationX = animatedValue

            //update the text when te animation reaches halfway, so it is perfectly upside down
            //and at maximum speed. This makes the sudden change hard to see.
            if (it.animatedFraction >= 0.5 && viewToAnimate.text != text) {
                viewToAnimate.text = text
            }
        }

        //when the animation ends, remove the current from the front, and start the one that is
        //at the front after removal. Also show the confetti
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animationQueue.removeFirst()
                if (animationQueue.size > 0) {
                    animationQueue.first.start()
                    confettiView.showConfettiInCenterWithRoot(viewToAnimate, konfettiViewParent)
                }
            }
        })

        //Add new animation to the queue, and play it if it is the only one.
        animationQueue.add(anim)
        if (animationQueue.size == 1) {
            animationQueue.first.start()
            confettiView.showConfettiInCenterWithRoot(viewToAnimate, konfettiViewParent)
        }
    }

    /**
     * Pauses the animation. Call this in onPause, otherwise problems may occur.
     */
    fun pause() {
        if (animationQueue.size > 0) {
            animationQueue.first.pause()
        }
    }

    /**
     * resumes the animation that was paused in onPause. Call  this in onResume
     */
    fun resume() {
        if (animationQueue.size > 0) {
            animationQueue.first.pause()
        }
    }


}