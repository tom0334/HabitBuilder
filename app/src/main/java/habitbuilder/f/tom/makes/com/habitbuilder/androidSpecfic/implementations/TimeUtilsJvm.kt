package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations

import habitbuilder.f.tom.makes.com.habitbuilder.common.TimeUtils
import java.util.*

/**
 * An JVM TimeUtils implementation based on the calendar class
 */
class TimeUtilsJvm: TimeUtils() {

    //util function that returns a calendar instance, set to the start of a day
    private fun getCalAtStartOfDay(timeNow: Long): Calendar {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeNow

        //set the time to the start of the day
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        return cal
    }

    override fun timeAtStartOfDay(timeNow: Long): Long {
        val cal = getCalAtStartOfDay(timeNow)
        return cal.timeInMillis
    }

    override fun timeAtStartOfNextDay(timeNow:Long):Long{
        val cal = getCalAtStartOfDay(timeNow)
        cal.add(Calendar.DATE,1)
        return cal.timeInMillis
    }

    override fun oneMonthAgo(now: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = now
        //subtract one month
        cal.add(Calendar.MONTH, -1)
        return cal.timeInMillis
    }
}