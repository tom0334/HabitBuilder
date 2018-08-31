package habitbuilder.f.tom.makes.com.habitbuilder

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter


class HabitsPagerAdapter(fm: FragmentManager, private val habits: List<Habit>): FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        assert(position in habits.indices)
        return HabitFrag.newInstance(position, habits[position].id)
    }

    override fun getCount(): Int {
        return habits.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Habit $position"
    }
}