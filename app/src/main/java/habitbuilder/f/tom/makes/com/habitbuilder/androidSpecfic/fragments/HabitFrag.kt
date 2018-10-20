package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import habitbuilder.f.tom.makes.com.habitbuilder.*
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.PARAM_ONE_ID
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.PARAM_TWO_ID
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
import habitbuilder.f.tom.makes.com.habitbuilder.common.HabitTimeStamp
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.SnappyHabitSaver
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.TimeUtilsJvm
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.utils.CelebrationAnimationManager
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views.TimeStampAddListener
import kotlinx.android.synthetic.main.fragment_habit.*


/**
 * A [Fragment] subclass that will show a single habit, with some basic information on how the
 * user is doing. It also lets the user add new times when the user has done the habit.
 */
class HabitFrag : Fragment(), TimeStampAddListener {

    //Primitives cannot be lateinit
    //todo: see if this can be removed
    private var indexInViewPager: Int = -1

    private lateinit var habit: Habit
    private lateinit var saver: SnappyHabitSaver
    private lateinit var celebrator : CelebrationAnimationManager

    /**
     * Contains the initialisation code for the fragment.
     */
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

    /**
     * This is called when the user adds a timestamp using the clickedView
     */
    override fun onTimestampAdded(timestamp: HabitTimeStamp, clickedView: View) {
        habit.addTimeStamp(timestamp)
        saver.save(habit)
        update(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        require(arguments!=null)
        require(context!=null)

        val args:Bundle = arguments!!
        this.indexInViewPager = args.getInt(PARAM_ONE_ID)
        val habitId = args.getString(PARAM_TWO_ID)
        this.saver = SnappyHabitSaver(activity!!)
        this.habit = saver.load(habitId)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_habit, container, false)
    }


    /**
     * Initializes the view and sets up the habitWeekView after it has been drawn
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.celebrator = CelebrationAnimationManager(viewKonfetti,habitFrag_rootView)
        addClickListeners()

        update(false)

        //IMPORTANT: setup() is called when the view is done, so it can use the width to determine
        //the amount of habitDayViews to show
        main_habit_week_view.post {
            main_habit_week_view.setup( this.habit, this, celebrator)
        }
    }

    override fun onResume() {
        super.onResume()
        celebrator.resume()
    }
    override fun onPause() {
        super.onPause()
        celebrator.pause()
    }


    /**
     * Updates the views in this fragment and in the HabitWeekView to match the new data of the
     * habit.
     *
     * @param animate whether the change of the amountTV needs to be animated with a rotate and
     * confetti. The animation will only be played if the text is different.
     */
    private fun update(animate:Boolean){
        val timesToday = habit.timesOnDay(System.currentTimeMillis(), TimeUtilsJvm())

        //Set the textColor for the amount TV to red or green to indicate if the goal has been reached
        if(habit.archievedGoalOnDay(timesToday)){
            habitFrag_goalTv.text = getString(R.string.habitfrag_goalReached, habit.goal)
            habitFrag_amountTv.setTextColor(ContextCompat.getColor(this.context!!, R.color.my_material_green))
        }else{
            habitFrag_goalTv.text = getString(R.string.habitfrag_goalNotReached, habit.goal)
            habitFrag_amountTv.setTextColor(ContextCompat.getColor(this.context!!, R.color.my_material_red))
        }

        //Update the text of the amount tv.
        if (animate && timesToday.toString() != habitFrag_amountTv.text ) {
            celebrator.startAnimation(habitFrag_amountTv, timesToday.toString(), duration = 500)
        }else{
            habitFrag_amountTv.text = timesToday.toString()
        }

        main_habit_week_view.update(animate)
    }

    /**
     * Adds click listeners for the buttons in this fragment.
     */
    private fun addClickListeners() {
        habitFrag_justNowButton.setOnClickListener {
            habit.addTimeStamp(HabitTimeStamp(System.currentTimeMillis()))
            //todo save the habit in a background thread to get rid of the lagspike in the animation
            saver.save(habit)
            update(true)
        }
    }



}
