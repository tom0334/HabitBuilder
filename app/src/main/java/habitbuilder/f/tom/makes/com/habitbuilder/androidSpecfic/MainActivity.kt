package habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import habitbuilder.f.tom.makes.com.habitbuilder.*
import habitbuilder.f.tom.makes.com.habitbuilder.common.Habit
import habitbuilder.f.tom.makes.com.habitbuilder.common.HabitDatabase
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.adapters.HabitsPagerAdapter
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.SnappyHabitSaver
import kotlinx.android.synthetic.main.layout_main_bottom_sheet.*
import android.view.View

import android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED
import android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED
import android.widget.LinearLayout
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.TimeUtilsJvm
import kotlinx.android.synthetic.main.activity_main.*


/**
 * The main activity that houses a Viewpager with a habit on every page.
 *
 * It reads the database to find the names of the habits to display them in the Tab names.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var saver: HabitDatabase
    private lateinit var adapter: HabitsPagerAdapter

    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.mainToolbar)
        setSupportActionBar(toolbar)

        this.saver = SnappyHabitSaver(this)

        //prepare the viewpager
        this.adapter = HabitsPagerAdapter(this.supportFragmentManager, saver.loadAll())
        val pager = findViewById<ViewPager>(R.id.mainPager)
        pager.adapter = adapter

        val tabLayout = findViewById<TabLayout>(R.id.mainTabLayout)
        tabLayout.setupWithViewPager(pager)

        this.sheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        setupSheet()
    }

    /**
     * This function sets up the bottom sheet and the views inside it.
     */
    private fun setupSheet() {
        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //this rotates the icon to show it upside down when the panel is expanded
                //also looks very pretty
                main_toggle_sheet_button.rotation = slideOffset * 180
                main_content.transitionBackGroundColor(slideOffset, R.color.colorNormal, R.color.colorFaded)
            }
        })

        //Setup the button that expands the sheet.
        with(sheetBehavior) {
            main_toggle_sheet_button.setOnClickListener {
                when (state) {
                    STATE_COLLAPSED -> state = STATE_EXPANDED
                    STATE_EXPANDED -> state = STATE_COLLAPSED
                }
            }
        }


        val timeUtils = TimeUtilsJvm()
        //setup the text in the peek area
        val habit = adapter.getHabitForPosition(mainPager.currentItem)

        val scoreThisWeek = habit.avgScoreThisWeek(System.currentTimeMillis(),timeUtils)
        val scoreThisMonth = habit.avgScoreThisMonth(System.currentTimeMillis(),timeUtils)
        val scoreAllTime = habit.avgScoreAllTime(System.currentTimeMillis())

        bottom_sheet_peek_week_tv.text = getString(R.string.bottom_sheet_peek_onAvgThisWeek, scoreThisWeek)
    }

    

    /**
     * Refresh the data from the database. This can be needed when a new habit is created,
     * or when the name of a habit changes.
     */
    private fun refresh(){
        val newData = saver.loadAll()
        adapter.data = newData
        adapter.notifyDataSetChanged()
    }

    /**
     * Called when the create habbit button is clicked.  This adds a new habit and refreshes.
     * todo: make a nice UI for this
     */
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
