package habitbuilder.f.tom.makes.com.habitbuilder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.R.menu
import android.view.MenuInflater
import android.view.MenuItem


class MainActivity : AppCompatActivity() {


    private lateinit var saver: HabitDatabase
    private lateinit var adapter:HabitsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.mainToolbar)
        setSupportActionBar(toolbar)

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

    private fun onCreateHabitClicked(){
        val habit = Habit(
                saver.generateNewHabitId(),
                "Test Habit",
                3
        )
        saver.save(habit)
        refresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_main_create_habit -> onCreateHabitClicked()
            else ->throw IllegalArgumentException("MainActivity: Unknown menu item clicked")
        }
        return true //consume the event
    }

    override fun onDestroy() {
        super.onDestroy()
        saver.close()
    }

}
