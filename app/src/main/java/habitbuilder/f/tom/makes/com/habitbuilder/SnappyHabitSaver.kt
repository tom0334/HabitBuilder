package habitbuilder.f.tom.makes.com.habitbuilder

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.snappydb.DB
import com.snappydb.DBFactory


val HABIT_BASE_KEY= "Habit:"

class SnappyHabitSaver(context:Context) : HabitDatabase {

    private val gson = Gson()
    private val db: DB = DBFactory.open(context, DB_NAME)

    override fun save(habit: Habit) {
        val json = Gson().toJson(habit)
        db.put(habit.id, json)
        Log.i("SnappyHabitSaver", "Saved habit: ${habit}")
    }

    override fun generateNewHabitId(): String{
        var num= 0
        while (db.exists(HABIT_BASE_KEY + num)){
            num++
        }
        return HABIT_BASE_KEY + num
    }

    override fun loadAll(): List<Habit> {
        val keys = db.findKeys(HABIT_BASE_KEY)
        val result = mutableListOf<Habit>()
        for (key in keys){
            val json  = db.get(key)
            val obj = gson.fromJson(json, Habit::class.java)
            result.add(obj)
        }

        return  result
    }

    override fun load(id: String): Habit {
        val json  = db.get(id)
        return gson.fromJson(json, Habit::class.java)
    }

    override fun close() {
        db.close()
    }
}