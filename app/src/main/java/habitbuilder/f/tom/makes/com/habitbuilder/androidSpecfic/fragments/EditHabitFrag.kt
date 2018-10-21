package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.fragments

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import habitbuilder.f.tom.makes.com.habitbuilder.R
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.PARAM_ONE_ID
import android.app.AlertDialog
import android.content.Context

import android.view.View
import android.widget.EditText
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.HabitCreatorListener
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
import kotlinx.android.synthetic.main.frag_create_habit.view.*
import android.widget.Toast


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

    /**
     * Initizizes the callback for when the user is done creating a habit.
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callback = try{
            context as HabitCreatorListener
        }catch (e: ClassCastException){
            //show a more usefull error
            throw ClassCastException(activity.toString() + " must implement HabitCreatorListener!")
        }
    }


    /**
     * This function is used instead of the normal onCreateView(), because it adds a title and
     * the two save and discard buttons in the material style easily.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity!!.layoutInflater
        val builder = AlertDialog.Builder(activity)

        val view = inflater.inflate(R.layout.frag_create_habit, null)
        val dialog = builder.setTitle("Create new Habit")
                .setView(view)
                .setPositiveButton("Save",null)
                .setNegativeButton("Discard") { dialog, id ->
                    callback.onDone(null)
                    //The fragment is discarded automatically
                }.create()

        setupView(view)

        //Overide the normal onclick of the positive button to only dismiss if the user has
        //typed in all fields.
        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val hab = parseHabit()
                if(hab!=null){
                    callback.onDone(hab)
                    dialog.dismiss()
                }
                //else the parseHabit function will show a toast indicating what the user needs
                //to do to create a valid habit.
            }
        }
        return dialog
    }


    /**
     * This initializes the contentView of the dialog, and sets the CV field in this class to
     * refer to that contentView.
     *
     * It changes the text of the goal explanation corresponding to if the habit is positive
     */
    private fun setupView(CV: View) {
        this.CV = CV

        //check the positive habit by default
        CV.createHabit_pos_radioButton.isChecked = true

        CV.createHabit_posNeg_radioGroup.setOnCheckedChangeListener { _ , _ ->
            val goalTv = CV.createHabit_goalText
            if (CV.createHabit_pos_radioButton.isChecked) {
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

        val name = CV.createHabit_habitName.text.toString()
        if (name.isEmpty()) {
            showError("Name is empty!")
            return null
        }

        val goal = try {
            CV.createHabit_goalEditText.parseTextOrHint()
        } catch (e: NumberFormatException) {
            showError("Invalid number input")
            return null
        }

        val goalDays = try {
            CV.createHabit_goalTimespanEditText.parseTextOrHint()
        }catch (e: NumberFormatException){
            showError("Invalid number input!")
            return null
        }

        return Habit(
                id = habitId,
                name = name,
                goal = goal,
                goalDays =goalDays,
                positive = CV.createHabit_pos_radioButton.isChecked
        )
    }


}