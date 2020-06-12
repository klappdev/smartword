package org.kl.smartword.event.word

import android.view.View
import android.widget.Toast
import org.kl.smartword.R
import org.kl.smartword.model.Word
import org.kl.smartword.db.WordDB
import org.kl.smartword.event.validate.ViewValidator
import org.kl.smartword.ui.AddWordActivity
import org.kl.smartword.util.formatted
import java.util.*

class AddWordListener(private val activity: AddWordActivity,
                      private val idLessonForWord: Int) : View.OnClickListener {
    private val nameField = activity.nameTextView
    private val transcriptionField = activity.transcriptionTextView
    private val translationField = activity.translationTextView
    private val associationField = activity.associationTextView
    private val etymologyField = activity.etymologyTextView
    private val otherFormField = activity.otherFormTextView
    private val antonymField = activity.antonymTextView
    private val irregularField = activity.irregularTextView

    override fun onClick(view: View?) {
        if (!ViewValidator.validate(nameField, "Name is empty") ||
            !ViewValidator.validate(nameField, "Word already exists", WordDB::checkIfExists) ||
            !ViewValidator.validate(transcriptionField, "transcription is empty") ||
            !ViewValidator.validate(translationField, "translation is empty") ||
            !ViewValidator.validate(associationField, "association is empty") ||
            !ViewValidator.validate(etymologyField, "etymology is empty")  ||
            !ViewValidator.validate(otherFormField, "other form is empty") ||
            !ViewValidator.validate(antonymField, "antonym is empty") ||
            !ViewValidator.validate(irregularField, "irregular is empty")) {
            return
        }

        val word = Word().apply {
            idLesson = idLessonForWord
            icon = R.drawable.word_icon
            name = nameField.text.toString()
            transcription = transcriptionField.text.toString()
            translation = translationField.text.toString()
            date = Date().formatted()
            association = associationField.text.toString()
            etymology = etymologyField.text.toString()
            otherForm = otherFormField.text.toString()
            antonym = antonymField.text.toString()
            irregular = irregularField.text.toString()
        }

        WordDB.add(word)
        Toast.makeText(activity, "Add word: ${word.name}", Toast.LENGTH_LONG)
             .show()
    }
}