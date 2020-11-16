package org.kl.smartword.event.lesson

import android.view.View
import java.util.*

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.model.Lesson
import org.kl.smartword.db.LessonDao
import org.kl.smartword.event.validate.ViewValidator
import org.kl.smartword.view.AddLessonActivity
import org.kl.smartword.util.formatted
import org.kl.smartword.util.toast

class AddLessonListener(private val activity: AddLessonActivity) : View.OnClickListener {
    private val nameField = activity.nameTextView
    private val descriptionField = activity.descriptionTextView
    private val disposables = activity.disposables

    override fun onClick(view: View?) {
        /*FIXME: make async function call*/
        if (!ViewValidator.validate(nameField, "Name is empty") ||
            !ViewValidator.validate(nameField, "Lesson already exists", LessonDao::checkIfExists) ||
            !ViewValidator.validate(descriptionField, "Description is empty")) {
            return
        }

        val lesson = Lesson(
            id = -1,
            name = nameField.text.toString(),
            description = descriptionField.text.toString(),
            date = Date().formatted()
        )

        disposables.add(LessonDao.add(lesson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    activity.toast("Can't add lesson: ${lesson.name}")
                }
                override fun onComplete() {
                    activity.toast("Added lesson: ${lesson.name}")
                }
            }))
    }
}
