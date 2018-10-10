package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views.HabitDayView
import kotlin.math.min

class HabitWeekView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    fun init(width:Int){
        Log.e("width" ,"$width")
        val maxAmountForSpace = width / 56.toPixel(this.context)

        val amount = min(maxAmountForSpace, 7)
        for (i in 0 until amount) {
            val dayView = HabitDayView(this.context)
            val params = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
            dayView.layoutParams = params
            this.addView(dayView)
        }
    }

}