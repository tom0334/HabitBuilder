package habitbuilder.f.tom.makes.com.habitbuilder


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
        val timeStamps: MutableList<HabitTimeStamp> = mutableListOf<HabitTimeStamp>()
        )
{

    fun checkData(){
        timeStamps.sortBy { it.time }
    }

    fun addTimeStamp(stamp:HabitTimeStamp){
        timeStamps.add(stamp)
        checkData()
    }

    fun timesPerDayUptoTimeStamp(upTo:Long):Float{
        if(timeStamps.size==0) return 0F

        val first = timeStamps[0].time
        val passedMillis= upTo - first
        if (passedMillis==0L) return 0F

        val passedDays=  passedMillis.toFloat() / (SECONDS_IN_DAY * 1000).toFloat()

        var count = 0
        for (stamp in this.timeStamps){
           if(stamp.time < upTo){
               count++
           }
        }
        return count.toFloat() /  passedDays.toFloat()
    }
    //this is the inverse of the times per day
    fun daysPerTimeUpToTimeStamp(upTo: Long):Float{
        val otherResult = timesPerDayUptoTimeStamp(upTo)
        return if (otherResult==0F) 0F else 1f/otherResult
    }

}