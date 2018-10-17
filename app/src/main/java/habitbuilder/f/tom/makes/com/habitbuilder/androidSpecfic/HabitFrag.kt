package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic



import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import habitbuilder.f.tom.makes.com.habitbuilder.*
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
import habitbuilder.f.tom.makes.com.habitbuilder.common.HabitTimeStamp
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.SnappyHabitSaver
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.TimeUtilsJvm
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views.TimeStampAddListener
import kotlinx.android.synthetic.main.fragment_habit.*
import kotlinx.android.synthetic.main.fragment_habit.view.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

//these are just paramms that are used for the .newinstance pattern
private val PARAM_ONE_ID = "PARAM_1"
private val PARAM_TWO_ID = "PARAM_2"

/**
 * A [Fragment] subclass that will show a single habit, with some basic information on how the
 * user is doing. It also lets the user add new times when the user has done the habit.
 */
class HabitFrag : Fragment(), TimeStampAddListener {


    override fun onTimestampAdded(timestamp: HabitTimeStamp) {
        habit.addTimeStamp(timestamp)
        saver.save(habit)
        showData()
    }

    //Primitives cannot be lateinit
    private var indexInViewPager: Int = -1
    private lateinit var habitId: String
    private lateinit var habit: Habit
    private lateinit var saver: SnappyHabitSaver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        require(arguments!=null)
        require(context!=null)

        val args:Bundle = arguments!!
        this.indexInViewPager = args.getInt(PARAM_ONE_ID)
        this.habitId = args.getString(PARAM_TWO_ID)
        this.saver = SnappyHabitSaver(activity!!)
        this.habit = saver.load(habitId)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_habit, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addClickListeners()
        showData()
        main_habit_week_view.post {
            main_habit_week_view.setup( this.habit, this)
        }
    }



    fun showData(){
        val timeUtils = TimeUtilsJvm()

        val timesToday = habit.timesOnDay(System.currentTimeMillis(), TimeUtilsJvm())

        if(habit.archievedGoalOnDay(timesToday)){
            habitFrag_goalTv.text = getString(R.string.habitfrag_goalReached, habit.goal)
            //show the large number in green
            habitFrag_amountTv.setTextColor(ContextCompat.getColor(this.context!!, R.color.my_material_green))
        }else{
            habitFrag_goalTv.text = getString(R.string.habitfrag_goalNotReached, habit.goal)
            //show the large number in red
            habitFrag_amountTv.setTextColor(ContextCompat.getColor(this.context!!, R.color.my_material_red))
        }
        habitFrag_amountTv.text = timesToday.toString()

        main_habit_week_view.update()
    }

    private fun addClickListeners() {
        habitFrag_justNowButton.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action== MotionEvent.ACTION_UP) {
                Toast.makeText(this.context, "Great!", Toast.LENGTH_LONG).show()
                habit.addTimeStamp(HabitTimeStamp(System.currentTimeMillis()))
                saver.save(habit)
                showData()
                showConFetti(view.x + motionEvent.x, view.y + motionEvent.y)
            }
            false
        }
    }

    private fun showConFetti(x: Float, y: Float){

        //val centerX = origin.x + origin.width/2
        //val centerY = origin.y + origin.height/2
         viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 360.0)
                .setSpeed(10f, 50f)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT)
                .addSizes(Size(12))
                .setPosition(x,y)
                //.setPosition(origin.x, origin.x + origin.width, origin.y, origin.y + origin.height)

                .burst(300)

    }


    companion object {
        fun newInstance(indexInViewPager: Int, habitId: String): HabitFrag {
            val f = HabitFrag()
            val args = Bundle()
            args.putInt(PARAM_ONE_ID, indexInViewPager)
            args.putString(PARAM_TWO_ID, habitId)
            f.arguments = args
            return f
        }
    }





}
