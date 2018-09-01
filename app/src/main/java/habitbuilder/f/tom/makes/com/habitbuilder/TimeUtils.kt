package habitbuilder.f.tom.makes.com.habitbuilder

import java.util.*

interface TimeUtils {
    fun timeAtStartOfDay(timeNow:Long):Long
    fun timeAtStartOfNextDay(timeNow: Long): Long
}

class TimeUtilsJvm: TimeUtils{

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

}