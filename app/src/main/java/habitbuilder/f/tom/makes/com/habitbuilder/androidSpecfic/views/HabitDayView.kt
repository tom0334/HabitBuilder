package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import habitbuilder.f.tom.makes.com.habitbuilder.R
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.TimeUtilsJvm
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.utils.CelebrationAnimationManager
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
import habitbuilder.f.tom.makes.com.habitbuilder.common.HabitTimeStamp
import kotlinx.android.synthetic.main.habit_day_view.view.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ViewConstructor")// Not needed, since the habitDayView is never used seperately.
/**
 * Class that represents a day in the habit week view. NOT intended to be used separately.
 * @param listener a TimeStampListener that has its OnTimeStampAdded function called when a timestamp
 * is added through this view.
 * @param context context to draw with
 * @param habit the habit to show in this view
 * @param dayStart the timestamp corresponding to the daysago
 * @param celebrator a CelebrationAnimationManager that manages the confetti animation. Optional.
 * @param isYesterday if this view shows yesterday, and thus should show the yesterday text at the top.
 * default value is false.
 */
class HabitDayView(context: Context,
                   private val listener: TimeStampAddListener,
                   private val habit: Habit,
                   private val dayStart: Long,
                   private val dateFormat: SimpleDateFormat,
                   private val celebrator: CelebrationAnimationManager? = null,
                   private val showDayOfWeek: Boolean = false,
                   private val isYesterday: Boolean = false
) : LinearLayout(context) {

    /**
     * Initializes a HabitDayView, and its clickListeners. Note that the listeners may be disabled
     * because the view button view is disabled. That happens when a dayView is for a day that is in
     * the future.
     */
    init {
        View.inflate(context, R.layout.habit_day_view, this)

        //add the short click listener. This only shows a toast.
        this.habitDayView_amount.setOnClickListener {
            Toast.makeText(this.context, context.getString(R.string.habit_day_view_toast_long_press), Toast.LENGTH_LONG).show()
        }

        //On longclick, a timestamp is added.
        this.habitDayView_amount.setOnLongClickListener {
            val newTimeStamp = HabitTimeStamp(this.dayStart)
            listener.onTimestampAdded(newTimeStamp, it)

            //return true to consume the touch event
            true
        }

        update(false)
    }

    /**
     * Updates this habitDayView after the habit changed.
     *
     * Also called in the setup function.
     * @param animate if the celebrator animation should be started.
     */
    fun update(animate: Boolean) {
        val utils = TimeUtilsJvm()

        val date = Date(dayStart)
        habitDayView_day_of_week.visibility = if (showDayOfWeek) View.VISIBLE else View.GONE

        if (this.isYesterday) {
            habitDayView_day_of_week.text = context.getString(R.string.habit_week_view_yesterday)
        } else {
            //EEE means day of the week, so 'Monday' for example.
            habitDayView_day_of_week.text = SimpleDateFormat("EEE").format(date)
        }

        habitDayView_date.text = this.dateFormat.format(date)


        val enabled = utils.timeAtStartOfDay(System.currentTimeMillis()) > this.dayStart

        //updates the button state.
        habitDayView_amount.isEnabled = enabled
        if(enabled){
            this.showEnabledAmountButton(animate)
        }else{
            showDisabledAmountButton()
        }
    }

    /**
     * Shows an enabled state for a date that is before the current date.
     */
    private fun showEnabledAmountButton(animate: Boolean) {
        //find the number today
        val timesToday = habit.timesOnDay(dayStart, TimeUtilsJvm())

        if (animate && habitDayView_amount.text != timesToday.toString()) {
            celebrator?.startAnimation(habitDayView_amount, timesToday.toString(), 500)
        } else {
            habitDayView_amount.text = timesToday.toString()
        }

        //show the background color, coreresponding to wheter the goal was reached
        habitDayView_amount.background = if (habit.archievedGoalOnDay(timesToday)) context.getDrawable(R.drawable.circle_green)
        else context.getDrawable(R.drawable.circle_red)
    }

    /**
     * Show a disables state for a day that is after the current state. These are for days that cannot
     * be filled in yet.
     */
    private fun showDisabledAmountButton(){
        habitDayView_amount.text = ""
        habitDayView_amount.background = context.getDrawable(R.drawable.circle_disabled)

    }

}