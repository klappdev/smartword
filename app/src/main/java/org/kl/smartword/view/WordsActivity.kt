package org.kl.smartword.view

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import org.kl.smartword.model.Word
import org.kl.smartword.view.adapter.WordsAdapter
import org.kl.smartword.event.word.ManageWordListener

class WordsActivity : AppCompatActivity() {
    private lateinit var emptyTextView: TextView
    private lateinit var wordsListView: ListView
    private lateinit var wordsAdapter: WordsAdapter
    private lateinit var addWordButton: FloatingActionButton
    private var disposables: CompositeDisposable = CompositeDisposable()
    private var idLesson: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words)

        this.emptyTextView = findViewById(R.id.word_empty_text_view)
        this.wordsListView = findViewById(R.id.word_list_view)

        with(wordsListView) {
            choiceMode = ListView.CHOICE_MODE_SINGLE
            emptyView = emptyTextView
            onItemLongClickListener = ManageWordListener()
            /*onItemClickListener = AdapterView.OnItemClickListener(::clickShowWord)*/
        }

        this.addWordButton = findViewById(R.id.add_word_button)
        this.addWordButton.setOnClickListener(::clickAddWord)

        this.idLesson = intent.getLongExtra("id_lesson", -1)

        disposables.add(WordDao.getAllByIdLesson(idLesson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Word>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Word>) {
                    wordsAdapter = WordsAdapter(this@WordsActivity.baseContext, result)
                    wordsListView.adapter = wordsAdapter
                }
            }))
    }

    override fun onResume() {
        super.onResume()

        disposables.add(WordDao.getAllByIdLesson(idLesson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Word>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Word>) {
                    wordsAdapter.listWords = result
                    wordsAdapter.notifyDataSetChanged()
                }
            }))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun clickAddWord(view: View?) {
        val intent = Intent(this, AddWordActivity::class.java)
        intent.putExtra("id_lesson", idLesson)

        this.startActivity(intent)
    }
}
