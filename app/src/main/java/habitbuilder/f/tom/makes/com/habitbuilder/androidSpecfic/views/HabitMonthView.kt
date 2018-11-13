package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.TimeUtilsJvm
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.utils.CelebrationAnimationManager
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit

class HabitMonthView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private val TAG = "HabitMonthView"

    private lateinit var listener: TimeStampAddListener
    private lateinit var habit: Habit
    //lateInit is not allowed on primitives
    private var montsAgoFromNow: Int = 0
    private val dayViews = mutableListOf<HabitDayView>()

    private var celebrator: CelebrationAnimationManager? = null



    fun setup(monthsAgoFromNow: Int, habit: Habit, listener: TimeStampAddListener, celebrator: CelebrationAnimationManager?){
        if (this.width == 0) {
            Log.e(TAG, "MeasuredWidth of the habitWeekView is zero! Did you call setup before the view is measured?")
        }

        this.habit = habit
        this.listener = listener
        this.celebrator = celebrator

        this.orientation = LinearLayout.VERTICAL
        this.montsAgoFromNow = monthsAgoFromNow

        val utils = TimeUtilsJvm()

        val timeAtStartOfMonth = utils.timeAtStartOfCertainDayInMonth(System.currentTimeMillis(), monthsAgoFromNow,1)

        val daysToSkipOnFirstWeek = utils.dayNumOfFirstDayInMoth(timeAtStartOfMonth)
        val daysInMonth = utils.daysInMonth(timeAtStartOfMonth)
        addChildViews(daysToSkipOnFirstWeek, daysInMonth)
    }

    /**
     * Adds all the habitDayViews to this MonthView, according to the specifics of the month
     * @param daysInMonth the amount of days in this month
     * @param daysToSkipOnFirstWeek the amount of days in the first week that were in the last month
     */
    private fun addChildViews(daysToSkipOnFirstWeek: Int, daysInMonth: Int) {
        fun createDummyView():View{
            val dummy = View(context)
            dummy.layoutParams = LinearLayout.LayoutParams(0,WRAP_CONTENT, 1.0f)
            return dummy
        }
        //a list of horizontal linearLayouts. Keep in mind this HabitMonthView is vertical!
        val rows = mutableListOf<LinearLayout>()

        //add a linearLayout for each row. There are always 5 rows in a month.
        for (i in 0..4){
            val row = LinearLayout(context)
            row.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            rows.add(LinearLayout(context))
        }

        //add dummy views for the days of the week that were in the previous month.
        for (i in 0..daysToSkipOnFirstWeek){
            rows[0].addView(createDummyView())
        }

        //the rownum is the  index in the rows list. It gets incremented when a row is full( when there are 7 items)
        var rowNum = 0
        for (i in 1 ..daysInMonth){
            if (rows[rowNum].childCount==7){
                rowNum++
            }
            val actualView = createDayView(i)
            rows[rowNum].addView(actualView)
        }

        //add dummy views to the last row until it is full as wel
        while (rows.last().childCount < 7){
            rows.last().addView( createDummyView())
        }

        rows.forEach {this.addView(it)}
    }


    private fun createDayView(dayOfMonth: Int): HabitDayView{
        val utils = TimeUtilsJvm()
        val dayStart = utils.timeAtStartOfCertainDayInMonth(System.currentTimeMillis(),this.montsAgoFromNow,dayOfMonth)
        val actualView = HabitDayView(this.context,listener,habit, dayStart, null)
        actualView.layoutParams = LinearLayout.LayoutParams(0,WRAP_CONTENT,1.0f)
        dayViews.add(actualView)
        return actualView
    }

    private fun update(animate: Boolean) = dayViews.forEach{it.update(animate)}

}