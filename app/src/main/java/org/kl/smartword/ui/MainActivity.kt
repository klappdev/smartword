package org.kl.smartword.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import org.kl.smartword.R
import org.kl.smartword.event.tab.ChangeTabEvent
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
        tabLayout.addTab(tabLayout.newTab().setText(R.string.main_tab))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.dictionary_tab))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        this.pagerAdapter = SectionPagerAdapter(2, supportFragmentManager)

        this.viewPager = findViewById(R.id.page_container)
        viewPager.offscreenPageLimit = 2
        viewPager.adapter = pagerAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewPager.addOnPageChangeListener(ChangeTabEvent(pagerAdapter))
    }
}
