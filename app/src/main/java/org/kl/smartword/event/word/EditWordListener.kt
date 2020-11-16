package org.kl.smartword.event.word

import android.view.View
import java.util.*

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.model.Word
import org.kl.smartword.db.WordDao
import org.kl.smartword.event.validate.ViewValidator
import org.kl.smartword.util.formatted
import org.kl.smartword.util.toast
import org.kl.smartword.view.EditWordActivity

class EditWordListener(private val activity: EditWordActivity,
                       private val idWord: Long,
                       private val idLesson: Long) : View.OnClickListener {
    private val nameField = activity.nameTextView
    private val transcriptionField = activity.transcriptionTextView
    private val translationField = activity.translationTextView
    private val associationField = activity.associationTextView
    private val etymologyField = activity.etymologyTextView
    private val otherFormField = activity.otherFormTextView
    private val antonymField = activity.antonymTextView
    private val irregularField = activity.irregularTextView
    private val disposables = activity.disposables

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

        val newWord = Word(
            id = idWord,
            idLesson = idLesson,
            name = nameField.text.toString(),
            transcription = transcriptionField.text.toString(),
            translation = translationField.text.toString(),
            date = Date().formatted(),
            association = associationField.text.toString(),
            etymology = etymologyField.text.toString(),
            otherForm = otherFormField.text.toString(),
            antonym = antonymField.text.toString(),
            irregular = irregularField.text.toString()
        )

        disposables.add(WordDao.update(newWord)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    activity.toast("Can't update word: ${newWord.name}")
                }
                override fun onComplete() {
                    activity.toast("Update word: ${newWord.name}")
                }
            }))
    }
}