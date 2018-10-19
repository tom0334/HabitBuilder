package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views

import android.view.View
import habitbuilder.f.tom.makes.com.habitbuilder.common.HabitTimeStamp

interface TimeStampAddListener {
fun onTimestampAdded(timestamp: HabitTimeStamp, clickedView: View)
}