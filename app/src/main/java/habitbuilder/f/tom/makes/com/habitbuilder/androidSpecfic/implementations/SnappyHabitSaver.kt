package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.snappydb.DB
import com.snappydb.DBFactory
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
import habitbuilder.f.tom.makes.com.habitbuilder.common.HabitDatabase

val DB_NAME = "HABITS_DB"
val DB_VERSION_KEY = "VERSION"
val DB_VERSION_NUM = 0
val HABIT_BASE_KEY= "Habit:"

class SnappyHabitSaver(context:Context) : HabitDatabase {

    companion object {
        fun initdatabase(context: Context){
            val snappydb = DBFactory.open(context, DB_NAME)
            if(!snappydb.exists(DB_VERSION_KEY)  || snappydb.getInt(DB_VERSION_KEY) != DB_VERSION_NUM){
                snappydb.putInt(DB_VERSION_KEY, DB_VERSION_NUM)
                Log.i("HabitBuilderApplication", "Inserted first db version $DB_VERSION_NUM")
            }
            Log.i("HabitBuilderApplication", "Keycount is ${snappydb.countKeys(DB_VERSION_KEY)}")
            snappydb.close()
        }
    }

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

