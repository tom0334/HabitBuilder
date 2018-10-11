package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views

import android.content.Context
import android.text.format.DateUtils
import android.view.View
import android.widget.LinearLayout
import habitbuilder.f.tom.makes.com.habitbuilder.R
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.TimeUtilsJvm
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
import kotlinx.android.synthetic.main.habit_day_view.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Class that represents a day in the habit week view. NOT intended to be used separately.
 */
class HabitDayView(context: Context, val habit: Habit, val dayStart: Long) : LinearLayout(context) {

    init {
        View.inflate(context, R.layout.habit_day_view,this)
        update()
    }

    fun update(){
        val timeUtils = TimeUtilsJvm()

        val date = Date(dayStart)
        habitDayView_date.text = SimpleDateFormat("EEE").format(date)

        val timesToday = habit.timesOnDay(dayStart,timeUtils)

        habitDayView_amount.background = if (habit.archievedGoalOnDay(timesToday)) context.getDrawable(R.drawable.circle_green)
        else context.getDrawable(R.drawable.circle_red)

        habitDayView_amount.text = timesToday.toString()
    }



}