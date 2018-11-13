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
import habitbuilder.f.tom.makes.com.habitbuilder.R.id.*
import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.fragments.EditHabitFrag

import habitbuilder.f.tom.makes.com.habitbuilder.androidSpecfic.implementations.TimeUtilsJvm
import habitbuilder.f.tom.makes.com.habitbuilder.common.HabitDatabaseInteractor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


/**
 * The main activity that houses a Viewpager with a habit on every page.
 *
 * It reads the database to find the names of the habits to display them in the Tab names.
 */
class MainActivity : AppCompatActivity(), HabitDatabaseInteractor {

    private lateinit var saver: HabitDatabase
    private lateinit var adapter: HabitsPagerAdapter

    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>


    /**
     * Saves CHANGES to a habit, does NOT save a new habit!
     *
     * @param changedHabit the habit that was changed.
     * @param nameChanged if the name of the habit was changed. This is determines wheter the
     * tabs at the top need to be refreshed. This isn't always refreshed for performance reasons.
     */
    override fun saveChangesToHabit(changedHabit: Habit, nameChanged: Boolean) {
        launch {
            //this is executed on a background thread
            saver.save(changedHabit)
        }
        if (nameChanged){
            adapter.notifyDataSetChanged()
        }

    }

    /**
     * Saves a new habit to the database, and then refreshes the adapter. Called when the
     * NewHabitFragment is closed.
     * @param newHabit a new habit that needs to be saved to the database.
     */
    override fun saveNewHabit(newHabit: Habit) {
        //do on UI thread:
        launch(UI) {
            launch {
                //this is executed on a background thread
                saver.save(newHabit)
            }
            //back on UI thread
            adapter.addNewHabitAndUpdate(newHabit)
        }
    }

    /**
     * Returns a habit from the database, called from the fragments. This function avoids having
     * the fragment load the habit from the database again. (this activity needs access to the
     * fragments as wel, to figure out the titles for the tabs).
     */
    override fun getHabit(id: String): Habit {
        return saver.load(id)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.mainToolbar)
        setSupportActionBar(toolbar)


        this.saver = SnappyHabitSaver(this)

        //prepare the viewpager
        this.adapter = HabitsPagerAdapter(this.supportFragmentManager, saver.loadAll().toMutableList())
        main_viewPager.adapter = adapter
        main_viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //todo add a fancy (fade?) animation to the text.
            }

            override fun onPageSelected(position: Int) {
                updateSheet(position)
            }

        })

        val tabLayout = findViewById<TabLayout>(R.id.mainTabLayout)
        tabLayout.setupWithViewPager(main_viewPager)

        this.sheetBehavior = BottomSheetBehavior.from(bottom_sheet)
        setupSheet()
        updateSheet(main_viewPager.currentItem)
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

        bottom_sheet_month_view.setup()

        //Setup the button that expands the sheet.
        with(sheetBehavior) {
            main_toggle_sheet_button.setOnClickListener {
                when (state) {
                    STATE_COLLAPSED -> state = STATE_EXPANDED
                    STATE_EXPANDED -> state = STATE_COLLAPSED
                }
            }
        }

    }

    /** This updates the bottom sheet when the page is scrolled.
     * @param the new page.
     */
    private fun updateSheet(currentPage: Int) {
        val timeUtils = TimeUtilsJvm()
        //setup the text in the peek area
        val habit = adapter.getHabitForPosition(currentPage)

        if (habit == null) {
            return
        }

        val scoreThisWeek = habit.avgScoreThisWeek(System.currentTimeMillis(), timeUtils)
        val scoreThisMonth = habit.avgScoreThisMonth(System.currentTimeMillis(), timeUtils)
        val scoreAllTime = habit.avgScoreAllTime(System.currentTimeMillis())

        bottom_sheet_peek_week_tv.text = getString(R.string.bottom_sheet_peek_onAvgThisWeek, scoreThisWeek)
    }

    /**
     * Creates the options menu (three dots in the actionbar) from xml.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    /**
     * Called when a button in the options menu is clicked.
     */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_main_create_habit -> onCreateHabitClicked()
            else -> throw IllegalArgumentException("MainActivity: Unknown menu item clicked")
        }
        return true //consume the event
    }

    /**
     * Called when the create habbit button is clicked. The fragment is a dialog where the user can
     * create a new habit. If it is successful, the SaveHabit function will be called, which will save
     * and update the UI.
     */
    private fun onCreateHabitClicked() {
        val frag = EditHabitFrag.newInstance(saver.generateNewHabitId())
        frag.show(supportFragmentManager, "CREATE_HABIT_TAG")
    }

    override fun onDestroy() {
        super.onDestroy()
        saver.close()
    }

}
