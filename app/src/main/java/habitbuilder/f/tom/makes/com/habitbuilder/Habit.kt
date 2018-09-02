package habitbuilder.f.tom.makes.com.habitbuilder

import kotlin.math.max


data class HabitTimeStamp(val time: Long)

interface HabitDatabase{
    fun save(habit:Habit)
    fun loadAll():List<Habit>
    fun load(id:String):Habit
    fun generateNewHabitId(): String
    fun close()
}

val SECONDS_IN_DAY = 86400

data class Habit(
        val id:String,
        var name: String,
        var goal: Int,
        val positive:Boolean = true,
        val timeStamps: MutableList<HabitTimeStamp> = mutableListOf()
        )
{

    fun checkData(){
        timeStamps.sortBy { it.time }
    }

    fun addTimeStamp(stamp:HabitTimeStamp){
        timeStamps.add(stamp)
        checkData()
    }

    /**
     * Returns the average amount of times this habit was completed in the period between start and upTo
     *
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

    fun timesInPeriod(from:Long, upTo:Long):Int{
        var count = 0
        for (stamp in this.timeStamps){
            if(stamp.time in from..upTo){
                count++
            }
        }
        return count
    }

    fun timesOnDay(randomTimeStampOnDay:Long, utils: TimeUtils):Int{
        val start = utils.timeAtStartOfDay(randomTimeStampOnDay)
        val end = utils.timeAtStartOfNextDay(randomTimeStampOnDay)
        return timesInPeriod(start, end)
    }


    //this is the inverse of the times per day
    fun daysPerTimeInPeriod(from:Long, upTo: Long):Float{
        val otherResult = avgScoreInPeriod(from,upTo)
        return if (otherResult==0F) 0F else 1f/otherResult
    }

    //when null is passed, it will be calculated
    fun archievedGoalToday(timesToday:Int):Boolean{
        if(positive){
            return timesToday >= goal
        }else{
            return timesToday <= goal
        }
    }

    fun avgScoreThisWeek(now: Long, timeUtils: TimeUtils): Float {
        val start = timeUtils.oneWeekAgo(now)
        return avgScoreInPeriod(start, now)
    }

    fun avgScoreThisMonth(now: Long, timeUtils: TimeUtils): Float {
        val start = timeUtils.oneMonthAgo(now)
        return avgScoreInPeriod(start, now)
    }

    fun avgScoreAllTime(now:Long): Float {
        //clamping should be true, else the score will always be very close to zero.
        return avgScoreInPeriod(0,now, true)
    }



}