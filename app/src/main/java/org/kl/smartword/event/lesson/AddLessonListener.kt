package org.kl.smartword.event.lesson

import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.model.Lesson
import org.kl.smartword.db.LessonDao
import org.kl.smartword.event.validate.ViewValidator
import org.kl.smartword.view.AddLessonActivity
import org.kl.smartword.util.toast

class AddLessonListener(private val activity: AddLessonActivity) : View.OnClickListener {
    private val disposables = activity.disposables

    override fun onClick(view: View?) {
        if (!ViewValidator.validate(activity.nameTextView, "Name is empty") ||
            !ViewValidator.validate(activity.descriptionTextView, "Description is empty")) {
            return
        }

        val name = activity.nameTextView.text.toString()
        val description = activity.descriptionTextView.text.toString()

        disposables.add(LessonDao.checkIfExists(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableSingleObserver<Boolean>() {
                override fun onError(e: Throwable) {}
                override fun onSuccess(exist: Boolean) {
                    if (exist) {
                        ViewValidator.error(activity.nameTextView, "Lesson already exists")
                    } else {
                        addLesson(Lesson(-1, name, description))
                    }
                }
            }))
    }

    private fun addLesson(lesson: Lesson) {
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
