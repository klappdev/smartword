package org.kl.smartword.event.word

import android.view.View
import java.util.*

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.model.Word
import org.kl.smartword.db.WordDao
import org.kl.smartword.event.validate.ViewValidator
import org.kl.smartword.view.AddWordActivity
import org.kl.smartword.util.formatted
import org.kl.smartword.util.toast

class AddWordListener(private val activity: AddWordActivity,
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
        /*FIXME: make async function call*/
        if (!ViewValidator.validate(nameField, "Name is empty") ||
            !ViewValidator.validate(nameField, "Word already exists", WordDao::checkIfExists) ||
            !ViewValidator.validate(transcriptionField, "transcription is empty") ||
            !ViewValidator.validate(translationField, "translation is empty") ||
            !ViewValidator.validate(associationField, "association is empty") ||
            !ViewValidator.validate(etymologyField, "etymology is empty")  ||
            !ViewValidator.validate(otherFormField, "other form is empty") ||
            !ViewValidator.validate(antonymField, "antonym is empty") ||
            !ViewValidator.validate(irregularField, "irregular is empty")) {
            return
        }

        val word = Word(
            id = -1,
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

        disposables.add(WordDao.add(word)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    activity.toast("Can't add word: ${word.name}")
                }
                override fun onComplete() {
                    activity.toast("Added word: ${word.name}")
                }
            }))
    }
}