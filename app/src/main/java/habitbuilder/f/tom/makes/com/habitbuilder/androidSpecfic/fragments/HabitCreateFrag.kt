package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.fragments

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import habitbuilder.f.tom.makes.com.habitbuilder.R
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.PARAM_ONE_ID
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.PARAM_TWO_ID
import android.app.AlertDialog
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.frag_create_habit.view.*


class HabitCreateFrag : AppCompatDialogFragment() {

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

    lateinit var contentView: View

    private val goalText by lazy { contentView.findViewById<TextView>(R.id.createHabit_goalText) }
    private val posRad by lazy { contentView.findViewById<RadioButton>(R.id.createHabit_pos_radioButton) }
    private val group by lazy { contentView.findViewById<RadioGroup>(R.id.createHabit_posNeg_radioGroup) }

    val nameED by lazy { contentView.createHabit_habitName }
    val goalED by lazy { contentView.createHabit_goalEditText }


    private lateinit var habitId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.habitId = arguments!!.getString(PARAM_ONE_ID)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity!!.layoutInflater
        val builder = AlertDialog.Builder(activity)

        val view = inflater.inflate(R.layout.frag_create_habit, null)
        val dialog = builder.setTitle("Create new Habit")
                .setView(view)
                .setPositiveButton("Save") { dialog, id ->
                    saveHabit()
                }
                .setNegativeButton("Discard") { dialog, id ->
                    //The fragment is discarded automatically
                }.create()

        this.contentView = view
        return dialog
    }


    fun setupView() {
        posRad.isChecked = true
        group.setOnCheckedChangeListener { radioGroup, buttonId ->
            if (posRad.isChecked) {
                goalText.text = getString(R.string.createHabitFrag_goalAtLeast)
            } else {
                goalText.text = getString(R.string.createHabitFrag_GoalLessThan)
            }
        }
    }

    private fun saveHabit() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}