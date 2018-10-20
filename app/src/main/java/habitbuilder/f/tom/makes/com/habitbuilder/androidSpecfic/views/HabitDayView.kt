package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views

import android.view.View
import android.widget.LinearLayout
import habitbuilder.f.tom.makes.com.habitbuilder.R
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.HabitWeekView
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.TimeUtilsJvm
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.utils.CelebrationAnimationManager
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
import kotlinx.android.synthetic.main.habit_day_view.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class that represents a day in the habit week view. NOT intended to be used separately.
 *
 * @param parent the parent HabitWeekView that created this view
 * @param context context to draw with
 * @param habit the habit to show in this view
 * @param daysAgo the amount of days ago this view shows.
 * @param dayStart the timestamp corresponding to the daysago
 */
class HabitDayView(private val celebrator: CelebrationAnimationManager, parent: HabitWeekView, val habit: Habit, val dayStart: Long, val daysAgo:Int) : LinearLayout(parent.context) {

    init {
        View.inflate(context, R.layout.habit_day_view,this)
        update(false)
    }

    /**
     * Updates this habitDayView after the habit changed.
     *
     * Also called in the setup function.
     */
    fun update(animate:Boolean){
        val timeUtils = TimeUtilsJvm()
        val date = Date(dayStart)


        if(daysAgo==1) {
            habitDayView_day_of_week.text = context.getString(R.string.habit_week_view_yesterday)
        }
        else{
            habitDayView_day_of_week.text = SimpleDateFormat("EEE").format(date)
        }

        habitDayView_date.text = SimpleDateFormat("dd-MMM").format(date)

        //find the number today
        val timesToday = habit.timesOnDay(dayStart,timeUtils)

        if(animate && habitDayView_amount.text != timesToday.toString()){
            celebrator.startAnimation(habitDayView_amount,timesToday.toString(),500)
        }else{
            habitDayView_amount.text = timesToday.toString()
        }

        //show the background color, coreresponding to wheter the goal was reached
        habitDayView_amount.background = if (habit.archievedGoalOnDay(timesToday)) context.getDrawable(R.drawable.circle_green)
        else context.getDrawable(R.drawable.circle_red)

    }





}