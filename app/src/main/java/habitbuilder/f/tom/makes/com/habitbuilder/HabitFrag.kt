package habitbuilder.f.tom.makes.com.habitbuilder



import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_habit.*

private val PARAM_ONE_ID = "PARAM_1"
private val PARAM_TWO_ID = "PARAM_2"
/**
 * A [Fragment] subclass that will show a single habit.
 *
 */
class HabitFrag : Fragment() {

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
    }

    fun showData(){
        val timesToday = habit.timesOnDay(System.currentTimeMillis(),TimeUtilsJvm())

        if(habit.archievedGoalToday(timesToday)){
            habitFrag_goalTv.text = getString(R.string.habitfrag_goalReached, habit.goal)
            habitFrag_amountTv.setTextColor(ContextCompat.getColor(this.context!!,R.color.my_material_green))
        }else{
            habitFrag_goalTv.text = getString(R.string.habitfrag_goalNotReached, habit.goal)
            habitFrag_amountTv.setTextColor(ContextCompat.getColor(this.context!!,R.color.my_material_red))
        }
        habitFrag_amountTv.text = timesToday.toString()
    }

    private fun addClickListeners() {
        habitFrag_justNowButton.setOnClickListener {
            Toast.makeText(this.context,"Great!",Toast.LENGTH_LONG).show()
            habit.addTimeStamp(HabitTimeStamp(System.currentTimeMillis()))
            saver.save(habit)
            showData()
        }
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
