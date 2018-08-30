package habitbuilder.f.tom.makes.com.habitbuilder

import org.junit.Assert
import org.junit.Test

import org.junit.Before
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class HabitUnitTest {

    var hab = Habit(UUID.randomUUID().toString(),"HabitName",2,DummySaver())

    val orderedTestDates = listOf(
            GregorianCalendar(2018,1,4),
            GregorianCalendar(2018,1,5),
            GregorianCalendar(2018,1,6)
    )

    val lastTime = orderedTestDates.last().timeInMillis

    @Before
    fun prepare(){
        hab = Habit(UUID.randomUUID().toString(),"HabitName",2,DummySaver())
    }

    //checks if the list of timestamps is in the correct order
    @Test
    fun testTimeStampOrder(){
        //add all dates in order
        for (date in orderedTestDates){
            hab.addTimeStamp(HabitTimeStamp( date.timeInMillis))
        }
        //check that they are still in order
        Assert.assertEquals(orderedTestDates[0].timeInMillis, hab.getTimeStamps()[0].time)
        Assert.assertEquals(orderedTestDates[1].timeInMillis, hab.getTimeStamps()[1].time)
        Assert.assertEquals(orderedTestDates[2].timeInMillis, hab.getTimeStamps()[2].time)
    }


    //tests to check if the list is still sorted after adding a earlier date later
    @Test
    fun testTimeStampsOrderMixed(){
        hab.addTimeStamp(HabitTimeStamp(orderedTestDates[1].timeInMillis))
        hab.addTimeStamp(HabitTimeStamp( orderedTestDates[0].timeInMillis))

        Assert.assertEquals(orderedTestDates[0].timeInMillis, hab.getTimeStamps()[0].time)
    }

    @Test
    fun testScoreZero(){
        Assert.assertEquals(0f, hab.scoreUptoTimeStamp(lastTime))
    }

    @Test
    fun testScoreOne(){
        hab.addTimeStamp(HabitTimeStamp( orderedTestDates[1].timeInMillis))
        Assert.assertEquals(1f, hab.scoreUptoTimeStamp(lastTime))
    }






}

class DummySaver: HabitDatabase{
    override fun save(habit: Habit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadAll(): List<Habit> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun load(id: String): Habit {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
