package habitbuilder.f.tom.makes.com.habitbuilder.common

/**
 * Contains an interface for some date/time related functions. Kotlin does not have a date/time library,
 * so for the android version i don't have much choice other than using the java one.
 *
 * This means there will be a JVM implementation of this class that uses java date/time functionality.
 *
 *
 * Later there might be others for other platforms.
 */

abstract class TimeUtils{

    //Should return the exact time at the start (00:00) of a day, on the day of timeNow
    abstract fun timeAtStartOfDay(timeNow:Long):Long

    //Should return the exact time at the start (00:00) of the day after timeNow.
    abstract fun timeAtStartOfNextDay(timeNow: Long): Long

    //Should return EXACTLY a month ago.
    abstract fun oneMonthAgo(now: Long): Long

    //Exactly one week ago, NOT from the start of the day
    abstract fun oneWeekAgo(timeNow: Long): Long

    //exactly one day ago. NOT from the start of the day
    abstract fun oneDayAgo(time:Long): Long

    abstract fun daysAgo(timeNow: Long, days: Int): Long

    //returns the num of the day of the week, starting at 0.
    //that is, if the first day of the month is a monday, this function should return 0.
    //if it is a sunday it should return 6.
    abstract fun dayNumOfFirstDayInMoth(timeInMonth: Long): Int

    //returns the amount of days in the month given by the current time millis
    abstract fun daysInMonth(currentTimeMillis: Long): Int

    //returns the time at the start of day x in the month of timeAtStartOfMonth
    abstract fun timeAtStartOfCertainDayInMonth(currentTime: Long, monthsAgo: Int, dayOfMonth: Int): Long
}

