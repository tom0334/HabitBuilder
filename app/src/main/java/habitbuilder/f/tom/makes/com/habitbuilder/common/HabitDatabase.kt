package habitbuilder.f.tom.makes.com.habitbuilder.common

/**
 * This is an interface for a database that saves a habit on the device. It can do so in any way it wants.
 *
 * It is an interface to make it possible to change what database the app uses at any time, or to make it use another
 * database solution on IOS.
 */

interface HabitDatabase{
    //Saves a habit. When a habit already exists, it updates it.
    fun save(habit: Habit)

    //Load all habits on the device.
    fun loadAll():List<Habit>

    //load one specific habit from the device.
    fun load(id:String): Habit

    //creates a new unique habit id, so it can be found again later by the load function
    fun generateNewHabitId(): String

    //closes this instance of the database.
    fun close()
}