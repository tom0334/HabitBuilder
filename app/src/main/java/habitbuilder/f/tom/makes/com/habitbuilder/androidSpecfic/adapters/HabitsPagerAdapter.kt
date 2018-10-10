package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.HabitFrag


class HabitsPagerAdapter(fm: FragmentManager, var data: List<Habit>): FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        assert(position in data.indices)
        return HabitFrag.newInstance(position, data[position].id)
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return data[position].name
    }

    /**
     * Returns the habit belonging to the given page
     */
    fun getHabitForPosition(pageNumber: Int): Habit {
        return data[pageNumber]
    }
}