package habitbuilder.f.tom.makes.com.habitbuilder

import org.junit.Assert
import org.junit.Test

import org.junit.Before
import java.util.*

/**
 * Unit Tests for the habit class
 */
class HabitUnitTest {

    var hab = Habit(HABIT_BASE_KEY + 0,"HabitName",2)

    val orderedTestDates = listOf(
            GregorianCalendar(2018,1,4),
            GregorianCalendar(2018,1,5),
            GregorianCalendar(2018,1,6)
    )

    val firstTime = orderedTestDates.first().timeInMillis
    val lastTime = orderedTestDates.last().timeInMillis

    @Before
    fun prepare(){
        hab = Habit(HABIT_BASE_KEY + 0,"HabitName",2)
    }

    //checks if the list of timestamps is in the correct order
    @Test
    fun testTimeStampOrder(){
        //add all dates in order
        for (date in orderedTestDates){
            hab.addTimeStamp(HabitTimeStamp( date.timeInMillis))
        }
        //check that they are still in order
        Assert.assertEquals(orderedTestDates[0].timeInMillis, hab.timeStamps[0].time)
        Assert.assertEquals(orderedTestDates[1].timeInMillis, hab.timeStamps[1].time)
        Assert.assertEquals(orderedTestDates[2].timeInMillis, hab.timeStamps[2].time)
    }


    //tests to check if the list is still sorted after adding a earlier date later
    @Test
    fun testTimeStampsOrderMixed(){
        hab.addTimeStamp(HabitTimeStamp(orderedTestDates[1].timeInMillis))
        hab.addTimeStamp(HabitTimeStamp( orderedTestDates[0].timeInMillis))

        Assert.assertEquals(orderedTestDates[0].timeInMillis, hab.timeStamps[0].time)
    }


    //these tests are for the score function
    @Test
    fun testScoreEmptyDataset(){
        Assert.assertEquals(0f, hab.avgScoreInPeriod(firstTime,lastTime))
    }


    //this test contains the scenario where it is exactly one day after having done the habit once
    @Test
    fun testScoreOne(){
        hab.addTimeStamp(HabitTimeStamp(firstTime))
        Assert.assertEquals(1f, hab.avgScoreInPeriod(firstTime,orderedTestDates[1].timeInMillis))
    }

    //exactly two days after adding the first data point
    @Test
    fun testScoreTwoDays(){
        hab.addTimeStamp(HabitTimeStamp(firstTime))
        //two days, only did the habit once. So the score should be 0.5
        Assert.assertEquals(0.5f, hab.avgScoreInPeriod(firstTime,lastTime))
    }




    //these tests are for the inverse function, the amount of days per time:
    @Test
    fun testScoreInverse(){
        hab.addTimeStamp(HabitTimeStamp( orderedTestDates[0].timeInMillis))
        //two days, only did the habit once. So it happens once every two days
        Assert.assertEquals(2f, hab.daysPerTimeInPeriod(firstTime,lastTime))
    }

    @Test
    fun testScoreInverseZeroTimePassed(){
        //the only timestamp is the same as the timeSince
        hab.addTimeStamp(HabitTimeStamp( lastTime))
        Assert.assertEquals(0f, hab.daysPerTimeInPeriod(lastTime,lastTime))
    }

}
