package habitbuilder.f.tom.makes.com.habitbuilder

import android.app.Application
import android.util.Log
import com.snappydb.DBFactory
import com.snappydb.DB


val DB_VERSION_KEY = "VERSION"
val DB_VERSION_NUM = 0

class HabitBuilderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val snappydb = DBFactory.open(applicationContext,"habitsDb")

        if(!snappydb.exists(DB_VERSION_KEY)  || snappydb.getInt(DB_VERSION_KEY) != DB_VERSION_NUM){
            snappydb.putInt(DB_VERSION_KEY, DB_VERSION_NUM)
            Log.i("HabitBuilderApplication", "Inserted first db version $DB_VERSION_NUM")
        }
        Log.i("HabitBuilderApplication", "Keycount is ${snappydb.countKeys(DB_VERSION_KEY)}")
        snappydb.close()
    }
}