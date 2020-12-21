package org.kl.smartword.event.lesson

import android.app.AlertDialog
import android.content.DialogInterface

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import org.kl.smartword.model.Lesson
import org.kl.smartword.db.LessonDao
import org.kl.smartword.view.MainActivity
import org.kl.smartword.view.adapter.DictionaryAdapter
import org.kl.smartword.view.fragment.DictionaryFragment
import org.kl.smartword.util.toast

class DeleteLessonListener {
    private val activity: MainActivity
    private val disposables: CompositeDisposable
    private val dictionaryAdapter: DictionaryAdapter

    constructor(dictionaryFragment: DictionaryFragment, disposables: CompositeDisposable) {
        this.activity = dictionaryFragment.activity as MainActivity
        this.dictionaryAdapter = dictionaryFragment.dictionaryAdapter
        this.disposables = disposables
    }

    operator fun invoke(): Boolean {
        val dialog = AlertDialog.Builder(activity)
        dialog.setTitle("Delete lesson")
            .setMessage("Do you want delete lesson?")
            .setCancelable(false)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton("Yes", ::clickPositiveButton)
            .setNegativeButton("No",  ::clickNegativeButton)
        dialog.show()

        return true
    }

    private fun clickPositiveButton(dialog: DialogInterface, id: Int) {
        val lesson = dictionaryAdapter.getCurrentItem()

        disposables.add(LessonDao.delete(lesson.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .andThen { refreshLessons() }
            .subscribeWith(object: DisposableCompletableObserver() {
                override fun onError(e: Throwable) {
                    activity.toast("Can't delete lesson: ${lesson.name}")
                }
                override fun onComplete() {
                    activity.toast("Delete lesson: ${lesson.name}")
                }
            }))

    }

    private fun clickNegativeButton(dialog: DialogInterface, id: Int) {
        dialog.cancel()
    }

    private fun refreshLessons() {
        disposables.add(LessonDao.getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<Lesson>>() {
                override fun onComplete() {}
                override fun onError(e: Throwable) {}
                override fun onNext(result: List<Lesson>) {
                    dictionaryAdapter.position = -1
                    dictionaryAdapter.listLessons.clear()
                    dictionaryAdapter.listLessons.addAll(result)
                    dictionaryAdapter.notifyDataSetChanged()
                }
            }))
    }
}