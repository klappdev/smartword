/*
 * Licensed under the MIT License <http://opensource.org/licenses/MIT>.
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2019 - 2024 https://github.com/klappdev
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
package org.kl.smartword.ui.lesson

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

import butterknife.BindView
import butterknife.ButterKnife

import org.kl.smartword.R
import org.kl.smartword.MainApplication
import org.kl.smartword.db.WordDao
import org.kl.smartword.event.word.*
import org.kl.smartword.model.Word
import org.kl.smartword.ui.lesson.LessonAdapter

class ShowLessonActivity : AppCompatActivity() {
    @BindView(R.id.word_empty_text_view)
    public lateinit var emptyTextView: TextView
    @BindView(R.id.word_list_view)
    public lateinit var wordsListView: ListView
    @BindView(R.id.add_word_button)
    public lateinit var addWordButton: FloatingActionButton

    @Inject
    public lateinit var wordDao: WordDao
    @Inject
    public lateinit var disposables: CompositeDisposable
    public lateinit var lessonAdapter: LessonAdapter

    private lateinit var navigateWordListener: NavigateWordListener
    private lateinit var resetWordListener: ResetWordListener
    private lateinit var sortWordListener: SortWordListener
    private lateinit var deleteWordListener: DeleteWordListener

    private var menuItemSelected: Boolean = false
    private var idLesson: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MainApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words)
        ButterKnife.bind(this)

        initView()
        initListeners()
        initWords()
    }

    override fun onResume() {
        super.onResume()
        initWords()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_word, menu)

        val searchMenuItem = menu?.findItem(R.id.action_word_search)
        searchMenuItem?.setOnActionExpandListener(SearchWordListener(this, idLesson))

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        supportActionBar?.setDisplayHomeAsUpEnabled(menuItemSelected)
        supportActionBar?.setDisplayShowHomeEnabled(menuItemSelected)

        menu?.findItem(R.id.action_word_search)?.isVisible = !menuItemSelected
        menu?.findItem(R.id.action_word_sort)?.isVisible = !menuItemSelected
        menu?.findItem(R.id.action_word_edit)?.isVisible = menuItemSelected
        menu?.findItem(R.id.action_word_delete)?.isVisible = menuItemSelected

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> resetWordListener()
        R.id.action_word_edit -> navigateWordListener.navigateEditWord()
        R.id.action_word_sort -> sortWordListener()
        R.id.action_word_delete -> deleteWordListener()
        else -> super.onOptionsItemSelected(item)
    }

    fun notifyMenuItemSelected(selected: Boolean): Boolean {
        this.menuItemSelected = selected
        invalidateOptionsMenu()

        return true
    }

    private fun initListeners() {
        this.resetWordListener = ResetWordListener(this)
        this.sortWordListener = SortWordListener(this, idLesson)
        this.deleteWordListener = DeleteWordListener(this)

        addWordButton.setOnClickListener(navigateWordListener::navigateAddWord)
    }

    private fun initView() {
        idLesson = intent.getLongExtra("id_lesson", -1)

        this.lessonAdapter = LessonAdapter(this, mutableListOf())
        this.navigateWordListener = NavigateWordListener(lessonAdapter, idLesson)

        with(wordsListView) {
            choiceMode = ListView.CHOICE_MODE_SINGLE
            emptyView = emptyTextView
            adapter = lessonAdapter
            onItemLongClickListener = ChooseWordListener(::notifyMenuItemSelected)
            onItemClickListener = AdapterView.OnItemClickListener(navigateWordListener::navigateShowWord)
        }
    }

    private fun initWords() {
        disposables.add(wordDao.getAllByIdLesson(idLesson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result: List<Word> ->
                lessonAdapter.position = -1
                lessonAdapter.listWords.clear()
                lessonAdapter.listWords.addAll(result)
                lessonAdapter.notifyDataSetChanged()
            })
    }
}
