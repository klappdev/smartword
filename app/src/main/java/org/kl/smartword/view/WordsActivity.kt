package org.kl.smartword.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.R
import org.kl.smartword.db.WordDao
import org.kl.smartword.event.word.*
import org.kl.smartword.model.Word
import org.kl.smartword.view.adapter.WordsAdapter

class WordsActivity : AppCompatActivity() {
    private lateinit var emptyTextView: TextView
    private lateinit var wordsListView: ListView
    private lateinit var wordsAdapter: WordsAdapter
    private val disposables = CompositeDisposable()

    private lateinit var addWordButton: FloatingActionButton
    private lateinit var navigateWordListener: NavigateWordListener
    private lateinit var resetWordListener: ResetWordListener
    private lateinit var sortWordListener: SortWordListener
    private lateinit var deleteWordListener: DeleteWordListener
    private var menuItemSelected: Boolean = false
    private var idLesson: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words)

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
        searchMenuItem?.setOnActionExpandListener(SearchWordListener(this, wordsAdapter, disposables))

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
        this.resetWordListener = ResetWordListener(this, wordsAdapter)
        this.sortWordListener = SortWordListener(wordsAdapter, disposables)
        this.deleteWordListener = DeleteWordListener(wordsAdapter, disposables)

        addWordButton.setOnClickListener(navigateWordListener::navigateAddWord)
    }

    private fun initView() {
        this.emptyTextView = findViewById(R.id.word_empty_text_view)
        this.wordsListView = findViewById(R.id.word_list_view)
        this.addWordButton = findViewById(R.id.add_word_button)

        idLesson = intent.getLongExtra("id_lesson", -1)

        this.wordsAdapter = WordsAdapter(this, mutableListOf())
        this.navigateWordListener = NavigateWordListener(wordsAdapter, idLesson)

        with(wordsListView) {
            choiceMode = ListView.CHOICE_MODE_SINGLE
            emptyView = emptyTextView
            adapter = wordsAdapter
            onItemLongClickListener = ChooseWordListener(::notifyMenuItemSelected)
            onItemClickListener = AdapterView.OnItemClickListener(navigateWordListener::navigateShowWord)
        }
    }

    private fun initWords() {
        disposables.add(WordDao.getAllByIdLesson(idLesson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Word>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Word>) {
                    wordsAdapter.position = -1
                    wordsAdapter.listWords.clear()
                    wordsAdapter.listWords.addAll(result)
                    wordsAdapter.notifyDataSetChanged()
                }
            }))
    }
}
