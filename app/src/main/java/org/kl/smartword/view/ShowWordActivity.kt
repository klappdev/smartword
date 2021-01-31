package org.kl.smartword.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.R
import org.kl.smartword.db.WordDao
import org.kl.smartword.model.Word

class ShowWordActivity : AppCompatActivity() {
    private lateinit var nameWordTextView: TextView
    private lateinit var dateWordTextView: TextView
    private lateinit var transcriptionTextView: TextView
    private lateinit var translationTextView: TextView
    private lateinit var associationTextView: TextView
    private lateinit var etymologyTextView: TextView
    private lateinit var otherFormTextView: TextView
    private lateinit var antonymTextView: TextView
    private lateinit var irregularTextView: TextView
    private lateinit var nextWordButton: MaterialButton

    private val disposables = CompositeDisposable()
    private var idWord: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_word)

        initView()
        initWord()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initView() {
        this.nameWordTextView = findViewById(R.id.name_word_text_view)
        this.dateWordTextView = findViewById(R.id.date_word_text_view)
        this.transcriptionTextView = findViewById(R.id.transcription_word_text_view)
        this.translationTextView = findViewById(R.id.translation_word_text_view)
        this.associationTextView = findViewById(R.id.association_word_text_view)
        this.etymologyTextView = findViewById(R.id.etymology_word_text_view)
        this.otherFormTextView = findViewById(R.id.other_form_word_text_view)
        this.antonymTextView = findViewById(R.id.antonym_word_text_view)
        this.irregularTextView = findViewById(R.id.irregular_word_text_view)
        this.nextWordButton = findViewById(R.id.next_word_button)

        idWord = intent.getLongExtra("id_word", -1)
    }

    private fun initWord() {
        disposables.add(WordDao.getById(idWord)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableMaybeObserver<Word>() {
                override fun onComplete() {}
                override fun onError(error: Throwable) {}
                override fun onSuccess(result: Word) {
                    nameWordTextView.text = result.name
                    dateWordTextView.text = result.date
                    transcriptionTextView.text = result.transcription
                    translationTextView.text = result.translation
                    associationTextView.text = result.association
                    etymologyTextView.text = result.etymology
                    otherFormTextView.text = result.otherForm
                    antonymTextView.text = result.antonym
                    irregularTextView.text = result.irregular
                }
            }))
    }
}