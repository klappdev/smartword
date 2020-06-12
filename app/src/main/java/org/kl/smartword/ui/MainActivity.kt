package org.kl.smartword.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout

import org.kl.smartword.R
import org.kl.smartword.db.DatabaseHelper
import org.kl.smartword.db.LessonDB
import org.kl.smartword.db.WordDB
import org.kl.smartword.event.tab.ChangeTabListener
import org.kl.smartword.ui.adapter.SectionPagerAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var pagerAdapter: SectionPagerAdapter
    private lateinit var dbHelper: DatabaseHelper

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: Toolbar
    private lateinit var addLessonButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        this.tabLayout = findViewById(R.id.tabs)
        with(tabLayout) {
            addTab(newTab().setText(R.string.main_tab))
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

        this.dbHelper = DatabaseHelper(applicationContext)

        this.addLessonButton = findViewById(R.id.add_lesson_button)
        this.addLessonButton.setOnClickListener(::clickAddLesson)
    }

    private fun clickAddLesson(view: View?) {
        val intent = Intent(this, AddLessonActivity::class.java)
        this.startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        this.dbHelper.close()
    }
}
