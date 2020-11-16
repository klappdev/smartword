package org.kl.smartword.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.R
import org.kl.smartword.db.WordDao
import org.kl.smartword.model.Word
import org.kl.smartword.event.word.EditWordListener

class EditWordActivity : AppCompatActivity() {
    private lateinit var editButton: Button
    lateinit var nameTextView: TextView
        private set
    lateinit var transcriptionTextView: TextView
        private set
    lateinit var translationTextView: TextView
        private set
    lateinit var associationTextView: TextView
        private set
    lateinit var etymologyTextView: TextView
        private set
    lateinit var otherFormTextView: TextView
        private set
    lateinit var antonymTextView: TextView
        private set
    lateinit var irregularTextView: TextView
        private set
    var disposables: CompositeDisposable = CompositeDisposable()
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_word)

        this.nameTextView = findViewById(R.id.name_word_text_view)
        this.transcriptionTextView = findViewById(R.id.transcription_word_text_view)
        this.translationTextView = findViewById(R.id.translation_word_text_view)
        this.associationTextView = findViewById(R.id.association_word_text_view)
        this.etymologyTextView = findViewById(R.id.etymology_word_text_view)
        this.otherFormTextView = findViewById(R.id.other_form_word_text_view)
        this.antonymTextView = findViewById(R.id.antonym_word_text_view)
        this.irregularTextView = findViewById(R.id.irregular_word_text_view)
        this.editButton = findViewById(R.id.edit_word_button)

        val idWord = intent.getLongExtra("id_word", -1)

        disposables.add(WordDao.getById(idWord)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableMaybeObserver<Word>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onSuccess(result: Word) {
                    nameTextView.text = result.name
                    transcriptionTextView.text = result.transcription
                    translationTextView.text = result.translation
                    associationTextView.text = result.association
                    etymologyTextView.text = result.etymology
                    otherFormTextView.text = result.otherForm
                    antonymTextView.text = result.antonym
                    irregularTextView.text = result.irregular

                    val listener = EditWordListener(this@EditWordActivity, result.id, result.idLesson)
                    editButton.setOnClickListener(listener)
                }
            }))
    }
}
