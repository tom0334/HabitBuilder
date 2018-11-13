package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations
import habitbuilder.f.tom.makes.com.habitbuilder.common.TimeUtils
import java.util.*



val MILLIS_IN_DAY = 86400 * 1000
val MILLIS_IN_WEEK = MILLIS_IN_DAY * 7
val SECONDS_IN_DAY = 86400

/**
 * An JVM TimeUtils implementation based on the calendar class
 */
class TimeUtilsJvm: TimeUtils() {

    override fun timeAtStartOfCertainDayInMonth(currentTime: Long, monthsAgo: Int, dayOfMonth: Int): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = currentTime

        cal.add(Calendar.MONTH, - monthsAgo)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        //set the time to the start of the day
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)

        return cal.timeInMillis
    }


    override fun daysInMonth(currentTimeMillis: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = currentTimeMillis
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    override fun dayNumOfFirstDayInMoth(timeInMonth: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeInMonth
        //weirdly enough, day of month starts at zero, while month starts at 1.
        cal.set(Calendar.DAY_OF_MONTH,0)
        //days of the week are indexed starting at one again, so we need to subtract 1
        return cal.get(Calendar.DAY_OF_WEEK) - 1
    }

    override fun daysAgo(timeNow: Long, days: Int): Long {
        return timeNow - days * MILLIS_IN_DAY
    }

    override fun oneWeekAgo(timeNow: Long): Long {
        return timeNow - MILLIS_IN_WEEK
    }

    override fun oneDayAgo(time: Long): Long {
        return  time - MILLIS_IN_DAY
    }

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