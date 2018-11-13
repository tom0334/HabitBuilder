package habitbuilder.f.tom.makes.com.habitbuilder.common

/**
 * An interface that all activities that uses or saves to the database should implement. Especially
 * useful if the data is needed or changed from within a fragment.
 */
interface HabitDatabaseInteractor {


    /**
     * Should save all changes made to this habit to the database,
     * and update any data the activity should update.
     */
    fun saveChangesToHabit(changedHabit: Habit, nameChanged : Boolean)


    /**
     * Should save a new habit to the database and update the activity if needed.
     */
    fun saveNewHabit(newHabit: Habit)

    /**
     * This function is used to work around the fact that it is impossible to pass arguments to
     * fragments cleanly. It is called by fragments that need access to a specific habit
     *
     * @param the id of a habit, normally passed in the newinstance of the fragment.
     */
    fun getHabit(id: String):Habit
}