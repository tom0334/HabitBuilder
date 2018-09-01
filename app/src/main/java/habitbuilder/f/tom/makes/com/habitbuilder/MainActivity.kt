package habitbuilder.f.tom.makes.com.habitbuilder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager

class MainActivity : AppCompatActivity() {


    private lateinit var saver: HabitDatabase
    private lateinit var adapter:HabitsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.saver = SnappyHabitSaver(this)

        //prepare the viewpager
        this.adapter =  HabitsPagerAdapter(this.supportFragmentManager, saver.loadAll())
        val pager = findViewById<ViewPager>(R.id.mainPager)
        pager.adapter = adapter

        val tabLayout = findViewById<TabLayout>(R.id.mainTabLayout)
        tabLayout.setupWithViewPager(pager)
    }

    private fun refresh(){
        val newData = saver.loadAll()
        adapter.data = newData
        adapter.notifyDataSetChanged()
    }

    private fun addHabit(){
        val habit = Habit(
                saver.generateNewHabitId(),
                "Test Habit",
                3
        )
        saver.save(habit)
    }

}
