package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.fragments

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import habitbuilder.f.tom.makes.com.habitbuilder.R
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.PARAM_ONE_ID
import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.*
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.HabitCreatorListener
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
import kotlinx.android.synthetic.main.frag_create_habit.view.*

class HabitCreateFrag : AppCompatDialogFragment(){

    /**
     * Contains the initialisation code for the fragment.
     */
    companion object {
        fun newInstance(habitId: String): HabitCreateFrag {
            val f = HabitCreateFrag()
            val args = Bundle()
            args.putString(PARAM_ONE_ID, habitId)
            f.arguments = args
            return f
        }
    }
    //The content view of the dialog. THE NORMAL VIEW FIELD IN FRAGMENT DOES NOT WORK HERE
    //that is caused by the fact that this is a dialogFragment, and the view is managed by the
    //dialog. I cannot set it since it is final. Ugh.
    lateinit var CV: View

    //A callback to the activity that will save the habit once it is constructed.
    lateinit var callback: HabitCreatorListener

    //ID of the habit. It can be a new habit that does not exist yet.
    private lateinit var habitId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.habitId = arguments!!.getString(PARAM_ONE_ID)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = try{
            context as HabitCreatorListener
        }catch (e: ClassCastException){
            //show a more usefull error
            throw ClassCastException(activity.toString() + " must implement HabitCreatorListener!")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity!!.layoutInflater
        val builder = AlertDialog.Builder(activity)

        val view = inflater.inflate(R.layout.frag_create_habit, null)
        val dialog = builder.setTitle("Create new Habit")
                .setView(view)
                .setPositiveButton("Save") { dialog, id ->
                    parseAndNotifyActivityOfHabit()
                }
                .setNegativeButton("Discard") { dialog, id ->
                    callback.onDone(null)
                    //The fragment is discarded automatically
                }.create()
        setupView(view)
        return dialog
    }


    fun setupView(CV: View) {
        this.CV = CV
        CV.createHabit_pos_radioButton.isChecked = true
        CV.createHabit_posNeg_radioGroup.setOnCheckedChangeListener { radioGroup, buttonId ->
            val goalTv = CV.createHabit_goalText
            if (CV.createHabit_pos_radioButton.isChecked) {
                goalTv.text = getString(R.string.createHabitFrag_goalAtLeast)
            } else {
                goalTv.text = getString(R.string.createHabitFrag_GoalLessThan)
            }
        }
    }

    private fun parseAndNotifyActivityOfHabit() {
        fun showError(error: String) = Toast.makeText(this.activity,error, Toast.LENGTH_LONG).show()

        val name = CV.createHabit_habitName.text.toString()
        if (name.isEmpty()) {
            showError("Name is empty!")
            return
        }

        val goal = try {
            CV.createHabit_goalEditText.text.toString().toInt()
        } catch (e: NumberFormatException) {
            showError("Invalid number input")
            return
        }

        val goalDays = try {
            CV.createHabit_goalTimespanEditText.text.toString().toInt()
        }catch (e: NumberFormatException){
            showError("Invalid number input!")
            return
        }

        val habit = Habit(
                id = habitId,
                name = name,
                goal = goal,
                goalDays =goalDays,
                positive = CV.createHabit_pos_radioButton.isChecked
        )
        callback.onDone(habit)
    }


}