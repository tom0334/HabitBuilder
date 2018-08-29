package habitbuilder.f.tom.makes.com.habitbuilder


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

private val PARAM_ONE_ID = "PARAM_1"

/**
 * A [Fragment] subclass that will show a single habit.
 *
 */
class HabitFrag : Fragment() {

    //Primitives cannot be lateinit
    private var indexInViewPager: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = this.arguments
        this.indexInViewPager = arguments?.get(PARAM_ONE_ID) as Int?  ?: throw IllegalArgumentException("Could not get argument!")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_habit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tv = view.findViewById<TextView>(R.id.habitFragText)
        tv.text= "Hello from Fragment ${this.indexInViewPager}"
    }


    companion object {
        fun newInstance(indexInViewPager: Int): HabitFrag {
            val f = HabitFrag()
            val args = Bundle()
            args.putInt(PARAM_ONE_ID, indexInViewPager)
            f.arguments = args
            return f
        }
    }




}
