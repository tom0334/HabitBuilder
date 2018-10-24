package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.fragments


import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import habitbuilder.f.tom.makes.com.habitbuilder.R
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.PARAM_ONE_ID
import android.content.Context
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
import android.widget.Toast
import habitbuilder.f.tom.makes.com.habitbuilder.common.HabitDatabaseInteractor
import kotlinx.android.synthetic.main.frag_create_habit.*


/**
 * This is a dialog that lets the user create or edit a habit's properties.
 * To create one, call the newInstance function with an already existing or new ID.
 *
 * The activity that houses this fragment needs to implement the HabitCreatorListener interface
 * to save the newly created habit to the database.
 */
class EditHabitFrag : AppCompatDialogFragment(){


    companion object {
        /**
         * Contains the initialisation code for the fragment.
         * @param habitId a random string, either corresponding to a habit that already exists (
         * to edit that habit) or a new one to create a new habit
         */
        fun newInstance(habitId: String): EditHabitFrag {
            val f = EditHabitFrag()
            val args = Bundle()
            args.putString(PARAM_ONE_ID, habitId)
            f.arguments = args
            return f
        }
    }


    //A callback to the activity that will save the habit once it is constructed.
    lateinit var callback: HabitDatabaseInteractor

    //ID of the habit. It can be a new habit that does not exist yet.
    private lateinit var habitId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.habitId = arguments!!.getString(PARAM_ONE_ID)
    }

    /**
     * Initizizes the callback for when the user is done creating a habit.
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = try{
            context as HabitDatabaseInteractor
        }catch (e: ClassCastException){
            //show a more usefull error
            throw ClassCastException(activity.toString() + " must implement HabitDatabaseInteractor!")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_create_habit, container)
    }

    /**
     * Initializes the view of this fragment, and sets up the click listeners for the buttons
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createHabit_button_save.setOnClickListener {
            val hab = parseHabit()
            if(hab!=null){
                callback.saveHabit(hab)
                dialog.dismiss()
            }
            //else the parseHabit function will show a toast indicating what the user needs
            //to do to create a valid habit.
        }

        createHabit_button_discard.setOnClickListener {
            dialog.dismiss()
        }


        //check the positive habit by default
        createHabit_pos_radioButton.isChecked = true

        createHabit_posNeg_radioGroup.setOnCheckedChangeListener { _ , _ ->
            val goalTv = createHabit_goalText
            if (createHabit_pos_radioButton.isChecked) {
                goalTv.text = getString(R.string.createHabitFrag_goalAtLeast)
            } else {
                goalTv.text = getString(R.string.createHabitFrag_GoalLessThan)
            }
        }
    }



    /**
     * Parses the views of this dialog to create a new habit. It can return null if not all
     * fields are filled correctly, for example if the user did not enter any name.
     */
    private fun parseHabit():Habit? {
        fun showError(error: String){
            Toast.makeText(this.context, error, Toast.LENGTH_SHORT).show()
        }
        /**
         * This function parses the text of a editText for an Int.
         * If the text is empty, it parses the hint.
         */
        @Throws(NumberFormatException::class)
        fun EditText.parseTextOrHint(): Int{
            return if (this.text.isEmpty()){
                this.hint.toString().toInt()
            }else{
                this.text.toString().toInt()
            }
        }

        val name = createHabit_habitName.text.toString()
        if (name.isEmpty()) {
            showError(getString(R.string.createHabitFrag_toast_error_name))
            return null
        }

        val goal = try {
            createHabit_goalEditText.parseTextOrHint()
        } catch (e: NumberFormatException) {
            showError(getString(R.string.createHabitFrag_toast_error_goal))
            return null
        }

        val goalDays = try {
            createHabit_goalTimespanEditText.parseTextOrHint()
        }catch (e: NumberFormatException){
            showError(getString(R.string.createHabitFrag_toast_error_goalDays))
            return null
        }

        return Habit(
                id = habitId,
                name = name,
                goal = goal,
                goalDays =goalDays,
                positive = createHabit_pos_radioButton.isChecked
        )
    }


}