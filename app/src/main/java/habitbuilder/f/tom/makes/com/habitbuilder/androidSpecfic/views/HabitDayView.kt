package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import habitbuilder.f.tom.makes.com.habitbuilder.R

class HabitDayView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.habit_day_view,this)
    }

}