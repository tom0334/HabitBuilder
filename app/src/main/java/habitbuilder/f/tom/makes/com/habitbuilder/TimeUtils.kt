package habitbuilder.f.tom.makes.com.habitbuilder

import java.util.*

abstract class TimeUtils{
    private val MILLIS_IN_DAY = 86400 * 1000
    private val MILLIS_IN_WEEK = MILLIS_IN_DAY * 7

    abstract fun timeAtStartOfDay(timeNow:Long):Long
    abstract fun timeAtStartOfNextDay(timeNow: Long): Long
    abstract fun oneMonthAgo(now: Long): Long

    //Exactly one week ago, NOT from the start of the day
    fun oneWeekAgo(timeNow: Long): Long{
        return timeNow - MILLIS_IN_WEEK
    }
}

class TimeUtilsJvm: TimeUtils() {

    private fun getCalAtStartOfDay(timeNow: Long):Calendar{
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeNow
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