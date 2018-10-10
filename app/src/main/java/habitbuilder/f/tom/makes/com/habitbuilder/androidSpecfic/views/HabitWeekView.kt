package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import habitbuilder.f.tom.makes.com.habitbuilder.R

class HabitWeekView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.habit_week_view,this)
    }

}
