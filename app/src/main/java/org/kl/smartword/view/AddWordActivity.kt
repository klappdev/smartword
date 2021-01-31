package org.kl.smartword.view

import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable

import org.kl.smartword.R
import org.kl.smartword.event.word.AddWordListener

class AddWordActivity : AppCompatActivity() {
    private lateinit var addButton: Button
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
    val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)

        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun initView() {
        this.nameTextView = findViewById(R.id.name_word_text_view)
        this.transcriptionTextView = findViewById(R.id.transcription_word_text_view)
        this.translationTextView = findViewById(R.id.translation_word_text_view)
        this.associationTextView = findViewById(R.id.association_word_text_view)
        this.etymologyTextView = findViewById(R.id.etymology_word_text_view)
        this.otherFormTextView = findViewById(R.id.other_form_word_text_view)
        this.antonymTextView = findViewById(R.id.antonym_word_text_view)
        this.irregularTextView = findViewById(R.id.irregular_word_text_view)
        this.addButton = findViewById(R.id.add_word_button)

        val idLesson = intent.getLongExtra("id_lesson", -1)
        addButton.setOnClickListener(AddWordListener(this, idLesson))
    }
}
