package habitbuilder.f.tom.makes.com.habitbuilder

import android.content.Context
import com.snappydb.DB
import com.snappydb.DBFactory


val HABIT_BASE_KEY= "Habit:"

class SnappyHabitSaver(context:Context) : HabitDatabase {

    val db: DB

    init {
        db = DBFactory.open(context, DB_NAME)
    }

    override fun save(habit: Habit) {
        db.put(habit.id, habit)
    }

    override fun generateNewHabitId(): String{
        var num= 0
        while (db.exists(HABIT_BASE_KEY + num)){
            num++
        }
        return HABIT_BASE_KEY + num
    }

    override fun loadAll(): List<Habit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun load(id: String): Habit {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        db.close()
    }
}