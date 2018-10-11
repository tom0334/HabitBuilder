package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic

import android.content.Context
import android.util.AttributeSet
import android.util.TimeUtils
import android.view.ViewGroup
import android.widget.LinearLayout
import habitbuilder.f.tom.makes.com.habitbuilder.R
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.TimeUtilsJvm
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

        this.habit = habit

        //the amount that fit. The days view can be quite a lot smaller than 56 dp, but it looks
        //nice this way.
        val maxAmountForSpace = width / context.resources.getDimensionPixelSize(R.dimen.habit_day_min_width)

        val amount = min(maxAmountForSpace, 7)


        var time = System.currentTimeMillis()
        for (i in 0 until amount) {
            time = TimeUtilsJvm().oneDayAgo(time)
            val dayView = HabitDayView(this.context,habit, time )

            dayView.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
            dayViews.add(dayView)
        }
        dayViews.reverse()
        dayViews.forEach { addView(it) }
    }

    fun update(){
        dayViews.forEach { it.update() }
    }


}