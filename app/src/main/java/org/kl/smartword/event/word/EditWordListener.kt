package org.kl.smartword.event.word

import android.view.View
import android.widget.Toast

import org.kl.smartword.model.Word
import org.kl.smartword.db.WordDB
import org.kl.smartword.event.validate.ViewValidator
import org.kl.smartword.ui.EditWordActivity

class EditWordListener(private val activity: EditWordActivity,
                       private val word: Word) : View.OnClickListener {
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
            !ViewValidator.validate(transcriptionField, "Transcription is empty") ||
            !ViewValidator.validate(translationField, "Translation is empty") ||
            !ViewValidator.validate(associationField, "Association is empty") ||
            !ViewValidator.validate(etymologyField, "Etymology is empty") ||
            !ViewValidator.validate(otherFormField, "Other form is empty") ||
            !ViewValidator.validate(antonymField, "Antonym is empty") ||
            !ViewValidator.validate(irregularField, "Irregular is empty")) {
            return
        }

        with (word) {
            name = nameField.text.toString()
            transcription = transcriptionField.text.toString()
            translation = translationField.text.toString()
            association = associationField.text.toString()
            etymology = etymologyField.text.toString()
            otherForm = otherFormField.text.toString()
            antonym = antonymField.text.toString()
            irregular = irregularField.text.toString()
        }

        WordDB.update(word)
        Toast.makeText(activity, "Update word: ${word.name}", Toast.LENGTH_LONG)
             .show()
    }
}