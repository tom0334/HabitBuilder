package habitbuilder.f.tom.makes.com.habitbuilder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val saver = SnappyHabitSaver(this)
        val habit = Habit(
                saver.generateNewHabitId(),
                "Test Habit",
                3
        )
        saver.save(habit)

        //prepare the viewpager
        val adapter = HabitsPagerAdapter(this.supportFragmentManager)
        val pager = findViewById<ViewPager>(R.id.mainPager)
        pager.adapter = adapter

        val tabLayout = findViewById<TabLayout>(R.id.mainTabLayout)
        tabLayout.setupWithViewPager(pager)




    }

}
