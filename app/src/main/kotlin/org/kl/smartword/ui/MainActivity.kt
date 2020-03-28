package org.kl.smartword.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import org.kl.smartword.R
import org.kl.smartword.db.LessonDB
import org.kl.smartword.ui.adapter.SectionPagerAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var pagerAdapter: SectionPagerAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: Toolbar

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
            /*addOnPageChangeListener(ChangeTabEvent(pagerAdapter))*/
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        LessonDB.getInstance(applicationContext).close()
    }
}
