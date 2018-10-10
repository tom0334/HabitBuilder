package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import habitbuilder.f.tom.makes.com.habitbuilder.R
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views.HabitDayView
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
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
    private lateinit var  habit:Habit

    fun init(width: Int, habit: Habit){
        assert(width > 0)

        //todo: show the actual data from this habit
        this.habit = habit

        //the amount that fit. The days view can be quite a lot smaller than 56 dp, but it looks
        //nice this way.
        val maxAmountForSpace = width / context.resources.getDimensionPixelSize(R.dimen.habit_day_min_width)

        val amount = min(maxAmountForSpace, 7)
        for (i in 0 until amount) {
            val dayView = HabitDayView(this.context)
            dayView.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)

            this.addView(dayView)
            dayViews.add(dayView)
        }
    }


}