package habitbuilder.f.tom.makes.com.habitbuilder

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

val NUM_FRAGS= 10

class HabitsPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        assert(position in 0..NUM_FRAGS)
        return HabitFrag.newInstance(position)
    }

    override fun getCount(): Int {
        return NUM_FRAGS
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Habit $position"
    }
}