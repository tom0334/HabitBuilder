package habitbuilder.f.tom.makes.com.habitbuilder


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

private val PARAM_ONE_ID = "PARAM_1"
private val PARAM_TWO_ID = "PARAM_2"
/**
 * A [Fragment] subclass that will show a single habit.
 *
 */
class HabitFrag : Fragment() {

    //Primitives cannot be lateinit
    private var indexInViewPager: Int = -1
    private var  habitId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.indexInViewPager = arguments?.get(PARAM_ONE_ID) as Int?  ?: throw IllegalArgumentException("Could not get argument one!")
        this.habitId = arguments?.get(PARAM_TWO_ID) as String? ?: throw IllegalArgumentException("Could not get argument two!")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_habit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tv = view.findViewById<TextView>(R.id.habitFragText)
        tv.text= "Hello from Fragment ${this.indexInViewPager}. The habitId is $habitId"
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
