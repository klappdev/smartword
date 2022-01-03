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
package org.kl.smartword.ui

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.Context
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
import javax.inject.Named

import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.disposables.CompositeDisposable

import org.kl.smartword.R
import org.kl.smartword.MainApplication
import org.kl.smartword.db.DatabaseHelper
import org.kl.smartword.ui.common.SectionPagerAdapter
import org.kl.smartword.event.lesson.*
import org.kl.smartword.event.settings.ChangeSettingsListener
import org.kl.smartword.event.tab.ChangeTabListener
import org.kl.smartword.settings.CommonSettings
import org.kl.smartword.settings.SettingsHelper
import org.kl.smartword.ui.lesson.CategoryFragment
import org.kl.smartword.ui.lesson.DictionaryFragment
import org.kl.smartword.ui.settings.SettingsActivity
import org.kl.smartword.work.LoadLessonService
import org.kl.smartword.work.LoadWordService

class MainActivity : AppCompatActivity() {
    @BindView(R.id.toolbar)
    public lateinit var toolbar: Toolbar
    @BindView(R.id.tabs)
    public lateinit var tabLayout: TabLayout
    @BindView(R.id.page_container)
    public lateinit var viewPager: ViewPager
    @BindView(R.id.add_lesson_button)
    public lateinit var addLessonButton: FloatingActionButton

    @Inject
    public lateinit var dbHelper: DatabaseHelper
    @Inject
    public lateinit var jobScheduler: JobScheduler
    @Inject @Named("LessonService")
    public lateinit var lessonJobInfo: JobInfo
    @Inject @Named("WordService")
    public lateinit var wordJobInfo: JobInfo
    @Inject
    public lateinit var disposable: CompositeDisposable

    public lateinit var dictionaryFragment: DictionaryFragment
    public lateinit var categoryFragment: CategoryFragment

    private lateinit var settings: CommonSettings
    private lateinit var changeSettingsListener: ChangeSettingsListener
    private var menuItemSelected = false

    override fun attachBaseContext(base: Context) {
        this.settings = CommonSettings.getInstance(base)
        SettingsHelper.setTheme(delegate, settings.isDefaultTheme)
        super.attachBaseContext(SettingsHelper.setLocale(base, settings.readLanguageSynchronously()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MainApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        ButterKnife.bind(this)

        initView()
        startServices()
    }

    override fun onStop() {
        stopServices()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
        disposable.dispose()
        settings.preferences.unregisterOnSharedPreferenceChangeListener(changeSettingsListener)
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
        android.R.id.home -> dictionaryFragment.resetLessonListener()
        R.id.action_lesson_edit -> dictionaryFragment.navigateLessonListener.navigateEditLesson()
        R.id.action_lesson_sort -> dictionaryFragment.sortLessonListener()
        R.id.action_lesson_delete -> dictionaryFragment.deleteLessonListener()
        R.id.action_settings -> { startActivity(Intent(this, SettingsActivity::class.java)); true }
        else -> super.onOptionsItemSelected(item)
    }

    public fun notifyMenuItemSelected(selected: Boolean): Boolean {
        this.menuItemSelected = selected
        invalidateOptionsMenu()
        return true
    }

    private fun startServices() {
        startService(Intent(this, LoadLessonService::class.java))
        jobScheduler.schedule(lessonJobInfo)

        startService(Intent(this, LoadWordService::class.java))
        jobScheduler.schedule(wordJobInfo)
    }

    private fun stopServices() {
        if (jobScheduler.getPendingJob(LoadLessonService.JOB_ID) != null) {
            jobScheduler.cancel(LoadLessonService.JOB_ID)
            stopService(Intent(this, LoadLessonService::class.java))
        }

        if (jobScheduler.getPendingJob(LoadWordService.JOB_ID) != null) {
            jobScheduler.cancel(LoadWordService.JOB_ID)
            stopService(Intent(this, LoadWordService::class.java))
        }
    }

    private fun initView() {
        this.changeSettingsListener = ChangeSettingsListener(this)
        settings.preferences.registerOnSharedPreferenceChangeListener(changeSettingsListener)

        setSupportActionBar(toolbar)

        with(tabLayout) {
            addTab(newTab().setText(R.string.category_tab))
            addTab(newTab().setText(R.string.dictionary_tab))

            tabGravity = TabLayout.GRAVITY_FILL
        }

        tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))

        with(viewPager) {
            offscreenPageLimit = 2
            adapter = SectionPagerAdapter(2, supportFragmentManager)

            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
            addOnPageChangeListener(ChangeTabListener(adapter as SectionPagerAdapter, context))
        }
    }
}
