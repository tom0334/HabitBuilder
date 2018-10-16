package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import habitbuilder.f.tom.makes.com.habitbuilder.R
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.TimeUtilsJvm
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views.HabitDayView
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views.TimeStampAddListener
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
import habitbuilder.f.tom.makes.com.habitbuilder.common.HabitTimeStamp
import kotlinx.android.synthetic.main.habit_day_view.view.*
import kotlin.math.min

/**
 * Class that represents a habit week.
 */
class HabitWeekView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    /**
     * Initializes the view. It needs to know the amount of space it has to determine how many
     * days to add.
     */

    private val dayViews = mutableListOf<HabitDayView>()
    private val listeners = mutableListOf<TimeStampAddListener>()

    private lateinit var  habit:Habit

    fun init(width: Int, habit: Habit, listener: TimeStampAddListener){
        assert(width > 0)

        this.habit = habit
        listeners.add(listener)

        addChildViews()

        for (dayView in dayViews) {
            
            dayView.habitDayView_amount.setOnClickListener {
                Toast.makeText(this.context, context.getString(R.string.habit_day_view_toast_long_press),Toast.LENGTH_LONG).show()
            }
            
            dayView.habitDayView_amount.setOnLongClickListener { view: View? ->
                val newTimeStamp = HabitTimeStamp(dayView.dayStart)
                listeners.forEach{it.onTimestampAdded(newTimeStamp)}
                //return true to consume the touch event
                true
            }
        }
    }


    private fun addChildViews(){
        //the amount that fit. The days view can be quite a lot smaller than 56 dp, but it looks
        //nice this way.
        val maxAmountForSpace = width / context.resources.getDimensionPixelSize(R.dimen.habit_day_min_width)

        val amount = min(maxAmountForSpace, 7)


        //start at one day ago. This means today is not showed here, as that is showed in the large
        //section already
        for (daysAgo in 1  .. amount) {
            //timeMillis corresponding to the amount of days ago
            val timeMillis = TimeUtilsJvm().daysAgo( System.currentTimeMillis(), daysAgo)
            val dayView = HabitDayView(this,habit, timeMillis, daysAgo )

            dayView.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
            dayViews.add(dayView)
        }
        dayViews.reverse()
        dayViews.forEach { addView(it) }
    }


    fun update() = dayViews.forEach { it.update() }


}