package org.kl.smartword.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.kl.smartword.R
import org.kl.smartword.db.WordDB
import org.kl.smartword.event.word.EditWordListener

class EditWordActivity : AppCompatActivity() {
    internal lateinit var nameTextView: TextView
    internal lateinit var transcriptionTextView: TextView
    internal lateinit var translationTextView: TextView
    internal lateinit var associationTextView: TextView
    internal lateinit var etymologyTextView: TextView
    internal lateinit var otherFormTextView: TextView
    internal lateinit var antonymTextView: TextView
    internal lateinit var irregularTextView: TextView
    private lateinit var editButton: Button

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

        val idWord = intent.getIntExtra("id_word", -1)
        val word = WordDB.get(idWord)

        nameTextView.text = word.name
        transcriptionTextView.text = word.transcription
        translationTextView.text = word.translation
        associationTextView.text = word.association
        etymologyTextView.text = word.etymology
        otherFormTextView.text = word.otherForm
        antonymTextView.text = word.antonym
        irregularTextView.text = word.irregular

        editButton.setOnClickListener(EditWordListener(this, word))
    }
}
