package habitbuilder.f.tom.makes.com.habitbuilder.common

import kotlin.math.max

//A simple habit timeStamp. Doesn't do much for now.
data class HabitTimeStamp(val time: Long)

/**
 * A Class that represents a habit. It keeps
 */
data class Habit(
        val id:String,
        var name: String,
        var goal: Int,
        val positive:Boolean = true,
        val timeStamps: MutableList<HabitTimeStamp> = mutableListOf()
        )
{

    /**
     * Adds a timestamp and sorts the list of timestamps again.
     * This is NOT immediately saved in the database!
     */
    fun addTimeStamp(stamp: HabitTimeStamp){
        timeStamps.add(stamp)
        timeStamps.sortBy { it.time }
    }

    /**
     * Returns the average amount of times this habit was completed in the period between start and upTo
     * If clampToFirst is enabled, the start time will be moved to the first time this habit has been completed.
     */
    fun avgScoreInPeriod(start: Long, upTo:Long, clampStartToFirst:Boolean = false):Float{
        require(upTo>= start)

        if(timeStamps.size==0) return 0F

        //the starttime can be the startTime Passed as argument, or the first ever recorded if clamping is enabled
        val startCountingAt= if (clampStartToFirst) max(timeStamps[0].time, start) else start
        val passedMillis= upTo - startCountingAt
        if (passedMillis==0L) return 0F

        //passed days is a float (2 days 12 hours is 2,5 days)
        val passedDays=  passedMillis.toFloat() / (SECONDS_IN_DAY * 1000).toFloat()
        //count is an int!
        val count: Int = timesInPeriod(start,upTo)
        return count.toFloat() /  passedDays
    }

    /**
     * Helper function that counts the amount of times in a period.
     */
    fun timesInPeriod(from:Long, upTo:Long):Int{
        return this.timeStamps.count { it.time in from ..upTo }
    }

    /**
     * Returns the amount of times the habit was done in the day given by the timestamp.
     */
    fun timesOnDay(randomTimeStampOnDay:Long, utils: TimeUtils):Int{
        val start = utils.timeAtStartOfDay(randomTimeStampOnDay)
        val end = utils.timeAtStartOfNextDay(randomTimeStampOnDay)
        return timesInPeriod(start, end)
    }

    /**
     * this is the inverse of the times per day
     */
    fun daysPerTimeInPeriod(from:Long, upTo: Long):Float{
        val otherResult = avgScoreInPeriod(from,upTo)
        return if (otherResult==0F) 0F else 1f/otherResult
    }

    /**
     * Returns if the goal is in the day given on the timestamp
     */
    fun achievedGoalToday(timesToday:Int):Boolean{
        if(positive){
            return timesToday >= goal
        }else{
            return timesToday <= goal
        }
    }

    /**
     * Returns the amount of times per day the habit was done on average.
     */
    fun avgScoreThisWeek(now: Long, timeUtils: TimeUtils): Float {
        val start = timeUtils.oneWeekAgo(now)
        //DO NOT clamp the result, otherwise the score will be super high when first using the app.
        return avgScoreInPeriod(start, now)
    }

    fun avgScoreThisMonth(now: Long, timeUtils: TimeUtils): Float {
        val start = timeUtils.oneMonthAgo(now)
        //DO NOT clamp the result, otherwise the score will be super high when first using the app.
        return avgScoreInPeriod(start, now)
    }

    fun avgScoreAllTime(now:Long): Float {
        //clamping should be true, else the score will always be very close to zero(it will start in 1970)
        return avgScoreInPeriod(0,now, true)
    }



}
