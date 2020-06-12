package org.kl.smartword.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.kl.smartword.R
import org.kl.smartword.event.word.AddWordListener

class AddWordActivity : AppCompatActivity() {
    internal lateinit var nameTextView: TextView
    internal lateinit var transcriptionTextView: TextView
    internal lateinit var translationTextView: TextView
    internal lateinit var associationTextView: TextView
    internal lateinit var etymologyTextView: TextView
    internal lateinit var otherFormTextView: TextView
    internal lateinit var antonymTextView: TextView
    internal lateinit var irregularTextView: TextView
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)

        this.nameTextView = findViewById(R.id.name_word_text_view)
        this.transcriptionTextView = findViewById(R.id.transcription_word_text_view)
        this.translationTextView = findViewById(R.id.translation_word_text_view)
        this.associationTextView = findViewById(R.id.association_word_text_view)
        this.etymologyTextView = findViewById(R.id.etymology_word_text_view)
        this.otherFormTextView = findViewById(R.id.other_form_word_text_view)
        this.antonymTextView = findViewById(R.id.antonym_word_text_view)
        this.irregularTextView = findViewById(R.id.irregular_word_text_view)
        this.addButton = findViewById(R.id.add_word_button)

        val idLesson = intent.getIntExtra("id_lesson", -1)
        addButton.setOnClickListener(AddWordListener(this, idLesson))
    }
}
