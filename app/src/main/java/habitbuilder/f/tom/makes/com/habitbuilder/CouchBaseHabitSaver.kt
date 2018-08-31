package habitbuilder.f.tom.makes.com.habitbuilder

import android.content.Context
import com.couchbase.lite.Database
import com.couchbase.lite.DatabaseConfiguration
import com.couchbase.lite.MutableDocument

class CouchBaseHabitSaver(context : Context) : HabitDatabase {

    private val database: Database

    init {
        val config = DatabaseConfiguration(context)
        this.database = Database(DATABASE_NAME, config)
    }

    override fun saveOrUpdate(habit: Habit) {
        val doc = database.getDocument(habit.id)
        val mutableDoc = if (doc==null) MutableDocument(habit.id) else doc


    }

    override fun loadAll(): List<Habit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun load(id: String): Habit {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}