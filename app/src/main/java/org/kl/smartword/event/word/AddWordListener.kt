package org.kl.smartword.event.word

import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.model.Word
import org.kl.smartword.db.WordDao
import org.kl.smartword.event.validate.ViewValidator
import org.kl.smartword.view.AddWordActivity
import org.kl.smartword.util.toast

class AddWordListener(private val activity: AddWordActivity,
                      private val idLesson: Long) : View.OnClickListener {
    private val disposables = activity.disposables

    override fun onClick(view: View?) {
        if (!ViewValidator.validate(activity.nameTextView, "Name is empty") ||
            !ViewValidator.validate(activity.transcriptionTextView, "transcription is empty") ||
            !ViewValidator.validate(activity.translationTextView, "translation is empty") ||
            !ViewValidator.validate(activity.associationTextView, "association is empty") ||
            !ViewValidator.validate(activity.etymologyTextView, "etymology is empty")  ||
            !ViewValidator.validate(activity.otherFormTextView, "other form is empty") ||
            !ViewValidator.validate(activity.antonymTextView, "antonym is empty") ||
            !ViewValidator.validate(activity.irregularTextView, "irregular is empty")) {
            return
        }

        val name = activity.nameTextView.text.toString()

        disposables.add(WordDao.checkIfExists(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<Boolean>() {
                override fun onError(e: Throwable) {}
                override fun onSuccess(exist: Boolean) {
                    if (exist) {
                        ViewValidator.error(activity.nameTextView, "Lesson already exists")
                    } else {
                        addWord(Word(-1, idLesson, name,
                                    transcription = activity.transcriptionTextView.text.toString(),
                                    translation = activity.translationTextView.text.toString(),
                                    association = activity.associationTextView.text.toString(),
                                    etymology = activity.etymologyTextView.text.toString(),
                                    otherForm = activity.otherFormTextView.text.toString(),
                                    antonym = activity.antonymTextView.text.toString(),
                                    irregular = activity.irregularTextView.text.toString()))
                    }
                }
            }))
    }

    private fun addWord(word: Word) {
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