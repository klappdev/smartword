package org.kl.smartword.event.lesson

import android.view.View
import java.util.*

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.model.Lesson
import org.kl.smartword.db.LessonDao
import org.kl.smartword.event.validate.ViewValidator
import org.kl.smartword.view.EditLessonActivity
import org.kl.smartword.util.formatted
import org.kl.smartword.util.toast

class EditLessonListener(private val activity: EditLessonActivity,
                         private val idLesson: Long) : View.OnClickListener {
    private val nameField = activity.nameTextView
    private val descriptionField = activity.descriptionTextView
    private val disposables = activity.disposables

    override fun onClick(view: View?) {
        if (!ViewValidator.validate(nameField, "Name is empty") ||
            !ViewValidator.validate(descriptionField, "Description is empty")) {
            return
        }

        val newLesson = Lesson(
            id = idLesson,
            name = nameField.text.toString(),
            description = descriptionField.text.toString(),
            date = Date().formatted()
        )

        disposables.add(LessonDao.update(newLesson)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    activity.toast("Can't update lesson: ${newLesson.name}")
                }
                override fun onComplete() {
                    activity.toast("Update lesson: ${newLesson.name}")
                }
            }))
    }
}