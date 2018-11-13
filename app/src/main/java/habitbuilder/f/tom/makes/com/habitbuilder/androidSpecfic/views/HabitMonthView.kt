package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.TimeUtilsJvm

class HabitMonthView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    fun setup(){

        this.orientation = LinearLayout.VERTICAL

        //todo make this dynamic
        val time = System.currentTimeMillis()

        val utils = TimeUtilsJvm()
        val daysToSkipOnFirstWeek = utils.dayNumOfFirstDayInMoth(time)
        val daysInMonth = utils.daysInMonth(time)
        addChildViews(daysToSkipOnFirstWeek, daysInMonth)
    }

    /**
     * Adds all the habitDayViews to this MonthView, according to the specifics of the month
     * @param daysInMonth the amount of days in this month
     * @param
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
            //todo make it show an actual habitDayView
            val actualView = TextView(context)
            actualView.layoutParams = LinearLayout.LayoutParams(0,WRAP_CONTENT,1.0f)
            actualView.text = i.toString()
            rows[rowNum].addView(actualView)
        }

        //add dummy views to the last row until it is full as wel
        while (rows.last().childCount < 7){
            rows.last().addView( createDummyView())
        }

        rows.forEach {this.addView(it)}
    }

}