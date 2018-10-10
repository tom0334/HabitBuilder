package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import habitbuilder.f.tom.makes.com.habitbuilder.R

/**
 * Class that represents a day in the habit week view. NOT intended to be used separately.
 */
class HabitDayView(context: Context?) : LinearLayout(context) {

    init {
        View.inflate(context, R.layout.habit_day_view,this)
    }

}