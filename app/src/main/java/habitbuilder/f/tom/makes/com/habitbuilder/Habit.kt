package habitbuilder.f.tom.makes.com.habitbuilder

import java.sql.Timestamp


data class HabitTimeStamp(val time: Long)

interface HabitDatabase{
    fun save(habit:Habit)
    fun loadAll():List<Habit>
    fun load(id:String):Habit
}


val SECONDS_IN_YEAR = 31556926

class Habit(
        val id:String,
        var name: String,
        var goal: Int,
        val saver: HabitDatabase
        ){
    private val timeStamps = mutableListOf<HabitTimeStamp>()

    fun checkData(){
        timeStamps.sortBy { it.time }
    }

    fun getTimeStamps(): List<HabitTimeStamp>{
        return this.timeStamps.toList()
    }

    fun addTimeStamp(stamp:HabitTimeStamp){
        timeStamps.add(stamp)
        checkData()
    }

    fun scoreUptoTimeStamp(upTo:Long):Float{
        if(timeStamps.size==0) return 0F

        var count = 0
        for (stamp in this.timeStamps){
           if(stamp.time < upTo){
               count++
           }
        }

        val first = timeStamps[0].time
        val passedDays=  (upTo - first) /SECONDS_IN_YEAR
        return count.toFloat() / goal.toFloat() * passedDays.toFloat()
    }

}