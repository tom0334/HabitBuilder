package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic



import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import habitbuilder.f.tom.makes.com.habitbuilder.*
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
import habitbuilder.f.tom.makes.com.habitbuilder.common.HabitTimeStamp
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.SnappyHabitSaver
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.TimeUtilsJvm
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.views.TimeStampAddListener
import kotlinx.android.synthetic.main.fragment_habit.*
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import java.util.*

//these are just paramms that are used for the .newinstance pattern
private val PARAM_ONE_ID = "PARAM_1"
private val PARAM_TWO_ID = "PARAM_2"

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

    //This queue is used for the rotate animations on the amountTV. It keeps a list of animators
    //to show after the current is played.
    private val animationQueue = LinkedList<ValueAnimator>()


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
     * This is called when the user adds a timestamp using the habitWeekView
     */
    override fun onTimestampAdded(timestamp: HabitTimeStamp) {
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
        this.habit = saver.load(habitId)

        this.saver = SnappyHabitSaver(activity!!)
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
        addClickListeners()

        update(false)

        //IMPORTANT: setup() is called when the view is done, so it can use the width to determine
        //the amount of habitDayViews to show
        main_habit_week_view.post {
            main_habit_week_view.setup( this.habit, this)
        }
    }

    override fun onResume() {
        super.onResume()
        //resume the animation that was paused in onPause
        if (animationQueue.size>0){
            animationQueue.first.pause()
        }
    }
    override fun onPause() {
        super.onPause()
        //pause the currently running animation
        if (animationQueue.size>0){
            animationQueue.first.pause()
        }
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
            animateAmountTextAndShowConfetti(timesToday.toString())
        }else{
            habitFrag_amountTv.text = timesToday.toString()
        }

        main_habit_week_view.update()
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

    /**
     * This animates the change of amount. It does so with a rotation. If an animation is already
     * playing, it will add it to the queue of animations to play. When an animation finishes it
     * starts the next one automatically.
     *
     * When an animation starts playing, the confetti is also started.
     *
     * @param text the new text to show.
     */
    private fun animateAmountTextAndShowConfetti(text: String){
        //make one full rotation
        val anim = ValueAnimator.ofFloat(0f, 360f)
        anim.duration = 500

        anim.addUpdateListener {
            val animatedValue  = it.animatedValue as Float
            habitFrag_amountTv.rotationX = animatedValue

            //update the text when te animation reaches halfway, so it is perfectly upside down
            //and at maximum speed. This makes the sudden change hard to see.
            if (it.animatedFraction>= 0.5 && habitFrag_amountTv.text != text){
                habitFrag_amountTv.text = text
            }
        }

        //when the animation ends, remove the current from the front, and start the one that is
        //at the front after removal. Also show the confetti
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                animationQueue.removeFirst()
                if (animationQueue.size > 0) {
                    animationQueue.first.start()
                    showConFetti()
                }
            }
        })

        //Add new animation to the queue, and play it if it is the only one.
        animationQueue.add(anim)
        if (animationQueue.size==1){
            animationQueue.first.start()
            showConFetti()
        }
    }

    /**
     * Shows confetti at the center of the amountTV.
     */
    private fun showConFetti(){
        val centerX = habitFrag_amountTv.x + habitFrag_amountTv.width/2
        val centerY = habitFrag_amountTv.y + habitFrag_amountTv.height/2

        viewKonfetti.build()
                .addColors(Color.RED, Color.GREEN, Color.MAGENTA, Color.BLUE)
                .setDirection(0.0,360.0)
                .setSpeed(10f, 20f)
                .setFadeOutEnabled(true)
                .setTimeToLive(1000L)
                .addShapes( Shape.CIRCLE)
                .addSizes(Size(12))
                .setPosition(centerX,centerY)
                .burst(200)
    }

}
