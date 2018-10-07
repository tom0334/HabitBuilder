package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic

import android.app.Application
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.SnappyHabitSaver

class HabitBuilderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SnappyHabitSaver.initdatabase(applicationContext)
    }
}