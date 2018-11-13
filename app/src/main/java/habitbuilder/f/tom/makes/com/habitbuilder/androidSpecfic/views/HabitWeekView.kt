package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import habitbuilder.f.tom.makes.com.habitbuilder.R
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.TimeUtilsJvm
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.utils.CelebrationAnimationManager
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
import kotlin.math.min

/**
 * Class that represents a habit week.
 */
class HabitWeekView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private val TAG = "HabitWeekView"

    /**
     * Initializes the view. It needs to know the amount of space it has to determine how many
     * days to add.
     */

    //The child views
    private val dayViews = mutableListOf<HabitDayView>()

    //Listeners for the timestamps.
    private lateinit var listener: TimeStampAddListener

    //the habit to show.
    private lateinit var habit: Habit

    private lateinit var celebrator: CelebrationAnimationManager

    /**
     * Initializes the HabitWeekView, and adds a listener for changes to the HabitWeekView. Note that
     * this function NEEDS to be called after the view is drawn, since it needs its width to
     * determine how many habitDayViews it should show.
     *
     * It is best to call it using view.post()
     * @param celebrator the celebrationManager that is used to show the confetti animations.
     * @param habit the habit to show in this view.
     * @param listener a class that implements the TimeStampAddListener interface. The function in
     * it is called when the user adds a timestemp. The Implementation is responsible for saving the
     * changes!
     */
    fun setup(habit: Habit, listener: TimeStampAddListener, celebrator: CelebrationAnimationManager) {
        if (this.width == 0) {
            Log.e(TAG, "MeasuredWidth of the habitWeekView is zero! Did you call setup before the view is measured?")
        }

        this.habit = habit
        this.listener = listener
        this.celebrator = celebrator

        addChildViews(listener)
    }


    /**
     * This creates the child day views. The amount is dependent on the width!
     */
    private fun addChildViews(listener: TimeStampAddListener) {
        //the amount that fit. The days view can be quite a lot smaller than 56 dp, but it looks
        //nice this way.
        val maxAmountForSpace = this.width / context.resources.getDimensionPixelSize(R.dimen.habit_day_min_width)

        val amount = min(maxAmountForSpace, 7)


        //start at the amount of days ago, and end at 1. 0 is not shown, since that is today and
        //that is already shown in the
        for (daysAgo in amount downTo 1) {
            //timeMillis corresponding to the amount of days ago
            val timeMillis = TimeUtilsJvm().daysAgo(System.currentTimeMillis(), daysAgo)
            val dayView = HabitDayView(this.context,listener,habit,timeMillis,celebrator, , showDayOfWeek = true, isYesterday = daysAgo==1)

            dayView.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)

            //add it to the list so we can update it later.
            dayViews.add(dayView)

            //show the view
            addView(dayView)
        }
    }

    /**
     * Updates all child HabitDayViews.
     */
    fun update(animate: Boolean) = dayViews.forEach { it.update(animate) }


}