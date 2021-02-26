/*
 * Licensed under the MIT License <http://opensource.org/licenses/MIT>.
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2019 - 2021 https://github.com/klappdev
 *
 * Permission is hereby  granted, free of charge, to any  person obtaining a copy
 * of this software and associated  documentation files (the "Software"), to deal
 * in the Software  without restriction, including without  limitation the rights
 * to  use, copy,  modify, merge,  publish, distribute,  sublicense, and/or  sell
 * copies  of  the Software,  and  to  permit persons  to  whom  the Software  is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE  IS PROVIDED "AS  IS", WITHOUT WARRANTY  OF ANY KIND,  EXPRESS OR
 * IMPLIED,  INCLUDING BUT  NOT  LIMITED TO  THE  WARRANTIES OF  MERCHANTABILITY,
 * FITNESS FOR  A PARTICULAR PURPOSE AND  NONINFRINGEMENT. IN NO EVENT  SHALL THE
 * AUTHORS  OR COPYRIGHT  HOLDERS  BE  LIABLE FOR  ANY  CLAIM,  DAMAGES OR  OTHER
 * LIABILITY, WHETHER IN AN ACTION OF  CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE  OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.kl.smartword.view.activity

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import javax.inject.Inject

import org.kl.smartword.R
import org.kl.smartword.WordApplication
import org.kl.smartword.db.DatabaseHelper
import org.kl.smartword.view.adapter.SectionPagerAdapter
import org.kl.smartword.event.tab.ChangeTabListener
import org.kl.smartword.view.fragment.*
import org.kl.smartword.event.lesson.*
import org.kl.smartword.work.LoadDictionaryService

class MainActivity : AppCompatActivity() {
    @Inject
    public lateinit var dbHelper: DatabaseHelper

    @Inject
    public lateinit var jobScheduler: JobScheduler

    @Inject
    public lateinit var jobInfo: JobInfo

    private lateinit var dictionaryFragment: DictionaryFragment
    private lateinit var categoryFragment: CategoryFragment

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: Toolbar
    private lateinit var addLessonButton: FloatingActionButton

    public  lateinit var navigateLessonListener: NavigateLessonListener
    private lateinit var resetLessonListener: ResetLessonListener
    private lateinit var sortLessonListener: SortLessonListener
    private lateinit var deleteLessonListener: DeleteLessonListener
    private var menuItemSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as WordApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        startService()
    }

    override fun onStop() {
        stopService()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchMenuItem = menu?.findItem(R.id.action_lesson_search)
        searchMenuItem?.setOnActionExpandListener(SearchLessonListener(dictionaryFragment))

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        supportActionBar?.setDisplayHomeAsUpEnabled(menuItemSelected)
        supportActionBar?.setDisplayShowHomeEnabled(menuItemSelected)

        menu?.findItem(R.id.action_lesson_search)?.isVisible = !menuItemSelected
        menu?.findItem(R.id.action_lesson_sort)?.isVisible = !menuItemSelected
        menu?.findItem(R.id.action_lesson_edit)?.isVisible = menuItemSelected
        menu?.findItem(R.id.action_lesson_delete)?.isVisible = menuItemSelected

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> resetLessonListener()
        R.id.action_lesson_edit -> navigateLessonListener.navigateEditLesson()
        R.id.action_lesson_sort -> sortLessonListener()
        R.id.action_lesson_delete -> deleteLessonListener()
        else -> super.onOptionsItemSelected(item)
    }

    fun notifyMenuItemSelected(selected: Boolean): Boolean {
        this.menuItemSelected = selected
        invalidateOptionsMenu()

        return true
    }

    fun initCategoryListeners(fragment: CategoryFragment) {
        this.categoryFragment = fragment

        /*TODO: init listeners*/
    }

    fun initDictionaryListeners(fragment: DictionaryFragment) {
        this.dictionaryFragment = fragment

        this.navigateLessonListener = NavigateLessonListener(fragment)
        this.sortLessonListener = SortLessonListener(fragment)
        this.deleteLessonListener = DeleteLessonListener(fragment)
        this.resetLessonListener = ResetLessonListener(fragment)

        addLessonButton.setOnClickListener(navigateLessonListener::navigateAddLesson)
    }

    private fun startService() {
        startService(Intent(this, LoadDictionaryService::class.java))
        jobScheduler.schedule(jobInfo)
    }

    private fun stopService() {
        if (jobScheduler.getPendingJob(LoadDictionaryService.JOB_ID) != null) {
            jobScheduler.cancel(LoadDictionaryService.JOB_ID)
            stopService(Intent(this, LoadDictionaryService::class.java))
        }
    }

    private fun initView() {
        this.toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        this.tabLayout = findViewById(R.id.tabs)
        with(tabLayout) {
            addTab(newTab().setText(R.string.category_tab))
            addTab(newTab().setText(R.string.dictionary_tab))

            tabGravity = TabLayout.GRAVITY_FILL
        }

        this.viewPager = findViewById(R.id.page_container)
        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))

        with(viewPager) {
            offscreenPageLimit = 2
            adapter = SectionPagerAdapter(2, supportFragmentManager)

            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            addOnPageChangeListener(ChangeTabListener(adapter as SectionPagerAdapter, context))
        }

        this.addLessonButton = findViewById(R.id.add_lesson_button)
    }
}
