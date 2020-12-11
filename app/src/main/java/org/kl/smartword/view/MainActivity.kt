package org.kl.smartword.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import io.reactivex.disposables.CompositeDisposable

import org.kl.smartword.R
import org.kl.smartword.db.DatabaseHelper
import org.kl.smartword.event.lesson.*
import org.kl.smartword.event.tab.ChangeTabListener
import org.kl.smartword.view.adapter.SectionPagerAdapter
import org.kl.smartword.view.fragment.DictionaryFragment

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var pagerAdapter: SectionPagerAdapter
    private lateinit var dictionaryFragment: DictionaryFragment
    var disposables: CompositeDisposable = CompositeDisposable()

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: Toolbar
    private lateinit var addLessonButton: FloatingActionButton

    lateinit var navigateLessonListener: NavigateLessonListener
    private lateinit var resetLessonListener: ResetLessonListener
    private lateinit var sortLessonListener: SortLessonListener
    private lateinit var deleteLessonListener: DeleteLessonListener
    private var menuItemSelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initTabs()

        this.dbHelper = DatabaseHelper(applicationContext)

        this.addLessonButton = findViewById(R.id.add_lesson_button)
    }

    override fun onDestroy() {
        super.onDestroy()

        this.dbHelper.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchMenuItem = menu?.findItem(R.id.action_search)
        searchMenuItem?.setOnActionExpandListener(SearchLessonListener(dictionaryFragment))

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> resetLessonListener()
        R.id.action_edit -> navigateLessonListener.navigateEditLesson()
        R.id.action_sort -> sortLessonListener()
        R.id.action_delete -> deleteLessonListener()
        else -> super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        supportActionBar?.setDisplayHomeAsUpEnabled(menuItemSelected)
        supportActionBar?.setDisplayShowHomeEnabled(menuItemSelected)

        menu?.findItem(R.id.action_search)?.isVisible = !menuItemSelected
        menu?.findItem(R.id.action_sort)?.isVisible = !menuItemSelected
        menu?.findItem(R.id.action_edit)?.isVisible = menuItemSelected
        menu?.findItem(R.id.action_delete)?.isVisible = menuItemSelected

        return true
    }

    fun notifyMenuItemSelected(selected: Boolean): Boolean {
        this.menuItemSelected = selected
        this.invalidateOptionsMenu()

        return true
    }

    fun initListeners(dictionaryFragment: DictionaryFragment) {
        this.dictionaryFragment = dictionaryFragment

        this.navigateLessonListener = NavigateLessonListener(dictionaryFragment)
        this.sortLessonListener = SortLessonListener(dictionaryFragment)
        this.deleteLessonListener = DeleteLessonListener(dictionaryFragment)
        this.resetLessonListener = ResetLessonListener(dictionaryFragment)

        this.addLessonButton.setOnClickListener(navigateLessonListener::navigateAddLesson)
    }

    private fun initTabs() {
        this.toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        this.tabLayout = findViewById(R.id.tabs)
        with(tabLayout) {
            addTab(newTab().setText(R.string.category_tab))
            addTab(newTab().setText(R.string.dictionary_tab))

            tabGravity = TabLayout.GRAVITY_FILL
        }

        this.pagerAdapter = SectionPagerAdapter(2, supportFragmentManager)

        this.viewPager = findViewById(R.id.page_container)
        this.tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))

        with(viewPager) {
            offscreenPageLimit = 2
            adapter = pagerAdapter

            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            addOnPageChangeListener(ChangeTabListener(pagerAdapter, this.context))
        }
    }
}
