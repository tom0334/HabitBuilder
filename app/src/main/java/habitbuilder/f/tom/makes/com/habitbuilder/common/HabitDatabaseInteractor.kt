package habitbuilder.f.tom.makes.com.habitbuilder.common

/**
 * An interface that all activities that uses or saves to the database should implement. Especially
 * useful if the data is needed or changed from within a fragment.
 */
interface HabitDatabaseInteractor {

    /**
     * @param habit the habit that the activity should save.
     */
    fun saveHabit(habit: Habit)


    /**
     * This function is used to work around the fact that it is impossible to pass arguments to
     * fragments cleanly. It is called by fragments that need access to a specific habit
     *
     * @param the id of a habit, normally passed in the newinstance of the fragment.
     */
    fun loadHabit(id: String):Habit
}